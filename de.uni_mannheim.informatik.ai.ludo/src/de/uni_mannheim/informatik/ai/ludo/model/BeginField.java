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

import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderer;

/**
 * There are four BeginFields in the game. Each for every {@link de.uni_mannheim.informatik.ai.ludo.model.Game.Color Color}.
 * A {@link de.uni_mannheim.informatik.ai.ludo.model.Pawn Pawn} of a specific color is set on the corresponding BeginField upon its introduction.
 * @author gtrefs
 */
public class BeginField extends ColorField{
    @Override
    public void takeRenderer(Renderer renderer) {
        renderer.render(this);
    }
}
