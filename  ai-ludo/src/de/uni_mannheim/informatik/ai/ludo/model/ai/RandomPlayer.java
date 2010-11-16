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
package de.uni_mannheim.informatik.ai.ludo.model.ai;

import de.uni_mannheim.informatik.ai.ludo.intent.IntentFactory;
import de.uni_mannheim.informatik.ai.ludo.intent.PlayerIntent;
import de.uni_mannheim.informatik.ai.ludo.model.Game;
import de.uni_mannheim.informatik.ai.ludo.model.Game.Color;
import de.uni_mannheim.informatik.ai.ludo.model.Path;
import de.uni_mannheim.informatik.ai.ludo.model.Pawn;
import de.uni_mannheim.informatik.ai.ludo.model.Player;
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEvent;
import de.uni_mannheim.informatik.ai.ludo.view.renderer.Renderer;

/**
 * This artificial Player is a random player which acts randomly.
 * @author gtrefs
 */
public class RandomPlayer implements Player {

    private Pawn[] pawns;
    private Game.Color color;
    private Path path;
    private String name;

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void movePawn() {
        Pawn randomPawn = pawns[(int)(100*Math.random()%pawns.length)];
        IntentFactory.getInstance().createAndDispatchMoveIntent(Game.getInstance(), randomPawn);
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
    public void rejectIntent(PlayerIntent intent) {
    }

    @Override
    public void successIntent(PlayerIntent intent) {
    }

    @Override
    public void enemyPawnThrownByIntent(Pawn enemyPawn, PlayerIntent intent) {
    }

    @Override
    public void gameWonByIntent(PlayerIntent intent) {
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void takeRenderer(Renderer renderer) {
        renderer.render(this);
    }

    @Override
    public void reset() {

    }
}
