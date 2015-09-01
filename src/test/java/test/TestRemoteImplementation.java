package test;

import java.util.Properties;

import lirc.Lirc.Signal;
import base.exception.LoaderException;
import base.exception.worker.ActivateException;
import base.work.Listen;

import com.github.boukefalos.lirc.Lirc;
import com.github.boukefalos.lirc.LircButton;
import com.github.boukefalos.lirc.Loader;
import com.github.boukefalos.lirc.Server;
import com.github.boukefalos.lirc.util.SignalObject;

public class TestRemoteImplementation extends Listen<Object> {
    protected Lirc lirc;

    public TestRemoteImplementation(Loader loader) {
        lirc = loader.getLirc();
        lirc.register(this);
    }

    public void activate() throws ActivateException {
        lirc.start();
        super.activate();        
    }

    public void input(SignalObject<LircButton> lircButtonSignal) {
         Object object = lircButtonSignal.object;
        if (object instanceof LircButton) {
            Signal signal = lircButtonSignal.signal;
            LircButton lircButton = lircButtonSignal.object;
            String code = lircButton.code;
            logger.error(signal.name() + " : " + code + " @ " + lircButton.remote);    
        }
    }
    
    public static void main(Properties localProperties, Properties remoteProperties) throws LoaderException {
        Loader localLoader = new Loader(localProperties);
        Loader remoteLoader = new Loader(remoteProperties);

        Server server = localLoader.getServer();        

        server.start();
        new TestRemoteImplementation(remoteLoader).start();

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {}
    }
}
