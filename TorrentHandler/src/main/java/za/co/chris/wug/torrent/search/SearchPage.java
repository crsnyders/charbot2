package za.co.chris.wug.torrent.search;

import java.util.List;

import za.co.chris.wug.beans.Torrent;

public class SearchPage {

	private int pageNumber;

	private List<Torrent> torrents;

	public SearchPage(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public List<Torrent> getTorrents() {
		return this.torrents;
	}

	public void setTorrents(List<Torrent> torrents) {
		this.torrents = torrents;
	}


}
