package za.co.chris.wug.processor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import za.co.chris.wug.exception.UnknownProcessorException;
import za.co.chris.wug.interfaces.Processor;
import za.co.chris.wug.interfaces.ProcessorHandler;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ProcessorHandlerImpl implements ProcessorHandler{

	@Autowired
	List<Processor> processors;

	@Override
	public  Processor getprocessor(String key) throws UnknownProcessorException {
		for(Processor proc : this.processors){
			if(proc.canHandle(key)){
				return proc;
			}
		}

		throw new UnknownProcessorException("No Proccessor Found");
	}
}
