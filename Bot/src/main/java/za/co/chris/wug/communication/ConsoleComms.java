package za.co.chris.wug.communication;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.exception.UnknownProcessorException;
import za.co.chris.wug.interfaces.Communication;
import za.co.chris.wug.interfaces.Processor;
import za.co.chris.wug.interfaces.ProcessorHandler;


@Component
public class ConsoleComms implements Communication,Runnable {

	private final Logger logger = LoggerFactory.getLogger(ConsoleComms.class);


	@Autowired
	Environment environment;

	@Autowired(required=false)
	private ProcessorHandler procHandler;

	private ExecutorService executor;

	private final String requestFrom = "console";

	private Scanner in;

	@Override
	public void initialize() {
		this.in = new Scanner(System.in);
		this.executor = Executors.newSingleThreadExecutor();
	}

	@Override
	public boolean connected() {
		return true;
	}

	@Override
	public void connect() {
		this.executor.submit(this);
	}

	@Override
	public void disconnect() {

	}

	@Override
	public void send(String message) {
		this.logger.info(message);
	}

	@Override
	public void run() {
		while(this.in.hasNext()){
			System.out.print("Enter Command: ");
			String msg= this.in.nextLine();
			this.logger.info("Message Received: [{}]",msg.trim());
			CommandObject command = new CommandObject(msg.trim());
			Processor proc;
			try {
				proc = this.procHandler.getprocessor(command.mainCommand);

				Object response = null;
				if(proc != null){

					response= proc.processCommand(command,this.requestFrom);
					send(response.toString());

				}
			} catch (UnknownProcessorException e) {
				e.printStackTrace();
			}
		}
	}


}
