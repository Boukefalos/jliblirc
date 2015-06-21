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
package com.github.boukefalos.lirc.implementation;

import java.util.ArrayList;
import java.util.Scanner;

import lirc.Lirc.Color;
import lirc.Lirc.Direction;
import lirc.Lirc.Number;
import lirc.Lirc.Signal;
import base.exception.worker.ActivateException;
import base.exception.worker.DeactivateException;
import base.work.Listen;

import com.github.boukefalos.lirc.Lirc;
import com.github.boukefalos.lirc.LircButton;
import com.github.boukefalos.lirc.LircClient;
import com.github.boukefalos.lirc.util.Multiplexer;
import com.github.boukefalos.lirc.util.SignalObject;

public class LocalImplementation extends Listen<Object> implements Lirc {
	protected ArrayList<Listen<Object>> listenList;
    protected Multiplexer<String> multiplexer;
    protected LircClient lircClient;

    public LocalImplementation() {
    	listenList = new ArrayList<Listen<Object>>();
        lircClient = new LircClient(this);
        multiplexer = new Multiplexer<String>();
        multiplexer.register(this);
    }

	public void register(Listen<Object> listen) {
		listenList.add(listen);		
	}

    public void remove(Listen<Object> listen) {
        listenList.remove(listen);
    }

    public void activate() throws ActivateException {
    	lircClient.start();
        super.activate();        
    }

    public void deactivate() throws DeactivateException {
        super.deactivate();
        lircClient.stop();
        multiplexer.stop();
    }

    public void exit() {
        super.exit();
        lircClient.exit();
        multiplexer.exit();
    }

	public void input(byte[] buffer) {
		String line = new String(buffer).trim();
        if (!line.startsWith("BEGIN")) {        	
        	Scanner scanner = new Scanner(line);
            scanner.next();
            scanner.next();
            String code = scanner.next();
            String remote = scanner.next();

        	// Need to pass as String to assure consistent hash
        	multiplexer.add(remote + " " + code);
        	scanner.close();
        }
	}

	public void input(SignalObject<Object> signalObject) {
		Object object = signalObject.object;
		if (object instanceof String) {
			// Translate String signal objects to LircButton signalObjects
			String[] input = ((String) object).split(" ");
			LircButton lircButton = new LircButton(input[0], input[1]);
			signalObject = new SignalObject<Object>(signalObject.signal, lircButton);
			otherParsings(signalObject.signal, input[1].toUpperCase());
		}
		// Pass signalObject to listens
        for (Listen<Object> listen : listenList) {
        	listen.add(signalObject);
        }
	}

	protected void otherParsings(Signal signal, String code) {
        for (Color color : Color.values()) {
            if (color.toString().equals(code)) {
            	add(new SignalObject<Color>(signal, color));
            }
        }
        for (Number number : Number.values()) {
            if (number.toString().equals(code)) {
            	add(new SignalObject<Number>(signal, number));
            }
        }
        for (Direction direction : Direction.values()) {
            if (direction.toString().equals(code)) {
            	add(new SignalObject<Direction>(signal, direction));
            }
        }
        try {
        	add(new SignalObject<Number>(signal, Number.valueOf(Integer.valueOf(code))));
        } catch (NumberFormatException e) {}
	}
}
