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

import de.uni_mannheim.informatik.ai.ludo.model.Field;
import de.uni_mannheim.informatik.ai.ludo.model.Game;
import de.uni_mannheim.informatik.ai.ludo.model.Path;
import de.uni_mannheim.informatik.ai.ludo.model.Pawn;
import de.uni_mannheim.informatik.ai.ludo.model.Player;

/**
 *
 * @author Dominique Ritze
 *
 * Representation of a simple utility function consisting of 7 terms.
 * The terms describe different situations, e.g. a pawn can beat the
 * opponent or get on a end field.
 * All values of the terms are given by the TermValues.
 *
 */
public class SimpleUtilityFunction implements UtilityFunction{

    //weights containing the values of the 7 terms
    Weights weights;

    /**
     * Constuctor.
     * Needs the values of the terms in case of weights.
     *
     * @param weights Values of the terms.
     */
    public SimpleUtilityFunction(Weights weights) {
        this.weights = weights;
    }

    /**
     * Compute the score of a pawn accoring to the different
     * terms and their values which are defined in TermValues.
     * Therefore all cases according to the terms must be considered.
     * For example this includes to check whether this pawn
     * might beat an opponent. If some situation/case is true,
     * some value, defined in weights, is added/substracted to/from the final score.
     * In the end, the final result is returned.
     *
     * @param pawn A pawn which should be evaluated.
     * @return The final score of the given pawn.
     */
    @Override
    public double getScore(Pawn pawn) {

        double score = 0.0;
        //first need to know the path the pawn takes
        Path path = pawn.getOwner().getPath();

        //determine the field on which the pawn is currently located
        Field ownField = path.getFieldOfPawn(pawn);
        int index = path.getPathIndexOfField(ownField);

        Game game = Game.getInstance();
        //iterate through all opponents
        for(Player x : game.getPlayers()) {

            //skip own player
            if(x.getColor() == pawn.getOwner().getColor()) {
                continue;
            }
            //iterate through opponent pawns
            for(Pawn z : x.getPawns()) {
                //field of the opponent
                Field fieldOfOpp = z.getOwner().getPath().getFieldOfPawn(z);
                int indexOpp = path.getPathIndexOfField(fieldOfOpp);
                //check whether the pawn of an opponent is within the range of "my" pawn
                if((Math.abs(indexOpp-index)<= 6.0 || Math.abs(indexOpp-index-40)<= 6.0 )&&
                        !(indexOpp == -1 || index == -1)
                        && !(index>40)) {
                    //beat other pawn directly
                    if(index+game.getDice().getCount() == indexOpp) {
                        score += weights.getDIRECT_BEAT();
                    }
                    //get into range to beat the opponent pawn
                    if(inRange(index+game.getDice().getCount(),indexOpp)) {
                        score += weights.getINDIRECT_BEAT();
                    }
                    //get out of beating range
                    if(inRange(indexOpp, index) && !inRange(indexOpp,index+game.getDice().getCount())) {
                        score += weights.getESCAPE_POSSIBLE_BEAT();
                    }
                    //negative
                    //get within a beatable range
                    if(!inRange(indexOpp, index) && inRange(indexOpp, index+game.getDice().getCount())) {
                        score -= weights.getENTER_POSSIBLE_BEAT();
                    }
                    //get onto another beginning field
                    if(index+game.getDice().getCount()%10 == 0) {
                        score -= weights.getSTART_POSITION();
                    }
                }
                //get to target field
                if(index+game.getDice().getCount()>40) {
                    score += weights.getENTER_TARGET();
                }
                //bonus
                score += index*weights.getPROGRESS_FACTOR()/40;
            }
        }
        return score;
    }

    /**
     * Check whether the first index is in range of the second one.
     * This means firstIndex in [secondIndex-6,secondIndex-1]
     *
     * @param firstIndex
     * @param secondIndex
     * @return
     */
    private static boolean inRange(int firstIndex, int secondIndex) {

        //before the "circle" closes, nothing to consider
        if(firstIndex<34 && secondIndex<34) {
            //just check whether the difference is less than 7
            if(Math.abs(secondIndex-firstIndex)<= 6.0) {
                return true;
            }
            else {
                return false;
            }
        }
        //at the end of the "circle", be careful of indicies
        if(firstIndex>33) {
            //adapt the indicies
            int adaptedFirstIndex = firstIndex-40;
            if(secondIndex>33) {
                int adaptedSecondIndex = secondIndex-40;
                //with adapted indicies easy to check whether difference less than 7
                if(Math.abs(adaptedSecondIndex-adaptedFirstIndex)<= 6.0) {
                    return true;
                }
                else {
                    return false;
                }
            }
            //second one must not be adapted because it is at
            //the beginning and not at the end
            if(Math.abs(secondIndex-adaptedFirstIndex)<= 6.0) {
                return true;
            }
            else {
                return false;
            }

        }
        return false;
    }
}
