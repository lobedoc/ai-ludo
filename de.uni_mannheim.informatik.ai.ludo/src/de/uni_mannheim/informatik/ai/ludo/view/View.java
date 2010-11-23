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
package de.uni_mannheim.informatik.ai.ludo.view;

import de.uni_mannheim.informatik.ai.ludo.model.Game;

/**
 * This interface is to be implemented by all classes which want to display the {@link de.uni_mannheim.informatik.ai.ludo.model.Game Game}.
 * @author gtrefs
 */
public interface View {
    /**
     * Called at init. Enables initialization issues.
     * @param game Game instance
     */
    public void init(Game game);

    /**
     * Called before the application stops.
     */
    public void shutdown();
}