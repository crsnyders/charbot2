package za.co.chris.wug.telnet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import za.co.chris.wug.interfaces.Communication;

public class TelnetServer {

	AnnotationConfigApplicationContext context;
	public TelnetServer() {
		this.context = new AnnotationConfigApplicationContext(TelnetServerConfig.class);
		this.context.scan("za.co.chris.wug.telnet");
		while(true){
			Communication coms = this.context.getBean(Communication.class);
			coms.initialize();
			coms.connect();
			System.out.println(coms.connected());
			while(true){
				try {
					Thread.sleep(5000);
					coms.send("test");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args){
		new TelnetServer();
	}
}
