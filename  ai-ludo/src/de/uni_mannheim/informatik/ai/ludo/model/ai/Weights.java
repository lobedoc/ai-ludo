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

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Dominique Ritze
 */
public class Weights {

    private static ArrayList<Weights> weights;
    private static int weightIndex;


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
    private static Random random;

    public Weights(Double[] values) {
        this.DIRECT_BEAT = values[0];
        this.INDIRECT_BEAT = values[1];
        this.ESCAPE_POSSIBLE_BEAT = values[2];
        this.ENTER_POSSIBLE_BEAT = values[3];
        this.START_POSITION = values[4];
        this.ENTER_TARGET = values[5];
        this.PROGRESS_FACTOR = values[6];
        random = new Random();
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

/*    public static Weights getCurrentWeight() {
        Weights w = Weights.weights.get(weightIndex);
        weightIndex++;
        return w;
    }*/

/*    public static void setWeights(ArrayList<Weights> weights) {
        weightIndex = 0;
        Weights.weights = weights;
    }*/

    public Weights mutateWeight() {
        int mutationIndex = random.nextInt(7);
        double newValue = random.nextInt(100);
        Weights mutatedWeight = new Weights(new Double[]{
            (0 == mutationIndex ? newValue : DIRECT_BEAT),
            (1 == mutationIndex ? newValue : INDIRECT_BEAT),
            (2 == mutationIndex ? newValue : ESCAPE_POSSIBLE_BEAT),
            (3 == mutationIndex ? newValue : ENTER_POSSIBLE_BEAT),
            (4 == mutationIndex ? newValue : START_POSITION),
            (5 == mutationIndex ? newValue : ENTER_TARGET),
            (6 == mutationIndex ? newValue : PROGRESS_FACTOR)});
        return mutatedWeight;
    }

    public Weights combineIndex(Weights mom, Weights dad) {
        int combinationIndex = random.nextInt(6)+1;
        Weights combinedWeight = new Weights(new Double[]{
            (0 < combinationIndex ? mom.DIRECT_BEAT : dad.DIRECT_BEAT),
            (1 < combinationIndex ? mom.INDIRECT_BEAT : dad.INDIRECT_BEAT),
            (2 < combinationIndex ? mom.ESCAPE_POSSIBLE_BEAT : dad.ESCAPE_POSSIBLE_BEAT),
            (3 < combinationIndex ? mom.ENTER_POSSIBLE_BEAT : dad.ENTER_POSSIBLE_BEAT),
            (4 < combinationIndex ? mom.START_POSITION : dad.START_POSITION),
            (5 < combinationIndex ? mom.ENTER_TARGET : dad.ENTER_TARGET),
            (6 < combinationIndex ? mom.PROGRESS_FACTOR : dad.PROGRESS_FACTOR)});
        return combinedWeight;
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
