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
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEvent;
import de.uni_mannheim.informatik.ai.ludo.model.events.RequestForUserInputEvent;
import de.uni_mannheim.informatik.ai.ludo.model.preferences.Preferences;
import de.uni_mannheim.informatik.ai.ludo.model.statistics.Statistics;

/**
 * Indicates that the current game has been won.
 * Read the corresponding documentation.
 * @author gtrefs
 */
public class GameWonState implements GameState {

    @Override
    public void processIntent(TransitionIntent intent) {
        intent.success();
        // Get the game
        Game game = intent.getTarget();
        // Tell the statistics, that the current player has won
        Statistics.getInstance().gameWonByPlayer(intent, game.getCurrentPlayer());
        // Tell the game, that this game has been played
        game.increaseGamesPlayed();
        // When the game is in simulation mode, we proceed to the next game or end (MAX_GAMES = currentGame)
        int gamesPlayed = game.getGamesPlayed();
        int maxGames = Preferences.getInstance().getMaxRound();
        // 1. simulation
        if (Preferences.getInstance().isInSimulationMode()) {
            // 1.1 End this game
            if (gamesPlayed >= maxGames) {
                game.setState(new EndState());
                game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.END_GAME));
                IntentFactory.getInstance().createAndDispatchEndGameIntent(game);
                return;
            }
            // 1.2 new game
            game.setState(new RoundStartedState());
            game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.NEW_GAME));
            IntentFactory.getInstance().createAndDispatchNewGameIntent(game);
            return;
        }
        // 2. no simulation
        game.fireRequestForUserInputEvent(new RequestForUserInputEvent(game, RequestForUserInputEvent.Type.GAME_END_REACHED));
    }

    @Override
    public void processIntent(RollDiceIntent roleDiceIntent) {
    }

    @Override
    public void processIntent(MoveIntent moveIntent) {
    }

    @Override
    public void processIntent(EndGameIntent intent) {
        Game game = intent.getTarget();
        intent.success();
        game.setState(new EndState());
        IntentFactory.getInstance().createAndDispatchEndGameIntent(game);
    }

    @Override
    public void processIntent(NewGameIntent intent) {
        Game game = intent.getTarget();
        intent.success();
        game.setState(new InitState());
        IntentFactory.getInstance().createAndDispatchNewGameIntent(game);
    }
}