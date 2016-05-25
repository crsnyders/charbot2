package za.co.chris.wug.processor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.interfaces.Communication;
import za.co.chris.wug.interfaces.Processor;

@Component("broadcast")
public class Broadcast implements Processor {

	@Autowired
	private List<Communication> comms;

	@Override
	public boolean canHandle(String arg0) {
		return arg0.equalsIgnoreCase("broadcast");
	}

	@Override
	public Object processCommand(CommandObject arg0,String requestFrom) {
		for(Communication comm : this.comms){
			if(comm.connected()){
				comm.send(arg0.secondaryCommand);
			}
		}
		return null;
	}

}
