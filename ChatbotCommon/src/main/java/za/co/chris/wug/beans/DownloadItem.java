package za.co.chris.wug.beans;

public class DownloadItem {

	private String title;
	private String link;
	private String destination;
	private String cookies;

	public DownloadItem(String title, String link, String destination,String cookies) {
		this.title = title;
		this.link = link;
		this.destination = destination;
		this.cookies = cookies;
	}

	public DownloadItem(String title, String link, String destination) {
		this.title = title;
		this.link = link;
		this.destination = destination;
	}

	public DownloadItem(String title, String link) {
		this.title = title;
		this.link = link;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getCookies() {
		return this.cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	@Override
	public String toString() {
		return this.title + " : " + this.link;
	}
}
