package za.co.chris.wug.torrent.watcher.elements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//<show nextEpisode="413" format="">
//<name>Glee</name>
//<showid>21704</showid>
//</show>
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Show {

	@XmlAttribute
	public int season;
	@XmlAttribute
	public int episode;
	@XmlAttribute
	public int format;
	@XmlElement
	public String name;
	@XmlElement
	public int showId;


	public Show() {
	}

	public Show( int showId,String name,int season, int episode,int format) {
		this.season =season;
		this.episode = episode;
		this.format = format;
		this.name = name;
		this.showId = showId;
	}


	public int getSeason() {
		return this.season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public int getEpisode() {
		return this.episode;
	}

	public void setEpisode(int episode) {
		this.episode = episode;
	}

	public int getFormat() {
		return this.format;
	}
	public void setFormat(int format) {
		this.format = format;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getShowId() {
		return this.showId;
	}
	public void setShowId(int showId) {
		this.showId = showId;
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(this.name).append(" ").append( this.season).append("x").append(this.episode);
		return builder.toString();
	}

}
