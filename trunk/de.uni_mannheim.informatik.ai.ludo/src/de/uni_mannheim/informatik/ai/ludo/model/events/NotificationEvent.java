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
 * This event indicates a notification to any view.
 * Such a notfication could be of the type update which should result into
 * an update of the view.
 *
 * @author gtrefs
 */
public class NotificationEvent extends EventObject {

    private Type type;

    public NotificationEvent(Object source, Type type) {
        super(source);
        this.type = type;
    }

    public enum Type {
        REFRESH, DICE_ROLLED, NO_MORE_ATTEMPS, PAWN_ENTERED, NO_MOVEABLE_PAWNS, MOVEABLE_PAWN_MOVED,
        GAME_WON, NEXT_ROUND_WITH_SAME_PLAYER, NEXT_ROUND_WITH_NEXT_PLAYER, NEW_GAME, END_GAME,
        START_WRITING_STATISTICS, END_WRITING_STATISTICS
    ,   PAWN_ENTERED_THREW_ENEMY_PAWN, TRIED_TO_MOVE_NONMOVEABLE_PAWN, ENEMY_PAWN_THROWN}

    public Type getType() {
        return type;
    }

    @Override
    public String toString(){
        return "Type ["+type.toString()+"]"+"\n Source ["+source+"]";
    }
}