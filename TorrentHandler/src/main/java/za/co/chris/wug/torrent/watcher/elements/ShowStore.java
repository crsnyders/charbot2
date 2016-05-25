package za.co.chris.wug.torrent.watcher.elements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


//<shows>
//<show nextEpisode="413" format="">
//     <name>Glee</name>
//     <showid>21704</showid>
//</show>
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ShowStore {
	@XmlElement
	public List<Show> shows = new ArrayList<>();;

	public List<Show> getShows() {
		return this.shows;
	}

	public void setShows(List<Show> shows) {
		this.shows = shows;
	}

	public void addShow(Show show){
		this.shows.add(show);
	}

	public Show removeShow(int index){
		return this.shows.remove(index);
	}
}
