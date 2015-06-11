package com.github.boukefalos.lirc.listen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import lirc.Lirc.Button;
import lirc.Lirc.Button.Builder;
import lirc.Lirc.Button.Type;
import lirc.Lirc.Color;
import lirc.Lirc.ColorButton;
import lirc.Lirc.Direction;
import lirc.Lirc.DirectionButton;
import lirc.Lirc.Number;
import lirc.Lirc.NumberButton;
import lirc.Lirc.Signal;
import base.sender.Sender;
import base.work.Listen;

import com.github.boukefalos.lirc.LircButton;
import com.github.boukefalos.lirc.util.SignalObject;

public class ServerListen extends Listen<Object> {
	protected Sender sender;

	public ServerListen(Sender sender) {
		this.sender = sender;
	}

	public void input(SignalObject<Object> signalObject) {
		Signal signal = signalObject.signal;
		Object object = signalObject.object;
		Builder builder = Button.newBuilder().setSignal(signal);
		if (object instanceof LircButton) {
			builder.setType(Type.LIRC);
			LircButton lircButton = (LircButton) object;
			builder.setLircButton(lircButton.getProto());
		} else if (object instanceof Color) {
			builder.setType(Type.COLOR);
			Color color = (Color) object;
			builder.setColorButton(ColorButton.newBuilder().setColor(color));
		} else if (object instanceof Number) {
			builder.setType(Type.NUMBER);
			Number number = (Number) object;
			builder.setNumberButton(NumberButton.newBuilder().setNumber(number));
		} else if (object instanceof Direction) {
			builder.setType(Type.DIRECTION);
			Direction direction = (Direction) object;
			builder.setDirectionButton(DirectionButton.newBuilder().setDirection(direction));
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
		try {
			builder.build().writeDelimitedTo(output);
			sender.send(output.toByteArray());
		} catch (IOException e) {
			logger.error("Failed to send command");
		}		
	}
}