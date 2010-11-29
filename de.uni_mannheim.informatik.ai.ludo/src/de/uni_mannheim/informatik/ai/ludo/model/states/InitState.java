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
package de.uni_mannheim.informatik.ai.ludo.model.states;

import de.uni_mannheim.informatik.ai.ludo.intent.EndGameIntent;
import de.uni_mannheim.informatik.ai.ludo.intent.IntentFactory;
import de.uni_mannheim.informatik.ai.ludo.intent.MoveIntent;
import de.uni_mannheim.informatik.ai.ludo.intent.NewGameIntent;
import de.uni_mannheim.informatik.ai.ludo.intent.RollDiceIntent;
import de.uni_mannheim.informatik.ai.ludo.intent.TransitionIntent;
import de.uni_mannheim.informatik.ai.ludo.model.Game;
import de.uni_mannheim.informatik.ai.ludo.model.events.RequestForUserInputEvent;

/**
 * The InitState is the first game state a game is in.
 * It waits for a {@link de.uni_mannheim.informatik.ai.ludo.intent.NewGameIntent NewGameIntent} in order to request initial input from the user.
 * After the initial input is provided this game state advances to the {@link de.uni_mannheim.informatik.ai.ludo.model.states.RoundStartedState RoundStartedState} if a {@link de.uni_mannheim.informatik.ai.ludo.intent.TransitionIntent TransitionIntent} occurs.
 * Read the corresponding documentation.
 * @author gtrefs
 */
public class InitState implements GameState{
    
    @Override
    public void processIntent(TransitionIntent intent) {
        intent.success();
        Game game = intent.getTarget();
        game.setState(new RoundStartedState());
        IntentFactory.getInstance().createAndDispatchTransitionIntent(game);
    }

    @Override
    public void processIntent(RollDiceIntent roleDiceIntent) {

    }

    @Override
    public void processIntent(MoveIntent moveIntent) {

    }

    @Override
    public void processIntent(EndGameIntent intent) {
        
    }

    @Override
    public void processIntent(NewGameIntent intent) {
        intent.success();
        Game game = intent.getTarget();
        game.reset();
        game.fireRequestForUserInputEvent(new RequestForUserInputEvent(game, RequestForUserInputEvent.Type.INIT_DATA));
    }

}