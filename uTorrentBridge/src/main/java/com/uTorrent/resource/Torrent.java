package com.uTorrent.resource;

import com.uTorrent.resource.json.JSONArray;
import com.uTorrent.resource.json.JSONException;

/**
 * represents a torrent and related information as provided by the utorrent API
 * 
 * @author glenn
 * 
 */
public class Torrent {

	private String _hash;
	private int _status;
	private String _name;
	private long _size;
	private int _progress;
	private long _downloaded;
	private long _uploaded;
	private int _ratio;
	private long _uploadSpeed;
	private long _downloadSpeed;
	private long _eta;
	private String _label;
	private int _peersConnected;
	private int _peersInSwarm;
	private int _seedsConnected;
	private int _seedsInSwarm;
	private long _availability;
	private int _queueOrder;
	private long _remaining;

	public Torrent(JSONArray array) {
		try {
			_hash = array.getString(0);
			_status = Integer.parseInt(array.getString(1));
			_name = array.getString(2);
			_size = Long.parseLong(array.getString(3));
			_progress = Integer.parseInt(array.getString(4));
			_downloaded = Long.parseLong(array.getString(5));
			_uploaded = Long.parseLong(array.getString(6));
			_ratio = Integer.parseInt(array.getString(7));
			_uploadSpeed = Long.parseLong(array.getString(8));
			_downloadSpeed = Long.parseLong(array.getString(9));
			_eta = Long.parseLong(array.getString(10));
			_label = array.getString(11);
			_peersConnected = Integer.parseInt(array.getString(12));
			_peersInSwarm = Integer.parseInt(array.getString(13));
			_seedsConnected = Integer.parseInt(array.getString(14));
			_seedsInSwarm = Integer.parseInt(array.getString(15));
			_availability = Long.parseLong(array.getString(16));
			_queueOrder = Integer.parseInt(array.getString(17));
			_remaining = Long.parseLong(array.getString(18));
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public final long getAvailability() {
		return _availability;
	}

	public final long getDownloaded() {
		return _downloaded;
	}

	public final long getDownloadSpeed() {
		return _downloadSpeed;
	}

	public final long getEta() {
		return _eta;
	}

	public final String getHash() {
		return _hash;
	}

	public final String getLabel() {
		return _label;
	}

	public final String getName() {
		return _name;
	}

	public final int getPeersConnected() {
		return _peersConnected;
	}

	public final int getPeersInSwarm() {
		return _peersInSwarm;
	}

	public final int getProgress() {
		return _progress;
	}

	public final int getQueueOrder() {
		return _queueOrder;
	}

	public final int getRatio() {
		return _ratio;
	}

	public final long getRemaining() {
		return _remaining;
	}

	public final int getSeedsConnected() {
		return _seedsConnected;
	}

	public final int getSeedsInSwarm() {
		return _seedsInSwarm;
	}

	public final long getSize() {
		return _size;
	}

	public final int getStatusNumber() {
		return _status;
	}

	public TorrentState getState() {
		if (_progress == 1000) {
			return TorrentState.COMPLETED;
		} else {
			return TorrentState.NOT_COMPLETED;
		}
	}

	public final TorrentStatus getStatus() {
		boolean flag = false;
		TorrentStatus ret = TorrentStatus.UNKONWN;

		if ((_status & 1) == 1) { // Started
			if ((_status & 32) == 32) { // paused
				ret = TorrentStatus.PAUSED;
				flag = true;
			} else { // seeding or leeching
				if ((_status & 64) == 64) {
					ret = (_progress == 1000) ? TorrentStatus.SEEDING : TorrentStatus.DOWNLOADING;
					flag = true;
				}
				else {
					ret = (_progress == 1000) ? TorrentStatus.FORCEDSEEDING : TorrentStatus.FORCEDDOWNLOADING;
					flag = true;
				}
			}
		} else if ((_status & 2) == 2) { // checking
			ret = TorrentStatus.CHECKING;
			flag = true;
		} else if ((_status & 16) == 16) { // error
			ret = TorrentStatus.ERROR;
			flag = true;
		} else if ((_status & 64) == 64) { // queued
			ret = TorrentStatus.QUEUED;
			flag = true;
		}

		if (_progress == 1000 && !flag) {
			ret = TorrentStatus.FINISHED;
		}
		else if (_progress < 1000 && !flag) {
			ret = TorrentStatus.STOPPED;
		}

		return ret;
	}

	public final long getUploaded() {
		return _uploaded;
	}

	public final long getUploadSpeed() {
		return _uploadSpeed;
	}

	public int compareTo(Torrent other) {
		return _name.compareToIgnoreCase(other._name);
	}

	public String toString() {
		return this.getName() + " - " + ((float) this.getProgress() / 10) + "% - " + (this.getDownloadSpeed() / 1024) + "kbs " + this.getStatus();
	}
}
