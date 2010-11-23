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
package de.uni_mannheim.informatik.ai.ludo.view.renderer.swingRenderer;

import de.uni_mannheim.informatik.ai.ludo.model.BeginField;
import de.uni_mannheim.informatik.ai.ludo.model.Board;
import de.uni_mannheim.informatik.ai.ludo.model.BoardField;
import de.uni_mannheim.informatik.ai.ludo.model.Dice;
import de.uni_mannheim.informatik.ai.ludo.model.EndField;
import de.uni_mannheim.informatik.ai.ludo.model.Field;
import de.uni_mannheim.informatik.ai.ludo.model.Game;
import de.uni_mannheim.informatik.ai.ludo.model.Path;
import de.uni_mannheim.informatik.ai.ludo.model.Pawn;
import de.uni_mannheim.informatik.ai.ludo.model.Player;
import de.uni_mannheim.informatik.ai.ludo.model.StartField;
import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderer;

/**
 * This class renders the Model to Swing.
 *
 * @author gtrefs
 */
public class SwingLudoRenderer implements Renderer{

    public void render(Game game) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void render(Board board) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void render(Dice dice) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void render(Field field) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void render(Path path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void render(Pawn pawn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void render(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void render(BoardField boardField) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void render(BeginField beginField) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void render(StartField startField) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void render(EndField endField) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}