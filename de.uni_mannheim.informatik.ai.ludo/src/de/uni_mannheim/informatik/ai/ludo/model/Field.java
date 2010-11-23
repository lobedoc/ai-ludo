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

/**
 * The interface Field represents any field on the board.
 * It has four implementing classes:
 * <b>StartField:</b> Field of the "out" area.
 * <b>BeginFiled:</b> Field which is the first field a player puts his pawns on.
 * <b>EndField:</b> Field of the "end" area.
 * <b>IntermediateField:</b> All field which are none of the above are intermediate fields.
 * @author gtrefs
 */
public abstract class Field implements Renderable{

    private Pawn pawnOnField;

    public Pawn getPawn(){
        return pawnOnField;
    }

    public boolean isEmpty(){
        return pawnOnField==null;
    }
    
    public Pawn takePawnFromField(){
        Pawn pawn = pawnOnField;
        pawnOnField = null;
        return pawn;
    }

    public void placePawnOnField(Pawn p){
        pawnOnField = p;
    }
}