package za.co.chris.wug.xmpp;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.AdHocCommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChatPacketListener implements PacketListener{

	private final Logger logger = LoggerFactory.getLogger(ChatPacketListener.class);

	@Autowired
	private AtomicBoolean recieverConnected;

	@Value("${xmpp.default.receiver}")
	String defaultReceiver;

	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Presence) {
			Presence presence = (Presence) packet;
			String from = presence.getFrom();
			if (from.contains("/")) {
				from = from.substring(0, from.indexOf("/"));
			}
			if (from.equals(this.defaultReceiver)) {
				if (presence.getType().equals(Presence.Type.available)) {
					this.recieverConnected = new AtomicBoolean(true);
				} else if (presence.getType().equals(
						Presence.Type.unavailable)) {
					this.recieverConnected = new AtomicBoolean(false);;
				}
			}
		}else if(packet instanceof AdHocCommandData){
			AdHocCommandData p = (AdHocCommandData)packet;
			this.logger.info(p.toXML());
		}
	}

}
