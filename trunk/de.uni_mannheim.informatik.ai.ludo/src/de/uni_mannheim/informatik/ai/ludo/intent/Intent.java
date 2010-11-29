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

/**
 * The intent is an intended acion of a player or the application.
 * This means, the correcponding action has not yet been validated by the game logic and therefore was not executed, yet. 
 * An intent can be either created by an human player, an artficial player or the application.
 * How the intent acts upon the invocation of the execute method must be defined by the corresponding implementations.
 * @author gtrefs
 */
public interface Intent<T> {
    public void takeVisitor(IntentVisitor visitor);
    public void execute();
    public void reject();
    public void success();
    public T getTarget();
}