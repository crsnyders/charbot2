package za.co.chris.wug.processor;

import java.util.Arrays;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.interfaces.Processor;

@Component("echo")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Echo implements Processor{

	@Override
	public boolean canHandle(String command) {
		return command.equalsIgnoreCase("echo");
	}

	@Override
	public Object processCommand(CommandObject commandObject,String requestFrom) {
		return "Echo: "+Arrays.toString(commandObject.payload.toArray())+"\n\r";
	}

}
