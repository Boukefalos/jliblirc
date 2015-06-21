package com.github.boukefalos.lirc.implementation;

import java.util.ArrayList;

import base.exception.worker.ActivateException;
import base.server.socket.TcpClient; // Change to channel?
import base.work.Listen;

import com.github.boukefalos.lirc.Lirc;
import com.github.boukefalos.lirc.listen.ClientListen;
import com.github.boukefalos.server.helper.ServerHelper;

// Fix dual Receiver and Sender roles
public class TcpImplementation extends Listen<byte[]> implements Lirc {
	protected TcpClient tcpClient;
	protected ClientListen listen;
	protected ArrayList<Listen<Object>> listenList;

	public TcpImplementation(String host, int port) {
		tcpClient = new TcpClient(host, port);
		tcpClient.register(this);
    	listenList = new ArrayList<Listen<Object>>();
    	listen = new ClientListen(this);
	}

	public void activate() throws ActivateException {
		listen.start();
		super.activate();
	}

	public void input(byte[] buffer) {
		Object object = ServerHelper.decode(this, buffer);
		if (object != null) {
			for (Listen<Object> listen : listenList) {
				listen.add(object);
			}
		}
	}

	public void register(Listen<Object> listen) {
		listenList.add(listen);		
	}
}
