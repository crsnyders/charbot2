package za.co.chris.wug.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import za.co.chris.wug.exception.UnknownServiceException;
import za.co.chris.wug.interfaces.Service;
import za.co.chris.wug.interfaces.ServiceHandler;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Services implements ServiceHandler{

	@Autowired(required=false)
	private List<Service> servicesList;

	private List<Future<?>> futureList;

	private final ExecutorService executor = Executors.newCachedThreadPool();

	public Services() {
		//		if(this.servicesList != null){
		//			for(Service service : this.servicesList){
		//				this.futureList.add(this.executor.submit(service));
		//			}
		//		}
		//		String out = new java.util.Scanner(new java.net.URL("http://www.whatsmyip.co.za").openStream()).useDelimiter("\\A").next();
	}

	@Override
	public Service getService(String key) throws UnknownServiceException {
		for(Service service : this.servicesList){
			if(service.canHandle(key)){
				return service;
			}
		}

		return null;
	}

	@Override
	public void stopService(Service service) {

	}

	@Override
	public void startService(Service service) {
		// TODO Auto-generated method stub

	}

}
