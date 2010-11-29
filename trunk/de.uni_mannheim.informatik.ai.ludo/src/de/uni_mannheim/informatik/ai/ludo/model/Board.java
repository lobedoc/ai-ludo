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

import de.uni_mannheim.informatik.ai.ludo.exceptions.ColorException;
import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderable;
import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the game board, where the pawns and fields are on.
 * It further enables the {@link de.uni_mannheim.informatik.ai.ludo.model.Game Game} to introduce new players to the board.
 * After introduction, a player has a path and pawns, which then can be used by the {@link de.uni_mannheim.informatik.ai.ludo.model.Player Player} to play Ludo.
 * @author gtrefs
 */
public class Board implements Renderable {

    private static final int NUMBER_OF_BOARD_FIELDS = 36;
    private static final int NUMBER_OF_BOARD_FIELDS_PER_CORNER = 9;
    // Fields
    private Map<Game.Color, List<StartField>> startFieldsByColor;
    private Map<Game.Color, BeginField> beginFieldsByColor;
    private Map<Game.Color, List<EndField>> endFieldsByColor;
    private Map<Game.Color, List<Field>> fieldsSortedByColor;
    private List<Field> boardFields;
    // Pawns
    private Map<Game.Color, List<Pawn>> pawnsByColor;
    // Registered Players
    private Map<Game.Color, Player> playersByColor;

    public Board() {
        playersByColor = new EnumMap<Game.Color, Player>(Game.Color.class);
        init();
    }

    // Generates all fields and pawns, specific to a color.
    private void init() {
        // build start fields
        startFieldsByColor = buildFieldsByType(StartField.class);
        // build end fields
        endFieldsByColor = buildFieldsByType(EndField.class);
        // build BeginFields
        buildBeginFields();
        // build boardFieldsSortedByColor
        buildFieldsSortedByColor();
        // Create the pawns per color
        createPawnsPerColor();
    }

    private void createPawnsPerColor() {
        pawnsByColor = new EnumMap<Game.Color, List<Pawn>>(Game.Color.class);
        for (Game.Color color : Game.Color.values()) {
            List<Pawn> pawns = new ArrayList<Pawn>();
            for (int i = 0; i < 4; i++) {
                pawns.add(new Pawn());
            }
            pawnsByColor.put(color, pawns);
        }
    }

    private void buildBeginFields() {
        beginFieldsByColor = new EnumMap<Game.Color, BeginField>(Game.Color.class);
        for (Game.Color color : Game.Color.values()) {
            beginFieldsByColor.put(color, new BeginField());
        }
    }

    private void buildFieldsSortedByColor() {
        fieldsSortedByColor = new EnumMap<Game.Color, List<Field>>(Game.Color.class);
        // build up the board fields interleaved by the begin fields
        // This list contains all fields which are reachable by everyone
        boardFields = new ArrayList<Field>();
        int colorIndex = 0;
        // At the end of this loop, there should be NUMBER_OF_BOARD_FIELDS + x (x=number of added begin fields) fields in the boardfields lis
        for (int i = 0; i < NUMBER_OF_BOARD_FIELDS; i++) {
            if (i % NUMBER_OF_BOARD_FIELDS_PER_CORNER == 0) {
                boardFields.add(beginFieldsByColor.get(Game.Color.values()[colorIndex++ % Game.Color.values().length]));
            }
            boardFields.add(new BoardField());
        }
        // Each color starts at a different begin field, therefore the sorting of the fields are different for each color
        // The offset indicates where to start to take the fields
        int offset = 0;
        for (Game.Color color : Game.Color.values()) {
            List<Field> listSpecificToColor = new ArrayList<Field>();
            for (int i = offset; i < offset + boardFields.size(); i++) {
                listSpecificToColor.add(boardFields.get(i % boardFields.size()));
            }
            fieldsSortedByColor.put(color, listSpecificToColor);
            // Increase offset
            offset += (NUMBER_OF_BOARD_FIELDS_PER_CORNER + 1);
        }
    }

