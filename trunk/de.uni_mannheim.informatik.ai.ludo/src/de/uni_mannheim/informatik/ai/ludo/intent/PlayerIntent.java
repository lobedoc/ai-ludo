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
package de.uni_mannheim.informatik.ai.ludo.intent;

import de.uni_mannheim.informatik.ai.ludo.model.Player;

/**
 * This interface reflects the intents which directly result from a users action.
 * @author gtrefs
 */
public interface PlayerIntent extends GameIntent{
    /**
     * Return the player of this intent.
     * @return the player which to whom this intent belongs to
     */
    public Player getPlayer();
    /**
     * Set the player of this intent.
     * @param player the player to whom this intent belongs to
     */
    public void setPlayer(Player player);
}