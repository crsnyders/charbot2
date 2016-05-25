package za.co.chris.wug.xmpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import za.co.chris.wug.interfaces.Communication;

public class XMPPTest{

	private final ApplicationContext context;
	private final Logger logger = LoggerFactory.getLogger(XMPPTest.class);

	public XMPPTest() {
		this.context = new AnnotationConfigApplicationContext(XMPPConfiguration.class);
		Communication connection = this.context.getBean(Communication.class);
		connection.initialize();
		connection.connect();
		while(true){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args){
		new XMPPTest();
	}

}

