package za.co.chris.wug.torrent.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.Torrent;

@Component
public class ProcessPage {

	private final Logger logger = LoggerFactory.getLogger(ProcessPage.class);

	@Inject
	private Environment environment;

	private Pattern torrentPattern;
	private Matcher torrentMatcher;

	public ProcessPage() {

	}

	public List<Torrent> proccesPage(String page) {
		this.torrentPattern = Pattern.compile(this.environment.getProperty("search.torrent.regex"));
		List<Torrent> tmp = new ArrayList<>();
		this.torrentMatcher = this.torrentPattern.matcher(page);
		while (this.torrentMatcher.find()) {

			String type = this.torrentMatcher.group("type");
			String title = this.torrentMatcher.group("title");
			String link = "http://torrents.ctwug.za.net/"+unCode(this.torrentMatcher.group("link"));
			String uploader = this.torrentMatcher.group("uploader");
			String size = this.torrentMatcher.group("size");
			String speed = "";
			int seeds = Integer.parseInt(this.torrentMatcher.group("seeds"));
			int leeches = Integer.parseInt(this.torrentMatcher.group("leech"));
			String age = this.torrentMatcher.group("age");
			this.logger.debug(title);
			tmp.add(new Torrent(title, type, link, size, speed, age, uploader, seeds, leeches));
		}
		return tmp;
	}

	private String unCode(String url) {
		url = url.replace("%24", "$");
		url = url.replace("%26", "&");
		url = url.replace("%2B", "+");
		url = url.replace("%2C", ",");
		url = url.replace("%2F", "/");
		url = url.replace("%3A", ";");
		url = url.replace("%3D", "=");
		url = url.replace("%3F", "?");
		url = url.replace("%20", " ");
		url = url.replace("%22", "\"");
		url = url.replace("%3C", "<");
		url = url.replace("%3E", ">");
		url = url.replace("%23", "#");
		url = url.replace("%25", "%");
		url = url.replace("%7B", "{");
		url = url.replace("%7D", "}");
		url = url.replace("%7C", "|");
		url = url.replace("%5C", "\\");
		url = url.replace("%5E", "^");
		url = url.replace("%7E", "~");
		url = url.replace("%5B", "[");
		url = url.replace("%5D", "]");
		url = url.replace("%28", "(");
		url = url.replace("%29", ")");
		url = url.replace("%2A", "*");
		url = url.replace("%27", "'");

		return url;
	}
}
