package com.github.boukefalos.lirc;

import java.util.Properties;

import org.picocontainer.Parameter;
import org.picocontainer.parameters.ConstantParameter;

import base.loader.AbstractLoader;
import base.work.Work;

import com.github.boukefalos.lirc.client.LircTcpClient;
import com.github.boukefalos.lirc.implementation.LocalImplementation;
import com.github.boukefalos.lirc.implementation.TcpImplementation;
import com.github.boukefalos.lirc.implementation.UdpImplementation;
import com.github.boukefalos.lirc.server.LircServer;
import com.github.boukefalos.lirc.server.LircTcpServer;
import com.github.boukefalos.lirc.server.LircUdpServer;

public class Loader extends AbstractLoader {
    protected static final String PROPERTIES_FILE = "lirc.properties";

	public Loader(Properties properties) {
		super();
	
		/* Add implementation */
		switch (properties.getProperty("implementation")) {
			case "local":
				pico.addComponent(LocalImplementation.class);
				break;				
			case "remote":
				//pico.addComponent(Remote.class);
				break;
		}

		/* Add protocol */
		if (properties.getProperty("protocol") != null) {
			switch (properties.getProperty("protocol")) {
				case "tcp":
					pico.addComponent(TcpImplementation.class, TcpImplementation.class, new Parameter[]{
						new ConstantParameter(properties.getProperty("remote.host")),
						new ConstantParameter(Integer.valueOf(properties.getProperty("remote.port")))});
					break;
				case "udp":
					pico.addComponent(UdpImplementation.class, UdpImplementation.class, new Parameter[] {
						new ConstantParameter(properties.getProperty("remote.host")),
						new ConstantParameter(Integer.valueOf(properties.getProperty("remote.port")))});
					break;
			}
		}

		/* Add server */
		if (properties.getProperty("server") != null) {
			switch (properties.getProperty("server.protocol")) {
				case "tcp":
					pico.addComponent(LircTcpServer.class, LircTcpServer.class, new Parameter[]{
						new ConstantParameter(getLirc()),
						new ConstantParameter(Integer.valueOf(properties.getProperty("server.port"))),
						new ConstantParameter(LircTcpClient.class)});
					break;
				case "udp":
					pico.addComponent(LircUdpServer.class, LircUdpServer.class, new Parameter[]{
						new ConstantParameter(getLirc()),
						new ConstantParameter(Integer.valueOf(properties.getProperty("server.port")))});
			}
			
		}
	}

    public Lirc getLirc() {
    	return pico.getComponent(Lirc.class);
    }

    public Work getServer() {
    	return (Work) pico.getComponent(LircServer.class);
    }

}
