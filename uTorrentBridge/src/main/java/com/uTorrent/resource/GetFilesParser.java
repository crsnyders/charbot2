package com.uTorrent.resource;

import java.util.Vector;

public class GetFilesParser {

	public Vector<TorrentFile>Parse(String contents) {
		String[] files = contents.split("\n");
		Vector<TorrentFile> torrentFiles =new  Vector<TorrentFile>();
		for(int i=1;i<files.length-2;i++) {
			String tmp ="";
			String[] tokens;
			if(files[i].startsWith(",")) {
				tmp = files[i].substring(2,files[i].length()-1);
				tokens = tmp.split(",");
				torrentFiles.add(new TorrentFile(tokens[0], tokens[1], tokens[2], tokens[3]));
			}
			else {
				tmp = files[i].substring(1,files[i].length()-1);
				tokens = tmp.split(",");
				torrentFiles.add(new TorrentFile(tokens[0], tokens[1], tokens[2], tokens[3]));
			}
		}

		return torrentFiles;

	}


}
