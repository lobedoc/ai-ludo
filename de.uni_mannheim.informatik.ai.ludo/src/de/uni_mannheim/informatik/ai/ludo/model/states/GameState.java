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
package de.uni_mannheim.informatik.ai.ludo.model.states;

import de.uni_mannheim.informatik.ai.ludo.intent.IntentVisitor;

/**
 * The GameState class is the abstract super class of all game state classes.
 * It implements the {@link de.uni_mannheim.informatik.ai.ludo.intent.IntentVisitor IntentVisitor} interface and therefore is able to visit any Intent which emerges within the game.
 * Further, the GameState represents abastract State class of the State Pattern. The next game state is determined by the current game state and the intent which occurs.
 * @author gtrefs
 * {@see http://en.wikipedia.org/wiki/State_pattern State Pattern}
 */
public interface GameState extends IntentVisitor{

}