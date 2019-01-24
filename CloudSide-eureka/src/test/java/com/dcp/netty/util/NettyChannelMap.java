package com.cloudside.config.netty.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.socket.SocketChannel;

public class NettyChannelMap {
	private Map<String, SocketChannel> map = new ConcurrentHashMap<String, SocketChannel>();
	private static NettyChannelMap instance = new NettyChannelMap();

	private NettyChannelMap() {

	}

	public static NettyChannelMap getInstance() {
		return instance;
	}

	public void addSocketChannel(String clientId, SocketChannel socketChannel) {
		map.put(clientId, socketChannel);
	}

	public SocketChannel getSocketChannel(String clientId) {
		return map.get(clientId);
	}

	public void removeSocketChannel(String clientId) {
		map.remove(clientId);
	}

	public void removeSocketChannel(SocketChannel socketChannel) {
		if (map.containsValue(socketChannel)) {// 查看是否包含
			String key = null;
			SocketChannel value = null;
			for (Map.Entry<String, SocketChannel> entry : map.entrySet()) {
				key = entry.getKey();
				value = entry.getValue();
				if (value == socketChannel) {
					break;
				}
			}
			map.remove(key);
		}
	}

	public void pushMsg2AllClient(Msg msg) {
		if (map.size() > 0){
			for (SocketChannel socketChannel : map.values()) {
				socketChannel.writeAndFlush(msg);
			}
		}
	}
}