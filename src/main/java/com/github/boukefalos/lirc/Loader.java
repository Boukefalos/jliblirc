package com.github.boukefalos.lirc;

import java.util.Properties;

import base.exception.LoaderException;
import base.loader.AbstractLoader;

import com.github.boukefalos.lirc.implementation.Local;
import com.github.boukefalos.lirc.implementation.Remote;

public class Loader extends AbstractLoader<Loader> {
    protected static final String PROPERTIES_FILE = "lirc.properties";

	public Loader(Properties properties) throws LoaderException {
		super();

		/* Add implementation */
		switch (properties.getProperty("implementation")) {
			case "local":
				pico.addComponent(Local.class);
				break;				
			case "remote":
				pico.addComponent(Remote.class);

				/* Add remote forwarder implementation */
				try {
					String protocol = properties.getOrDefault("server.protocol", "tcp").toString();
					String implementation = properties.getOrDefault("tcp.implementation", "socket").toString();
					int port = Integer.valueOf(properties.getProperty("remote.port"));
					addForwarder(protocol, implementation, port);
				} catch (NumberFormatException e) {
					throw new LoaderException("Failed to parse remote.port");
				}				
				break;
		}

		/* Add server */
		if (properties.getProperty("server") != null) {
			pico.addComponent(Server.class);

			/* Add sender implementation */
			try {
				String protocol = properties.getOrDefault("server.protocol", "tcp").toString();
				String implementation = properties.getOrDefault("tcp.implementation", "socket").toString();
				int port = Integer.valueOf(properties.getProperty("server.port"));
				addSender(protocol, implementation, "localhost", port);
			} catch (NumberFormatException e) {
				throw new LoaderException("Failed to parse server.port");
			}	
		}
	}

	public Lirc getLirc() {
    	return pico.getComponent(Lirc.class);
    }

    public Server getServer() {
    	return pico.getComponent(Server.class);
    }
}
