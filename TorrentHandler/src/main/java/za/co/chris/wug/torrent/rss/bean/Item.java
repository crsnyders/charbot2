package za.co.chris.wug.torrent.rss.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

	@XmlElement
	public String title;
	@XmlElement
	public String guid;
	@XmlElement
	public String link;
	@XmlElement
	public String pubDate;
	@XmlElement
	public String category;
	@XmlElement
	public String description;

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGuid() {
		return this.guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getLink() {
		return this.link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getPubDate() {
		return this.pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getCategory() {
		return this.category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
//<item>
//<title>The.Crazy.Ones.S01E18.HDTV.x264-KILLERS</title>
//<guid>http://torrents.ctwug.za.net/download.php?id=36719&amp;passkey=c7e2d88a52c7acde0d0e73f19aad26e6</guid>
//<link>http://torrents.ctwug.za.net/download.php?id=36719&amp;passkey=c7e2d88a52c7acde0d0e73f19aad26e6</link>
//<pubDate>Fri, 14 Mar 2014 10:31:16 +0200</pubDate>
//<category> TV: SD-Series</category>
//<description>Category: TV: SD-Series  Size: 191.42 MB Added: 2014-03-14 10:31:16 Seeders: 19 Leechers: 4</description>
//</item>