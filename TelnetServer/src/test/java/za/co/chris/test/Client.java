package za.co.chris.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

	public Client() throws InterruptedException {
		Bootstrap boot = new Bootstrap();
		boot.group(new NioEventLoopGroup())
		.handler(new PipelineConfig())
		.channel(NioSocketChannel.class)
		.connect("127.0.0.1", 11112).sync().channel();
	}

	public static void main(String[] args) throws InterruptedException{
		new Client();
	}
}
