package com.uTorrent.resource;

import za.co.chris.wug.utils.NumberUtils;

import com.uTorrent.resource.json.JSONArray;
import com.uTorrent.resource.json.JSONException;

/**
 * A file contained within a torrent
 * 
 * @author glenn
 * 
 */
public class TorrentFile {
	public static int DONT_DOWNLOAD = 0;
	public static int LOW = 1;
	public static int NORMAL = 2;
	public static int HIGH = 3;

	private String _fileName;
	private long _fileSize;
	private long _downloaded;
	private int _priority;

	public TorrentFile(JSONArray array) {
		try {
			this._fileName = array.getString(0);
			this._fileSize = Long.parseLong(array.getString(1));
			this._downloaded = Long.parseLong(array.getString(2));
			this._priority = Integer.parseInt(array.getString(3));
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public TorrentFile(String _fileName, String _fileSize, String _downloaded, String _priority)
	{
		this._fileName = _fileName;
		this._fileSize = Long.parseLong(_fileSize);
		this._downloaded = Long.parseLong(_downloaded);
		this._priority = Integer.parseInt(_priority);
	}

	public final long getDownloaded() {
		return this._downloaded;
	}

	public final String getFileName() {
		return this._fileName;
	}

	public final int getPriority() {
		return this._priority;
	}

	public final void setPriority(int value) {
		this._priority = value;
	}

	public final String getPriorityString() {
		switch (this._priority) {
		case 0:
			return "Don't Download";
		case 1:
			return "Low Priority";
		case 2:
			return "Normal Priority";
		case 3:
			return "High priority";
		default:
			return "Unknown";
		}
	}

	public final long getFileSize() {
		return this._fileSize;
	}

	public double getProgress() {
		return (this._downloaded / (double) this._fileSize) * 100;
	}

	@Override
	public String toString() {
		return this.getFileName().concat(" - ").concat(NumberUtils.roundTwoDecimals(this.getProgress())).concat("%");
	}
}
