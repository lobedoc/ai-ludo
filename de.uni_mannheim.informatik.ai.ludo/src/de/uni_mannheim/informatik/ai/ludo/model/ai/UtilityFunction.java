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
import de.uni_mannheim.informatik.ai.ludo.model.Pawn;

/**
 *
 * @author Dominique Ritze
 *
 * The utility function is used to evaluate possible following game states.
 * In order to determine the best possible following state, the possible
 * moves must be evaluated. Therefore a score is assigned to each possible
 * move. This interface is used to provide an uniform way such that all
 * utility functions are forced to implement a method to score the pawns.
 *
 */
public interface UtilityFunction {

    /**
     * Computes a score for a given pawn.
     * Afterwards the pawn with the highest score will be moved.
     *
     * @param pawn Possible pawn to move.
     * @return The core of the pawn.
     */
    public double getScore(Pawn pawn);

}