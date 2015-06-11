package com.github.boukefalos.lirc.implementation;

import java.util.ArrayList;

import base.exception.worker.ActivateException;
import base.receiver.Receiver;
import base.server.socket.TcpClient; // Change to channel?
import base.work.Listen;

import com.github.boukefalos.lirc.Lirc;
import com.github.boukefalos.lirc.listen.ClientListen;
import com.github.boukefalos.server.helper.ServerHelper;

// Fix dual Receiver and Sender roles
public class TcpImplementation extends TcpClient implements Lirc, Receiver {
	protected ArrayList<Listen<Object>> listenList;
	protected ClientListen listen;

	public TcpImplementation(String host, int port) {
		super(host, port);
    	listenList = new ArrayList<Listen<Object>>();
    	listen = new ClientListen(this);
    	register(this);
	}

	public void activate() throws ActivateException {
		listen.start();
		super.activate();
	}

	public void receive(byte[] buffer) {
		// Decode byte array
		ServerHelper.receive(this, buffer);		
	}

	public void register(Listen<Object> listen) {
		listenList.add(listen);		
	}

	public void add(Object object) {
		// Receive decoded object from ServerHelper
		listen.add(object);	
	}

	public void input(Object object) {		
		// Forward to all listens
		for (Listen<Object> listen : listenList) {
			listen.add(object);
		}
	}
}
