/*
 *  Copyright (C) 2010 domi-desktop
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

/**
 *
 * @author domi-desktop
 */
public class Setting {

    public Setting(int startPop, int samples, int instances, int mutation) {
        this.START_POPULATION = startPop;
        this.NUMBER_OF_SAMPLES = samples;
        this.NUMBER_OF_NEW_INSTANCES = (instances*startPop)/100;
        this.PROBABILITY_MUTATION = mutation/100.00;
    }

    //size population
    private int START_POPULATION;
    private int POPULATION;
    //number of games played by a individual
    private int NUMBER_OF_SAMPLES;
    //how many generation should be generated, can be used for termination criteria
    private int NUMBER_OF_GENERATIONS = 25;
    //number of new instances in the next generation
    private int NUMBER_OF_NEW_INSTANCES;
    //probability of a mutation
    private double PROBABILITY_MUTATION;

    /**
     * @return the START_POPULATION
     */
    public int getSTART_POPULATION() {
        return START_POPULATION;
    }

    /**
     * @param START_POPULATION the START_POPULATION to set
     */
    public void setSTART_POPULATION(int START_POPULATION) {
        this.START_POPULATION = START_POPULATION;
    }

    /**
     * @return the POPULATION
     */
    public int getPOPULATION() {
        return POPULATION;
    }

    /**
     * @param POPULATION the POPULATION to set
     */
    public void setPOPULATION(int POPULATION) {
        this.POPULATION = POPULATION;
    }

    /**
     * @return the NUMBER_OF_SAMPLES
     */
    public int getNUMBER_OF_SAMPLES() {
        return NUMBER_OF_SAMPLES;
    }

    /**
     * @param NUMBER_OF_SAMPLES the NUMBER_OF_SAMPLES to set
     */
    public void setNUMBER_OF_SAMPLES(int NUMBER_OF_SAMPLES) {
        this.NUMBER_OF_SAMPLES = NUMBER_OF_SAMPLES;
    }

    /**
     * @return the NUMBER_OF_GENERATIONS
     */
    public int getNUMBER_OF_GENERATIONS() {
        return NUMBER_OF_GENERATIONS;
    }

    /**
     * @param NUMBER_OF_GENERATIONS the NUMBER_OF_GENERATIONS to set
     */
    public void setNUMBER_OF_GENERATIONS(int NUMBER_OF_GENERATIONS) {
        this.NUMBER_OF_GENERATIONS = NUMBER_OF_GENERATIONS;
    }

    /**
     * @return the NUMBER_OF_NEW_INSTANCES
     */
    public int getNUMBER_OF_NEW_INSTANCES() {
        return NUMBER_OF_NEW_INSTANCES;
    }

    /**
     * @param NUMBER_OF_NEW_INSTANCES the NUMBER_OF_NEW_INSTANCES to set
     */
    public void setNUMBER_OF_NEW_INSTANCES(int NUMBER_OF_NEW_INSTANCES) {
        this.NUMBER_OF_NEW_INSTANCES = NUMBER_OF_NEW_INSTANCES;
    }

    /**
     * @return the PROBABILITY_MUTATION
     */
    public double getPROBABILITY_MUTATION() {
        return PROBABILITY_MUTATION;
    }

    /**
     * @param PROBABILITY_MUTATION the PROBABILITY_MUTATION to set
     */
    public void setPROBABILITY_MUTATION(double PROBABILITY_MUTATION) {
        this.PROBABILITY_MUTATION = PROBABILITY_MUTATION;
    }

    @Override
    public String toString() {
        return "pop: " + START_POPULATION + " samples: " + NUMBER_OF_SAMPLES +
                " instances: " + NUMBER_OF_NEW_INSTANCES + " mutation: " + PROBABILITY_MUTATION;
    }


}
