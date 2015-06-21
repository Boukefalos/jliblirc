package com.github.boukefalos.lirc;

import base.work.Listen;

public interface Lirc {
	public void start();	
	// Required for Client / ClientListen to forward from separate Thread
	public void input(Object object);
	public void register(Listen<Object> listen);
}
