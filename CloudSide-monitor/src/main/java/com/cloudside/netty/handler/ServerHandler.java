package com.cloudside.netty.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudside.netty.common.FileUploadFile;
import com.cloudside.netty.common.Msg;
import com.cloudside.netty.common.NettyChannelMap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

public class ServerHandler extends ChannelInboundHandlerAdapter {
	private int byteRead;
	private volatile int start = 0;
	private volatile int lastLength = 0;
	public RandomAccessFile randomAccessFile;
	private FileUploadFile fileUploadFile;
	private Logger log = LoggerFactory.getLogger(ServerHandler.class);

	public ServerHandler(FileUploadFile ef) {
		if (ef.getFile().exists()) {
			if (!ef.getFile().isFile()) {
				System.out.println("Not a file :" + ef.getFile());
				return;
			}
		}
		this.fileUploadFile = ef;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
		log.info("服务端结束传递文件channelInactive()");
		SocketChannel socketChannel = (SocketChannel) ctx.channel();
		NettyChannelMap.getInstance().removeSocketChannel(socketChannel);
	}

	public void channelActive(ChannelHandlerContext ctx) {
		log.info("服务端活动channelActive()" + ctx.channel());
		try {
			randomAccessFile = new RandomAccessFile(fileUploadFile.getFile(), "r");
			randomAccessFile.seek(fileUploadFile.getStarPos());
			// lastLength = (int) randomAccessFile.length() / 10;
			lastLength = 1024 * 10;
			byte[] bytes = new byte[lastLength];
			if ((byteRead = randomAccessFile.read(bytes)) != -1) {
				fileUploadFile.setEndPos(byteRead);
				fileUploadFile.setBytes(bytes);
				ctx.writeAndFlush(fileUploadFile);
			} else {
			}
			log.info("channelActive()文件已经读完 " + byteRead);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Integer) {
			start = (Integer) msg;
			if (start != -1) {
				randomAccessFile = new RandomAccessFile(fileUploadFile.getFile(), "r");
				randomAccessFile.seek(start);
				log.info("长度：" + (randomAccessFile.length() - start));
				int a = (int) (randomAccessFile.length() - start);
				int b = (int) (randomAccessFile.length() / 1024 * 2);
				if (a < lastLength) {
					lastLength = a;
				}
				log.info("文件长度：" + (randomAccessFile.length()) + ",start:" + start + ",a:" + a + ",b:" + b
						+ ",lastLength:" + lastLength);
				byte[] bytes = new byte[lastLength];
				// log.info("-----------------------------" + bytes.length);
				if ((byteRead = randomAccessFile.read(bytes)) != -1 && (randomAccessFile.length() - start) > 0) {
					// log.info("byte 长度：" + bytes.length);
					fileUploadFile.setEndPos(byteRead);
					fileUploadFile.setBytes(bytes);
					try {
						ctx.writeAndFlush(fileUploadFile);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					randomAccessFile.close();
					//ctx.close();
					log.info("文件已经读完channelRead()--------" + byteRead);
				}
			}
		} else if (msg instanceof Msg) {
			Msg clientMsg = (Msg) msg;
			if(clientMsg != null){				
				log.info("服务端：" + clientMsg.getTitle() + clientMsg.getContent());
				Msg pushMsg = new Msg();
				pushMsg.setContent("当前时间为：" + new Date());
				pushMsg.setTitle("服务端返回消息");
				ctx.writeAndFlush(pushMsg);
			}
		}
	}

	// @Override
	// public void channelRead(ChannelHandlerContext ctx, Object msg) throws
	// Exception {
	// System.out.println("Server is speek ："+msg.toString());
	// FileRegion filer = (FileRegion) msg;
	// String path = "E://Apk//APKMD5.txt";
	// File fl = new File(path);
	// fl.createNewFile();
	// RandomAccessFile rdafile = new RandomAccessFile(path, "rw");
	// FileRegion f = new DefaultFileRegion(rdafile.getChannel(), 0,
	// rdafile.length());
	//
	// System.out.println("This is" + ++counter + "times receive server:["
	// + msg + "]");
	// }

	// @Override
	// public void channelReadComplete(ChannelHandlerContext ctx) throws
	// Exception {
	// ctx.flush();
	// }

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	// @Override
	// protected void channelRead0(ChannelHandlerContext ctx, String msg)
	// throws Exception {
	// String a = msg;
	// System.out.println("This is"+
	// ++counter+"times receive server:["+msg+"]");
	// }

}
