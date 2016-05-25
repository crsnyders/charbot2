package za.co.chris.wug.torrent.watcher.elements;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

//<show>
//<showid>16137</showid>
//<name>Britain's Got Talent</name>
//<link>http://www.tvrage.com/Britains_Got_Talent</link>
//<country>UK</country>
//<started>2007</started>
//<ended>0</ended>
//<seasons>8</seasons>
//<status>Returning Series</status>
//<classification>Reality</classification>
//<genres>
//<genre>Comedy</genre>
//<genre>Dance</genre>
//<genre>Family</genre>
//<genre>Music</genre>
//<genre>Talent</genre>
//</genres>
//</show>
@XmlRootElement(name="show")
@XmlAccessorType(XmlAccessType.FIELD)
public class TVRageShow {
	@XmlElement
	public int showid;
	@XmlElement
	public String name;
	@XmlElement
	public String link;
	@XmlElement
	public String country;
	@XmlElement
	public String started;
	@XmlElement
	public String ended;
	@XmlElement
	public String seasons;
	@XmlElement
	public String status;
	@XmlElement
	public String classification;
	@XmlElementWrapper(name="genres")
	public List<String> genre;

	public int getShowid() {
		return this.showid;
	}
	public void setShowid(int showid) {
		this.showid = showid;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return this.link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getCountry() {
		return this.country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getStarted() {
		return this.started;
	}
	public void setStarted(String started) {
		this.started = started;
	}
	public String getEnded() {
		return this.ended;
	}
	public void setEnded(String ended) {
		this.ended = ended;
	}
	public String getSeasons() {
		return this.seasons;
	}
	public void setSeasons(String seasons) {
		this.seasons = seasons;
	}
	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getClassification() {
		return this.classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public List<String> getGenre() {
		return this.genre;
	}
	public void setGenre(List<String> genre) {
		this.genre = genre;
	}

}
