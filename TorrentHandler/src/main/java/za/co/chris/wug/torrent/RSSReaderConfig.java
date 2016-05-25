package za.co.chris.wug.torrent;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import za.co.chris.wug.torrent.rss.bean.Channel;
import za.co.chris.wug.torrent.rss.bean.Item;
import za.co.chris.wug.torrent.rss.bean.Rss;
import za.co.chris.wug.torrent.watcher.elements.TVRageShow;
import za.co.chris.wug.torrent.watcher.elements.Show;
import za.co.chris.wug.torrent.watcher.elements.ShowStore;
import za.co.chris.wug.torrent.watcher.elements.TVRageResult;

@Configuration
public class RSSReaderConfig {

	@Bean
	@Qualifier("jaxbMarshaller")
	public Jaxb2Marshaller getjaxbMarshaller(){
		Jaxb2Marshaller jb = new Jaxb2Marshaller();
		jb.setClassesToBeBound(Rss.class,Channel.class,Item.class,TVRageResult.class,TVRageShow.class,ShowStore.class,Show.class);
		return jb;
	}
}