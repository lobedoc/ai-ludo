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
package de.uni_mannheim.informatik.ai.ludo.model.statistics;

import de.uni_mannheim.informatik.ai.ludo.intent.EndGameIntent;
import de.uni_mannheim.informatik.ai.ludo.intent.Intent;
import de.uni_mannheim.informatik.ai.ludo.intent.IntentVisitor;
import de.uni_mannheim.informatik.ai.ludo.intent.MoveIntent;
import de.uni_mannheim.informatik.ai.ludo.intent.NewGameIntent;
import de.uni_mannheim.informatik.ai.ludo.intent.RollDiceIntent;
import de.uni_mannheim.informatik.ai.ludo.intent.TransitionIntent;
import de.uni_mannheim.informatik.ai.ludo.model.Game;
import de.uni_mannheim.informatik.ai.ludo.model.Pawn;
import de.uni_mannheim.informatik.ai.ludo.model.Player;
import de.uni_mannheim.informatik.ai.ludo.model.preferences.Preferences;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The statistics class is used to store statistics specific to the game.
 * This enables easy evaluation of results, so whether a specific AI is better than another.
 * @author gtrefs
 */
public class Statistics {

    // The singleton
    private static Statistics INSTANCE = new Statistics();
    // The current game statistics
    private GameStatistic currentGameStatistics;
    // List of previosly played games
    private List<GameStatistic> gameStatistics;
    // Visit successful intents
    private SuccessIntentVisitor successIntentVisitor;
    // Visot rejected intents
    private RejectIntentVisitor rejectIntentVisitor;
    // Number of Games won by player
    private Map<Player, Integer> numberOfGamesWonByPlayer;
    // The OutWriter
    private StatisticsOutWriter outWriter;
    // The players
    private List<Player> players;

    public static Statistics getInstance() {
        return INSTANCE;
    }

    private Statistics() {
        successIntentVisitor = new SuccessIntentVisitor();
        rejectIntentVisitor = new RejectIntentVisitor();
        currentGameStatistics = new GameStatistic();
        gameStatistics = new ArrayList<GameStatistic>();
        numberOfGamesWonByPlayer = new HashMap<Player, Integer>();
        players = Game.getInstance().getPlayers();
        outWriter = Preferences.getInstance().loadPropertyAsClass(StatisticsOutWriter.class, "ludo.statistic.outWriterClass");
    }

    public List<Player> getPlayers() {
        if (players == null) {
            players = Game.getInstance().getPlayers();
        }
        return players;
    }

    public void increaseNumberOfRounds(){
        currentGameStatistics.increaseNumberOfRounds();
    }

    public void writeOut() {
        outWriter.write(this);
    }

    public void successfullIntent(Intent intent) {
        intent.takeVisitor(successIntentVisitor);
    }

    public void rejectedIntent(Intent intent) {
        intent.takeVisitor(rejectIntentVisitor);
    }

    public void gameWonByPlayer(Intent intent, Player winner) {
        // Increase win count of player
        Integer gamesWonByPlayer = numberOfGamesWonByPlayer.get(winner);
        if (gamesWonByPlayer == null) {
            gamesWonByPlayer = 0;
        }
        gamesWonByPlayer++;
        numberOfGamesWonByPlayer.put(winner, gamesWonByPlayer);
        currentGameStatistics.setGameWonBy(winner);
        // Add the currentGameStatistics to gameStatistics
        gameStatistics.add(currentGameStatistics);
        // Create a new currentGameStatistics
        currentGameStatistics = new GameStatistic();
    }

    public void enemyPawnThrownByPlayer(Intent intent, Player thrower, Pawn thrownPawn) {
        // increase the number of thrown pawns
        currentGameStatistics.increaseNumberOfPawnsThrown();
        // Who has thrown an enemy pawn ?
        currentGameStatistics.increaseNumberOfPawnsThrownPerRoundPerPlayer(thrower, Game.getInstance().getCurrentRound());
    }

    public double getPercentOfWinsOfPlayer(Player player) {
        Integer gamesWon = numberOfGamesWonByPlayer.get(player);
        if (gamesWon == null) {
            return 0;
        }
        return (((double) gamesWon) / Game.getInstance().getGamesPlayed()) * 100;
    }

    public Integer getNumberOfWinsOfPlayer(Player player) {
        return numberOfGamesWonByPlayer.get(player);
    }

    public List<GameStatistic> getGameStatistics() {
        return gameStatistics;
    }

