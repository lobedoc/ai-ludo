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
import java.util.ArrayList;
import java.util.List;

/**
 * The path represents the path a player has to master with all of its pawns
 * in order to win the game.
 * A path is defined as a line of fields of which the first field is of the
 * type BeginField belonging to the owner of the path, three intermidiate
 * fields BeginFields but belonging to either none or opponent players and
 * four end fields only accessible by the owner of the path.
 * A path consists of 44 fields (including BeginFields and EndFields).
 * Further a player has 4 start fields where all his pawns are placed
 * at the beginning.
 * @author gtrefs
 */
public class Path implements Renderable{

    private List<Field> path;
    private List<StartField> startFields;
    private List<EndField> endFields;
    private List<Field> pathFieldsWithoutEndFields;
    // The player to which the path belongs to.
    private Player owner;

    Path(List<Field> pathFieldsWithoutEndFields, List<StartField> startFields, List<EndField> endFields) {
        this.pathFieldsWithoutEndFields = pathFieldsWithoutEndFields;
        this.startFields = startFields;
        this.endFields = endFields;
        this.path = new ArrayList<Field>(pathFieldsWithoutEndFields);
        path.addAll(endFields);
    }

    /**
     * This method returns true if no owner pawn is on the path field (without
     * the end Fields)
     * @return true - if no pawn of the player is on any field which is not a end or start field.
     */
    public boolean noPawnOnBoardFields() {
        for (Field f : pathFieldsWithoutEndFields) {
            if(!f.isEmpty()){
               Pawn pawnOnField = f.getPawn();
               for(Pawn p:owner.getPawns()){
                   if(p.equals(pawnOnField)){
                       return false;
                   }
               }
            }
        }
        return true;
    }

    public boolean allEndFieldsFull() {
        boolean ret = true;
        for (Field e : endFields) {
            ret &= (!e.isEmpty());
        }
        return ret;
    }

    public boolean isAtLeastOneStartFieldFull() {
        boolean ret = false;
        for (Field s : startFields) {
            ret |= (!s.isEmpty());
        }
        return ret;
    }

    public Field getFullStartField() {
        for (Field s : startFields) {
            if (!s.isEmpty()) {
                return s;
            }
        }
        return null;
    }

    public boolean placeOnStartField(Pawn p) {
        if (p.getOwner().equals(owner)) {
            for (Field s : startFields) {
                if (s.isEmpty()) {
                    s.placePawnOnField(p);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public Field getFieldOfPawn(Pawn pawn) {
        for (Field s : startFields) {
            if (!s.isEmpty() && s.getPawn().equals(pawn)) {
                return s;
            }
        }
        for (Field f : path) {
            if (!f.isEmpty() && f.getPawn().equals(pawn)) {
                return f;
            }
        }
        return null;
    }

    public int getPathIndexOfField(Field f) {
        return path.indexOf(f);
    }

    public Field getFieldByIndex(int index) {
        if (index > path.size() - 1) {
            return null;
        }
        return path.get(index);
    }

    public boolean isOnStartField(Pawn p){
        boolean ret = false;
        for(StartField s:startFields){
            ret |= (s.getPawn()!= null && s.getPawn().equals(p));
        }
        return ret;
    }

    public boolean beginFieldNotPossessedByAnotherOwnerPawn() {
        return getBeginField().isEmpty() || !getBeginField().getPawn().getOwner().equals(owner);
    }

    public boolean fieldNotPossedByAnotherOwnerPawn(Field f) {
        if (f == null) {
            return false;
        }
        return f.isEmpty() || !f.getPawn().getOwner().equals(owner);
    }

    public boolean allStartFieldsEmpty() {
        return getFullStartField() == null ? true : false;
    }

    public Field getBeginField() {
        return path.get(0);
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public void takeRenderer(Renderer renderer) {
        renderer.render(this);
    }
}