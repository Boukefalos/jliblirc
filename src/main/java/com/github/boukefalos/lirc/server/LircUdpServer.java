package com.github.boukefalos.lirc.server;

import base.server.datagram.UdpServer;

import com.github.boukefalos.lirc.Lirc;
import com.github.boukefalos.server.helper.ServerHelper;

public class LircUdpServer extends UdpServer implements LircServer {
    protected Lirc lirc;

	public LircUdpServer(Lirc lirc, int port) {
		this(lirc, port, BUFFER_SIZE);
	}

	public LircUdpServer(Lirc lirc, int port, int bufferSize) {
		super(port, bufferSize);
		this.lirc = lirc;
		this.bufferSize = bufferSize;
	}

	protected void receive(byte[] buffer) {		
		ServerHelper.receive(lirc, buffer);
	}
}