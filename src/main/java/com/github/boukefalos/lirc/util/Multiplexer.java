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
package com.github.boukefalos.lirc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lirc.Lirc.Signal;
import base.work.Listen;

public class Multiplexer<T> {
    public static final int TIMEOUT = 150;

    protected int timeout;
    protected ArrayList<Listen<Object>> listenList;
	protected ScheduledExecutorService executor;
    protected HashMap<T,Integer> counterMap;

    public Multiplexer() {
        this(TIMEOUT);
    }

    public Multiplexer(int timeout) {
    	this.timeout = timeout;
    	listenList = new ArrayList<Listen<Object>>();
        executor = Executors.newSingleThreadScheduledExecutor();
        counterMap = new HashMap<T,Integer>();
    }

    public void register(Listen<Object> listen) {
    	listenList.add(listen);
    }

    public void remove(Listen<SignalObject<T>> listen) {
    	listenList.remove(listen);
    }

	public synchronized void add(T object) {    	
		Expire expire = new Expire(this, object);
		executor.schedule(expire, (long) timeout, TimeUnit.MILLISECONDS);
    	int counter = counterMap.getOrDefault(object, 0);
    	if (counter == 0) {
    		for (Listen<Object> listen : listenList) {
    			listen.add(new SignalObject<T>(Signal.BEGIN, object));
    		}
    	}
		counterMap.put(object, counter + 1);
    }

	protected synchronized void expire(T object) {
		int counter = counterMap.get(object);
		counterMap.put(object, counter - 1);
		if (counter == 1) {
    		for (Listen<Object> listen : listenList) {
    			listen.add(new SignalObject<T>(Signal.END, object));
    		}
		}
	}

	public class Expire implements Runnable {
		protected Multiplexer<T> multiplexer;
		protected T object;

		public Expire(Multiplexer<T> multiplexer, T object) {
			this.multiplexer = multiplexer;
			this.object = object;
		}

		public void run() {
			multiplexer.expire(object);
		}		
	}

	public void stop() {
		executor.shutdown();
		
	}
	
	public void exit() {
		stop();
		// Should cancel all scheduled Runnables
	}
}