    public void setGameStatistics(List<GameStatistic> gameStatistics) {
        this.gameStatistics = gameStatistics;
    }

    public StatisticsOutWriter getOutWriter() {
        return outWriter;
    }

    public void setOutWriter(StatisticsOutWriter outWriter) {
        this.outWriter = outWriter;
    }

    public class GameStatistic {

        public GameStatistic() {
            numberOfMoveIntentsPerPlayerAndRound = new HashMap<Player, Map<Integer, Integer>>();
            numberOfRollDiceIntentsPerPlayerAndRound = new HashMap<Player, Map<Integer, Integer>>();
            numberOfPawnsThrownPerPlayerAndRound = new HashMap<Player, Map<Integer, Integer>>();
            numberOfRejectedMoveIntentsPerPlayerAndRound = new HashMap<Player, Map<Integer, Integer>>();
            numberOfRejectedRollDiceIntentsPerPlayerAndRound = new HashMap<Player, Map<Integer, Integer>>();
        }
        private int numberOfRounds;
        private Player gameWonBy;
        private Map<Player, Map<Integer, Integer>> numberOfMoveIntentsPerPlayerAndRound;
        private Map<Player, Map<Integer, Integer>> numberOfRollDiceIntentsPerPlayerAndRound;
        private Map<Player, Map<Integer, Integer>> numberOfPawnsThrownPerPlayerAndRound;
        private Map<Player, Map<Integer, Integer>> numberOfRejectedMoveIntentsPerPlayerAndRound;
        private Map<Player, Map<Integer, Integer>> numberOfRejectedRollDiceIntentsPerPlayerAndRound;
        private int numberOfMoveIntents;
        private int numberOfRejectedMoveIntents;
        private int numberOfRollDiceIntents;
        private int numberOfRejectedRollDiceIntents;
        private int numberOfPawnsThrown;

        private int getNumberPerRoundAndPlayer(Player player, Integer round, Map<Player, Map<Integer, Integer>> map) {
            Map<Integer, Integer> roundToNum = map.get(player);
            if (roundToNum == null) {
                return 0;
            }
            Integer ret = roundToNum.get(round);
            return ret == null ? 0 : ret;
        }

        private void increaseNumberPerRoundPerPlayer(Player player, Integer round, Map<Player, Map<Integer, Integer>> map) {
            Map<Integer, Integer> roundToNum = map.get(player);
            if (roundToNum == null) {
                roundToNum = new HashMap<Integer, Integer>();
            }
            Integer numOfMoveIntents = roundToNum.get(round);
            if (numOfMoveIntents == null) {
                numOfMoveIntents = 0;
            }
            numOfMoveIntents++;
            roundToNum.put(round, numOfMoveIntents);
            map.put(player, roundToNum);
        }

        private double getAvarageForPlayerPerRound(Player player, Map<Player, Map<Integer, Integer>> map) {
            Map<Integer, Integer> numberOfMoveIntentsForPlayer = map.get(player);
            int total = 0;
            for (int i = 0; i < numberOfRounds; i++) {
                total += numberOfMoveIntentsForPlayer.get(i);
            }
            return ((double) total) / numberOfRounds;
        }

        // Rejects
        public void increaseNumberOfRejectedRollDiceIntentsPerPlayerAndRound(Player player, Integer round) {
            increaseNumberPerRoundPerPlayer(player, round, numberOfRejectedRollDiceIntentsPerPlayerAndRound);
        }

        public void increaseNumberOfRejectedMoveIntentsPerPlayerAndRound(Player player, Integer round) {
            increaseNumberPerRoundPerPlayer(player, round, numberOfRejectedMoveIntentsPerPlayerAndRound);
        }

        public int getNumberOfRejectedRollDiceIntentsPerPlayerAndRound(Player player, Integer round) {
            return getNumberPerRoundAndPlayer(player, round, numberOfRejectedRollDiceIntentsPerPlayerAndRound);
        }

        public int getNumberOfRejectedMoveIntentsPerPlayerAndRound(Player player, Integer round) {
            return getNumberPerRoundAndPlayer(player, round, numberOfRejectedMoveIntentsPerPlayerAndRound);
        }

        public double getAverageRejectedRollDiceIntentForPlayer(Player player) {
            return getAvarageForPlayerPerRound(player, numberOfRejectedRollDiceIntentsPerPlayerAndRound);
        }

        public double getAverageRejectedMoveIntentForPlayer(Player player) {
            return getAvarageForPlayerPerRound(player, numberOfRejectedMoveIntentsPerPlayerAndRound);
        }

