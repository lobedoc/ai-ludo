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

import de.uni_mannheim.informatik.ai.ludo.intent.IntentFactory;
import de.uni_mannheim.informatik.ai.ludo.model.Game;
import de.uni_mannheim.informatik.ai.ludo.model.Player;
import de.uni_mannheim.informatik.ai.ludo.model.events.NotificationEvent;
import de.uni_mannheim.informatik.ai.ludo.model.states.EndState;
import de.uni_mannheim.informatik.ai.ludo.model.states.RoundStartedState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Dominique Ritze
 */
public class GeneticUtility {

    /**
     * Größe Population ändern (10, 25, 50, 100)
     * -> bei sehr kleiner Population STart ändern?
     * Größe Stichprobe pro Individuum (10,100,1000)
     * Anzahl neuer Instanzen pro Generation (10,25,50,75)
     * W-keit Mutation (5,10,25,50)
     * Terminierungskriterium -> nach x Generationen,
     * median ersten k, median aktuelle population
     */
    private static List<Setting> allSettings = new ArrayList<Setting>();
    private static Setting currentSetting;

    public static int newInstances = 0;
    public static int numberOfGenerations = 1;    
    private static Population population = new Population();
    private static int settingIndex = 0;
    private static int currentlyPlayed = 1;
    private static int gamesPlayed = 1;

    private static Random random = new Random();
   
    public static void procedureAfterGame(Game game) {

        int gamessPlayedTotal = game.getGamesPlayed();

        //just to define different test setting which are successively executed
        if(gamessPlayedTotal == 1) {
            Integer[] populationSize = new Integer[]{250};
            Integer[] sampleSize = new Integer[]{100};
            Integer[] numberNewInstances = new Integer[]{75};
            Integer[] ProbMutation = new Integer[]{100};
            for(int i=0; i<populationSize.length; i++) {
                for(int j=0; j<sampleSize.length; j++) {
                    for(int k=0; k<numberNewInstances.length; k++) {
                        if(numberNewInstances[k]>=populationSize[i]) {
                                continue;
                            }
                        for(int l=0; l<ProbMutation.length; l++) {                            
                            allSettings.add(new Setting(populationSize[i], sampleSize[j],
                                    numberNewInstances[k], ProbMutation[l]));
                        }
                    }
                }
            }
            currentSetting = allSettings.get(settingIndex);
            settingIndex++;
        }

        //if the player won, increase the score of its individual
        if(game.getCurrentPlayer() instanceof Player1) {
            ((Player1)game.getCurrentPlayer()).getIndividual().increaseScore();
        }

        //within the first phase where the first population is generated
        if(gamesPlayed < (currentSetting.getNUMBER_OF_SAMPLES()*currentSetting.getSTART_POPULATION())) {            
            //a new individual within the first generating should be generated
            if(gamesPlayed%currentSetting.getNUMBER_OF_SAMPLES() == 0) {
                //set the new individual
                for(Player p : game.getPlayers()) {
                    if(!(p instanceof Player1)) {
                        continue;
                    }
                    //first population only contains individuals with random weights
                    ((Player1)p).setIndividual(randomIndividual());
                }
            }
            currentlyPlayed++;
            gamesPlayed++;
            //start a new game
            startNewGame(game);
            return;
        }

        //new individual should be created 
        if(currentlyPlayed == currentSetting.getNUMBER_OF_SAMPLES()*currentSetting.getSTART_POPULATION()
                || currentlyPlayed == currentSetting.getNUMBER_OF_SAMPLES()) {
            GeneticUtility.newInstances++;
            currentlyPlayed = 0;
            gamesPlayed++;
            //enough new individuals created, delete weakest individuals and start a new generation
            if(GeneticUtility.newInstances == currentSetting.getNUMBER_OF_NEW_INSTANCES()
                    || gamesPlayed-1 == currentSetting.getSTART_POPULATION()*currentSetting.getNUMBER_OF_SAMPLES()) {
                population.clearLastOnes();
                GeneticUtility.newInstances = 0;
                GeneticUtility.numberOfGenerations++;
            }
            //check termination criterium and exit if it is satisfied
            if(checkTerminationCriterium()) {
                //all settings have been tested
                System.out.println("currentSetting: " + currentSetting.toString() +
                        " best: " + population.getCurrentPopulation().get(population.getCurrentPopulation().size()-1).getScore());
                System.out.println(population.getCurrentPopulation().get(population.getCurrentPopulation().size()-1).getWeights());
                if(settingIndex == allSettings.size()) {
                    endGame(game);
                    return;
                }
                //a test with one setting has been finished but there are other ones
                else {
                    //grab next setting and reset all values
                    currentSetting = allSettings.get(settingIndex);
                    settingIndex++;                    
                    population = new Population();
                    currentlyPlayed = 0;
                    gamesPlayed = 0;
                    newInstances = 0;
                    numberOfGenerations = 1;                    
                    startNewGame(game);
                    return;
                }
            }

            //create individual by recombination and mutation and bind it to the player
            for(Player p : game.getPlayers()) {
                if(!(p instanceof Player1)) {
                    continue;
                }
                ((Player1)p).setIndividual(createIndividualByMutationAndRecombination());
            }
        }
        currentlyPlayed++;
        gamesPlayed++;
        startNewGame(game);
        return;
    }

