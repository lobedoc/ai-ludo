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
package de.uni_mannheim.informatik.ai.ludo.view.shellView;

import de.uni_mannheim.informatik.ai.ludo.Ludo;
import de.uni_mannheim.informatik.ai.ludo.exceptions.InputDataException;
import de.uni_mannheim.informatik.ai.ludo.model.Game;
import de.uni_mannheim.informatik.ai.ludo.model.Pawn;
import de.uni_mannheim.informatik.ai.ludo.model.Player;
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEvent;
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEventListener;
import de.uni_mannheim.informatik.ai.ludo.model.events.RequestForUserInputEvent;
import de.uni_mannheim.informatik.ai.ludo.model.events.RequestForUserInputEventListener;
import de.uni_mannheim.informatik.ai.ludo.model.preferences.Preferences;
import de.uni_mannheim.informatik.ai.ludo.view.View;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This view is supposed to print out the game on a shell.
 * Use this one to display the simulation.
 * @author gtrefs
 */
public class ShellView implements View {

    private NotificationListener notificationListener;
    private RequestForUserInputListener requestForUserInputListener;

    public ShellView() {
        notificationListener = new NotificationListener();
        requestForUserInputListener = new RequestForUserInputListener();
    }

    @Override
    public void init(Game game) {
        // Listen to the events to occur
        game.addNotificationEventListener(notificationListener);
        game.addRequestForUserInputEventListener(requestForUserInputListener);
    }

    @Override
    public void shutdown() {
    }

    private void createInitData() {
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> colors = new ArrayList<String>();
        ArrayList<String> classes = new ArrayList<String>();
        String playersString = Preferences.getInstance().getProperty("ludo.view.shellView.init.players");
        String[] playersDecon = playersString.split(";");
        for (String playerDecon : playersDecon) {
            String[] playerDetails = playerDecon.split(":");
            names.add(playerDetails[0]);
            colors.add(playerDetails[1]);
            classes.add(playerDetails[2]);
        }
        try {
            Ludo.getInstance().processInitDataFromView(names, colors, classes);
        } catch (InputDataException ex) {
            Ludo.getInstance().exit(1);
        }
    }

    private void endGameReached() {
        // Ask the player to repeat the game
        // Hand over to controller
    }

    private void diceRolled(NotificationEvent e) {
        Game game = (Game) e.getSource();
 //       System.out.println("Player " + game.getCurrentPlayer().getName() + " [" + game.getCurrentPlayer().getClass().getName() + "] rolled the dice. Dice count: " + game.getDice().getCount());
    }

    private void noMoreAttemps(NotificationEvent e) {
        Game game = (Game) e.getSource();
 //       System.out.println("Player " + game.getCurrentPlayer().getName() + " [" + game.getCurrentPlayer().getClass().getName() + "] has no more attemps. Next player.");
    }

    private void pawnEnteredThrewEnemyPawn(NotificationEvent e) {
        Pawn enemyPawn = (Pawn) e.getSource();
        Game game = Game.getInstance();
 //       System.out.println("Player " + game.getCurrentPlayer().getName() + " [" + game.getCurrentPlayer().getClass().getName() + "] threw pawn on entry. Enemy pawn Owner: " + enemyPawn.getOwner().getName() + " [" + enemyPawn.getOwner().getClass().getName() + "]");
    }

    private void pawnEntered(NotificationEvent e) {
        Game game = Game.getInstance();
 //       System.out.println("Player " + game.getCurrentPlayer().getName() + " [" + game.getCurrentPlayer().getClass().getName() + "] enters the arena.");
    }

    private void refresh(NotificationEvent e) {
 //       System.out.println("[REFRESH]");
    }

    private void triedToMoveNonmoveablePawn(NotificationEvent e) {
        Game game = (Game) e.getSource();
//        System.out.println("Player " + game.getCurrentPlayer().getName() + " [" + game.getCurrentPlayer().getClass().getName() + "] tried to move a pawn which is unmoveable. Let's give him another chance.");
    }

    private void noMoveablePawns(NotificationEvent e) {
        Game game = (Game) e.getSource();
 //       System.out.println("Player " + game.getCurrentPlayer().getName() + " [" + game.getCurrentPlayer().getClass().getName() + "] has no moveable pawns. Finish this round.");
    }

