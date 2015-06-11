package com.github.boukefalos.lirc.client;

import java.nio.channels.SocketChannel;

import base.server.channel.TcpServerClient;

import com.github.boukefalos.lirc.server.LircTcpServer;

public class LircTcpClient extends TcpServerClient {
	protected LircTcpServer server;

	public LircTcpClient(LircTcpServer server, SocketChannel socketChannel, Integer bufferSize) {
		super(server, socketChannel, bufferSize);
		this.server = server;		
	}

	public void receive(byte[] buffer) {
		
		System.err.println(123);
		System.err.println(new String(buffer).trim());
		
	}
}
