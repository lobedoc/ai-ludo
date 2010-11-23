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
package de.uni_mannheim.informatik.ai.ludo.view.renderer;

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

/**
 * This interface is to implemented by every class which wants to display the
 * game in some matter. The rendereing technology is up to the programmer.
 * This class implements the visitor pattern.
 * @author gtrefs
 */
public interface Renderer {
    public void render(Game game);
    public void render(Board board);
    public void render(Dice dice);
    public void render(BoardField boardField);
    public void render(BeginField beginField);
    public void render(StartField startField);
    public void render(EndField endField);
    public void render(Path path);
    public void render(Pawn pawn);
    public void render(Player player);
}