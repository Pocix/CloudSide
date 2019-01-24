package com.cloudside.netty.work;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.cloudside.netty.common.FileUploadFile;
import com.cloudside.netty.common.Msg;
import com.cloudside.netty.common.NettyChannelMap;
import com.cloudside.netty.handler.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
//文件上传服务端 
public class DcpServer {
	
    public void bind(int port,
			final FileUploadFile fileUploadFile) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                	System.out.println("有客户端连接上来:"+ch.localAddress().toString());
                	SocketChannel socketChannel = (SocketChannel) ch;
                	NettyChannelMap.getInstance().addSocketChannel(ch.localAddress().toString(), socketChannel);
                	ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));  
                    ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null))); // 最大长度
                    ch.pipeline().addLast(new ServerHandler(fileUploadFile));
                }
            });
            ChannelFuture f = b.bind(port).sync();
            System.out.println("server 等待连接：");
            
			// 开启定时器推送
//			new Thread(new Runnable() {
//				public void run() {
//					try {
//						while (true) {
//							Thread.sleep(1000 * 1);
//							Msg pushMsg = new Msg();
//							pushMsg.setContent("当前时间为：" + new Date());
//							pushMsg.setTitle("服务端推送整点报时");
//							NettyChannelMap.getInstance().pushMsg2AllClient(pushMsg);
//						}
//
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//
//				}
//			}).start();
            f.channel().closeFuture().sync();
            System.out.println("end");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port =  FILE_PORT;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        try {
			FileUploadFile uploadFile = new FileUploadFile();
			File file = new File("D:/workspace/settings.xml");// d:/source.rar,D:/2014work/apache-maven-3.5.0-bin.tar.gz
			String fileMd5 = file.getName();// 文件名
			uploadFile.setFile(file);
			uploadFile.setFile_md5(fileMd5);
			uploadFile.setStarPos(0);// 文件开始位置
			new DcpServer().bind(port, uploadFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
    public static final int FILE_PORT = 9991;
}