        // Success (Total - Rejected)
        public int getNumberOfSucceessfulRollDiceIntentsPerPlayerAndRound(Player player, Integer round) {
            return getNumberPerRoundAndPlayer(player, round, numberOfRollDiceIntentsPerPlayerAndRound) - getNumberPerRoundAndPlayer(player, round, numberOfRejectedRollDiceIntentsPerPlayerAndRound);
        }

        public int getNumberOfSucceessfulMoveIntentsPerPlayerAndRound(Player player, Integer round) {
            return getNumberPerRoundAndPlayer(player, round, numberOfMoveIntentsPerPlayerAndRound) - getNumberPerRoundAndPlayer(player, round, numberOfRejectedMoveIntentsPerPlayerAndRound);
        }

        public double getAverageSucceededRollDiceIntentForPlayer(Player player) {
            return getAvarageForPlayerPerRound(player, numberOfRollDiceIntentsPerPlayerAndRound) - getAvarageForPlayerPerRound(player, numberOfRejectedRollDiceIntentsPerPlayerAndRound);
        }

        public double getAverageSuceededMoveIntentForPlayer(Player player) {
            return getAvarageForPlayerPerRound(player, numberOfMoveIntentsPerPlayerAndRound) - getAvarageForPlayerPerRound(player, numberOfRejectedMoveIntentsPerPlayerAndRound);
        }

        // Total
        public int getNumberOfPawnsThrownPerRoundAndPlayer(Player player, Integer round) {
            return getNumberPerRoundAndPlayer(player, round, numberOfPawnsThrownPerPlayerAndRound);
        }

        public void increaseNumberOfPawnsThrownPerRoundPerPlayer(Player player, Integer round) {
            increaseNumberPerRoundPerPlayer(player, round, numberOfPawnsThrownPerPlayerAndRound);
        }

        public int getNumberOfRollDiceIntentsPerPlayerAndRound(Player player, Integer round) {
            return getNumberPerRoundAndPlayer(player, round, numberOfRollDiceIntentsPerPlayerAndRound);
        }

        public int getNumberOfMoveIntentsPerPlayerAndRound(Player player, Integer round) {
            return getNumberPerRoundAndPlayer(player, round, numberOfMoveIntentsPerPlayerAndRound);
        }

        public void increaseNumberOfMoveIntentsPerPlayerAndRound(Player player, Integer round) {
            increaseNumberPerRoundPerPlayer(player, round, numberOfMoveIntentsPerPlayerAndRound);
        }

        public void increaseNumberOfRollDiceIntentsPerPlayerAndRound(Player player, Integer round) {
            increaseNumberPerRoundPerPlayer(player, round, numberOfRollDiceIntentsPerPlayerAndRound);
        }

        public void increaseNumberOfPawnsThrown() {
            numberOfPawnsThrown++;
        }

        public int getNumberOfPawnsThrown() {
            return numberOfPawnsThrown;
        }

        public double getAverageMoveIntentsPerRound() {
            return ((double) numberOfMoveIntents) / numberOfRounds;
        }

        public double getAverageRollDiceIntentsPerRound() {
            return ((double) numberOfRollDiceIntents) / numberOfRounds;
        }

        public double getAveragePawnThrowsPerRound() {
            return ((double) numberOfPawnsThrown) / numberOfRounds;
        }

        public double getAveragePawnsThrownPerRoundForPlayer(Player player) {
            return getAvarageForPlayerPerRound(player, numberOfPawnsThrownPerPlayerAndRound);
        }

        public double getAverageMoveIntentPerRoundForPlayer(Player player) {
            return getAvarageForPlayerPerRound(player, numberOfMoveIntentsPerPlayerAndRound);
        }

        public double getAverageRollDiceIntentForPlayer(Player player) {
            return getAvarageForPlayerPerRound(player, numberOfRollDiceIntentsPerPlayerAndRound);
        }

        public Player getGameWonBy() {
            return gameWonBy;
        }

        public void setGameWonBy(Player gameWonBy) {
            this.gameWonBy = gameWonBy;
        }

        public int getNumberOfMoveIntents() {
            return numberOfMoveIntents;
        }

        public void setNumberOfMoveIntents(int numberOfMoveIntents) {
            this.numberOfMoveIntents = numberOfMoveIntents;
        }

        public void increaseNumberOfMoveIntents() {
            numberOfMoveIntents++;
        }

        public int getNumberOfRejectedRollDiceIntents() {
            return numberOfRejectedRollDiceIntents;
        }

