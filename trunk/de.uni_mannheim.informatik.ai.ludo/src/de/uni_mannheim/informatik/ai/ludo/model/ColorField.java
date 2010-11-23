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

package de.uni_mannheim.informatik.ai.ludo.model;

/**
 * This class represents a field which is specific to a {@link de.uni_mannheim.informatik.ai.ludo.model.Game.Color Color}.
 * @author gtrefs
 */
public abstract class ColorField extends Field {

    private Game.Color color;

    public Game.Color getColor(){
        return color;
    }

    public void setColor(Game.Color color){
        this.color = color;
    }
}
