package za.co.chris;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Bootstrap {

	AnnotationConfigApplicationContext context;
	Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	public Bootstrap() {
		this.context = new AnnotationConfigApplicationContext(ChatBotConfig.class);
	}

	public static void main(String[] args){
		//		System.setProperty("socksProxyHost ", "127.0.0.1");
		//		System.setProperty("socksProxyPort", "8080");

		//		System.setProperty("http.proxyHost ", "127.0.0.1");
		//		System.setProperty("http.proxyPort", "8081");


		new Bootstrap();
	}
}
