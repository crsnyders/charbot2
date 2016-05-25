package za.co.chris.wug.interfaces;

import za.co.chris.wug.exception.UnknownProcessorException;

public interface ProcessorHandler {

	public Processor getprocessor(String key) throws UnknownProcessorException;

}
