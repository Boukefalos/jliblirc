package com.github.boukefalos.server.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import lirc.Lirc.Button;
import lirc.Lirc.Button.Type;
import lirc.Lirc.Color;
import lirc.Lirc.Direction;
import lirc.Lirc.Number;
import lirc.Lirc.Signal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.boukefalos.lirc.Lirc;
import com.github.boukefalos.lirc.LircButton;
import com.github.boukefalos.lirc.util.SignalObject;

public class ServerHelper {
	protected static Logger logger = LoggerFactory.getLogger(ServerHelper.class);

	public static SignalObject<?> decode(Lirc lirc, byte[] buffer) {
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
