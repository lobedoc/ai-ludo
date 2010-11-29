/*
 *  Copyright (C) 2010 gtrefs
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.uni_mannheim.informatik.ai.ludo.exceptions;

/**
 * This exception is thrown when two players have the same color or a player has no color.
 * @author gtrefs
 */
public class ColorException extends Exception {

    /**
     * Creates a new instance of <code>ColorException</code> without detail message.
     */
    public ColorException() {
    }


    /**
     * Constructs an instance of <code>ColorException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ColorException(String msg) {
        super(msg);
    }
}
