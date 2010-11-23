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

import de.uni_mannheim.informatik.ai.ludo.model.Game;

/**
 * The intent is an intended acion of a player. This means, the correcponding 
 * action has not yet been validated by the game logic and therefore was not
 * done by the game. An intent can be either created by an human player or
 * by an artficial player. The intent itself takes a GameState and calls the
 * correnspond processPlayerIntent method. This conforms with the visitor
 * pattern.
 * There are two types of intents.
 * <b>MoveIntent:</b> This class represents the intent of the user of moving a
 * pawn to a new location.
 * <b>RollDiceIntent:</b> This class represents the intent of roling the dice.
 *
 * @author gtrefs
 */
public interface Intent<T> {
    public void takeVisitor(IntentVisitor visitor);
    public void execute();
    public void reject();
    public void success();
    public T getTarget();
}