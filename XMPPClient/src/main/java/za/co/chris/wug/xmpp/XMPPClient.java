package za.co.chris.wug.xmpp;

import javax.inject.Inject;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import za.co.chris.wug.interfaces.Communication;

@Component
public class XMPPClient implements Communication,ConnectionListener{

	private final Logger logger = LoggerFactory.getLogger(XMPPClient.class);
	@Autowired
	XMPPConnection connection;
	@Inject
	private Environment environment;
	@Autowired
	private MessageListener messageListner;
	@Autowired
	private PacketFilter packetFilter;
	@Autowired
	private PacketListener packetListener;
	@Autowired
	private ChatManagerListnerImpl chatManagerListener;

	@Value("${xmpp.username}")
	private String username;
	@Value("${xmpp.password}")
	private String password;
	@Value("${xmpp.default.receiver}")
	private String defaultReceiver;

	@Override
	public void initialize() {
		this.connection.addPacketListener(this.packetListener, this.packetFilter);
		this.connection.getChatManager().addChatListener(this.chatManagerListener);
	}

	@Override
	public void connect() {
		try {
			this.logger.info("Connecting");
			this.connection.connect();
			this.logger.info("Connected");

			this.logger.info("Logging in");
			this.connection.login(this.environment.getProperty("xmpp.username"), this.environment.getProperty("xmpp.password"));
			this.logger.info("Logged in");
			this.connection.addConnectionListener(this);

		} catch (XMPPException e) {
			this.logger.error("Failed to connect/login",e);
		}
	}

	@Override
	public boolean connected() {
		return this.connection.isConnected();
	}

	@Override
	public void disconnect() {
		this.connection.disconnect();
	}

	@Override
	public void send(String message) {
		if(this.chatManagerListener.getlastChat() != null){
			Chat chat = this.chatManagerListener.getlastChat();
			try {
				chat.sendMessage(message);
			} catch (XMPPException e) {
				this.logger.error("Could not send message",e);
			}
		}else{
			Message chatMessage = new Message();
			chatMessage.setTo(this.defaultReceiver);
			chatMessage.setBody(message);
			this.connection.sendPacket(chatMessage);
		}
	}

	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectionClosedOnError(Exception e) {
		this.logger.error("Connection closed",e);

	}

	@Override
	public void reconnectingIn(int seconds) {
		this.logger.info("reconnecting in: {} seconds" ,seconds);

		try {
			Thread.sleep(seconds*1000);
			this.connection.connect();
			this.connection.login(this.username, this.password);
		} catch (XMPPException | InterruptedException e1) {
			this.logger.error("Error reconnecting",e1);
		}
	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reconnectionFailed(Exception e) {
		// TODO Auto-generated method stub

	}


}
