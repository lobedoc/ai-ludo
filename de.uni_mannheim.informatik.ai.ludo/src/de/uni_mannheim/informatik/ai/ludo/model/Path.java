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
 * The path represents the path a {@link Player Player} has to master with all of its {@link Pawn Pawn}s in order to win the game.
 * A path is defined as a line of 44 fields. The first field is of the type {@link BeginField BeginField} determined by the Player's {@link Game.Color Color}.
 * The path further contains three intermidiate BeginFields which reflect the begin points of each other color and four player color's {@link EndField EndField}s.
 * Additionally, a Player has 4 {@link StartField StartField}s which are not present on the path, but reflect the "spawning" point of each pawn.
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
     * This method returns true if no owner pawn is on the path field (without the end Fields)
     * @return true if no pawn of the player is on any field which is not a end or start field.
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

    /**
     * This method returns true if all {@link EndField EndField}s are full.
     * @return true if all EndFields are full
     */
    public boolean allEndFieldsFull() {
        boolean ret = true;
        for (Field e : endFields) {
            ret &= (!e.isEmpty());
        }
        return ret;
    }

    /**
     * Returns true if at least one {@link StartField} is full.
     * @return true if at least one StartField is full
     */
    public boolean isAtLeastOneStartFieldFull() {
        boolean ret = false;
        for (Field s : startFields) {
            ret |= (!s.isEmpty());
        }
        return ret;
    }

    /**
     * Returns a {@link StartField} which is full.
     * @return full StartField or null
     */
    public Field getFullStartField() {
        for (Field s : startFields) {
            if (!s.isEmpty()) {
                return s;
            }
        }
        return null;
    }

    /**
     * Place a {@link Pawn Pawn} on a {@link StartField}.
     * @param p Pawn which should be placed on a StartField
     * @return true if executed successfully otherwise false
     */
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

    /**
     * Returns the {@link Field} of the given {@link Pawn}.
     * @param pawn the Pawn
     * @return the Field of the given pawn
     */
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

    /**
     * Returns the index of a given {@link Field}
     * @param f the Field
     * @return the index of the Field.
     */
    public int getPathIndexOfField(Field f) {
        return path.indexOf(f);
    }

    /**
     * Returns the {@link Field} for a given index.
     * @param index the index
     * @return the Field
     */
    public Field getFieldByIndex(int index) {
        if (index > path.size() - 1) {
            return null;
        }
        return path.get(index);
    }

    /**
     * Returns true if the given{@link Pawn} is on a {@link StartField}.
     * @param p the Pawn
     * @return true if the given Pawn is on a StartField
     */
    public boolean isOnStartField(Pawn p){
        boolean ret = false;
        for(StartField s:startFields){
            ret |= (s.getPawn()!= null && s.getPawn().equals(p));
        }
        return ret;
    }

    /**
     * Returns true if the {@link BeginField} of this Path is not possessed by another owner pawn
     * @return true if the BeginField of this Path is not possessed by another owner pawn
     */
    public boolean beginFieldNotPossessedByAnotherOwnerPawn() {
        return getBeginField().isEmpty() || !getBeginField().getPawn().getOwner().equals(owner);
    }

    /**
     * Returns true if the given {@link Field} is not possesses by another owner pawn
     * @param f the Field
     * @return true if the given Field is not possesses by another owner pawn
     */
    public boolean fieldNotPossedByAnotherOwnerPawn(Field f) {
        if (f == null) {
            return false;
        }
        return f.isEmpty() || !f.getPawn().getOwner().equals(owner);
    }

    /**
     * Returns true if all {@link StartField}s are empty.
     * @return true if all StartField are empty
     */
    public boolean allStartFieldsEmpty() {
        return getFullStartField() == null ? true : false;
    }

    /**
     * Returns the {@link BeginField} of this path.
     * @return the BeginField
     */
    public Field getBeginField() {
        return path.get(0);
    }

    /**
     * Returns the owner of this path
     * @return the owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this paths
     * @param owner the owner
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public void takeRenderer(Renderer renderer) {
        renderer.render(this);
    }
}