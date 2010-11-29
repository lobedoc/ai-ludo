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

import de.uni_mannheim.informatik.ai.ludo.intent.PlayerIntent;
import de.uni_mannheim.informatik.ai.ludo.model.Game.Color;
import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderer;

/**
 * This class reflects a real human player.
 * @author gtrefs
 */
public class HumanPlayer implements Player{

    public void setColor(Color color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Color getColor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPawns(Pawn[] pawns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Pawn[] getPawns() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Pawn getPawn(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPath(Path path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Path getPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Pawn movePawn(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void rollTheDice(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void rejectIntent(PlayerIntent intent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void successIntent(PlayerIntent intent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void enemyPawnThrownByIntent(Pawn enemyPawn, PlayerIntent intent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void gameWonByIntent(PlayerIntent intent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void takeRenderer(Renderer renderer) {
        renderer.render(this);
    }

}