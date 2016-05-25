package za.co.chris.wug.torrent;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.beans.Torrent;
import za.co.chris.wug.exception.ProviderException;
import za.co.chris.wug.interfaces.Provider;
import za.co.chris.wug.torrent.search.ReadWebPage;


@Component
public class TorrentSearch implements Provider<Torrent>{

	private final Logger logger = LoggerFactory.getLogger(TorrentSearch.class);

	@Autowired
	private ReadWebPage readWebPage;

	@Override
	public List<Torrent> process(CommandObject command) throws ProviderException{
		try {
			return this.readWebPage.search(command.payload.get(0));
		} catch (IOException e) {
			this.logger.error("Could not get results",e);
			throw new ProviderException("Could not get results",e);
		}
	}

}
