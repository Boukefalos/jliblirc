package com.github.boukefalos.lirc.util;

import lirc.Lirc.Signal;

public class SignalObject<T> {
    public Signal signal;
    public T object;

    public SignalObject(Signal signal, T object) {
        this.signal = signal;
        this.object = object;
    }

    public String bla() {
        return signal + " " + object;
    }

}
