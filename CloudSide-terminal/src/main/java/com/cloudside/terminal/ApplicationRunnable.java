package com.cloudside.terminal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.cloudside.netty.common.Msg;
import com.cloudside.terminal.work.DcpClient;

@Component
public class ApplicationRunnable implements ApplicationRunner{

	@Value("${cloudside.serverIp}")
	private String serverIp;
	
	@Value("${cloudside.serverPort}")
	private int serverPort;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			Msg msg = new Msg();
			msg.setTitle("Client");
			msg.setType("1");
			msg.setContent("abccccc");
			DcpClient client = new DcpClient();
			client.remoteHost = "127.0.0.1";
			client.remotePort = serverPort;
			client.msg = msg;
			client.init();
			client.doConnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
