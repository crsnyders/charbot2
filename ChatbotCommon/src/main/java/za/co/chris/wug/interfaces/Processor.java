package za.co.chris.wug.interfaces;

import za.co.chris.wug.beans.CommandObject;

public interface Processor{

	public boolean canHandle(String command);

	public Object processCommand(CommandObject commandObject, String requestFrom);

}
