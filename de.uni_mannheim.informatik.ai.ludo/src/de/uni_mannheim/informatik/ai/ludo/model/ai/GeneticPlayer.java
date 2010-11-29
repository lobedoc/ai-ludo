/*
 *  Copyright (C) 2010 Gregor Trefs, Dominique Ritze
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.uni_mannheim.informatik.ai.ludo.model.ai;

import de.uni_mannheim.informatik.ai.ludo.intent.PlayerIntent;
import de.uni_mannheim.informatik.ai.ludo.model.Pawn;
import de.uni_mannheim.informatik.ai.ludo.model.PlayerAdapter;
import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderer;
import java.util.ArrayList;

/**
 *
 * @author Dominique Ritze
 */
public class GeneticPlayer extends PlayerAdapter{

    private ArrayList<Pawn> possiblePawns = new ArrayList<Pawn>();
    private Pawn pawnToMove;
    private Weights weights;

    public GeneticPlayer() {
        //this.weights = new Weights(new Double[]{95.0,4.0,0.0,50.0,3.0,7.0,55.0});
        //this.weights = new Weights(new Double[]{91.0,15.0,0.0,57.0,44.0,37.0,25.0});
        this.weights = new Weights(new Double[]{73.0,11.0,9.0,81.0,35.0,73.0,23.0});
    }

    @Override
    public Pawn movePawn() {
        double maximalValue = -100;
        pawnToMove = this.possiblePawns.get(0);
        //iterate over all own pawns and choose the one with the biggest score
        for(int i=0; i<this.possiblePawns.size(); i++){
            //get the socre of the utility measure for each pawn
            SimpleUtilityFunction utilityMeasure = new SimpleUtilityFunction(this.weights);
            if(maximalValue< utilityMeasure.getScore(possiblePawns.get(i))) {
                maximalValue = utilityMeasure.getScore(possiblePawns.get(i));
                pawnToMove = possiblePawns.get(i);
            }
        }
        return pawnToMove;
    }

    @Override
    public void setPawns(Pawn[] pawns) {
        this.pawns = pawns;
        for(int i=0; i<4; i++) {
            possiblePawns.add(this.pawns[i]);
        }
    }

    @Override
    public void rejectIntent(PlayerIntent intent) {
        possiblePawns.remove(possiblePawns.indexOf(pawnToMove));
    }

    @Override
    public void successIntent(PlayerIntent intent) {
        if(possiblePawns.size() == 4) {
            return;
        }
        possiblePawns.clear();
        for(int i=0; i<4; i++) {
            possiblePawns.add(this.pawns[i]);
        }
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
