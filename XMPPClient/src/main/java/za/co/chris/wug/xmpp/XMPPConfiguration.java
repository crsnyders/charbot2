package za.co.chris.wug.xmpp;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
//@ComponentScan("za.co.chris.wug.xmpp")
public class XMPPConfiguration {

	@Autowired
	private MessageListener messageListner;
	@Autowired
	private PacketFilter packetFilter;
	@Autowired
	private PacketListener packetListener;
	@Value("${xmpp.host}")
	private String host;
	@Value("${xmpp.port}")
	private int port;
	@Value("${xmpp.serviceName}")
	private String serviceName;
	@Bean(name = "googleTalkConnection")
	public XMPPConnection getConnection() {
		return new XMPPConnection(getConnectionConfig());
	}

	@Bean
	public ConnectionConfiguration getConnectionConfig() {
		ConnectionConfiguration config = new ConnectionConfiguration(this.host, this.port, this.serviceName);
		return config;
	}

	@Bean(name = "recieverConnected")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public AtomicBoolean reciverConnected() {
		return new AtomicBoolean();
	}

}
