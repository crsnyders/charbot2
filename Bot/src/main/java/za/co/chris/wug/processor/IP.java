package za.co.chris.wug.processor;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.interfaces.Processor;

@Component("ip")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class IP implements Processor {

	Pattern ipadressPattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}.\\d{1,3}");
	@Override
	public boolean canHandle(String command) {
		return command.equalsIgnoreCase("ip");
	}

	@Override
	public Object processCommand(CommandObject commandObject,String requestFrom) {
		String response ="Could not find ip address";
		try {
			String out = new java.util.Scanner(new java.net.URL("http://www.whatsmyip.co.za").openStream()).useDelimiter("\\A").next();
			Matcher match = this.ipadressPattern.matcher(out);
			if(match.find()){
				response= "Found Ip address: "+match.group();
			}
		} catch (IOException e) {

			response="Could not get ip: "+e.getLocalizedMessage();
		}
		return response;
	}

}
