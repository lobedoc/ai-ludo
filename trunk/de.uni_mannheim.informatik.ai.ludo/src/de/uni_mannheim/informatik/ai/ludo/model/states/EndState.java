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
import de.uni_mannheim.informatik.ai.ludo.intent.MoveIntent;
import de.uni_mannheim.informatik.ai.ludo.intent.NewGameIntent;
import de.uni_mannheim.informatik.ai.ludo.intent.RollDiceIntent;
import de.uni_mannheim.informatik.ai.ludo.intent.TransitionIntent;
import de.uni_mannheim.informatik.ai.ludo.model.Game;
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEvent;
import de.uni_mannheim.informatik.ai.ludo.model.statistics.Statistics;

/**
 * Indicates, that the game has ended.
 * Read the corresponding documentation.
 * @author gtrefs
 */
public class EndState implements GameState{

    @Override
    public void processIntent(TransitionIntent intent) {

    }

    @Override
    public void processIntent(RollDiceIntent roleDiceIntent) {

    }

    @Override
    public void processIntent(MoveIntent moveIntent) {

    }

    @Override
    public void processIntent(EndGameIntent intent) {
        // Get the game
        Game game = intent.getTarget();
        intent.success();
        // Do some clean up.
        game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.START_WRITING_STATISTICS));
        Statistics.getInstance().writeOut();
        game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.END_WRITING_STATISTICS));
        // Tell the game, that all is over
        game.endGame();
    }

    @Override
    public void processIntent(NewGameIntent intent) {
    }

}