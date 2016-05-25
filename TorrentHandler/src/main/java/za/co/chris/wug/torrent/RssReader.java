package za.co.chris.wug.torrent;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.beans.Torrent;
import za.co.chris.wug.exception.ProviderException;
import za.co.chris.wug.interfaces.Provider;
import za.co.chris.wug.torrent.rss.bean.Item;
import za.co.chris.wug.torrent.rss.bean.Rss;
import za.co.chris.wug.torrent.rss.interfaces.XMLMapper;

@Component
public class RssReader implements Provider<Torrent>{
	private final String list = "list";

	private final String descriptionRegex = "Category: (.*?)  Size: (.*?) Added: (.*?) Seeders: (\\d*) Leechers: (\\d*)";
	private final Pattern descriptionPattern = Pattern.compile(this.descriptionRegex);
	private Matcher matcher;
	private String type;
	private String size;
	private String speed;
	private String age;
	private String uploader;
	private int seeds;
	private int leeches;

	private final Logger logger = LoggerFactory.getLogger(RssReader.class);

	@Autowired
	private XMLMapper mapper;

	@javax.annotation.Resource
	private Environment environment;



	public List<Torrent> listTorrents(){
		List<Torrent> torrents = new ArrayList<>();
		HttpURLConnection connnection = null;
		try{
			connnection = getRssInputStream();
			Rss rssTorrent= (Rss)this.mapper.readXmlObjectFromInputStream(connnection.getInputStream());
			connnection.disconnect();

			for(Item item : rssTorrent.channel.items){
				torrents.add(parseElement(item));
			}
		}catch(IOException e){
			this.logger.error("Exception while fetching rss list.",e);
		}
		finally{
			if(connnection!= null){
				connnection.disconnect();
			}
		}
		return torrents;

	}

	public Torrent parseElement(Item entry){
		this.matcher = this.descriptionPattern.matcher(entry.getDescription());

		if(this.matcher.matches()){
			this.type = this.matcher.group(1);
			this.size = this.matcher.group(2);
			this.age = this.matcher.group(3);
			this.seeds = Integer.parseInt(this.matcher.group(4));
			this.leeches = Integer.parseInt(this.matcher.group(5));
			this.speed ="";
		}
		return new Torrent(entry.getTitle(), this.type, entry.getLink(), this.size, this.speed, this.age, this.uploader, this.seeds, this.leeches);
	}

	private HttpURLConnection getRssInputStream() throws IOException{
		URL rssUrl = new URL(this.environment.getProperty("ctwug.rss.url"));
		HttpURLConnection connection = (HttpURLConnection) rssUrl.openConnection();
		return connection;
	}

	@Override
	public List<Torrent> process(CommandObject command) throws ProviderException {
		switch(command.mainCommand){
		case list:
			return listTorrents();
		default:
			throw new ProviderException("Invalid command");
		}
	}
}
