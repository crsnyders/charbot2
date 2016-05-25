package nl.stil4m.transmission.api.domain;


import java.util.List;

import nl.stil4m.transmission.api.torrent.TorrentStatusEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TorrentInfo {

	private Long activityDate;
	private Long addedDate;
	private Long bandwidthPriority;

	private String comment;

	private Long corruptEver;

	private String creator;

	private Long dateAdded;

	private Long dateCreated;
	private Long desiredAvailable;
	private Long doneDate;
	private String downloadDir;
	private Long downloadedEver;
	private Long downloadLimit;
	private Boolean downloadLimited;
	private Long error;
	private String errorString;
	private Long eta;
	private Long etaIdle;
	private List<File> files;
	private List<FileStat> fileStats;
	private String hashString;
	private Long haveUnchecked;
	private Long haveValid;
	private Boolean honorsSessionLimits;
	private Long id;

	@JsonProperty("isFinished")
	private Boolean isFinished;

	@JsonProperty("isPrivate")
	private Boolean isPrivate;

	@JsonProperty("isStalled")
	private Boolean isStalled;

	private Long leftUntilDone;
	private String magnetLink;
	private Long manualAnnounceTime;
	private Long maxConnectedPeers;
	private Double metadataPercentComplete;
	private String name;

	@JsonProperty("peer-limit")
	private Long peerLimit;
	private List<Peer> peers;
	private Long peersConnected;
	private Object peersFrom;
	private Long peersGettingFromUs;
	private Long peersSendingToUs;
	private Double percentDone;
	private String pieces;
	private Long pieceCount;
	private Long pieceSize;
	private List<Integer> priorities;
	private Long queuePosition;
	private Long rateDownload =0L;
	private Long rateUpload =0L;
	private Double recheckProgress;
	private Long secondsDownloading;
	private Long secondsSeeding;
	private Long seedIdleLimit;
	private Long seedIdleMode;
	private Double seedRatioLimit;
	private Long seedRatioMode;
	private Long sizeWhenDone;
	private Long startDate;
	private Long status;
	private List<Tracker> trackers;
	private List<TrackerStat> trackerStats;
	private Long totalSize;
	private String torrentFile;
	private Long uploadedEver;
	private Long uploadLimit;
	private Boolean uploadLimited;
	private Double uploadRatio;
	private List wanted;
	private List webseeds;
	private Long webseedsSendingToUs;

	public Long getActivityDate() {
		return this.activityDate;
	}

	public void setActivityDate(Long activityDate) {
		this.activityDate = activityDate;
	}

	public Long getAddedDate() {
		return this.addedDate;
	}

	public void setAddedDate(Long addedDate) {
		this.addedDate = addedDate;
	}

	public Long getBandwidthPriority() {
		return this.bandwidthPriority;
	}

	public void setBandwidthPriority(Long bandwidthPriority) {
		this.bandwidthPriority = bandwidthPriority;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getCorruptEver() {
		return this.corruptEver;
	}

	public void setCorruptEver(Long corruptEver) {
		this.corruptEver = corruptEver;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Long getDateAdded() {
		return this.dateAdded;
	}

	public void setDateAdded(Long dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Long getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Long dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Long getDesiredAvailable() {
		return this.desiredAvailable;
	}

	public void setDesiredAvailable(Long desiredAvailable) {
		this.desiredAvailable = desiredAvailable;
	}

	public Long getDoneDate() {
		return this.doneDate;
	}

	public void setDoneDate(Long doneDate) {
		this.doneDate = doneDate;
	}

	public String getDownloadDir() {
		return this.downloadDir;
	}

	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

	public Long getDownloadedEver() {
		return this.downloadedEver;
	}

	public void setDownloadedEver(Long downloadedEver) {
		this.downloadedEver = downloadedEver;
	}

	public Long getDownloadLimit() {
		return this.downloadLimit;
	}

	public void setDownloadLimit(Long downloadLimit) {
		this.downloadLimit = downloadLimit;
	}

	public Boolean getDownloadLimited() {
		return this.downloadLimited;
	}

	public void setDownloadLimited(Boolean downloadLimited) {
		this.downloadLimited = downloadLimited;
	}

	public Long getError() {
		return this.error;
	}

	public void setError(Long error) {
		this.error = error;
	}

	public String getErrorString() {
		return this.errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	public Long getEta() {
		return this.eta;
	}

	public void setEta(Long eta) {
		this.eta = eta;
	}

	public Long getEtaIdle() {
		return this.etaIdle;
	}

	public void setEtaIdle(Long etaIdle) {
		this.etaIdle = etaIdle;
	}

	public String getHashString() {
		return this.hashString;
	}

	public void setHashString(String hashString) {
		this.hashString = hashString;
	}

	public Long getHaveUnchecked() {
		return this.haveUnchecked;
	}

	public void setHaveUnchecked(Long haveUnchecked) {
		this.haveUnchecked = haveUnchecked;
	}

	public Long getHaveValid() {
		return this.haveValid;
	}

	public void setHaveValid(Long haveValid) {
		this.haveValid = haveValid;
	}

	public Boolean getHonorsSessionLimits() {
		return this.honorsSessionLimits;
	}

	public void setHonorsSessionLimits(Boolean honorsSessionLimits) {
		this.honorsSessionLimits = honorsSessionLimits;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getFinished() {
		return this.isFinished;
	}

	public void setFinished(Boolean finished) {
		this.isFinished = finished;
	}

	public Boolean getPrivate() {
		return this.isPrivate;
	}

	public void setPrivate(Boolean aPrivate) {
		this.isPrivate = aPrivate;
	}

	public Boolean getStalled() {
		return this.isStalled;
	}

	public void setStalled(Boolean stalled) {
		this.isStalled = stalled;
	}

	public Long getLeftUntilDone() {
		return this.leftUntilDone;
	}

	public void setLeftUntilDone(Long leftUntilDone) {
		this.leftUntilDone = leftUntilDone;
	}

	public String getMagnetLink() {
		return this.magnetLink;
	}

	public void setMagnetLink(String magnetLink) {
		this.magnetLink = magnetLink;
	}

	public Long getManualAnnounceTime() {
		return this.manualAnnounceTime;
	}

	public void setManualAnnounceTime(Long manualAnnounceTime) {
		this.manualAnnounceTime = manualAnnounceTime;
	}

	public Long getMaxConnectedPeers() {
		return this.maxConnectedPeers;
	}

	public void setMaxConnectedPeers(Long maxConnectedPeers) {
		this.maxConnectedPeers = maxConnectedPeers;
	}

	public Double getMetadataPercentComplete() {
		return this.metadataPercentComplete;
	}

	public void setMetadataPercentComplete(Double metadataPercentComplete) {
		this.metadataPercentComplete = metadataPercentComplete;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPeerLimit() {
		return this.peerLimit;
	}

	public void setPeerLimit(Long peerLimit) {
		this.peerLimit = peerLimit;
	}

	public Long getPeersConnected() {
		return this.peersConnected;
	}

	public void setPeersConnected(Long peersConnected) {
		this.peersConnected = peersConnected;
	}

	public Object getPeersFrom() {
		return this.peersFrom;
	}

	public void setPeersFrom(Object peersFrom) {
		this.peersFrom = peersFrom;
	}

	public Long getPeersGettingFromUs() {
		return this.peersGettingFromUs;
	}

	public void setPeersGettingFromUs(Long peersGettingFromUs) {
		this.peersGettingFromUs = peersGettingFromUs;
	}

	public Long getPeersSendingToUs() {
		return this.peersSendingToUs;
	}

	public void setPeersSendingToUs(Long peersSendingToUs) {
		this.peersSendingToUs = peersSendingToUs;
	}

	public Double getPercentDone() {
		return this.percentDone;
	}

	public void setPercentDone(Double percentDone) {
		this.percentDone = percentDone;
	}

	public String getPieces() {
		return this.pieces;
	}

	public void setPieces(String pieces) {
		this.pieces = pieces;
	}

	public Long getPieceCount() {
		return this.pieceCount;
	}

	public void setPieceCount(Long pieceCount) {
		this.pieceCount = pieceCount;
	}

	public Long getPieceSize() {
		return this.pieceSize;
	}

	public void setPieceSize(Long pieceSize) {
		this.pieceSize = pieceSize;
	}


	public Long getQueuePosition() {
		return this.queuePosition;
	}

	public void setQueuePosition(Long queuePosition) {
		this.queuePosition = queuePosition;
	}

	public Long getRateDownload() {
		return this.rateDownload;
	}

	public void setRateDownload(Long rateDownload) {
		this.rateDownload = rateDownload;
	}

	public Long getRateUpload() {
		return this.rateUpload;
	}

	public void setRateUpload(Long rateUpload) {
		this.rateUpload = rateUpload;
	}

	public Double getRecheckProgress() {
		return this.recheckProgress;
	}

	public void setRecheckProgress(Double recheckProgress) {
		this.recheckProgress = recheckProgress;
	}

	public Long getSecondsDownloading() {
		return this.secondsDownloading;
	}

	public void setSecondsDownloading(Long secondsDownloading) {
		this.secondsDownloading = secondsDownloading;
	}

	public Long getSecondsSeeding() {
		return this.secondsSeeding;
	}

	public void setSecondsSeeding(Long secondsSeeding) {
		this.secondsSeeding = secondsSeeding;
	}

	public Long getSeedIdleLimit() {
		return this.seedIdleLimit;
	}

	public void setSeedIdleLimit(Long seedIdleLimit) {
		this.seedIdleLimit = seedIdleLimit;
	}

	public Long getSeedIdleMode() {
		return this.seedIdleMode;
	}

	public void setSeedIdleMode(Long seedIdleMode) {
		this.seedIdleMode = seedIdleMode;
	}

	public Double getSeedRatioLimit() {
		return this.seedRatioLimit;
	}

	public void setSeedRatioLimit(Double seedRatioLimit) {
		this.seedRatioLimit = seedRatioLimit;
	}

	public Long getSeedRatioMode() {
		return this.seedRatioMode;
	}

	public void setSeedRatioMode(Long seedRatioMode) {
		this.seedRatioMode = seedRatioMode;
	}

	public Long getSizeWhenDone() {
		return this.sizeWhenDone;
	}

	public void setSizeWhenDone(Long sizeWhenDone) {
		this.sizeWhenDone = sizeWhenDone;
	}

	public Long getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public TorrentStatusEnum getStatusEnum(){
		if(this.getStatus() ==0){
			if(this.isFinished){
				return TorrentStatusEnum.SEEDING_COMPLETE;
			}
			return TorrentStatusEnum.PAUSED;
		}else if(this.getStatus()==3){
			return TorrentStatusEnum.QUEUED;
		}else if(this.getStatus()==4){
			if(this.peersConnected == 0){
				return TorrentStatusEnum.IDLE;
			}
			return TorrentStatusEnum.DOWNLOADING;
		}else if(this.getStatus()==6){
			return TorrentStatusEnum.SEEDING;
		}
		return TorrentStatusEnum.UNKNOWN;
	}

	public Long getTotalSize() {
		return this.totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}

	public String getTorrentFile() {
		return this.torrentFile;
	}

	public void setTorrentFile(String torrentFile) {
		this.torrentFile = torrentFile;
	}

	public Long getUploadedEver() {
		return this.uploadedEver;
	}

	public void setUploadedEver(Long uploadedEver) {
		this.uploadedEver = uploadedEver;
	}

	public Long getUploadLimit() {
		return this.uploadLimit;
	}

	public void setUploadLimit(Long uploadLimit) {
		this.uploadLimit = uploadLimit;
	}

	public Boolean getUploadLimited() {
		return this.uploadLimited;
	}

	public void setUploadLimited(Boolean uploadLimited) {
		this.uploadLimited = uploadLimited;
	}

	public Double getUploadRatio() {
		return this.uploadRatio;
	}

	public void setUploadRatio(Double uploadRatio) {
		this.uploadRatio = uploadRatio;
	}


	public Long getWebseedsSendingToUs() {
		return this.webseedsSendingToUs;
	}

	public void setWebseedsSendingToUs(Long webseedsSendingToUs) {
		this.webseedsSendingToUs = webseedsSendingToUs;
	}

	public List<File> getFiles() {
		return this.files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public List<FileStat> getFileStats() {
		return this.fileStats;
	}

	public void setFileStats(List<FileStat> fileStats) {
		this.fileStats = fileStats;
	}

	public List<Peer> getPeers() {
		return this.peers;
	}

	public void setPeers(List<Peer> peers) {
		this.peers = peers;
	}

	public List<Integer> getPriorities() {
		return this.priorities;
	}

	public void setPriorities(List<Integer> priorities) {
		this.priorities = priorities;
	}

	public List<Tracker> getTrackers() {
		return this.trackers;
	}

	public void setTrackers(List<Tracker> trackers) {
		this.trackers = trackers;
	}

	public List<TrackerStat> getTrackerStats() {
		return this.trackerStats;
	}

	public void setTrackerStats(List<TrackerStat> trackerStats) {
		this.trackerStats = trackerStats;
	}

	public List getWanted() {
		return this.wanted;
	}

	public void setWanted(List wanted) {
		this.wanted = wanted;
	}

	public List getWebseeds() {
		return this.webseeds;
	}

	public void setWebseeds(List webseeds) {
		this.webseeds = webseeds;
	}

	@Override
	public String toString() {
		return "TorrentInfo [activityDate=" + this.activityDate + ", addedDate=" + this.addedDate + ", bandwidthPriority=" + this.bandwidthPriority + ", comment=" + this.comment + ", corruptEver=" + this.corruptEver
				+ ", creator=" + this.creator + ", dateAdded=" + this.dateAdded + ", dateCreated=" + this.dateCreated + ", desiredAvailable=" + this.desiredAvailable + ", doneDate=" + this.doneDate + ", downloadDir="
				+ this.downloadDir + ", downloadedEver=" + this.downloadedEver + ", downloadLimit=" + this.downloadLimit + ", downloadLimited=" + this.downloadLimited + ", error=" + this.error + ", errorString="
				+ this.errorString + ", eta=" + this.eta + ", etaIdle=" + this.etaIdle + ", files=" + this.files + ", fileStats=" + this.fileStats + ", hashString=" + this.hashString + ", haveUnchecked=" + this.haveUnchecked
				+ ", haveValid=" + this.haveValid + ", honorsSessionLimits=" + this.honorsSessionLimits + ", id=" + this.id + ", isFinished=" + this.isFinished + ", isPrivate=" + this.isPrivate + ", isStalled=" + this.isStalled
				+ ", leftUntilDone=" + this.leftUntilDone + ", magnetLink=" + this.magnetLink + ", manualAnnounceTime=" + this.manualAnnounceTime + ", maxConnectedPeers=" + this.maxConnectedPeers
				+ ", metadataPercentComplete=" + this.metadataPercentComplete + ", name=" + this.name + ", peerLimit=" + this.peerLimit + ", peers=" + this.peers + ", peersConnected=" + this.peersConnected + ", peersFrom="
				+ this.peersFrom + ", peersGettingFromUs=" + this.peersGettingFromUs + ", peersSendingToUs=" + this.peersSendingToUs + ", percentDone=" + this.percentDone + ", pieces=" + this.pieces + ", pieceCount="
				+ this.pieceCount + ", pieceSize=" + this.pieceSize + ", priorities=" + this.priorities + ", queuePosition=" + this.queuePosition + ", rateDownload=" + this.rateDownload + ", rateUpload=" + this.rateUpload
				+ ", recheckProgress=" + this.recheckProgress + ", secondsDownloading=" + this.secondsDownloading + ", secondsSeeding=" + this.secondsSeeding + ", seedIdleLimit=" + this.seedIdleLimit + ", seedIdleMode="
				+ this.seedIdleMode + ", seedRatioLimit=" + this.seedRatioLimit + ", seedRatioMode=" + this.seedRatioMode + ", sizeWhenDone=" + this.sizeWhenDone + ", startDate=" + this.startDate + ", status=" + this.status
				+ ", trackers=" + this.trackers + ", trackerStats=" + this.trackerStats + ", totalSize=" + this.totalSize + ", torrentFile=" + this.torrentFile + ", uploadedEver=" + this.uploadedEver + ", uploadLimit="
				+ this.uploadLimit + ", uploadLimited=" + this.uploadLimited + ", uploadRatio=" + this.uploadRatio + ", wanted=" + this.wanted + ", webseeds=" + this.webseeds + ", webseedsSendingToUs=" + this.webseedsSendingToUs
				+ ", statusEnum=" + this.getStatusEnum()+"]";
	}
}
