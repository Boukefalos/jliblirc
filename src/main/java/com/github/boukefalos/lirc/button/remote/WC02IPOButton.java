/**
 * Copyright (C) 2015 Rik Veenboer <rik.veenboer@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.boukefalos.lirc.button.remote;

import com.github.boukefalos.lirc.button.RemoteButton;

public enum WC02IPOButton implements RemoteButton {
    MINUS ("MINUS"),
    PLUS ("PLUS"),
    NEXT ("NEXT"),
    PREVIOUS ("PREVIOUS"),
    PLAY ("PLAY"),
    HOLD ("HOLD");

    public static final String NAME = "WC02-IPO";

    protected String code;

    private WC02IPOButton(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
    public String getRemote() {
        return NAME;
    }
}
