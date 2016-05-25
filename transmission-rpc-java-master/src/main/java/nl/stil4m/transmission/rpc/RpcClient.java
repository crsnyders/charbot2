package nl.stil4m.transmission.rpc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import nl.stil4m.transmission.http.InvalidResponseStatus;
import nl.stil4m.transmission.http.RequestExecutor;
import nl.stil4m.transmission.http.RequestExecutorException;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RpcClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);
	private static final Integer STATUS_OK = 200;

	private final RpcConfiguration configuration;
	private final ObjectMapper objectMapper;
	private final Map<String, String> headers;
	private final DefaultHttpClient defaultHttpClient = new DefaultHttpClient();

	private final RequestExecutor requestExecutor;

	public RpcClient(RpcConfiguration configuration, ObjectMapper objectMapper) {
		this.requestExecutor = new RequestExecutor(objectMapper, configuration, this.defaultHttpClient);
		this.configuration = configuration;
		this.objectMapper = objectMapper;
		this.headers = new HashMap<>();
	}

	public <T, V> void execute(RpcCommand<T, V> command, Map<String, String> h) throws RpcException {
		try {
			executeCommandInner(command, h);
		} catch (RequestExecutorException | IOException e) {
			throw new RpcException(e);
		} catch (InvalidResponseStatus e) {
			LOGGER.trace("Failed execute command. Now setup and try again", e);
			setup();
			try {
				executeCommandInner(command, h);
			} catch (Exception | RequestExecutorException | InvalidResponseStatus inner) {
				throw new RpcException(inner);
			}
		}
	}

	private <T, V> void executeCommandInner(RpcCommand<T, V> command, Map<String, String> h) throws RequestExecutorException, InvalidResponseStatus, IOException, RpcException {
		for (Map.Entry<String, String> entry : h.entrySet()) {
			this.requestExecutor.removeAllHeaders();
			this.requestExecutor.configureHeader(entry.getKey(), entry.getValue());
		}

		RpcRequest<T> request = command.buildRequestPayload();
		RpcResponse<V> response = this.requestExecutor.execute(request, RpcResponse.class, STATUS_OK);

		Map args = (Map) response.getArguments();
		String stringValue = this.objectMapper.writeValueAsString(args);
		response.setArguments((V) this.objectMapper.readValue(stringValue, command.getArgumentsObject()));
		if (!command.getTag().equals(response.getTag())) {
			throw new RpcException(String.format("Invalid response tag. Expected %d, but got %d", command.getTag(), request.getTag()));
		}
		command.setResponse(response);

		if (!"success".equals(response.getResult())) {
			throw new RpcException("Rpc Command Failed: " + response.getResult(), command);
		}
	}

	private void setup() throws RpcException {
		try {
			if (this.configuration.UseAuthentication()) {
				getClient().getCredentialsProvider().setCredentials(
						new AuthScope(this.configuration.getHost().getHost(), this.configuration.getHost().getPort()),
						new UsernamePasswordCredentials(this.configuration.getUsername(), this.configuration.getPassword()));
			}
			HttpPost httpPost = createPost();
			HttpResponse result = this.defaultHttpClient.execute(httpPost);
			putSessionHeader(result);
			EntityUtils.consume(result.getEntity());
		} catch (IOException e) {
			throw new RpcException(e);
		}
	}

	protected HttpPost createPost() {
		return new HttpPost(this.configuration.getHost());
	}

	protected DefaultHttpClient getClient() {
		return this.defaultHttpClient;
	}

	public void executeWithHeaders(RpcCommand command) throws RpcException {
		execute(command, this.headers);
	}

	private void putSessionHeader(HttpResponse result) {
		this.headers.put("X-Transmission-Session-Id", result.getFirstHeader("X-Transmission-Session-Id").getValue());
	}
}
