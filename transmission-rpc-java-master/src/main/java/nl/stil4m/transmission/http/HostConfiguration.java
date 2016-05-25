package nl.stil4m.transmission.http;

import java.net.URI;

public interface HostConfiguration {

	URI getHost();
	String getUsername();
	String getPassword();
	boolean UseAuthentication();

}
