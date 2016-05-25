package za.co.chris.wug.telnet;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("ChannelInitializer")
public class PipelineConfig extends ChannelInitializer<SocketChannel>{

	@Autowired
	@Qualifier("messageHandler")
	private ChannelHandler messageHandler;
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast("String decoder", new StringDecoder());
		pipeline.addLast("String encoder",new StringEncoder());
		pipeline.addLast("Message handler", this.messageHandler);
	}

}
