package za.co.chris.wug.torrent.watcher;

import static za.co.chris.wug.utils.NumberUtils.parseIndexes;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.interfaces.Processor;
import za.co.chris.wug.interfaces.Service;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@EnableScheduling
public class Watcher implements Processor,Service {

	private final String list ="list";
	private final String add ="add";
	private final String remove ="remove";
	private final String search ="search";

	public Watcher() {
		System.err.println(Calendar.getInstance().getTime());
	}
	@Autowired
	private ShowHandler showhandler;

	@Override
	public boolean canHandle(String command) {
		return ("watch").equalsIgnoreCase(command);
	}

	@Override
	public Object processCommand(CommandObject commandObject,String requestFrom) {

		if(commandObject.secondaryCommand!= null && commandObject.secondaryCommand.equalsIgnoreCase(this.list)){
			return this.showhandler.listShows();
		}else if(commandObject.secondaryCommand!= null && commandObject.secondaryCommand.equalsIgnoreCase(this.add)){
			return this.showhandler.addShow(commandObject.payload.get(0), 480, 1,0);
		}else if(commandObject.secondaryCommand!= null && commandObject.secondaryCommand.equalsIgnoreCase(this.remove)){
			return this.showhandler.removeShow(parseIndexes(commandObject.payload));
		}
		else if(commandObject.secondaryCommand!= null && commandObject.secondaryCommand.equalsIgnoreCase(this.search)){
			return this.showhandler.searchShow(commandObject.payload.get(0));
		}
		return null;
	}


}
