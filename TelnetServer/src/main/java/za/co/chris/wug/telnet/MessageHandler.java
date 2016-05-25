package za.co.chris.wug.telnet;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.chris.wug.beans.CommandObject;
import za.co.chris.wug.interfaces.Processor;
import za.co.chris.wug.interfaces.ProcessorHandler;



@Sharable
@Component("messageHandler")
public class MessageHandler extends SimpleChannelInboundHandler<String> {

	private final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

	@Autowired(required=false)
	private ProcessorHandler procHandler;

	public boolean isActive;

	private final String requestFrom = "telnet";
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// Send greeting for a new connection.
		this.isActive = true;
		ctx.write(
				"Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
		ctx.write("It is " + new Date() + " now.\r\n");
		ctx.flush();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		try{
			msg = trimNonChars(msg);
			this.logger.info("Message Received: [{}]",msg.trim());
			CommandObject command = new CommandObject(msg.trim());
			Processor proc = this.procHandler.getprocessor(command.mainCommand);
			Object response = null;
			if(proc != null){

				response= proc.processCommand(command,this.requestFrom);
				ctx.writeAndFlush( response.toString());

			}
		}catch(Exception e){
			ctx.writeAndFlush(e.getMessage()+"\r\n");
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		super.disconnect(ctx, promise);
		this.isActive= false;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		this.logger.error("Unexpected exception from downstream.", cause);
	}

	private String trimNonChars(String message){
		char[] chars = message.toCharArray();

		List<Character> newString = new ArrayList<>();
		for(int i =0;i<chars.length;i++){
			if(Integer.valueOf(chars[i]) >31 && Integer.valueOf(chars[i])<256){
				newString.add(new Character(chars[i]));
			}
		}
		char[] newChars = new char[newString.size()];

		for(int i = 0; i< newString.size();i++){
			newChars[i] = newString.get(i);
		}
		return new String(newChars).replaceFirst("'", "");
	}
}
