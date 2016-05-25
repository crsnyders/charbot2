package za.co.chris.wug.interfaces;

public interface Communication {

	public void initialize();

	public boolean connected();

	public void connect();

	public void disconnect();

	public void send(String message);
}
