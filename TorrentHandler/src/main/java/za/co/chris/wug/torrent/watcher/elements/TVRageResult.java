package za.co.chris.wug.torrent.watcher.elements;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//<Results>
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
//</Results>
@XmlRootElement(name="Results")
@XmlAccessorType(XmlAccessType.FIELD)
public class TVRageResult {

	@XmlElement
	public List<TVRageShow> show;

	public List<TVRageShow> getShow() {
		return this.show;
	}

	public void setShow(List<TVRageShow> show) {
		this.show = show;
	}
}
