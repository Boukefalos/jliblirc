package test;

import com.github.boukefalos.lirc.Lirc;
import com.github.boukefalos.lirc.LircButton;
import com.github.boukefalos.lirc.implementation.Local;
import com.github.boukefalos.lirc.util.SignalObject;

import base.exception.worker.ActivateException;
import base.work.ReflectiveListen;
import lirc.Lirc.Signal;

public class TestLocal extends ReflectiveListen {    
    public static void main(String[] args) {
        new TestLocal().start();
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {}
    }

    protected Lirc lirc;

    public TestLocal() {
        lirc = new Local();
        lirc.register(this);
    }

    public void activate() throws ActivateException {
        lirc.start();
        super.activate();        
    }

    public void start() {
        super.start();
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
}
