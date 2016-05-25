package za.co.chris.wug.xmpp;

import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.springframework.stereotype.Component;

@Component
public class ChatPacketFilter implements PacketFilter{

	@Override
	public boolean accept(Packet packet) {
		return true;
	}

}
