package test;

import lirc.Lirc.Signal;
import base.exception.worker.ActivateException;
import base.work.Listen;

import com.github.boukefalos.lirc.Lirc;
import com.github.boukefalos.lirc.LircButton;
import com.github.boukefalos.lirc.implementation.LocalImplementation;
import com.github.boukefalos.lirc.util.SignalObject;

public class TestLocal extends Listen<Object> {
	
	public static void main(String[] args) {
		new TestLocal().start();
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {}
	}

	protected Lirc lirc;

	public TestLocal() {
		lirc = new LocalImplementation();
		lirc.register(this);
	}

    public void activate() throws ActivateException {
        logger.debug("Activate " + getClass().getSimpleName());
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
