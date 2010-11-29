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
import de.uni_mannheim.informatik.ai.ludo.model.Field;
import de.uni_mannheim.informatik.ai.ludo.model.Game;
import de.uni_mannheim.informatik.ai.ludo.model.Path;
import de.uni_mannheim.informatik.ai.ludo.model.Pawn;
import de.uni_mannheim.informatik.ai.ludo.model.Player;
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEvent;
import de.uni_mannheim.informatik.ai.ludo.model.statistics.Statistics;

/**
 * This state indicates that moveable {@link de.uni_mannheim.informatik.ai.ludo.model.Pawn}s have been marked and the player should move a pawn.
 * Read the corresponding documentation.
 * @author gtrefs
 */
public class MoveablePawnsMarkedState implements GameState {

    private Pawn[] markedPawns;
    private int diceCount;

    public MoveablePawnsMarkedState(Pawn[] markedPawns, int diceCount) {
        this.markedPawns = markedPawns;
        this.diceCount = diceCount;
    }

    @Override
    public void processIntent(TransitionIntent intent) {
        intent.success();
        Game game = intent.getTarget();
        if (markedPawns.length == 0) {
            game.setState(new RoundFinishedState(diceCount));
            game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.NO_MOVEABLE_PAWNS));
            IntentFactory.getInstance().createAndDispatchTransitionIntent(game);
            return;
        }
        Player currentPlayer = game.getCurrentPlayer();
        IntentFactory.getInstance().createAndDispatchMoveIntent(game, currentPlayer.movePawn());
    }

    @Override
    public void processIntent(RollDiceIntent roleDiceIntent) {
    }

    @Override
    public void processIntent(MoveIntent moveIntent) {
        Game game = moveIntent.getTarget();
        Pawn pawnToMove = moveIntent.getPawnToMove();
        Player currentPlayer = pawnToMove.getOwner();
        for (Pawn p : markedPawns) {
            if (pawnToMove.equals(p)) {
                Path playerPath = game.getCurrentPlayer().getPath();
                Field currentField = playerPath.getFieldOfPawn(p);
                // Take pawn from current field
                currentField.takePawnFromField();
                // New field
                int newIndex = playerPath.getPathIndexOfField(currentField) + diceCount;
                Field newField = playerPath.getFieldByIndex(newIndex);
                // Hit Test !
                if (!newField.isEmpty()) {
                    Pawn otherPlayersPawn = newField.takePawnFromField();
                    otherPlayersPawn.getOwner().getPath().placeOnStartField(otherPlayersPawn);
                    // Tell the player that he actually kicked a butt.
                    currentPlayer.enemyPawnThrownByIntent(otherPlayersPawn, moveIntent);
                    // Tell the statistics that this player kicked a butt.
                    Statistics.getInstance().enemyPawnThrownByPlayer(moveIntent, currentPlayer, otherPlayersPawn);
                    // Tell the view the about the lucky message
                    game.fireNotificationEvent(new NotificationEvent(otherPlayersPawn, NotificationEvent.Type.ENEMY_PAWN_THROWN));
                }

                newField.placePawnOnField(p);
                // Intent has been successfull
                moveIntent.success();
                // Notifiy about the move
                game.setState(new RoundFinishedState(diceCount));
                game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.MOVEABLE_PAWN_MOVED));
                IntentFactory.getInstance().createAndDispatchTransitionIntent(game);
                return;
            }
        }
        // No moveable pawn selected.
        moveIntent.reject();
        // Notify the view
        game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.TRIED_TO_MOVE_NONMOVEABLE_PAWN));
        // Do it once again.
        IntentFactory.getInstance().createAndDispatchMoveIntent(game, game.getCurrentPlayer().movePawn());
    }

    @Override
    public void processIntent(EndGameIntent intent) {
    }

    @Override
    public void processIntent(NewGameIntent intent) {
    }
}
