package com.github.boukefalos.lirc;

import java.io.IOException;
import java.util.Properties;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.parameters.ConstantParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.work.Work;

import com.github.boukefalos.lirc.client.LircTcpClient;
import com.github.boukefalos.lirc.implementation.LocalImplementation;
import com.github.boukefalos.lirc.implementation.TcpImplementation;
import com.github.boukefalos.lirc.implementation.UdpImplementation;
import com.github.boukefalos.lirc.server.LircServer;
import com.github.boukefalos.lirc.server.LircTcpServer;
import com.github.boukefalos.lirc.server.LircUdpServer;

public class Loader {
    protected static final String PROPERTIES_FILE = "Lirc.properties";
	protected Logger logger = LoggerFactory.getLogger(Loader.class);
    protected MutablePicoContainer pico;

	public Loader(Properties properties) {
		/* Initialise container */
		pico = new DefaultPicoContainer();
	
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

    public static Loader getLoader() throws IOException {
    	return getLoader(PROPERTIES_FILE);    	
    }

	public static Loader getLoader(String propertiesFile) throws IOException {
		/* Read properties file */
		Properties properties = new Properties();
		properties.load(Loader.class.getClassLoader().getResourceAsStream(propertiesFile));

		/* Initialise loader */
		return new Loader(properties);
	}

    public Lirc getLirc() {
    	return pico.getComponent(Lirc.class);
    }

    public Work getServer() {
    	return (Work) pico.getComponent(LircServer.class);
    }

}
