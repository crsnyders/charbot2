package za.co.chris.wug.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatManagerListnerImpl implements ChatManagerListener {

	@Autowired
	MessageListener chatmessageListener;

	private Chat chat;

	@Override
	public void chatCreated(Chat chat, boolean createdLocally) {
		if(!createdLocally){
			chat.addMessageListener(this.chatmessageListener);
			this.chat = chat;
		}
	}

	public Chat getlastChat(){
		return this.chat;
	}

}
