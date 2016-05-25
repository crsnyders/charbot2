package za.co.chris.wug.processor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.beans.DownloadItem;
import za.co.chris.wug.beans.Torrent;
import za.co.chris.wug.interfaces.IndexLinkHandler;
import za.co.chris.wug.interfaces.Processor;
import za.co.chris.wug.utils.NumberUtils;
import za.co.chris.wug.utils.TTLogin;

@Component("download")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Downloader implements Processor {
	@Inject
	private Environment environment;

	@Autowired
	private TTLogin login;

	private final String DOWNLOAD_OPTION = "download";
	private final String DOWNLOAD_DESCRIPTION = "To use " + this.DOWNLOAD_OPTION + " either specify an index number from list or input "
			+ "a filename and http link seperated by a \",\"\r\n" + "e.g. " + this.DOWNLOAD_OPTION + " 5 or "
			+ this.DOWNLOAD_OPTION + " somefile.zip,http:\\\\someaddress";
	final static int BYTE_SIZE = 1024;
	private final Logger logger = LoggerFactory.getLogger(Downloader.class);
	private final int poolSize = 5;
	private final ExecutorService pool = Executors.newFixedThreadPool(this.poolSize);

	@Autowired(required=false)
	private IndexLinkHandler linkHandler;


	@Override
	public Object processCommand(CommandObject commandObject,String requestFrom) {
		try {
			if(commandObject.payload.size()==0){
				return this.DOWNLOAD_DESCRIPTION;
			}
			return this.pool.submit(new DownloadThread(getDownloadList(requestFrom,commandObject.payload))).get();
		} catch (InterruptedException | ExecutionException e) {
			return "Could not download: "+e.getMessage();
		}
	}

	private List<DownloadItem> getDownloadList(String requestFrom,List<String> payload) {
		List<DownloadItem> downloadList = new ArrayList<>();
		if (payload.size() == 2 && !isInteger(payload.get(1))) {
			if(payload.get(0).startsWith("C:/") || payload.get(0).startsWith("C:\\")){
				String location = payload.get(0);
				location = this.environment.getProperty("download.default.location")+location;
				payload.set(0, location);
			}
			File file = new File(payload.get(0));
			downloadList.add(new DownloadItem(file.getName(), payload.get(1), file.getParent()));
		} else {
			List<Integer> indexes = NumberUtils.parseIndexes(payload);
			for (Integer index : indexes) {
				if (this.linkHandler!= null) {
					Torrent torrent = this.linkHandler.getTorrent(requestFrom,index);
					try {
						downloadList.add(new DownloadItem(torrent.title+".torrent", torrent.link, this.environment.getProperty("download.rss.location"),this.login.getCookies()));
					} catch (IOException e) {
						this.logger.error("Could not add item to download list, could not get cookies.",e);
					}
				}
			}
		}
		return downloadList;
	}

	private boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}


	class DownloadThread implements Callable<String> {
		StringBuilder output = new StringBuilder();
		OutputStream outStream = null;
		URLConnection urlConnection = null;
		List<DownloadItem> downloadList;
		private InputStream is = null;

		public DownloadThread(List<DownloadItem> downloadList) {
			this.downloadList = downloadList;
		}

		@Override
		public String call() throws Exception {
			for (DownloadItem downloadItem : this.downloadList) {
				download(downloadItem);
			}

			return this.output.toString();
		}

		private void download(DownloadItem downloadItem) {
			byte[] buf;
			int ByteRead, bytesWritten = 0;
			String destinationDir = downloadItem.getDestination();
			String localFileName = downloadItem.getTitle();
			if (!destinationDir.endsWith("\\") && !destinationDir.endsWith("/")) {
				destinationDir = destinationDir + "/";
			}

			try {
				URL url = new URL(downloadItem.getLink());
				this.outStream = new BufferedOutputStream(new FileOutputStream(destinationDir + localFileName));
				this.urlConnection = url.openConnection();
				if(downloadItem.getCookies()!= null){
					this.urlConnection.setRequestProperty("Cookie", downloadItem.getCookies());
				}
				this.is = this.urlConnection.getInputStream();
				buf = new byte[BYTE_SIZE];
				while ((ByteRead = this.is.read(buf)) != -1) {
					this.outStream.write(buf, 0, ByteRead);
					bytesWritten += ByteRead;
				}
				this.output.append("Downloaded Successfully.\n\r");
				this.output.append("File downloaded to:\"").append(destinationDir).append(localFileName).append("\"\n\rNo ofbytes :").append(bytesWritten).append("\r\n");
			} catch (IOException e) {
				if (e instanceof IOException) {
					this.output.append("Could not download. \n").append(e).append("\r\n");
					Downloader.this.logger.error(this.output.toString());
				} else {
					this.output.append("Cannot create file at point: ").append(destinationDir).append(localFileName).append("\r\n");
					Downloader.this.logger.error(this.output.toString());
				}
			}

			finally {
				try {
					this.is.close();
					this.outStream.close();
				} catch (IOException e) {
					Downloader.this.logger.error("Exception occured while cloaing streams.\n\r" + e);
				}

			}
		}

	}

	@Override
	public boolean canHandle(String command) {
		return command.equalsIgnoreCase("download");
	}

}
