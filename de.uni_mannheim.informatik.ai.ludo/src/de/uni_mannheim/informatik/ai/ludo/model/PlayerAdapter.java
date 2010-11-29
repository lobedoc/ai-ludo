/*
 *  Copyright (C) 2010 gtrefs
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

package de.uni_mannheim.informatik.ai.ludo.model;

import de.uni_mannheim.informatik.ai.ludo.intent.IntentFactory;
import de.uni_mannheim.informatik.ai.ludo.model.Game.Color;
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEvent;

/**
 * This class implements the {@link Player} interface.
 * It provides methods which are common to all artificial players.
 * @author gtrefs
 */
public abstract class PlayerAdapter implements Player{

    protected Pawn[] pawns;
    protected Game.Color color;
    protected Path path;
    protected String name;

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void rollTheDice() {
        Game game = Game.getInstance();
        // Roll the dice
        game.getDice().rollDice();
        // Tell the view about the situation
        game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.DICE_ROLLED));
        // Update
        IntentFactory.getInstance().createAndDispatchRollDiceIntent(Game.getInstance(), this);
    }

    @Override
    public void setPawns(Pawn[] pawns) {
        this.pawns = pawns;
    }

    @Override
    public Pawn[] getPawns() {
        return pawns;
    }

    @Override
    public Pawn getPawn(int index) {
        return pawns[index % pawns.length];
    }

    @Override
    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
