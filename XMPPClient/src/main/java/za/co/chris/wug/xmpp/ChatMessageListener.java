package za.co.chris.wug.xmpp;

import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.exception.UnknownProcessorException;
import za.co.chris.wug.interfaces.Processor;
import za.co.chris.wug.interfaces.ProcessorHandler;

@Component
public class ChatMessageListener implements MessageListener {

	private final Logger logger = LoggerFactory.getLogger(ChatMessageListener.class);

	@Autowired(required=false)
	private ProcessorHandler procHandler;

	private final String requestFrom = "xmpp";

	@Override
	public void processMessage(Chat chat, Message message) {
		Collection<Body> bodies = message.getBodies();
		for(Body body: bodies){
			try {
				this.logger.info(body.getMessage());
				CommandObject command = new CommandObject(body.getMessage());
				Processor proc;
				try{
					proc =this.procHandler.getprocessor(command.mainCommand);
					String result = (String)proc.processCommand(command,this.requestFrom);
					chat.sendMessage(result.replace("\r", ""));
				}catch (UnknownProcessorException e) {
					chat.sendMessage("Could not proccess");
				}
			} catch (XMPPException e) {
				this.logger.error("Eception sending chat message",e);
			}
		}
	}

}
