package za.co.chris.wug.interfaces;

import za.co.chris.wug.beans.Torrent;


public interface IndexLinkHandler {

	public Torrent getTorrent(String requestFrom,int index);
}
