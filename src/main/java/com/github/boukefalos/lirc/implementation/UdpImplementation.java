package com.github.boukefalos.lirc.implementation;

import java.net.UnknownHostException;

import base.sender.UdpSender;
import base.work.Listen;

import com.github.boukefalos.lirc.Lirc;

public class UdpImplementation extends UdpSender implements Lirc {
	public UdpImplementation(String host, int port) throws UnknownHostException {
		super(host, port);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(Listen<Object> listen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void input(Object object) {
		// TODO Auto-generated method stub
		
	}
	
	// add way to receive udp packets
}
