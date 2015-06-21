/**
 * Copyright (C) 2015 Rik Veenboer <rik.veenboer@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.boukefalos.lirc;

import java.io.IOException;

import base.server.channel.TcpClient;
import base.work.Listen;

public class LircClient extends TcpClient {
	public static final int BUFFER_SIZE = 1024;
    public static final String IP = "localhost";
    public static final int PORT = 8765;
    protected String send;
	protected Listen<Object> listen;

    public LircClient(Listen<Object> listen) {
    	super(IP, PORT, BUFFER_SIZE);
        //send = Native.getValue(Registry.CURRENT_USER, "Software\\LIRC", "password");
		this.listen = listen;
	}

	public void send(LircButton button) {
        send(button, 0);
    }

    public void send(LircButton lircButton, int repeat) {
        if (send == null) {
            return;
        }
        String command = String.format("%s %s %s \n", send, lircButton.remote, lircButton.code);
        try {
			send(command.getBytes());
		} catch (IOException e) {
			logger.error("", e);
		}
    }

    public void input(byte[] buffer) {
    	listen.add(buffer);
    }
}
