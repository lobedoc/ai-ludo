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
 * It is the registry point of all parties which are interested in model events. 
 * It has different {@link de.uni_mannheim.informatik.ai.ludo.model.states.GameState GameState}s each of which representing a game state in the real world.
 * @author gtrefs
 */
public class Game implements Renderable {

    /**
     * This enum represents the colors present in the game.
     */
    public enum Color {

        RED, BLUE, GREEN, YELLOW
    }

    /*
     * Events
     */
    // UserInputEvents
    private List<RequestForUserInputEventListener> requestListeners;

    /**
     * Registers a {@link de.uni_mannheim.informatik.ai.ludo.model.events.RequestForUserInputEventListener RequestForUserInputEventListener}.
     * @param l RequestForUserInputEventListener interested in requests for user inputs
     */
    public void addRequestForUserInputEventListener(RequestForUserInputEventListener l) {
        requestListeners.add(l);
    }

    /**
     * Removes an {@link de.uni_mannheim.informatik.ai.ludo.model.events.RequestForUserInputEventListener RequestForUserInputEventListener}.
     * @param l RequestForUserInputEventListener which should be removed
     */
    public void removeRequestForUserInputEventListener(RequestForUserInputEventListener l) {
        requestListeners.remove(l);
    }

    /**
     * Fires an {@link de.uni_mannheim.informatik.ai.ludo.model.events.RequestForUserInputEvent RequestForUserInputEvent} to every registered {@link de.uni_mannheim.informatik.ai.ludo.model.events.RequestForUserInputEventListener RequestForUserInputEventListener}.
     * @param e Event which should be fired
     */
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

    /**
     * Registers a {@link de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEventListener NotificationEventListener}.
     * @param l NotificationEventListener interested in notifications
     */
    public void addNotificationEventListener(NotificationEventListener l) {
        notificationListeners.add(l);
    }

    /**
     * Removes an {@link de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEventListener NotificationEventListener}.
     * @param l NotificationEventListener which should be removed
     */
    public void removeNotificationEventListener(NotificationEventListener l) {
        notificationListeners.remove(l);
    }

    /**
     * Fires an {@link de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEvent NotificationEvent} to every registered {@link de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEventListener NotificationEventListener}.
     * @param e Event which should be fired
     */
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

    /**
     * Return total games played.
     * @return Number of games played.
     */
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    /**
     * Iterate the total games played number by 1.
     */
    public void increaseGamesPlayed() {
        gamesPlayed++;
    }

    /**
     * End the current game.
     */
    public void endGame() {
        // Some crucial cleanup
        // Tell the controller, we are finished
        Ludo.getInstance().exit(0);
    }

    /**
     * Start the game.
     */
    public void start() {
        IntentFactory.getInstance().createAndDispatchNewGameIntent(this);
    }

    /**
     * Handle an {@link de.uni_mannheim.informatik.ai.ludo.intent.Intent Intent}.
     * @param intent Intent coming from the {@link de.uni_mannheim.informatik.ai.ludo.intent.IntentDispatcher IntentDispatcher}
     */
    public void handleIntent(Intent intent) {
        intent.takeVisitor(state);
    }

    /**
     * Reset the game.
     * Leads to a notification.
     */
    public void reset() {
        board.reset();
        playerIndex = 0;
        currentRound = 1;
        fireNotificationEvent(new NotificationEvent(this, NotificationEvent.Type.NEW_GAME));
    }

    /**
     * Advance to the next player.
     */
    public void nextPlayer() {
        currentPlayer = players.get(playerIndex++ % players.size());
    }

    /**
     * Increase the round count by 1.
     */
    public void nextRound() {
        currentRound++;
    }

    /**
     * Returns the current roud count.
     * @return current round count
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Return the current {@link de.uni_mannheim.informatik.ai.ludo.model.states.GameState GameState}
     * @return current GameState
     */
    public GameState getState() {
        return state;
    }

    /**
     * Sets the current {@link de.uni_mannheim.informatik.ai.ludo.model.states.GameState GameState}
     * @param state GameState
     */
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Returns the Game's {@link de.uni_mannheim.informatik.ai.ludo.model.Dice Dice}.
     * @return Dice
     */
    public Dice getDice() {
        return dice;
    }

    /**
     * Returns the {@link Player Player} which is currently in turn.
     * @return Player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Add a {@link Player Player} to the game.
     * If the Player is not introduceable, it is not added to the game.
     * @param Player the Player
     */
    public void addPlayer(Player player) {
        try {
            board.introducePlayerToTheBoard(player);
        } catch (ColorException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, "Player " + player.getName() + " could not be added to the game.", ex);
            return;
        }
        players.add(player);
    }

    /**
     * Return all {@link Player Player}s which play this game.
     * @return List of all Players which play this game.
     */
    public List<Player> getPlayers() {
        return players;
    }
}
