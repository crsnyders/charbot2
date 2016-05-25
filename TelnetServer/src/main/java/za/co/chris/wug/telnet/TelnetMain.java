package za.co.chris.wug.telnet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import za.co.chris.wug.interfaces.Communication;

@Component
public class TelnetMain implements Communication {

	private final Logger logger = LoggerFactory.getLogger(TelnetMain.class);

	@Autowired
	private ServerBootstrap bootstrap;

	@Autowired
	Environment environment;

	@Autowired
	@Qualifier("ChannelInitializer")
	private ChannelHandler initializer;

	@Value("${telnet.server.port}")
	private int serverPort;

	@Autowired
	@Qualifier("group")
	private NioEventLoopGroup group;

	@Autowired
	@Qualifier("messageHandler")
	private MessageHandler messageHandler;

	private final ChannelGroup channelGroup = new DefaultChannelGroup(new DefaultEventExecutor());

	@Override
	public void initialize() {
		this.bootstrap.group(this.group,this.group).channel(NioServerSocketChannel.class);
		this.bootstrap.childHandler(this.initializer);
	}

	@Override
	public boolean connected() {
		return this.messageHandler.isActive;
	}

	@Override
	public void connect() {
		try{
			Channel channel = this.bootstrap.bind(this.serverPort).sync().channel();
			this.channelGroup.add(channel);
		} catch (InterruptedException e) {
			this.logger.error("Could not bind telnet server",e);
		}
	}

	@Override
	public void disconnect() {
		this.group.shutdownGracefully();
	}

	@Override
	public void send(String message) {
		this.channelGroup.writeAndFlush(message);
	}

}

