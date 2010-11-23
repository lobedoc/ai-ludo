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
package de.uni_mannheim.informatik.ai.ludo.model;

import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderable;
import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderer;

/**
 * This class represents a dice in the game.
 * @author gtrefs
 */
public final class Dice implements Renderable {

    private int count;
    private static Dice instance = new Dice();

    private Dice() {
        count = 1;
    }

    public static Dice getInstance() {
        return instance;
    }

    public int rollDice() {
        count = (int) Math.ceil(Math.random() * 6);
        return count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void takeRenderer(Renderer renderer) {
        renderer.render(this);
    }
}
