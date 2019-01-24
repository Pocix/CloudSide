package com.dcp.netty.work;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dcp.netty.handler.ClientHandler;
import com.dcp.netty.util.Msg;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
//文件上传客户端
public class DcpClient {
	

	private Logger log = LoggerFactory.getLogger(DcpClient.class);
	
	private volatile EventLoopGroup workerGroup;
	private volatile Bootstrap boot;
	private volatile boolean closed = false;
    private int remotePort;
    private String remoteHost;
    private Msg msg;
    
	public void init() throws Exception {
		closed = false;
		EventLoopGroup group = new NioEventLoopGroup(4);
			boot = new Bootstrap();
			boot.group(group).channel(NioSocketChannel.class)
//		            .option(ChannelOption.SO_KEEPALIVE, true)
					.handler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addFirst(new ChannelInboundHandlerAdapter() {
								@Override
								public void channelInactive(ChannelHandlerContext ctx) throws Exception {
									super.channelInactive(ctx);
									ctx.channel().eventLoop().schedule(() -> doConnect(), 1, TimeUnit.SECONDS);
								}
							});
							ch.pipeline().addLast(new ObjectEncoder());
							ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
							ch.pipeline().addLast(new ClientHandler(msg));
						}
					});
	}

	private void doConnect() {
		if (closed) {
			return;
		}
		ChannelFuture future = boot.connect(new InetSocketAddress(remoteHost, remotePort));
		future.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture f) throws Exception {
				if (f.isSuccess()) {
					log.info("Started Tcp Client: " + getServerInfo());
				} else {
					log.info("Started Tcp Client Failed: " + getServerInfo());
					f.channel().eventLoop().schedule(() -> doConnect(), 1, TimeUnit.SECONDS);
				}
			}
		});
	}
	
	public void close() {
		closed = true;
		workerGroup.shutdownGracefully();
		log.info("Stopped Tcp Client: " + getServerInfo());
	}
	
	public static final int FILE_PORT = 9991;
	
	public static void main(String[] args) {
		int port = FILE_PORT;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		try {
			Msg msg = new Msg();
			msg.setTitle("消息标题");
			msg.setType("1");
			msg.setContent("abccccc");
			DcpClient client = new DcpClient();
			client.remoteHost = "127.0.0.1";
			client.remotePort = port;
			client.msg = msg;
			client.init();
			client.doConnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getServerInfo() {
		return String.format("RemoteHost=%s RemotePort=%d", remoteHost, remotePort);
	}
}
