package nl.stil4m.transmission.rpc;

import java.net.MalformedURLException;
import java.net.URI;

import nl.stil4m.transmission.api.TransmissionRpcClient;
import nl.stil4m.transmission.api.domain.TorrentInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RPCTest {

	private TransmissionRpcClient rpcClient;

	private TorrentInfo torrent;

	public static void main(String[] args){
		RPCTest rpc =new RPCTest();
		try {
			rpc.before();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void before() throws RpcException, MalformedURLException, InterruptedException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		RpcConfiguration rpcConfiguration = new RpcConfiguration();

		rpcConfiguration.setHost(URI.create("http://localhost:9091/transmission/rpc"));
		RpcClient client = new RpcClient(rpcConfiguration, objectMapper);
		this.rpcClient = new TransmissionRpcClient(client);
		System.out.println(this.rpcClient.getAllTorrentsInfo().getTorrents());
	}
}
