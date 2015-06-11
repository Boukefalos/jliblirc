package com.github.boukefalos.lirc.listen;

import base.work.Listen;

import com.github.boukefalos.lirc.Lirc;

public class ClientListen extends Listen<Object> {
	protected Lirc lirc;

	public ClientListen(Lirc lirc) {
		this.lirc = lirc;
	}

	public void input(Object object) {
		lirc.input(object);
	}

	// forward send requests to sender
}
