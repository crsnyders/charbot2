package za.co.chris.wug.beans;

import java.util.ArrayList;
import java.util.List;

public class CommandObject {

	public final String mainCommand;
	public String secondaryCommand;
	public final List<String> payload;

	public CommandObject(String command, List<String> payload) {
		this.mainCommand = command.toLowerCase();
		this.payload = payload;
	}
	public CommandObject(String command,String secondaryCommand, List<String> payload) {
		this.mainCommand = command.toLowerCase();
		this.secondaryCommand = secondaryCommand.toLowerCase();
		this.payload = payload;
	}

	public CommandObject(String msg) {
		this.payload = new ArrayList<>();

		String[] tokens  = msg.split(" ",3);
		this.mainCommand = tokens[0].toLowerCase();
		if(tokens.length == 2){
			this.secondaryCommand = tokens[1].toLowerCase();
			tokenizePayload(tokens[1]);
		} else if(tokens.length == 3){
			this.secondaryCommand = tokens[1].toLowerCase();
			tokenizePayload(tokens[2]);
		}

	}

	private void tokenizePayload(String msg) {
		String[] tokens = msg.split(",");
		for (int i = 0; i < tokens.length; i++) {
			this.payload.add(tokens[i].trim());
		}
	}
}
