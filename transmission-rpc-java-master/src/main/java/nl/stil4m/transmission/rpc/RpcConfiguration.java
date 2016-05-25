package nl.stil4m.transmission.rpc;

import java.net.URI;

import nl.stil4m.transmission.http.HostConfiguration;

public class RpcConfiguration implements HostConfiguration {

	private URI host;

	private String username;

	private String password;

	private boolean useAuthentication;

	@Override
	public URI getHost() {
		return this.host;
	}

	public void setHost(URI host) {
		this.host = host;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUseAuthentication(boolean useAuthentication){
		this.useAuthentication =useAuthentication;
	}
	@Override
	public boolean UseAuthentication() {
		return this.useAuthentication;
	}


}
