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

package de.uni_mannheim.informatik.ai.ludo.model.ai;

import java.util.Random;

/**
 *
 * Datastructure to save the values for the accoring terms of
 * the utility function. Just contains a variable for each term
 * and set/get methods as well as a printing method.
 *
 * Terms:
 * Following actions should result into positive utility:
 * Beat other player = directBeat
 * Get into position where other pawn is potentially beatable next round
 * = indirectBeat
 * Get out of a position where own pawn is potentially beatable by other player
 * = escapePossibleBeat
 * Get pawn on an end field = enterTarget
 * Following actions should result into negative utility:
 * Move own pawn into a position where it can potetially be beaten by other's
 * player pawn = enterPossibleBeat
 * Move ends on a beginfield of another player = startPosition
 * Bonus. To reflect the ultimate goal of a player to get all player's pawns on
 * the player's end fields, a utility bonus is granted to each pawn reflecting
 * the distance between it's current field and the first end field
 * (Bonus = 1 <=> Pawn on 1. Field, ..., Bonus = 40 <=> Pawn on 40. Field)
 * = progressFactor * field index
 *
 *
 * @author Dominique Ritze
 */
public class Weights {

    private static Random random;
    private double directBeat;
    private double indirectBeat;
    private double escapePossibleBeat;
    private double enterPossibleBeat;
    private double startPosition;
    private double enterTarget;
    private double progressFactor;

    /**
     * Constructor to set the weights to defined values.
     *
     * @param values Predefined values for the values.
     */
    public Weights(Double[] values) {
        this.directBeat = values[0];
        this.indirectBeat = values[1];
        this.escapePossibleBeat = values[2];
        this.enterPossibleBeat = values[3];
        this.startPosition = values[4];
        this.enterTarget = values[5];
        this.progressFactor = values[6];
    }

    /**
     * Constructor to create values set to random values.
     *
     */
    public Weights() {
        random = new Random();
        this.directBeat = random.nextInt(100);
        this.indirectBeat = random.nextInt(100);
        this.escapePossibleBeat = random.nextInt(100);
        this.enterPossibleBeat = random.nextInt(100);
        this.startPosition = random.nextInt(100);
        this.enterTarget = random.nextInt(100);
        this.progressFactor = random.nextInt(100);

    }

    /**
     * Print method to get to know the values.
     *
     * @return String containing all values.
     */
    @Override
    public String toString() {
        return("direct: " + this.getDIRECT_BEAT() + " indirect: "+
        this.getINDIRECT_BEAT() + " escape poss beat: " +
        this.getESCAPE_POSSIBLE_BEAT() + " enter poss beat: " +
        this.getENTER_POSSIBLE_BEAT() + " start position: " +
        this.getSTART_POSITION() + " enter target: "+
        this.getENTER_TARGET() + " progress factor: " +
        this.getPROGRESS_FACTOR());
    }

    /**
     * @return the directBeat
     */
    public double getDIRECT_BEAT() {
        return directBeat;
    }

    /**
     * @return the indirectBeat
     */
    public double getINDIRECT_BEAT() {
        return indirectBeat;
    }

    /**
     * @return the escapePossibleBeat
     */
    public double getESCAPE_POSSIBLE_BEAT() {
        return escapePossibleBeat;
    }

    /**
     * @return the enterPossibleBeat
     */
    public double getENTER_POSSIBLE_BEAT() {
        return enterPossibleBeat;
    }

    /**
     * @return the startPosition
     */
    public double getSTART_POSITION() {
        return startPosition;
    }

    /**
     * @return the enterTarget
     */
    public double getENTER_TARGET() {
        return enterTarget;
    }

    /**
     * @return the progressFactor
     */
    public double getPROGRESS_FACTOR() {
        return progressFactor;
    }

}