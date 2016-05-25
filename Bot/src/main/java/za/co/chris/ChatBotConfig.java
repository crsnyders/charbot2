package za.co.chris;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import za.co.chris.wug.interfaces.Communication;

@Configuration
@ComponentScan("za.co.chris.wug")
@PropertySource(value={"file:${configpath}config.properties"})
public class ChatBotConfig {

	private final Logger logger = LoggerFactory.getLogger(ChatBotConfig.class);

	@Autowired
	private List<Communication> communications;

	@PostConstruct
	private void initializeComms(){
		for(Communication communication: this.communications){
			this.logger.info("Initializing communication: {}",communication.getClass().getSimpleName());
			communication.initialize();
			this.logger.info("Connecting communication: {}",communication.getClass().getSimpleName());
			communication.connect();
		}
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
		properties.setLocation(new ClassPathResource("application.properties"));
		properties.setIgnoreResourceNotFound(false);
		return properties;
	}
}
