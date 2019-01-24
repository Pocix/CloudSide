package com.cloudside.config.netty.handler;

import java.io.File;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudside.config.netty.util.FileUploadFile;
import com.cloudside.config.netty.util.Msg;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
	private int byteRead;
    private volatile int start = 0;
    private String file_dir = "D:/workspace/tmp/";
    
    private Msg clientMsg = null;
    private Logger log = LoggerFactory.getLogger(ClientHandler.class);
    
    public ClientHandler() {
	}
    
    
    public ClientHandler(Msg msg) {
		this.clientMsg = msg;
	}
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
    	// TODO Auto-generated method stub
//    	super.channelActive(ctx);
    	ctx.writeAndFlush(clientMsg);
    	log.info("客户端：channelActive()");
    	// 开启定时器推送
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					while (true) {
//						Thread.sleep(1000 * 2);
//						Msg ping = new Msg();
//						ping.setContent("当前时间为：" + new Date());
//						ping.setTitle("客户端推送Ping");
//						ctx.writeAndFlush(ping);
//					}
//
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//
//			}
//		}).start();
    	ctx.fireChannelActive();
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	// TODO Auto-generated method stub
    	super.channelInactive(ctx);
    	log.info("从连接中。。。。。。。");
    	//ctx.flush();
    	//ctx.close();
    }
    
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileUploadFile) {
            FileUploadFile ef = (FileUploadFile) msg;
            byte[] bytes = ef.getBytes();
            byteRead = ef.getEndPos();
            String md5 = ef.getFile_md5();//文件名
            String path = file_dir + File.separator + md5;
            File file = new File(path);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(start);
            randomAccessFile.write(bytes);
            start = start + byteRead;
            System.out.println("path:"+path+","+byteRead);
            if (byteRead > 0) {
                ctx.writeAndFlush(start);
                randomAccessFile.close();
                if(byteRead!=1024 * 10){
                	Thread.sleep(1000);
                	channelInactive(ctx);
                }
            } else {
            	//System.out.println("文件接收完成");
            	//ctx.flush(); 
                //ctx.close();
            }
            
        }else if(msg instanceof Msg){
			Msg clientMsg = (Msg) msg;
			log.info("客户端："+clientMsg.getTitle()+clientMsg.getContent());
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
//        ctx.close();
        log.info("FileUploadServerHandler--exceptionCaught()");
    }
    
}
