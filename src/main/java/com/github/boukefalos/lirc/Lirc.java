package com.github.boukefalos.lirc;

import base.Control;
import base.work.Listen;

public interface Lirc extends Control {
	public void register(Listen<Object> listen);
	public void remove(Listen<Object> listen);
	//public void send(String remote, String code);
}
