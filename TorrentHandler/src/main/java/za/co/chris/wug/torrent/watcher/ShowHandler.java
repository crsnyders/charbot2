package za.co.chris.wug.torrent.watcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import za.co.chris.wug.torrent.rss.interfaces.XMLMapper;
import za.co.chris.wug.torrent.watcher.elements.Show;
import za.co.chris.wug.torrent.watcher.elements.ShowStore;
import za.co.chris.wug.torrent.watcher.elements.TVRageResult;
import za.co.chris.wug.torrent.watcher.elements.TVRageShow;

@Component
public class ShowHandler {
	private final String showSearch = "http://services.tvrage.com/feeds/search.php?show=";

	@Value("${configpath}")
	private String configPath;

	private final String showStoreFile  ="shows.xml";
	private final Logger logger = LoggerFactory.getLogger(ShowHandler.class);
	private ShowStore showStore;

	@Autowired
	private XMLMapper mapper;

	public TVRageResult search(String show){
		HttpURLConnection connection = null;
		try{
			connection = getTvrageInputStream(show);
			TVRageResult results  = (TVRageResult) this.mapper.readXmlObjectFromInputStream(connection.getInputStream());
			return results;
		}catch(IOException e){
			this.logger.error("Could not find shows",e);
			return null;
		}finally{
			if(connection != null){
				connection.disconnect();
			}
		}
	}
	public TVRageShow getShow(String show){

		TVRageResult results  = search(show);
		if(results!= null){
			List<TVRageShow> shows = results.getShow();
			if(shows!= null && shows.size() >0){
				return shows.get(0);
			}
		}
		return null;

	}


	private HttpURLConnection getTvrageInputStream(String show) throws IOException{
		URL rssUrl = new URL(this.showSearch+show.replace(" ", "+"));
		HttpURLConnection connection = (HttpURLConnection) rssUrl.openConnection();
		return connection;
	}

	private ShowStore getShowStore(){
		if(this.showStore == null) {
			try {
				this.showStore = (ShowStore) this.mapper.readXmlObjectFromInputStream(new FileInputStream(this.configPath+this.showStoreFile));
			} catch (IOException e) {
				this.logger.error("Could not read show file");
				this.showStore =  new ShowStore();
			}
		}
		return this.showStore;
	}

	private ShowStore writeShowStore(){
		if(this.showStore != null) {
			try {
				this.mapper.writeXmlObjectToOutputStream(this.showStore, new FileOutputStream(this.configPath+this.showStoreFile));
			} catch (IOException e) {
				this.logger.error("Could not write show file");
			}
		}
		return this.showStore;
	}

	public String listShows() {
		ShowStore store = getShowStore();
		StringBuilder builder = new StringBuilder();
		for(int i =0;i< store.getShows().size();i++){
			builder.append(i).append(") ").append(store.getShows().get(i)).append("\n\r");
		}
		return builder.toString();
	}



	public String addShow(String show,int format, int season, int episode){
		TVRageShow showresult =  getShow(show);
		if(showresult == null){
			return "Could not find show '".concat(show).concat("'\n\r");
		}else{
			Show newShow = new Show(showresult.showid,showresult.name,season,episode,format);
			getShowStore().addShow(newShow);
			writeShowStore();
			return "Show: '".concat(newShow.name).concat("' added to watch list.\n\r");
		}
	}
	public String removeShow(List<Integer> index){
		StringBuilder builder = new StringBuilder();
		Collections.sort(index);
		Collections.reverse(index);
		for(int i =0;i<index.size();i++){
			Show removed = getShowStore().removeShow(index.get(i));
			builder.append("Show '").append(removed.name).append("' removed from watch list.\n\r");
		}
		writeShowStore();
		return builder.toString();
	}


	public boolean isNewEpisode(Show parsedShow) {
		for(Show show : getShowStore().getShows()){
			if(parsedShow.showId == show.showId){
				if(parsedShow.season>= show.season){
					if(parsedShow.getEpisode()> show.getEpisode()){
						show = parsedShow;
						writeShowStore();
						return true;
					}
				}
			}
		}
		return false;
	}


	public String searchShow(String showName) {
		TVRageResult results = search(showName);
		StringBuilder builder = new StringBuilder();
		if(results != null){
			for(TVRageShow show : results.show){
				builder.append(show.getName()).append(" : ").append(show.getLink()).append("\n\r");
			}
		}else{
			builder.append("No results.\n\r");
		}
		return builder.toString();
	}

}
