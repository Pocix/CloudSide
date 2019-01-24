package com.cloudside.config.monitor;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.cloudside.netty.common.FileUploadFile;
import com.cloudside.netty.work.DcpServer;

//@Component
public class NettyApp implements ApplicationRunner {

	@Value("${cloudside.serverPort}")
	private int serverPort;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			FileUploadFile uploadFile = new FileUploadFile();
			File file = new File("D:/workspace/settings.xml");// d:/source.rar,D:/2014work/apache-maven-3.5.0-bin.tar.gz
			String fileMd5 = file.getName();// 文件名
			uploadFile.setFile(file);
			uploadFile.setFile_md5(fileMd5);
			uploadFile.setStarPos(0);// 文件开始位置
			new DcpServer().bind(serverPort, uploadFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