    private <T> EnumMap<Game.Color, List<T>> buildFieldsByType(Class<T> clazz) {
        EnumMap<Game.Color, List<T>> retMap = new EnumMap<Game.Color, List<T>>(Game.Color.class);
        for (Game.Color color : Game.Color.values()) {
            try {
                List<T> instances = new ArrayList<T>();
                for (int i = 0; i < 4; i++) {
                    instances.add(clazz.newInstance());
                }
                retMap.put(color, instances);
            } catch (InstantiationException ex) {
                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retMap;
    }

    @Override
    public void takeRenderer(Renderer renderer) {
        renderer.render(this);
    }

    private Path createPathForColor(Game.Color color) {
        return new Path(fieldsSortedByColor.get(color), startFieldsByColor.get(color), endFieldsByColor.get(color));
    }

    /**
     * Returns the pawns of a specific color.
     * @param color color of the pawns
     * @return the pawns of the provided color
     */
    public Pawn[] getPawnsByColor(Game.Color color) {
        return pawnsByColor.get(color).toArray(new Pawn[0]);
    }

    private void placePawnsOnStartField(Game.Color color) {
        List<Pawn> pawns = pawnsByColor.get(color);
        List<StartField> startFields = startFieldsByColor.get(color);
        for (int i = 0; i < 4; i++) {
            startFields.get(i).placePawnOnField(pawns.get(i));
        }
    }

    /**
     * Registers a player to the board.
     * If the color was previosly chosen by another player or the player's color property is null, a {@link de.uni_mannheim.informatik.ai.ludo.exceptions.ColorException ColorException} is thrown.
     * After introduction, the player has a path, pawns and is able to play the game.
     * @param player Player which wants to be introduced to this board.
     * @throws ColorException if another player was previously introduced with the same color or the the player's color property is null
     */
    public void introducePlayerToTheBoard(Player player) throws ColorException {
        Game.Color playerColor = player.getColor();
        if (playerColor == null) {
            throw new ColorException("Player " + player.getName() + " has no color.");
        }
        if (playersByColor.get(playerColor) != null) {
            throw new ColorException(playersByColor.get(playerColor).getName() + " already introduced for color " + playerColor);
        }
        // Create path for player color
        Path playerColorPath = createPathForColor(playerColor);
        // Set the player as owner
        playerColorPath.setOwner(player);
        // Give the path to the player
        player.setPath(playerColorPath);
        // Get the pawns for this color
        Pawn[] colorPawns = getPawnsByColor(playerColor);
        // Tell the pawns about their new master
        for (Pawn p : colorPawns) {
            p.setOwner(player);
        }
        // Tell the player about his new slaves
        player.setPawns(colorPawns);
        // Place the pawn on the startFields
        placePawnsOnStartField(playerColor);
        // Register the player
        playersByColor.put(playerColor, player);
    }

    /**
     * Returns the player with the specific color.
     * @param color of the player
     * @return Player instance with the provided color
     */
    public Player getPlayerByColor(Game.Color color) {
        return playersByColor.get(color);
    }

    private void clearStartAndEndFieldsOfColorFromPawns(Game.Color color) {
        // Clear start fields
        for (StartField s : startFieldsByColor.get(color)) {
            s.takePawnFromField();
        }
        // Clear end fields
        for (EndField e : endFieldsByColor.get(color)) {
            e.takePawnFromField();
        }
    }

    /**
     * Sets all pawns of the participating players back on the start fields.
     */
    public void reset() {
        // Clear the board fields
        for (Field f : boardFields) {
            f.takePawnFromField();
        }
        // Clear start and end fields
        // Reset pawns for specific color
        Collection<Player> players = playersByColor.values();
        Iterator<Player> itPlayers = players.iterator();
        while (itPlayers.hasNext()) {
            Player p = itPlayers.next();
            Game.Color playerColor = p.getColor();
            clearStartAndEndFieldsOfColorFromPawns(playerColor);
            // Place the pawns on the start fields once again
            placePawnsOnStartField(playerColor);
        }
    }
}
