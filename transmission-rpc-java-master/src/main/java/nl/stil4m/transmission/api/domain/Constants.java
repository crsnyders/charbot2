package nl.stil4m.transmission.api.domain;

import java.util.List;

import com.google.common.collect.Lists;

public class Constants {

	public static final List<String> TORRENT_INFO_FIELDS = Lists.newArrayList(
			"activityDate", "addedDate", "bandwidthPriority", "comment", "corruptEver", "creator", "dateCreated", "desiredAvailable", "doneDate", "downloadDir", "downloadedEver", "downloadLimit", "downloadLimited", "error", "errorString", "eta", "etaIdle", "files", "fileStats", "hashString", "haveUnchecked", "haveValid", "honorsSessionLimits", "id", "isFinished", "isPrivate", "isStalled", "leftUntilDone", "magnetLink", "manualAnnounceTime", "maxConnectedPeers", "metadataPercentComplete", "name", "peer-limit", "peers", "peersConnected", "peersFrom", "peersGettingFromUs", "peersSendingToUs", "percentDone", "pieces", "pieceCount", "pieceSize", "priorities", "queuePosition", "rateDownload", "rateUpload", "recheckProgress", "secondsDownloading", "secondsSeeding", "seedIdleLimit", "seedIdleMode", "seedRatioLimit", "seedRatioMode", "sizeWhenDone", "startDate", "status", "trackers", "trackerStats", "totalSize", "torrentFile", "uploadedEver", "uploadLimit", "uploadLimited", "uploadRatio", "wanted", "webseeds", "webseedsSendingToUs"
			);
}
