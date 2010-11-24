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
import java.util.ArrayList;

/**
 *
 * @author gtrefs
 */
public class DiceRolledState implements GameState {

    private int attemps = 0;

    @Override
    public void processIntent(TransitionIntent intent) {
    }

    private Pawn[] determineMoveablePawns(Player p, Path playerPath, int diceCount) {
        ArrayList<Pawn> ret = new ArrayList<Pawn>();
        Pawn[] playerPawns = p.getPawns();

        for (Pawn pawn : playerPawns) {
            if (playerPath.isOnStartField(pawn)) {
                continue;
            }
            Field pawnField = playerPath.getFieldOfPawn(pawn);
            int fieldIndex = playerPath.getPathIndexOfField(pawnField);
            int nextFieldIndex = fieldIndex + diceCount;
            Field nextField = playerPath.getFieldByIndex(nextFieldIndex);
            if (nextField != null && playerPath.fieldNotPossedByAnotherOwnerPawn(nextField)) {
                // The pawn has to move from the begin field if he can, so he gets the only moveable pawn
                if (pawnField.equals(playerPath.getBeginField()) && !playerPath.allStartFieldsEmpty()) {
                    return new Pawn[]{pawn};
                }
                ret.add(pawn);
            }
        }
        return ret.toArray(new Pawn[0]);
    }

    @Override
    public void processIntent(RollDiceIntent roleDiceIntent) {
        // Intent arrived
        // Get the game
        Game game = roleDiceIntent.getTarget();
        // Within this state the intent is valid.
        // Increase attempt count, if the dice number was not 6.
        int diceCount = game.getDice().getCount();
        if (diceCount != 6) {
            attemps++;
        }
        // Intent has been successfull
        roleDiceIntent.success();
        Player currentPlayer = game.getCurrentPlayer();
        Path playerPath = currentPlayer.getPath();
        if (diceCount != 6 && playerPath.noPawnOnBoardFields()) {
            if (attemps == 3) {
                game.setState(new RoundFinishedState(diceCount));
                game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.NO_MORE_ATTEMPS));
                IntentFactory.getInstance().createAndDispatchTransitionIntent(game);
                return;
            }
            currentPlayer.rollTheDice();
            return;
        }
        if (diceCount == 6 && playerPath.beginFieldNotPossessedByAnotherOwnerPawn() && playerPath.isAtLeastOneStartFieldFull()) {
            // Take pawn and place it on the beginField
            Field s = playerPath.getFullStartField();
            Pawn ownerPawn = s.takePawnFromField();
            Field f = playerPath.getBeginField();
            // Hit check !
            if (!f.isEmpty()) {
                Pawn pawnOnField = f.takePawnFromField();
                pawnOnField.getOwner().getPath().placeOnStartField(pawnOnField);
                // Tell the player that he actually kicked a butt.
                currentPlayer.enemyPawnThrownByIntent(pawnOnField, roleDiceIntent);
                game.fireNotificationEvent(new NotificationEvent(pawnOnField, NotificationEvent.Type.PAWN_ENTERED_THREW_ENEMY_PAWN));
            }
            // Place my pawn on field
            f.placePawnOnField(ownerPawn);
            game.fireNotificationEvent(new NotificationEvent(ownerPawn, NotificationEvent.Type.PAWN_ENTERED));
            // Role the dice once again
            currentPlayer.rollTheDice();
            return;
        }
        game.setState(new MoveablePawnsMarkedState(determineMoveablePawns(currentPlayer, playerPath, diceCount), diceCount));
        game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.REFRESH));
        IntentFactory.getInstance().createAndDispatchTransitionIntent(game);
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
