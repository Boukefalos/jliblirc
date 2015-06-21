package com.github.boukefalos.lirc.server;

import base.exception.worker.ActivateException;
import base.server.channel.TcpServer;

import com.github.boukefalos.lirc.Lirc;
import com.github.boukefalos.lirc.listen.ServerListen;
import com.github.boukefalos.server.helper.ServerHelper;

public class LircTcpServer extends TcpServer implements LircServer {
	protected Lirc lirc;
	protected ServerListen listen;

	public LircTcpServer(Lirc lirc, int port, Class<?> clientClass) {
		super(port, clientClass);
		this.lirc = lirc;
		listen = new ServerListen(this);
		lirc.register(listen);
	}

	public void activate() throws ActivateException {
		lirc.start();
		listen.start();
		super.activate();
	}

	public void receive(byte[] buffer) {
		ServerHelper.decode(lirc, buffer);
	}
}