    private static void  startNewGame(Game game) {
        game.setState(new RoundStartedState());
        game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.NEW_GAME));
        IntentFactory.getInstance().createAndDispatchNewGameIntent(game);
        return;
    }

    private static void endGame(Game game) {
        game.setState(new EndState());
        game.fireNotificationEvent(new NotificationEvent(game, NotificationEvent.Type.END_GAME));
        IntentFactory.getInstance().createAndDispatchEndGameIntent(game);
        return;
    }

    private static boolean checkTerminationCriterium() {
        return numberOfGenerations == currentSetting.getNUMBER_OF_GENERATIONS();
    }


    public static Individual createIndividualByMutationAndRecombination() {
        int firstInd = random.nextInt(currentSetting.getSTART_POPULATION()-currentSetting.getNUMBER_OF_NEW_INSTANCES());
        int secondInd = random.nextInt(currentSetting.getSTART_POPULATION()-currentSetting.getNUMBER_OF_NEW_INSTANCES());
        Individual generatedIndividual = createIndividualByRecombination(population.currentPopulation.get(firstInd),
                population.currentPopulation.get(secondInd));
        if((random.nextInt(101))<=(currentSetting.getPROBABILITY_MUTATION()*100)){
            generatedIndividual = createIndividualByMutation(generatedIndividual);
        }
        GeneticUtility.population.saveIndividual(generatedIndividual);
        return generatedIndividual;
    }

    /**
     * 
     * @return
     */
    public static Individual randomIndividual() {
        Individual individual = new Individual();
        GeneticUtility.population.saveIndividual(individual);
        return individual;
    }

    public static Individual createIndividualByMutation(Individual ind) {
        int mutationIndex = random.nextInt(7);
        double newValue = random.nextInt(100);
        Weights mutatedWeight = new Weights(new Double[]{
            (0 == mutationIndex ? newValue : ind.getWeights().DIRECT_BEAT),
            (1 == mutationIndex ? newValue : ind.getWeights().INDIRECT_BEAT),
            (2 == mutationIndex ? newValue : ind.getWeights().ESCAPE_POSSIBLE_BEAT),
            (3 == mutationIndex ? newValue : ind.getWeights().ENTER_POSSIBLE_BEAT),
            (4 == mutationIndex ? newValue : ind.getWeights().START_POSITION),
            (5 == mutationIndex ? newValue : ind.getWeights().ENTER_TARGET),
            (6 == mutationIndex ? newValue : ind.getWeights().PROGRESS_FACTOR)});
        return new Individual(mutatedWeight);
    }

    public static Individual createIndividualByRecombination(Individual ind1, Individual ind2) {
        int combinationIndex = random.nextInt(6)+1;
        Weights combinedWeights = new Weights(new Double[]{
            (0 < combinationIndex ? ind1.getWeights().DIRECT_BEAT : ind2.getWeights().DIRECT_BEAT),
            (1 < combinationIndex ? ind1.getWeights().INDIRECT_BEAT : ind2.getWeights().INDIRECT_BEAT),
            (2 < combinationIndex ? ind1.getWeights().ESCAPE_POSSIBLE_BEAT : ind2.getWeights().ESCAPE_POSSIBLE_BEAT),
            (3 < combinationIndex ? ind1.getWeights().ENTER_POSSIBLE_BEAT : ind2.getWeights().ENTER_POSSIBLE_BEAT),
            (4 < combinationIndex ? ind1.getWeights().START_POSITION : ind2.getWeights().START_POSITION),
            (5 < combinationIndex ? ind1.getWeights().ENTER_TARGET : ind2.getWeights().ENTER_TARGET),
            (6 < combinationIndex ? ind1.getWeights().PROGRESS_FACTOR : ind2.getWeights().PROGRESS_FACTOR)});
        return new Individual(combinedWeights);
    }

    public static class Population {

        List<Individual> currentPopulation;

        public Population() {
            currentPopulation = new ArrayList<Individual>();
        }

        public void saveIndividual(Individual player) {
            this.currentPopulation.add(player);
        }

        public List<Individual> getCurrentPopulation() {
            Collections.sort(this.currentPopulation);
            return this.currentPopulation;
        }

        public void clearAll() {
            this.currentPopulation.clear();
        }

        public void clearLastOnes() {
            Collections.sort(this.currentPopulation);
            for(int i=0; i<currentSetting.getNUMBER_OF_NEW_INSTANCES(); i++) {
                this.currentPopulation.remove(0);                
            }
        }
    }
}

