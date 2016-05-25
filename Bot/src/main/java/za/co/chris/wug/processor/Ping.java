package za.co.chris.wug.processor;

import java.net.InetAddress;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.interfaces.Processor;


@Component("ping")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Ping implements Processor {

	@Override
	public Object processCommand(CommandObject commandObject,String requestFrom) {
		boolean reachable = false;
		InetAddress address = null;
		String hostName = "";
		String hostIp = "";
		String respose = "";

		for (String payload : commandObject.payload) {
			try {

				if (payload.matches("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}")) {

					address = InetAddress.getByName(payload);
					hostName = address.getCanonicalHostName();
					hostIp = address.getHostAddress();
				} else {

					address = InetAddress.getByName(payload);
					hostIp = address.getHostAddress();
					hostName = address.getCanonicalHostName();

					if (hostName.matches("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}")) {
						hostName = payload;
					}
				}

				reachable = address.isReachable(10000);
				respose += "Pinging Host: " + hostName + "(" + hostIp + ")\n\r";
				respose += "Is host reachable? " + reachable + "\n\r";
			} catch (Exception e) {
				respose = e.getMessage();
			}
		}
		return respose;
	}

	@Override
	public boolean canHandle(String command) {
		return command.equalsIgnoreCase("ping");
	}
}
