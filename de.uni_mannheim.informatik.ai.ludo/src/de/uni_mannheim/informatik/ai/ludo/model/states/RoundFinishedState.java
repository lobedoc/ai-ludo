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
import de.uni_mannheim.informatik.ai.ludo.model.Path;
import de.uni_mannheim.informatik.ai.ludo.model.Player;
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEvent;
import de.uni_mannheim.informatik.ai.ludo.model.statistics.Statistics;

/**
 * Indicates that a round has finished.
 * Read the corresponding documentation.
 * @author gtrefs
 */
public class RoundFinishedState implements GameState {

    private int diceCount;

    public RoundFinishedState(int diceCount) {
        this.diceCount = diceCount;
    }

    @Override
    public void processIntent(TransitionIntent intent) {
        intent.success();
        Game game = intent.getTarget();
        /*
         * 1. All pawns of currentPlayer at end fields --> game is won by current player
         * 2. If diceCount == 6 --> repeat round with same player
         * 3. If diceCount < 6 --> next round with next player
         */
        Player currentPlayer = game.getCurrentPlayer();
        Path playerPath = currentPlayer.getPath();
        if (playerPath.allEndFieldsFull()) {
            game.setState(new GameWonState());
            game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.GAME_WON));
            IntentFactory.getInstance().createAndDispatchTransitionIntent(game);
            return;
        }
        game.setState(new RoundStartedState());
        // Advance to next round
        game.nextRound();
        // Do the same with the statistics
        Statistics.getInstance().increaseNumberOfRounds();
        if (diceCount == 6) {
            // Same player
            game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.NEXT_ROUND_WITH_SAME_PLAYER));
        } else {
            // Next player
            game.nextPlayer();
            game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.NEXT_ROUND_WITH_NEXT_PLAYER));
        }
        IntentFactory.getInstance().createAndDispatchTransitionIntent(game);
        return;
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
    }
}