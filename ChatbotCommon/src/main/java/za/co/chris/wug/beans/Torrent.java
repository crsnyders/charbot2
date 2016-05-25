package za.co.chris.wug.beans;

public class Torrent {
	public final String title,type,link,size,speed,added,uploader;
	public final int seeds,leeches;

	public Torrent(String title, String type, String link, String size,
			String speed, String age, String uploader, int seeds, int leeches) {
		this.title = title;
		this.type = type;
		this.link = link;
		this.size = size;
		this.speed = speed;
		this.added = age;
		this.uploader = uploader;
		this.seeds = seeds;
		this.leeches = leeches;

	}

}
