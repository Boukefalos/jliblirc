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
package com.github.boukefalos.lirc;

public class LircButton {
    public String remote;
    public String code;

    public LircButton(String remote, String code) {
        this.remote = remote;
        this.code = code;
    }

    public  lirc.Lirc.LircButton getProto() {
        return lirc.Lirc.LircButton.newBuilder().setRemote(remote).setCode(code).build();
    }
}
