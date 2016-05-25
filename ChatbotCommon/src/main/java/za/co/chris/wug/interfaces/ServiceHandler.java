package za.co.chris.wug.interfaces;

import za.co.chris.wug.exception.UnknownServiceException;


public interface ServiceHandler {
	public Service getService(String key) throws UnknownServiceException;

	public void stopService(Service service);

	public void startService(Service service);


}
