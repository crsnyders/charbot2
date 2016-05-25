package za.co.chris.wug.utorrent;

import static za.co.chris.wug.utils.NumberUtils.parseIndexes;
import static za.co.chris.wug.utils.NumberUtils.roundTwoDecimals;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.interfaces.TorrentManager;

import com.uTorrent.UTorrent;
import com.uTorrent.resource.Torrent;
import com.uTorrent.resource.TorrentFile;
import com.uTorrent.resource.TorrentState;
import com.uTorrent.resource.exception.ServerNotFoundException;
import com.uTorrent.resource.exception.UnauthorizedException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class uTorrentManager implements TorrentManager {

	@Inject
	private Environment environment;

	private final UTorrent uTorrent;
	private final String busy = "busy";
	private final String done = "done";
	private final String remove = "remove";
	private final String start = "start";
	private final String forcestart = "forcestart";
	private final String stop = "stop";
	private final String pause = "pause";
	private final String getfiles = "getfiles";
	private final String info = "info";

	private final boolean intialized = false;

	private final String[] commands = new String[]{this.busy,this.done,this.remove,this.start,this.forcestart,this.stop,this.pause,this.getfiles,this.info};
	private List<Torrent> localList;

	private final Logger logger = LoggerFactory.getLogger(uTorrentManager.class);

	public uTorrentManager() {
		this.uTorrent = UTorrent.getInstance();
	}

	public void init(){
		boolean useSSL = this.environment.getProperty("utorrent.usessl",Boolean.class,false);
		String address = this.environment.getProperty("utorrent.address","localhost");
		int port = this.environment.getProperty("utorrent.port",Integer.class,8080);
		String username = this.environment.getProperty("utorrent.username");
		String password = this.environment.getProperty("utorrent.password");

		this.uTorrent.setServerSettings(useSSL, address, port);
		this.uTorrent.setUserCredentials(username, password);
	}

	@Override
	public boolean canHandle(String command) {
		for(String commandlocal : this.commands){
			if(commandlocal.equalsIgnoreCase(command)){
				return true;
			}
		}
		return false;
	}

	//	@Override
	public String getDescription() {
		return "Provides the interaction between utorrent and chatbot";
	}

	@Override
	public String processCommand(final CommandObject commandObject) {
		if(!this.intialized){
			init();
		}

		String response = "";
		switch (commandObject.mainCommand) {
		case busy:
			try {
				response = formatStatusResponse(getAllWithState(uTorrentManager.this.uTorrent.checkTorrents(), TorrentState.NOT_COMPLETED));
			} catch (ServerNotFoundException | UnauthorizedException e) {
				response="Could not get busy torrents.\n\r";
				this.logger.error("Could not get busy torrents.",e);
			}
			break;
		case done:
			try {
				response = formatStatusResponse(getAllWithState(uTorrentManager.this.uTorrent.checkTorrents(), TorrentState.COMPLETED));
			} catch (ServerNotFoundException | UnauthorizedException e) {
				response="Could not get complete torrents.\n\r";
				this.logger.error("Could not get complete torrents.",e);
			}
			break;
		case getfiles:
			response = getTorrentFile(commandObject.payload);
			break;
		default:
			response = processTorrentList(commandObject.payload, commandObject.mainCommand);

		}

		return response;
	}

	private List<Torrent> getAllWithState(List<Torrent> torrents, TorrentState state){
		List<Torrent> torrentsTemp = new ArrayList<>();
		for (Torrent torrent : torrents) {
			if (torrent.getState() == state) {
				torrentsTemp.add(torrent);
			}
		}
		return torrentsTemp;
	}
	private String formatStatusResponse(List<Torrent> torrents) {
		this.localList = torrents;
		String reponse = "";
		int i = 0;
		for (Torrent torrent : torrents) {
			reponse += (String.valueOf(i)).concat(")").concat(torrent.toString()).concat("\r\n");
			i++;
		}
		return reponse;
	}

	private String getTorrentFile(List<String> indexes) {
		String response = "";
		for (Integer index : parseIndexes(indexes)) {
			Torrent torrent = getRaw().get(index);
			response += ("Files in torrent ").concat(torrent.getName()).concat("\r\n");
			try {
				for (TorrentFile torrentFile : this.uTorrent.getFilesInTorrent(torrent)) {
					response += (torrentFile.toString()).concat("\r\n");
				}
			} catch (ServerNotFoundException | UnauthorizedException e) {
				response += ("Counld not get torrent files. Reason: ").concat(e.getMessage()).concat("\r\n");
				this.logger.error("Could not get torrent files.",e);
			}
		}
		return response;
	}

	private String processTorrentList(List<String> indexes, String option) {
		StringBuilder response = new StringBuilder();
		for (Integer index : parseIndexes(indexes)) {
			switch (option) {
			case remove:
				response.append(removeTorrent(getRaw().get(index))).append("\r\n");
				break;
			case start:
				response.append(startTorrent(getRaw().get(index))).append("\r\n");
				break;
			case stop:
				response.append(stopTorrent(getRaw().get(index))).append("\r\n");
				break;
			case pause:
				response.append(pauseTorrent(getRaw().get(index))).append("\r\n");
				break;
			case forcestart:
				response.append(forceStartTorrent(getRaw().get(index))).append("\r\n");
				break;
			case info:
				response.append(infoTorrent(getRaw().get(index))).append("\r\n");
			}
		}
		return response.toString();
	}


	private String removeTorrent(Torrent torrent) {
		try {
			this.uTorrent.remove(torrent, true);
			return String.format("Torrent: %s removed from list.",torrent.getName());
		} catch (ServerNotFoundException | UnauthorizedException e) {
			return String.format("Torrent: %s not removed from list. Reason: %s",torrent.getName(),e.getMessage());
		}
	}


	private String forceStartTorrent(Torrent torrent) {
		try {
			this.uTorrent.forceStart(torrent);
			return String.format("Torrent: %s forcefuly started.",torrent.getName());
		} catch (ServerNotFoundException | UnauthorizedException e) {
			return String.format("Torrent: %s not forcefuly started. Reason: %s",torrent.getName(),e.getMessage());
		}
	}


	private String startTorrent(Torrent torrent) {
		try {
			this.uTorrent.start(torrent);
			return String.format("Torrent: %s started.",torrent.getName());
		} catch (ServerNotFoundException | UnauthorizedException e) {
			return String.format("Torrent: %s not started. Reason: %s",torrent.getName(),e.getMessage());
		}
	}


	protected String stopTorrent(Torrent torrent) {
		try {
			this.uTorrent.stop(torrent);
			return String.format("Torrent: %s stopped.",torrent.getName());
		} catch (ServerNotFoundException | UnauthorizedException e) {
			return String.format("Torrent: %s not stopped. Reason: %s",torrent.getName(),e.getMessage());
		}
	}


	private String pauseTorrent(Torrent torrent) {
		try {
			this.uTorrent.stop(torrent);
			return String.format("Torrent: %s paused.",torrent.getName());
		} catch (ServerNotFoundException | UnauthorizedException e) {
			return String.format("Torrent: %s not paused. Reason: %s",torrent.getName(),e.getMessage());
		}
	}


	private String infoTorrent(Torrent torrent){
		Double downloaded = new Double(torrent.getDownloaded());
		Double size = new Double(torrent.getSize());
		int seeds = torrent.getSeedsConnected();
		int seedSwarm = torrent.getSeedsInSwarm();
		int leechers = torrent.getPeersConnected();
		int leachSwarm = torrent.getPeersInSwarm();
		String status = torrent.getStatus().toString();
		return String.format("Downloaded: %s Seeders: %d(%d) Leechers: %d(%d) Status: %s", sizeFormat(downloaded,size),seeds,seedSwarm,leechers,leachSwarm,status);
	}



	public List<Torrent> getRaw() {
		return this.localList;
	}

	private String sizeFormat(Double downloaded,Double size){
		int counter = 0;
		String unit = "";
		do {
			size = size / 1024;
			downloaded = downloaded / 1024;

			counter++;
		} while (size / 1024 > 1);

		switch (counter) {
		case 0:
			unit = "b";
			break;
		case 1:
			unit = "KB";
			break;
		case 2:
			unit = "MB";
			break;
		case 3:
			unit = "GB";
			break;
		}
		return roundTwoDecimals(downloaded) + "/" + roundTwoDecimals(size) + " " + unit;
	}
}
