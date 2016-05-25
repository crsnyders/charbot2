package za.co.chris.wug.torrent.search;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.Torrent;
import za.co.chris.wug.utils.TTLogin;

@Component
public class ReadWebPage {

	private final Environment environment;

	@Autowired
	private TTLogin login;

	private final Logger logger = LoggerFactory.getLogger(ReadWebPage.class);
	private final String torrentPageRegex;

	private final Pattern torrentPagePattern;
	private Matcher torrentPageMatcher;

	@Autowired
	private ProcessPage processPage;

	@Inject
	public ReadWebPage(Environment environment) {
		this.environment =environment;
		this.torrentPageRegex = this.environment.getProperty("search.page.regex");
		this.torrentPagePattern = Pattern.compile(this.torrentPageRegex);
	}

	boolean firstRead = true;
	int pages = 1;

	public List<Torrent> search(String param) throws IOException {
		return search(param,0);
	}
	public List<Torrent> search(String param,int pageNumber) throws IOException {
		String urlAddress = String.format(this.environment.getProperty("search.utl.pattern"),param.replace(" ", "+"),pageNumber);

		this.logger.info("Search " + param.replace(" ", "+") + " pageNumber " + pageNumber );

		URL url = new URL(urlAddress);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);

		connection.setRequestProperty("Cookie", this.login.getCookies());
		this.logger.debug("Search url: " + urlAddress);
		this.logger.debug("cookie settings: " + this.login.getCookies());
		Scanner input = new Scanner(new InputStreamReader(connection.getInputStream()));

		StringBuilder page = new StringBuilder();
		while (input.hasNext()) {
			page.append(input.nextLine());
		}
		input.close();
		connection.disconnect();

		List<Torrent> tmp = this.processPage.proccesPage(page.toString());
		this.torrentPageMatcher = this.torrentPagePattern.matcher(page);
		if(this.torrentPageMatcher.find()) {
			tmp.addAll(search(param,Integer.parseInt(this.torrentPageMatcher.group(1))));
		}
		return tmp;
	}
}
