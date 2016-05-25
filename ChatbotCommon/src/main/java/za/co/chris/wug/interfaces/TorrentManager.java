package za.co.chris.wug.interfaces;

import za.co.chris.wug.beans.CommandObject;

public interface TorrentManager {

	public boolean canHandle(String command);
	public String processCommand(final CommandObject commandObject);
}
