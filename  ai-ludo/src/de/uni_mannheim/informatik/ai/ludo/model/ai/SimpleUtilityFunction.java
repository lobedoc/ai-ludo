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
 */
public class SimpleUtilityFunction implements UtilityFunction{

    @Override
    public int getScore(Pawn pawn) {

        int score = 0;
        Path path = pawn.getOwner().getPath();

        Field ownField = path.getFieldOfPawn(pawn);
        int index = path.getPathIndexOfField(ownField);

        Game game = Game.getInstance();
        for(Player x : game.getPlayers()) {
                if(x.getColor() == pawn.getOwner().getColor()) {
                    continue;
                }
                for(Pawn z : x.getPawns()) {
                    Field fieldOfOpp = z.getOwner().getPath().getFieldOfPawn(z);
                    int indexOpp = path.getPathIndexOfField(fieldOfOpp);
                    //check whether the pawn of an opponent is within the range of "my" pawn
                    if((Math.abs(indexOpp-index)<= 6.0 || Math.abs(indexOpp-index-40)<= 6.0 )&&
                            !(indexOpp == -1 || index == -1)
                            && !(index>40)) {
                        //beat other pawn directly
                        if(index+game.getDice().getCount() == indexOpp) {
                            score += 20;
                        }
                        //get into range to beat the opponent pawn
                        if(inRange(index+game.getDice().getCount(),indexOpp)) {
                            score +=5;
                        }
                        //get out of beating range
                        if(inRange(indexOpp, index) && !inRange(indexOpp,index+game.getDice().getCount())) {
                            score += 25;
                        }
                        //negative
                        //get within a beatable range
                        if(!inRange(indexOpp, index) && inRange(indexOpp, index+game.getDice().getCount())) {
                            score -= 25;
                        }
                        //get onto another start field
                        //TODO check whether pawns are inside or not?
                        if(index+game.getDice().getCount()%10 == 0) {
                            score -= 25;
                        }
                        
                    }
                    //get to target field
                    if(index+game.getDice().getCount()>40) {
                        score += 30;
                    }
                    //bonus 
                    score += index;
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
        if(firstIndex<34 && secondIndex<34) {
            if(Math.abs(secondIndex-firstIndex)<= 6.0) {
                return true;
            }
            else {
                return false;
            }
        }
        if(firstIndex>33) {
            int adaptedFirstIndex = firstIndex-40;
            if(secondIndex>33) {
                int adaptedSecondIndex = secondIndex-40;
                if(Math.abs(adaptedSecondIndex-adaptedFirstIndex)<= 6.0) {
                    return true;
                }
                else {
                    return false;
                }
            }
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
