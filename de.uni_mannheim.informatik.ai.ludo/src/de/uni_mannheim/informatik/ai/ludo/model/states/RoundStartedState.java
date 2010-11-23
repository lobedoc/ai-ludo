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
import de.uni_mannheim.informatik.ai.ludo.model.Player;
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEvent;

/**
 * The IdelState class is the first state the game enters.
 * The current player is handed over to the processIntent
 * method which invokes the rollTheDice() method on the player. If user input
 * is required, a corresponding exception is thrown which results into a event
 * to the view. If the player is an artificial player, the method returns a
 * normal dice intent which is being processed by this state.
 * This processing eventually results into a state transition.
 *
 * @author gtrefs
 */
public class RoundStartedState implements GameState {


    @Override
    public void processIntent(TransitionIntent intent) {
        // intent has been successfull
        intent.success();
        Game game = intent.getTarget();
        Player currentPlayer = game.getCurrentPlayer();
        game.setState(new DiceRolledState());
        currentPlayer.rollTheDice();
        // To avoid anomalies, the player has to notify the view.
        // game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.DICE_ROLLED));
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
        // Reset the game
        game.reset();
        // Invoke transition
        IntentFactory.getInstance().createAndDispatchTransitionIntent(game);

    }
}