package za.co.chris.wug.torrent;

import static za.co.chris.wug.utils.NumberUtils.parseIndexes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.beans.Torrent;
import za.co.chris.wug.exception.ProviderException;
import za.co.chris.wug.interfaces.IndexLinkHandler;
import za.co.chris.wug.interfaces.Processor;
import za.co.chris.wug.interfaces.TorrentManager;
import za.co.chris.wug.torrent.enums.InUse;



@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WugTorrents implements Processor,IndexLinkHandler{

	private final Map<String,List<Torrent>> torrentList = new HashMap<>();

	private InUse inuse;

	@Autowired
	private TorrentManager torrentManager;

	@Autowired
	private TorrentSearch torrentSearch;

	@Autowired
	private RssReader rssTorrents;


	private final String search = "search";
	private final String more = "more";
	private final String info = "info";
	private final String link = "link";
	private final String list = "list";

	private final int maxLines = 100;

	private int currentLine = 0;


	private final String[] commands = new String[]{this.search,this.more,this.list,this.info,this.link};

	@Override
	public boolean canHandle(String command) {
		for(String commandlocal : this.commands){
			if(commandlocal.equalsIgnoreCase(command)){
				return true;
			}
		}

		return this.torrentManager.canHandle(command);
	}

	@Override
	public Object processCommand(CommandObject commandObject,String requestFrom) {
		String response="";
		switch(commandObject.mainCommand){
		case search:
			this.currentLine =0;
			this.inuse = InUse.WEB;
			try {
				response = formatResponse(requestFrom,this.torrentSearch.process(commandObject));
			} catch (ProviderException e) {
				response= e.getMessage();
			}
			break;

		case list:
			this.inuse = InUse.RSS;
			this.currentLine =0;
			try {
				response = formatResponse(requestFrom,this.rssTorrents.process(commandObject));
			} catch (ProviderException e) {
				response= e.getMessage();
			}
			break;
		case more:
			response = getMore(requestFrom);
			break;
		case info:
			switch(this.inuse){
			case RSS:
			case WEB:
				response = torrentInfo(requestFrom,commandObject.payload);
				break;
			case UTORRENT:
				response = this.torrentManager.processCommand(commandObject);
				break;
			}
			break;
		case link:
			response =torrentLinks(requestFrom,commandObject.payload);
			break;
		default:
			this.inuse = InUse.UTORRENT;
			response = this.torrentManager.processCommand(commandObject);
			break;

		}
		return response;
	}


	private String getMore(String requestFrom){
		StringBuilder builder = new StringBuilder();

		for(int i = 0;i<this.maxLines;i++){
			if(this.currentLine>=this.torrentList.get(requestFrom).size()){
				break;
			}
			Torrent torrent =  this.torrentList.get(requestFrom).get(i);
			builder.append(this.currentLine).append(": ").append(torrent.title).append("\n\r");
			this.currentLine++;
		}
		return builder.toString();
	}

	private String formatResponse(String requestFrom,List<Torrent> torrents){
		this.torrentList.put(requestFrom, torrents);
		return getMore(requestFrom);
	}

	@Override
	public Torrent getTorrent(String requestFrom,int index) {
		synchronized (this.torrentList) {
			return this.torrentList.get(requestFrom).get(index);
		}
	}

	public String torrentInfo(String requestFrom,List<String> indexs){
		StringBuilder response = new StringBuilder();
		for(int index :parseIndexes(indexs)){
			response.append(index).append(": ").append(infoGen(this.torrentList.get(requestFrom).get(index))).append("\n\r");
		}
		return response.toString();
	}

	public String torrentLinks(String requestFrom,List<String> indexs){
		StringBuilder response = new StringBuilder();
		for(int index :parseIndexes(indexs)){
			response.append(index).append(": ").append(this.torrentList.get(requestFrom).get(index).link).append("\n\r");
		}
		return response.toString();
	}
	private String infoGen(Torrent torrent){

		String size =torrent.size;
		int seeds = torrent.seeds;
		int leechers = torrent.leeches;
		String added = torrent.added;
		String type = torrent.type;

		return String.format("Category: %s Size: %s Added: %s Seeders: %d Leechers: %d ", type,size,added,seeds,leechers);
	}


}
