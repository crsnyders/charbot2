package za.co.chris.wug.utorrent;

import static za.co.chris.wug.utils.NumberUtils.parseIndexes;
import static za.co.chris.wug.utils.NumberUtils.roundTwoDecimals;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import nl.stil4m.transmission.api.TransmissionRpcClient;
import nl.stil4m.transmission.api.domain.File;
import nl.stil4m.transmission.api.domain.RemoveTorrentInfo;
import nl.stil4m.transmission.api.domain.TorrentAction;
import nl.stil4m.transmission.api.domain.TorrentInfo;
import nl.stil4m.transmission.api.domain.ids.NumberListIds;
import nl.stil4m.transmission.api.torrent.TorrentStatusEnum;
import nl.stil4m.transmission.rpc.RpcClient;
import nl.stil4m.transmission.rpc.RpcConfiguration;
import nl.stil4m.transmission.rpc.RpcException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.interfaces.TorrentManager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class TransmissionManager implements TorrentManager{

	@Inject
	private Environment environment;

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
	private List<TorrentInfo> localList;

	private final Logger logger = LoggerFactory.getLogger(TransmissionManager.class);

	private static TransmissionRpcClient rpcClient;

	public TransmissionManager() {
	}

	@PostConstruct
	public void init(){
		if(rpcClient  == null){
			String url = this.environment.getProperty("transmission.rpc.url","localhost");
			boolean useAuthentication = this.environment.getProperty("transmission.rpc.useAuthentication",Boolean.class,false);
			String username = this.environment.getProperty("transmission.rpc.username","");
			String password = this.environment.getProperty("transmission.rpc.password","");

			ObjectMapper objectMapper = new ObjectMapper();
			RpcConfiguration rpcConfiguration = new RpcConfiguration();
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			rpcConfiguration.setHost(URI.create(url));
			rpcConfiguration.setPassword(username);
			rpcConfiguration.setUsername(password);
			rpcConfiguration.setUseAuthentication(useAuthentication);
			RpcClient client = new RpcClient(rpcConfiguration, objectMapper);
			rpcClient = new TransmissionRpcClient(client);
		}
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
		return "Provides the interaction between transmission and chatbot";
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
				response = formatStatusResponse(getAllWithState(rpcClient.getAllTorrentsInfo().getTorrents(), TorrentStatusEnum.DOWNLOADING));
			} catch (RpcException e) {
				response="Could not get busy torrents.\n\r";
				this.logger.error("Could not get busy torrents.",e);
			}
			break;
		case done:
			try {
				response = formatStatusResponse(getAllWithState(rpcClient.getAllTorrentsInfo().getTorrents(), TorrentStatusEnum.SEEDING,TorrentStatusEnum.SEEDING_COMPLETE));
			} catch (RpcException e) {
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

	private List<TorrentInfo> getAllWithState(List<TorrentInfo> list, TorrentStatusEnum... statuses){
		List<TorrentInfo> torrentsTemp = new ArrayList<>();

		for (TorrentInfo torrent : list) {
			for(TorrentStatusEnum status : statuses){
				if (torrent.getStatusEnum() == status) {
					torrentsTemp.add(torrent);
				}
			}
		}
		return torrentsTemp;
	}
	private String formatStatusResponse(List<TorrentInfo> torrents) {
		this.localList = torrents;
		String reponse = "";
		int i = 0;
		for (TorrentInfo torrent : torrents) {
			try{
				reponse += (String.valueOf(i)).concat(")").concat(torrentToString(torrent)).concat("\r\n");
				i++;
			}catch(Exception e){
				this.logger.error("Format response error",e);
			}
		}
		return reponse;
	}

	private String getTorrentFile(List<String> indexes) {
		String response = "";
		for (Integer index : parseIndexes(indexes)) {
			TorrentInfo torrent = getRaw().get(index);
			response += ("Files in torrent ").concat(torrent.getName()).concat("\r\n");

			for (File torrentFile : torrent.getFiles()) {
				response += (torrentFile.getName()).concat("(").concat(String.valueOf((torrentFile.getBytesCompleted()/torrentFile.getLength())*100)).concat(")").concat("\r\n");
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

	private String removeTorrent(TorrentInfo torrent) {
		try {
			rpcClient.removeTorrent(new RemoveTorrentInfo(new NumberListIds(torrent.getId()), true));
			return String.format("Torrent: %s removed from list.",torrent.getName());
		} catch (RpcException e) {
			return String.format("Torrent: %s not removed from list. Reason: %s",torrent.getName(),e.getMessage());
		}
	}

	private String forceStartTorrent(TorrentInfo torrent) {
		try {
			rpcClient.doAction(new NumberListIds(torrent.getId()), TorrentAction.START_NOW);
			return String.format("Torrent: %s forcefuly started.",torrent.getName());
		} catch (RpcException e) {
			return String.format("Torrent: %s not forcefuly started. Reason: %s",torrent.getName(),e.getMessage());
		}
	}

	private String startTorrent(TorrentInfo torrent) {
		try {
			rpcClient.doAction(new NumberListIds(torrent.getId()), TorrentAction.START);
			return String.format("Torrent: %s started.",torrent.getName());
		} catch (RpcException e) {
			return String.format("Torrent: %s not started. Reason: %s",torrent.getName(),e.getMessage());
		}
	}

	private String stopTorrent(TorrentInfo torrent) {
		try {
			rpcClient.doAction(new NumberListIds(torrent.getId()), TorrentAction.STOP);
			return String.format("Torrent: %s stopped.",torrent.getName());
		} catch (RpcException e) {
			return String.format("Torrent: %s not stopped. Reason: %s",torrent.getName(),e.getMessage());
		}
	}

	private String pauseTorrent(TorrentInfo torrent) {
		try {
			rpcClient.doAction(new NumberListIds(torrent.getId()), TorrentAction.STOP);
			return String.format("Torrent: %s paused.",torrent.getName());
		} catch (RpcException e) {
			return String.format("Torrent: %s not paused. Reason: %s",torrent.getName(),e.getMessage());
		}
	}

	private String infoTorrent(TorrentInfo torrent){
		Double percentage = torrent.getPercentDone();
		Long size = torrent.getTotalSize();
		Long seeds = torrent.getPeersSendingToUs();
		Long leechers = torrent.getPeersGettingFromUs();
		String status = torrent.getStatusEnum().name();

		return String.format("Downloaded: %s Seeders: %d Leechers: %d Status: %s", sizeFormat(percentage,size),seeds,leechers,status);
	}



	public List<TorrentInfo> getRaw() {
		return this.localList;
	}

	private String sizeFormat(Double percentage,Long totalSize){
		int counter = 0;
		String unit = "";
		do {
			totalSize = totalSize / 1024;
			counter++;
		} while (totalSize / 1024 > 1);

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
		return String.format("%d/%d %s (%d%)", (roundTwoDecimals(totalSize*percentage)),roundTwoDecimals(totalSize),unit,(int)(percentage*100));
	}

	public String torrentToString(TorrentInfo torrentInfo) {
		return torrentInfo.getName() + " - " + (torrentInfo.getPercentDone() * 100) + "% - " + (torrentInfo.getRateDownload() / 1024) + "kbs " + torrentInfo.getStatusEnum();
	}

}
