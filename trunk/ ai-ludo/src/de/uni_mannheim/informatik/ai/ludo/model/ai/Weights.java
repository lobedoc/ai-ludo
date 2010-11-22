/*
 *  Copyright (C) 2010 Domi
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
 * @author Domi
 */
public class Weights {

    private static Random random;

    //offense
    public final double DIRECT_BEAT;
    public final double INDIRECT_BEAT;
    //defensive
    public final double ESCAPE_POSSIBLE_BEAT;
    public final double ENTER_POSSIBLE_BEAT;
    public final double START_POSITION;
    //target oriented
    public final double ENTER_TARGET;
    public final double PROGRESS_FACTOR;

    public Weights(Double[] values) {
        this.DIRECT_BEAT = values[0];
        this.INDIRECT_BEAT = values[1];
        this.ESCAPE_POSSIBLE_BEAT = values[2];
        this.ENTER_POSSIBLE_BEAT = values[3];
        this.START_POSITION = values[4];
        this.ENTER_TARGET = values[5];
        this.PROGRESS_FACTOR = values[6];
    }

    //constructor random values
    public Weights() {
        random = new Random();
        this.DIRECT_BEAT = random.nextInt(100);
        this.INDIRECT_BEAT = random.nextInt(100);
        this.ESCAPE_POSSIBLE_BEAT = random.nextInt(100);
        this.ENTER_POSSIBLE_BEAT = random.nextInt(100);
        this.START_POSITION = random.nextInt(100);
        this.ENTER_TARGET = random.nextInt(100);
        this.PROGRESS_FACTOR = random.nextInt(100);

    }

     @Override
    public String toString() {
        return("direct: " + this.DIRECT_BEAT + " indirect: "+
        this.INDIRECT_BEAT + " escape poss beat: " +
        this.ESCAPE_POSSIBLE_BEAT + " enter poss beat: " +
        this.ENTER_POSSIBLE_BEAT + " start position: " +
        this.START_POSITION + " enter target: "+
        this.ENTER_TARGET + " progress factor: " +
        this.PROGRESS_FACTOR);
    }

   }