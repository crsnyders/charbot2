package za.co.chris.wug.torrent.watcher;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.beans.Torrent;
import za.co.chris.wug.interfaces.Processor;
import za.co.chris.wug.torrent.RssReader;
import za.co.chris.wug.torrent.watcher.elements.Show;
import za.co.chris.wug.torrent.watcher.elements.TVRageShow;

@Component
public class ShowPoller  {

	public static Logger logger = LoggerFactory.getLogger(ShowPoller.class);

	//    option 1 matches How I Met Your Mother - 701 - HDTV XviD LOL.avi
	//    option 2 matches Hawaii Five 0 2010 S02E03 HDTV XviD LOL.torrent

	static String option1 ="(?<showName>.*)\\D*? -(?<season> \\d\\d?)\\D*?(?<episode>\\d\\d) -\\D*? .*";
	static String option2 ="(?<showName>.*)D*?[sS](?<season>\\d\\d)D*?[eE](?<episode>\\d\\d)D*? .*";
	static String option3 ="(?<showName>.*)\\D*? -(?<season> \\d\\d?)\\D*?(?<episode>\\d\\d).*";
	static String option4 ="(?<showName>.*)\\D*?(?<season> \\d\\d?)\\D*?x?(?<episode>\\d\\d)\\D*? .*";

	private final String showNameGroup = "showName";
	private final String seasonGroup = "season";
	private final String episodeGroup = "episode";

	private final String requestFrom = "showPoller";

	public static final String[] REGEX = {option1,option2,option3,option4};

	public static final Pattern[] COMPILED_REGEX = new Pattern[REGEX.length];

	@Autowired
	private ShowHandler showHandler;

	@Autowired
	private RssReader reader;

	@Autowired
	@Qualifier("download")
	private Processor downlaod;

	@Autowired
	@Qualifier("broadcast")
	private Processor broadcast;

	private Torrent firstNowLast;

	static {
		for (int i = 0; i < REGEX.length; i++) {
			COMPILED_REGEX[i] = Pattern.compile(REGEX[i]);
		}
	}

	public  Show parseShow(String fileName) {
		if(fileName.indexOf(".")!=fileName.length()-4) {
			fileName = fileName.substring(0, fileName.length()-4).replace(".", " ").replace("  "," ")+fileName.substring(fileName.length()-4);
		}
		int idx = 0;
		Matcher matcher = null;
		for (int i = 0; i < REGEX.length; i++) {
			matcher = COMPILED_REGEX[i].matcher(fileName);
			if (matcher.matches()) {
				String show = matcher.group(this.showNameGroup).trim();
				int season = Integer.parseInt(matcher.group(this.seasonGroup).trim());
				int episode = Integer.parseInt(matcher.group(this.episodeGroup).trim());
				logger.debug("Matched: "+fileName+" to option "+idx);
				TVRageShow tvrageShow = this.showHandler.getShow(show);
				if(tvrageShow == null){
					return null;
				}
				Show parsedShow = new Show();
				parsedShow.setEpisode(episode);
				parsedShow.setSeason(season);
				parsedShow.setName(tvrageShow.name);
				parsedShow.setShowId(tvrageShow.showid);
				parsedShow.setFormat(getFormat(fileName));
				return parsedShow;
			}
		}
		logger.warn("Could not match:"+fileName);
		return null;
	}

	private int getFormat(String filename){
		if(filename.contains("1080p")){
			return 1080;
		}else if(filename.contains("720p")){
			return 720;
		}else{
			return 480;
		}
	}

	//	@Scheduled(fixedRate=1800000L)
	@Scheduled(fixedRate=60000L)
	private void lookForNew(){
		List<Torrent> torrents =this.reader.listTorrents();
		for(int i = 0;i< torrents.size();i++){
			Torrent torrent = torrents.get(i);
			if(this.firstNowLast!= null && this.firstNowLast.link.equals(torrent.link)){
				break;
			}
			Show parsedShow = parseShow(torrent.title);
			if(parsedShow == null){
				continue;
			}

			if(this.showHandler.isNewEpisode(parsedShow)){
				this.downlaod.processCommand(new CommandObject("download "+i),this.requestFrom);
				this.broadcast.processCommand(new CommandObject("broadcast "+torrent.title+" added to download.\n\r"),this.requestFrom);
			}
		}
		this.firstNowLast = torrents.get(0);
	}
}