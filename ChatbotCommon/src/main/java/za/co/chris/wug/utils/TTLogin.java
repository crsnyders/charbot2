package za.co.chris.wug.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class TTLogin {

	@Inject
	private Environment environment;

	private String cookies;

	private final Logger logger = LoggerFactory.getLogger(TTLogin.class);

	public void login() throws IOException{
		HttpURLConnection  connection = (HttpURLConnection) (new URL("http://torrents.ctwug.za.net/account-login.php")).openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		String userName = this.environment.getProperty("search.username");
		String password = this.environment.getProperty("search.password");
		String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8") + "&" +
				URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(data);
		writer.flush();

		this.logger.debug(connection.getResponseCode()+" : "+connection.getResponseMessage());
		if(connection.getResponseCode() != 200){
			throw new IOException("Could not login");
		}
		String finalCookie = "";
		for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
			if (header.getKey() != null && header.getKey().equals("Set-Cookie")) {
				for (String cookiePart : header.getValue()) {
					finalCookie += cookiePart.split(";")[0] + ";";
				}
			}
		}
		this.cookies = ((finalCookie.endsWith(";")) ? finalCookie.substring(0, finalCookie.length() - 1) : finalCookie);
	}


	public String getCookies() throws IOException{
		if(this.cookies == null){
			login();
		}
		return this.cookies;
	}
}
