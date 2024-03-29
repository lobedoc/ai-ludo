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

import de.uni_mannheim.informatik.ai.ludo.intent.IntentFactory;
import de.uni_mannheim.informatik.ai.ludo.intent.PlayerIntent;
import de.uni_mannheim.informatik.ai.ludo.model.Game;
import de.uni_mannheim.informatik.ai.ludo.model.Game.Color;
import de.uni_mannheim.informatik.ai.ludo.model.Path;
import de.uni_mannheim.informatik.ai.ludo.model.Pawn;
import de.uni_mannheim.informatik.ai.ludo.model.Player;
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEvent;
import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderer;
import java.util.ArrayList;

/**
 * This artificial Player which uses a utility function to evaluate the game states.
 * In this case the values of the terms of the utility function are determined
 * by using a genetic algorithm.
 *
 * @author Dominique Ritze
 */
public class GeneticPlayer implements Player{

    private Pawn[] pawns;
    private ArrayList<Pawn> possiblePawns = new ArrayList<Pawn>();
    private Game.Color color;
    private Path path;
    private String name;
    private Pawn pawnToMove;
    private Weights weights;

    public GeneticPlayer() {
    //size = 100    this.weights = new Weights(new Double[]{95.0,4.0,0.0,50.0,3.0,7.0,55.0});
    //size = 500    this.weights = new Weights(new Double[]{91.0,15.0,0.0,57.0,44.0,37.0,25.0});
        this.weights = new Weights(new Double[]{73.0,11.0,9.0,81.0,35.0,73.0,23.0});
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void movePawn() {
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
        //move the pawn
        IntentFactory.getInstance().createAndDispatchMoveIntent(Game.getInstance(), pawnToMove);
    }

    @Override
    public void rollTheDice() {
        Game game = Game.getInstance();
        // Roll the dice
        game.getDice().rollDice();
        // Tell the view about the situation
        game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.DICE_ROLLED));
        // Update
        IntentFactory.getInstance().createAndDispatchRollDiceIntent(Game.getInstance(), this);
    }

    @Override
    public void setPawns(Pawn[] pawns) {
        this.pawns = pawns;
        for(int i=0; i<4; i++) {
            possiblePawns.add(this.pawns[i]);
        }
    }

    @Override
    public Pawn[] getPawns() {
        return pawns;
    }

    @Override
    public Pawn getPawn(int index) {
        return pawns[index % pawns.length];
    }

    @Override
    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public Path getPath() {
        return path;
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
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void takeRenderer(Renderer renderer) {
        renderer.render(this);
    }

}
