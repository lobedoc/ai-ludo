/*
 * Copyright (C) 2010 Gregor Trefs, Dominique Ritze
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.uni_mannheim.informatik.ai.ludo.model.events;

import java.util.EventObject;

/**
 * This event indicates that user input is required.
 * There are only two types of user input, namely roling the dice and
 * moving the pawn. In order to avoid a classes overhead both types
 * are merged into this class.
 * Default type is a request for a user move.
 *
 * @author gtrefs
 */
public class RequestForUserInputEvent extends EventObject {

    public enum Type {

        REQUEST_FOR_MOVE, REQUEST_FOR_ROLLING_THE_DICE, INIT_DATA, GAME_END_REACHED;
    }
    private Type actType = Type.REQUEST_FOR_MOVE;
    private String message;

    public RequestForUserInputEvent(Object source, Type type) {
        super(source);
        actType = type;
    }

    public RequestForUserInputEvent(Object source) {
        super(source);
    }

    public Type getType() {
        return actType;
    }

    public void setType(Type type) {
        actType = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Type [" + actType.toString() + "]" + "\n Source [" + source + "]";
    }
}