        public int getNumberOfSuccessfulRollDiceIntents(){
            return numberOfRollDiceIntents-numberOfRejectedRollDiceIntents;
        }

        public void increaseNumberOfRejectedRollDiceIntents() {
            numberOfRejectedRollDiceIntents++;
        }

        public void setNumberOfRejectedRollDiceIntents(int numberOfRejectedRollDiceIntents) {
            this.numberOfRejectedRollDiceIntents = numberOfRejectedRollDiceIntents;
        }

        public int getNumberOfRejectedMoveIntents() {
            return numberOfRejectedMoveIntents;
        }

        public int getNumberOfSuccessfulMoveIntents(){
            return numberOfMoveIntents-numberOfRejectedMoveIntents;
        }

        public void increaseNumberOfRejectedMoveIntents() {
            numberOfRejectedMoveIntents++;
        }

        public void setNumberOfRejectedMoveIntents(int numberOfRejectedMoveIntents) {
            this.numberOfRejectedMoveIntents = numberOfRejectedMoveIntents;
        }

        public int getNumberOfRollDiceIntents() {
            return numberOfRollDiceIntents;
        }

        public void setNumberOfRollDiceIntents(int numberOfRollDiceIntents) {
            this.numberOfRollDiceIntents = numberOfRollDiceIntents;
        }

        public void increaseNumberOfRollDiceIntents() {
            numberOfRollDiceIntents++;
        }

        public int getNumberOfRounds() {
            return numberOfRounds;
        }

        public void setNumberOfRounds(int numberOfRounds) {
            this.numberOfRounds = numberOfRounds;
        }

        public void increaseNumberOfRounds() {
            numberOfRounds++;
        }
    }

    private class SuccessIntentVisitor implements IntentVisitor {

        @Override
        public void processIntent(TransitionIntent intent) {
            // Not of interest
        }

        @Override
        public void processIntent(RollDiceIntent intent) {
            // Increase the total of RolldiceIntents
            currentGameStatistics.increaseNumberOfRollDiceIntents();
            currentGameStatistics.increaseNumberOfRollDiceIntentsPerPlayerAndRound(intent.getPlayer(), Game.getInstance().getCurrentRound());
        }

        @Override
        public void processIntent(MoveIntent intent) {
            // Okay,this move intent was successfull
            // Increase total move intents
            currentGameStatistics.increaseNumberOfMoveIntents();
            // Increase the move intents specific to this user
            currentGameStatistics.increaseNumberOfMoveIntentsPerPlayerAndRound(intent.getPlayer(), Game.getInstance().getCurrentRound());
        }

        @Override
        public void processIntent(EndGameIntent intent) {
            // Not of interest
        }

        @Override
        public void processIntent(NewGameIntent intent) {
            // Not of interest
        }
    }

    private class RejectIntentVisitor implements IntentVisitor {

        @Override
        public void processIntent(TransitionIntent intent) {
            // Not of interest
        }

        @Override
        public void processIntent(RollDiceIntent intent) {
            // The roll dice intent has been rejected
            // Increase the total number
            currentGameStatistics.increaseNumberOfRollDiceIntents();
            // increase the rejected number
            currentGameStatistics.increaseNumberOfRollDiceIntents();
            // Increase the number of roll dice intents of this player
            currentGameStatistics.increaseNumberOfRollDiceIntentsPerPlayerAndRound(intent.getPlayer(), Game.getInstance().getCurrentRound());
            // Increase the rejected roll dice intents
            currentGameStatistics.increaseNumberOfRejectedRollDiceIntentsPerPlayerAndRound(intent.getPlayer(), Game.getInstance().getCurrentRound());
        }

        @Override
        public void processIntent(MoveIntent intent) {
            // The move intent has been rejected
            // Increase the total number
            currentGameStatistics.increaseNumberOfMoveIntents();
            // increase the rejected number
            currentGameStatistics.increaseNumberOfRejectedMoveIntents();
            // Increase the number of move intents of this player
            currentGameStatistics.increaseNumberOfMoveIntentsPerPlayerAndRound(intent.getPlayer(), Game.getInstance().getCurrentRound());
            // Increase the rejected move intents
            currentGameStatistics.increaseNumberOfRejectedMoveIntentsPerPlayerAndRound(intent.getPlayer(), Game.getInstance().getCurrentRound());
        }

        @Override
        public void processIntent(EndGameIntent intent) {
            // Not of interest
        }

        @Override
        public void processIntent(NewGameIntent intent) {
            // Not of interest
        }
    }
}
