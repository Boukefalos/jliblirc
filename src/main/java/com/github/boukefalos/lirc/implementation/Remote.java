package com.github.boukefalos.lirc.implementation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import lirc.Lirc.Button;
import lirc.Lirc.Button.Type;
import lirc.Lirc.Color;
import lirc.Lirc.Direction;
import lirc.Lirc.Number;
import lirc.Lirc.Signal;
import base.Forwarder;
import base.server.receiver.AbstractReceiver;
import base.work.Listen;

import com.github.boukefalos.lirc.Lirc;
import com.github.boukefalos.lirc.LircButton;
import com.github.boukefalos.lirc.util.SignalObject;

public class Remote extends AbstractReceiver implements Lirc {
    protected ArrayList<Listen<Object>> listenList;

    public Remote(Forwarder forwarder) {
        super(forwarder);
        listenList = new ArrayList<Listen<Object>>();
    }

    public void register(Listen<Object> listen) {
        listenList.add(listen);        
    }

    public void remove(Listen<Object> listen) {
        listenList.remove(listen);        
    }

    public void receive(byte[] buffer) {
        Object object = decode(buffer);
        if (object != null) {
            for (Listen<Object> listen : listenList) {
                listen.add(object);
            }
        }
    }

    public SignalObject<?> decode(byte[] buffer) {
        ByteArrayInputStream input = new ByteArrayInputStream(buffer);
        try {
            Button button = Button.parseDelimitedFrom(input);
            Type type = button.getType();
            Signal signal = button.getSignal();
            switch (type) {
                case COLOR:
                    Color color = button.getColorButton().getColor();
                    return new SignalObject<Color>(signal, color);
                case DIRECTION:
                    Direction direction = button.getDirectionButton().getDirection();
                    return new SignalObject<Direction>(signal, direction);
                case NUMBER:
                    Number number = button.getNumberButton().getNumber();
                    return new SignalObject<Number>(signal, number);
                case LIRC:
                    String remote = button.getLircButton().getRemote();
                    String code = button.getLircButton().getCode();
                    LircButton lircButton = new LircButton(remote, code);
                    return new SignalObject<LircButton>(signal, lircButton);
            }
        } catch (IOException e) {
            logger.error("Failed to parse input");
        }
        return null;
    }
}