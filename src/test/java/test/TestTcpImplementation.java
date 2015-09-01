package test;

import java.util.Properties;

import base.exception.LoaderException;

public class TestTcpImplementation {
    public static void main(String[] args) throws LoaderException {
        Properties localProperties = new Properties();
        localProperties.setProperty("implementation", "local");
        localProperties.setProperty("server", "true");
        localProperties.setProperty("server.port", "8883");
        localProperties.setProperty("server.protocol", "tcp");

        Properties remoteProperties = new Properties();
        remoteProperties.setProperty("implementation", "remote");
        remoteProperties.setProperty("protocol", "tcp");
        remoteProperties.setProperty("remote.host", "localhost");
        remoteProperties.setProperty("remote.port", "8883");

        TestRemoteImplementation.main(localProperties, remoteProperties);
    }
}
