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
package de.uni_mannheim.informatik.ai.ludo;

import de.uni_mannheim.informatik.ai.ludo.exceptions.InputDataException;
import de.uni_mannheim.informatik.ai.ludo.intent.IntentDispatcher;
import de.uni_mannheim.informatik.ai.ludo.intent.IntentFactory;
import de.uni_mannheim.informatik.ai.ludo.model.Game;
import de.uni_mannheim.informatik.ai.ludo.model.Player;
import de.uni_mannheim.informatik.ai.ludo.model.preferences.Preferences;
import de.uni_mannheim.informatik.ai.ludo.model.states.GameState;
import de.uni_mannheim.informatik.ai.ludo.model.states.InitState;
import de.uni_mannheim.informatik.ai.ludo.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starting class and controller.
 * Takes input from any view validates and dispatches valid input to the model. 
 * @author gtrefs
 */
public class Ludo {

    private View view;
    private static Ludo instance = new Ludo();

    public static Ludo getInstance() {
        return instance;
    }

    private Ludo() {
        this.view = Preferences.getInstance().loadDefaultView();
    }

    /**
     * Start the application.
     */
    public void start() {
        view.init(Game.getInstance());
        // Start the IntentDispatcher
        IntentDispatcher.getInstance().start();
        // Start the game
        Game.getInstance().start();
    }

    public static void main(String[] args) {
        Ludo.getInstance().start();
    }

    private Game.Color determineGameColorByString(String colorString){
        String lowerCaseColorString = colorString.toLowerCase();
        for(Game.Color color:Game.Color.values()){
            if(color.toString().toLowerCase().equals(lowerCaseColorString)){
                return color;
            }
        }
        return null;
    }

    /**
     * This method is invoked by any view when all init data has successfully been provided.
     * It throws an InputDataException if the Game is not in the init state or if the provided input is not valid (e.g. a player class can't be found).
     *
     * @param names The names of the players
     * @param colors The colors of the players
     * @param classes The player classes
     * @throws InputDataException thrown if the game is not in the init state or two players do have the same color
     */
    public void processInitDataFromView(List<String> names, List<String> colors, List<String> classes) throws InputDataException {
        GameState currentState = Game.getInstance().getState();
        List<Player> playersToAddToTheGame = new ArrayList<Player>();
        // If we are not in the init state, there should be nothing to initialize
        if (!currentState.getClass().equals(InitState.class)) {
            throw new InputDataException("Init data only allowed during Init Game State.");
        }
        // Load the classes and validate input
        for (int i = 0; i < names.size(); i++) {
            String classString = classes.get(i);
            try {
                Player player = (Player) Class.forName(classString).newInstance();
                player.setName(names.get(i));
                Game.Color color = determineGameColorByString(colors.get(i));
                if(color == null){
                    throw new InputDataException("Declared color ("+classString+") is not valid. Color must be one of BLUE, GREEN, YELLOW or RED.");
                }
                player.setColor(color);
                for(Player p:playersToAddToTheGame){
                    if(p.getColor().equals(player.getColor())){
                        throw new InputDataException("Same color detected. "+p.getClass()+" uses the same color as "+player.getClass()+".");
                    }
                }
                playersToAddToTheGame.add(player);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Ludo.class.getName()).log(Level.SEVERE, null, ex);
                throw new InputDataException("Class could not be found: " + classString);
            } catch (ClassCastException e) {
                Logger.getLogger(Ludo.class.getName()).log(Level.SEVERE, null, e);
                throw new InputDataException("Class " + classString + " does not implement the Player interface.");
            } catch (InstantiationException ex) {
                Logger.getLogger(Ludo.class.getName()).log(Level.SEVERE, null, ex);
                throw new InputDataException("Class " + classString + " could not be instantiated. Default constructor has arguments.");
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Ludo.class.getName()).log(Level.SEVERE, null, ex);
                throw new InputDataException("Default constructor of Class " + classString + " is not public.");
            }
        }
        // Everything has been fine so far, so now add the players
        for(Player player:playersToAddToTheGame){
            Game.getInstance().addPlayer(player);
        }
        // Advance to the first player
        Game.getInstance().nextPlayer();
        // Everything is initialized. Transist to the next game.
        IntentFactory.getInstance().createAndDispatchTransitionIntent(Game.getInstance());
    }

    /**
     * End the game.
     * @param code code to be returned by the application
     */
    public void exit(int code){
        // End the dispatcher
        IntentDispatcher.getInstance().stop();
        System.exit(code);
    }
}
