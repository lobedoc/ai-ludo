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
package de.uni_mannheim.informatik.ai.ludo.intent;

import de.uni_mannheim.informatik.ai.ludo.model.Game;
import de.uni_mannheim.informatik.ai.ludo.model.Pawn;
import de.uni_mannheim.informatik.ai.ludo.model.Player;
import de.uni_mannheim.informatik.ai.ludo.model.statistics.Statistics;

/**
 * This class creates concrete Intents which are dispatchable by the Intent-
 * Dispatcher.
 * @author gtrefs
 */
public class IntentFactory {

    private static IntentFactory INSTANCE = new IntentFactory();

    public static IntentFactory getInstance() {
        return INSTANCE;
    }

    private IntentFactory() {
    }

    public Intent createMoveIntent(Game game, Pawn pawnToMove) {
        return new MoveIntentImpl(game, pawnToMove);
    }

    public Intent createRollDiceIntent(Game game, Player player) {
        return new RollDiceIntentImpl(game, player);
    }

    public Intent createTransitionIntent(Game game) {
        return new TransitionIntentImpl(game);
    }

    public void createAndDispatchTransitionIntent(Game game) {
        IntentDispatcher dispatcher = IntentDispatcher.getInstance();
        Intent transitionIntent = createTransitionIntent(game);
        dispatcher.dispatchIntent(transitionIntent);
    }

    public void createAndDispatchMoveIntent(Game game, Pawn pawn) {
        IntentDispatcher dispatcher = IntentDispatcher.getInstance();
        Intent moveIntent = createMoveIntent(game, pawn);
        dispatcher.dispatchIntent(moveIntent);
    }

    public void createAndDispatchRollDiceIntent(Game game, Player player) {
        IntentDispatcher dispatcher = IntentDispatcher.getInstance();
        Intent rollIntent = createRollDiceIntent(game, player);
        dispatcher.dispatchIntent(rollIntent);
    }

    public Intent createNewGameIntent(Game game) {
        return new NewGameIntentImpl(game);
    }

    public void createAndDispatchNewGameIntent(Game game) {
        IntentDispatcher dispatcher = IntentDispatcher.getInstance();
        Intent newGameIntent = createNewGameIntent(game);
        dispatcher.dispatchIntent(newGameIntent);
    }

    public Intent createEndGameIntent(Game game) {
        return new EndGameIntentImpl(game);
    }

    public void createAndDispatchEndGameIntent(Game game) {
        IntentDispatcher dispatcher = IntentDispatcher.getInstance();
        Intent endGameIntent = createEndGameIntent(game);
        dispatcher.dispatchIntent(endGameIntent);
    }

    private class MoveIntentImpl implements MoveIntent {

        private Game game;
        private Pawn pawnToMove;
        private Player player;

        private MoveIntentImpl(Game game, Pawn pawnToMove) {
            this.game = game;
            this.pawnToMove = pawnToMove;
            this.player = pawnToMove.getOwner();
        }

        public void takeVisitor(IntentVisitor visitor) {
            visitor.processIntent(this);
        }

        public Pawn getPawnToMove() {
            return pawnToMove;
        }

        public void setPawnToMove(Pawn pawnToMove) {
            this.pawnToMove = pawnToMove;
        }

        public void execute() {
            game.handleIntent(this);
        }

        public void reject() {
            player.rejectIntent(this);
            Statistics.getInstance().rejectedIntent(this);
        }

        public void success() {
            player.successIntent(this);
            Statistics.getInstance().successfullIntent(this);
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public Game getTarget() {
            return game;
        }
    }

    private class RollDiceIntentImpl implements RollDiceIntent {

        private Game game;
        private Player player;

        private RollDiceIntentImpl(Game game, Player player) {
            this.game = game;
            this.player = player;
        }

        public void takeVisitor(IntentVisitor visitor) {
            visitor.processIntent(this);
        }

        public void execute() {
            game.handleIntent(this);
        }

        public void reject() {
            player.rejectIntent(this);
            Statistics.getInstance().rejectedIntent(this);
        }

        public void success() {
            player.successIntent(this);
            Statistics.getInstance().successfullIntent(this);
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public Game getTarget() {
            return game;
        }
    }

    private class TransitionIntentImpl implements TransitionIntent {

        private Game game;

        private TransitionIntentImpl(Game game) {
            this.game = game;
        }

        public void takeVisitor(IntentVisitor visitor) {
            visitor.processIntent(this);
        }

        public void execute() {
            game.handleIntent(this);
        }

        public void reject() {
            Statistics.getInstance().rejectedIntent(this);
        }

        public void success() {
            Statistics.getInstance().successfullIntent(this);
        }

        public Game getTarget() {
            return game;
        }
    }

    private class NewGameIntentImpl implements NewGameIntent {

        private Game game;

        private NewGameIntentImpl(Game game) {
            this.game = game;
        }

        public void takeVisitor(IntentVisitor visitor) {
            visitor.processIntent(this);
        }

        public void execute() {
           game.handleIntent(this);
        }

        public void reject() {
            Statistics.getInstance().rejectedIntent(this);
        }

        public void success() {
            Statistics.getInstance().successfullIntent(this);
        }

        public Game getTarget() {
            return game;
        }
    }

    private class EndGameIntentImpl implements EndGameIntent {

        private Game game;

        public EndGameIntentImpl(Game game) {
            this.game = game;
        }

        public void takeVisitor(IntentVisitor visitor) {
            visitor.processIntent(this);
        }

        public void execute() {
            game.handleIntent(this);
        }

        public void reject() {
            Statistics.getInstance().rejectedIntent(this);
        }

        public void success() {
            Statistics.getInstance().successfullIntent(this);
        }

        public Game getTarget() {
            return game;
        }
    }
}