    private void gameWon(NotificationEvent e) {
        Game game = (Game) e.getSource();
//        System.out.println("Player " + game.getCurrentPlayer().getName() + " [" + game.getCurrentPlayer().getClass().getName() + "] won. Congratulations.");
    }

    private void nextRoundWithSamePlayer(NotificationEvent e) {
        Game game = (Game) e.getSource();
  //      System.out.println("Player " + game.getCurrentPlayer().getName() + " [" + game.getCurrentPlayer().getClass().getName() + "] threw a 6. Next round with same player.");
    }

    private void nextRoundWithNextPlayer(NotificationEvent e) {
        Game game = (Game) e.getSource();
  //      System.out.println("Player " + game.getCurrentPlayer().getName() + " [" + game.getCurrentPlayer().getClass().getName() + "] didn't threw a 6. Next round with next player.");
    }

    private void newGame(NotificationEvent e) {
  //      System.out.println("New game with same configuration started.");
    }

    private void endGame(NotificationEvent e) {
  //      System.out.println("End game.");
    }

    private void startWritingStatistics(NotificationEvent e) {
  //      System.out.println("Start writing statistics.");
    }

    private void endWritingStatistics(NotificationEvent e) {
  //      System.out.println("End writing statistics.");
    }

    private void moveablePawnMoved(NotificationEvent e) {
        Game game = (Game) e.getSource();
 //       System.out.println("Player " + game.getCurrentPlayer().getName() + " [" + game.getCurrentPlayer().getClass().getName() + "] moved a moveable pawn.");
    }

    private void enemyPawnThrown(NotificationEvent e) {
        Pawn enemyPawn = (Pawn) e.getSource();
        Game game = Game.getInstance();
 //       System.out.println("Player " + game.getCurrentPlayer().getName() + " [" + game.getCurrentPlayer().getClass().getName() + "] threw a pawn. Enemy pawn Owner: " + enemyPawn.getOwner().getName() + " [" + enemyPawn.getOwner().getClass().getName() + "]");
    }

    private class NotificationListener implements NotificationEventListener {

        @Override
        public void notify(NotificationEvent e) {
            //Logger.getLogger("ludo.view.shellView").log(Level.INFO, "Notificationevent arrived.\n {0}", e);
            switch (e.getType()) {
                case DICE_ROLLED:
                    diceRolled(e);
                    break;
                case END_GAME:
                    endGame(e);
                    break;
                case END_WRITING_STATISTICS:
                    endWritingStatistics(e);
                    break;
                case GAME_WON:
                    gameWon(e);
                    break;
                case MOVEABLE_PAWN_MOVED:
                    moveablePawnMoved(e);
                    break;
                case NEW_GAME:
                    newGame(e);
                    break;
                case NEXT_ROUND_WITH_NEXT_PLAYER:
                    nextRoundWithNextPlayer(e);
                    break;
                case NEXT_ROUND_WITH_SAME_PLAYER:
                    nextRoundWithSamePlayer(e);
                    break;
                case NO_MORE_ATTEMPS:
                    noMoreAttemps(e);
                    break;
                case NO_MOVEABLE_PAWNS:
                    noMoveablePawns(e);
                    break;
                case PAWN_ENTERED:
                    pawnEntered(e);
                    break;
                case REFRESH:
                    refresh(e);
                    break;
                case START_WRITING_STATISTICS:
                    startWritingStatistics(e);
                    break;
                case PAWN_ENTERED_THREW_ENEMY_PAWN:
                    pawnEnteredThrewEnemyPawn(e);
                    break;
                case TRIED_TO_MOVE_NONMOVEABLE_PAWN:
                    triedToMoveNonmoveablePawn(e);
                    break;
                case ENEMY_PAWN_THROWN:
                    enemyPawnThrown(e);
                    break;
            }
        }
    }

    private class RequestForUserInputListener implements RequestForUserInputEventListener {

        @Override
        public void demandForInputOccured(RequestForUserInputEvent r) {
            //Logger.getLogger("ludo.view.shellView").log(Level.INFO, "RequestForUserInputEvent arrived.\n {0}", r);
            switch (r.getType()) {
                case INIT_DATA:
                    createInitData();
                    break;
                case REQUEST_FOR_MOVE:
                    break;
                case REQUEST_FOR_ROLLING_THE_DICE:
                    break;
                case GAME_END_REACHED:
                    endGameReached();
                    break;
            }
        }
    }
}
