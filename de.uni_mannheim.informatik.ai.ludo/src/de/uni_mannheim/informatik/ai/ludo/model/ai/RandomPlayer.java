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
package de.uni_mannheim.informatik.ai.ludo.model.ai;

import de.uni_mannheim.informatik.ai.ludo.intent.PlayerIntent;
import de.uni_mannheim.informatik.ai.ludo.model.Pawn;
import de.uni_mannheim.informatik.ai.ludo.model.PlayerAdapter;
import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderer;

/**
 * This artificial Player is a random player which acts randomly.
 * @author gtrefs
 */
public class RandomPlayer extends PlayerAdapter {

    @Override
    public Pawn movePawn() {
        return pawns[(int) (100 * Math.random() % pawns.length)];
    }

    @Override
    public void rejectIntent(PlayerIntent intent) {
    }

    @Override
    public void successIntent(PlayerIntent intent) {
    }

    @Override
    public void enemyPawnThrownByIntent(Pawn enemyPawn, PlayerIntent intent) {
    }

    @Override
    public void gameWonByIntent(PlayerIntent intent) {
    }

    @Override
    public void takeRenderer(Renderer renderer) {
        renderer.render(this);
    }
}
