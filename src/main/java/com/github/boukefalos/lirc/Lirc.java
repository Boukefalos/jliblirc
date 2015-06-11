package com.github.boukefalos.lirc;

import base.work.Listen;

public interface Lirc {
	public void start();
	public void register(Listen<Object> listen);
	public void add(Object object);
	
	// Required for Client / ClientListen to forward from separate Thread
	public void input(Object object);
}
