package za.co.chris.wug.telnet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelnetServerConfig {

	//	@Bean(name="eventGroup")
	//	public NioEventLoopGroup eventLoopGroup(){
	//		return new NioEventLoopGroup();
	//	}
	@Bean(name="group")
	public NioEventLoopGroup workerGroup(){
		return new NioEventLoopGroup();
	}

	@Bean
	public ServerBootstrap getBootstrap() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		return bootstrap;
	}
}
