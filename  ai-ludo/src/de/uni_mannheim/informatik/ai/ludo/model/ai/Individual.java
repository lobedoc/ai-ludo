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
public class Individual implements Comparable{
    
    private int score = 0;
    private Weights weights;    

    public Individual(Weights weights) {
        this.weights = weights;
    }

    public Individual(int score, Weights weights) {
        this.score = score;
        this.weights = weights;
    }

    public Individual() {
        this.weights = new Weights();
        this.score = 0;
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public int compareTo(Object o) {
        return this.getScore()-((Individual)o).getScore();
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
    

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    public void increaseScore() {
        this.score++;
    }

    /**
     * @return the weights
     */
    public Weights getWeights() {
        return weights;
    }

    /**
     * @param weights the weights to set
     */
    public void setWeights(Weights weights) {
        this.weights = weights;
    }
}


