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
import java.util.ArrayList;
import java.util.Scanner;

import base.receiver.Receiver;
import base.server.channel.TcpClient;
import base.work.Listen;

public class LircClient extends TcpClient implements Receiver {
	public static final int BUFFER_SIZE = 1024;
    public static final String IP = "localhost";
    public static final int PORT = 8765;

    protected ArrayList<Listen<Object>> listenList;
    protected String send;

	public LircClient() {
        super(IP, PORT, BUFFER_SIZE);
        //send = Native.getValue(Registry.CURRENT_USER, "Software\\LIRC", "password");
        listenList = new ArrayList<Listen<Object>>();
        register(this);
    }

     public void register(Listen<Object> listen) {
        listenList.add(listen);
    }

    public void remove(Listen<String[]> listen) {
        listenList.remove(listen);
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

	public void receive(byte[] buffer) {
		receive(new String(buffer).trim());
	}

	protected void receive(String line) {
        if (!line.startsWith("BEGIN")) {        	
        	Scanner scanner = new Scanner(line);
        	receive(scanner);
        	scanner.close();
        }
	}

	protected void receive(Scanner scanner) {
        scanner.next();
        scanner.next();
        String code = scanner.next();
        String remote = scanner.next();
        for (Listen<Object> lircbuttonListener : listenList) {
        	// Need to pass as String to assure consistent hash
        	lircbuttonListener.add(remote + " " + code);
        }
	}
}
