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
package de.uni_mannheim.informatik.ai.ludo.model;

import de.uni_mannheim.informatik.ai.ludo.Ludo;
import de.uni_mannheim.informatik.ai.ludo.exceptions.ColorException;
import de.uni_mannheim.informatik.ai.ludo.intent.Intent;
import de.uni_mannheim.informatik.ai.ludo.intent.IntentFactory;
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEvent;
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEventListener;
import de.uni_mannheim.informatik.ai.ludo.model.events.RequestForUserInputEvent;
import de.uni_mannheim.informatik.ai.ludo.model.events.RequestForUserInputEventListener;
import de.uni_mannheim.informatik.ai.ludo.model.states.GameState;
import de.uni_mannheim.informatik.ai.ludo.model.states.InitState;
import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderable;
import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Game class is the main model class.
 * It is the registry point of all parties which are interested in model events. It has different states each of which represent a game state in the real world.
 * The game states are implemented with the {@link http://en.wikipedia.org/wiki/State_pattern state pattern}.
 * See interface {@link de.uni_mannheim.informatik.ai.ludo.model.states.GameState GameState} for further details.
 *
 * @author gtrefs
 */
public class Game implements Renderable {

    public enum Color {

        RED, BLUE, GREEN, YELLOW
    }

    /*
     * Events
     */
    // UserInputEvents
    private List<RequestForUserInputEventListener> requestListeners;

    public void addRequestForUserInputEventListener(RequestForUserInputEventListener l) {
        requestListeners.add(l);
    }

    public void removeRequestForUserInputEventListener(RequestForUserInputEventListener l) {
        requestListeners.remove(l);
    }

    public void fireRequestForUserInputEvent(RequestForUserInputEvent e) {
        synchronized (this) {
            List<RequestForUserInputEventListener> listenerList = new ArrayList<RequestForUserInputEventListener>(requestListeners);
            for (RequestForUserInputEventListener l : listenerList) {
                l.demandForInputOccured(e);
            }
        }
    }
    // Notification events
    private List<NotificationEventListener> notificationListeners;

    public void addNotificationEventListener(NotificationEventListener l) {
        notificationListeners.add(l);
    }

    public void removeNotificationEventListener(NotificationEventListener l) {
        notificationListeners.remove(l);
    }

    public void fireNotificationEvent(NotificationEvent e) {
        synchronized (this) {
            List<NotificationEventListener> listenerList = new ArrayList<NotificationEventListener>(notificationListeners);
            for (NotificationEventListener l : listenerList) {
                l.notify(e);
            }
        }
    }
    /*
     * Game vars
     */
    private GameState state;
    private List<Player> players;
    private Board board;
    private Dice dice;
    private Player currentPlayer;
    private int playerIndex = 0;
    // How many games already played ?
    private int gamesPlayed = 0;
    private int currentRound = 1;

    /*
     * Singleton
     */
    private static Game INSTANCE = new Game();

    public static Game getInstance() {
        return INSTANCE;
    }

    private Game() {
        notificationListeners = new ArrayList<NotificationEventListener>();
        requestListeners = new ArrayList<RequestForUserInputEventListener>();
        dice = Dice.getInstance();
        players = new ArrayList<Player>();
        state = new InitState();
        board = new Board();
    }

    @Override
    public void takeRenderer(Renderer renderer) {
        renderer.render(this);
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void increaseGamesPlayed() {
        gamesPlayed++;
    }

    public void endGame() {
        // Some crucial cleanup
        // Tell the controller, we are finished
        Ludo.getInstance().exit(0);
    }

    public void start() {
        IntentFactory.getInstance().createAndDispatchNewGameIntent(this);
    }

    public void handleIntent(Intent intent) {
        intent.takeVisitor(state);
    }

    public void reset() {
        board.reset();
        playerIndex = 0;
        currentRound = 1;
        fireNotificationEvent(new NotificationEvent(this, NotificationEvent.Type.NEW_GAME));
    }

    public void nextPlayer() {
        currentPlayer = players.get(playerIndex++ % players.size());
    }

    public void nextRound() {
        currentRound++;
    }

    public int getCurrentRound(){
        return currentRound;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Dice getDice() {
        return dice;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void addPlayer(Player player) {
        try {
            board.introducePlayerToTheBoard(player);
        } catch (ColorException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, "Player " + player.getName() + " could not be added to the game.", ex);
            return;
        }
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

}
