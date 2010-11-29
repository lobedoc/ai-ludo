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
package de.uni_mannheim.informatik.ai.ludo.model.statistics;

import de.uni_mannheim.informatik.ai.ludo.model.Player;
import de.uni_mannheim.informatik.ai.ludo.model.preferences.Preferences;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gtrefs
 */
public class XMLOutWriter implements StatisticsOutWriter {

    private static String ROOT_START = "<ludo>";
    private static String ROOT_END = "</ludo>";

    @Override
    public void write(Statistics statistics) {
        StringBuilder builder = new StringBuilder();
        builder.append(ROOT_START);
        builder.append("\n\t<gamesPlayed>");
        builder.append(statistics.getGameStatistics().size());
        builder.append("\t</gamesPlayed>\n");
        List<Player> players = statistics.getPlayers();
        for (Player p : players) {
            builder.append("\t<player color='").append(p.getColor().toString()).append("'>\n");
            builder.append("\t\t<gamesWon percentage='").append(statistics.getPercentOfWinsOfPlayer(p)).append("'>").append(statistics.getNumberOfWinsOfPlayer(p)).append("</gamesWon>\n");
            builder.append("\t\t<name>").append(p.getName()).append("</name>\n");
            builder.append("\t\t<class>").append(p.getClass().getName()).append("</class>\n");
            builder.append("\t</player>\n");
        }
        for (Statistics.GameStatistic g : statistics.getGameStatistics()) {
            builder.append("\t<game roundsPlayed='").append(g.getNumberOfRounds()).append("'>\n");
            builder.append("\t\t<winner>").append(g.getGameWonBy().getColor()).append("</winner>\n");
            builder.append("\t\t<totalMoveIntents rejected='").append(g.getNumberOfRejectedMoveIntents()).append("' successful='").append(g.getNumberOfSuccessfulMoveIntents()).append("'>").append(g.getNumberOfMoveIntents()).append("</totalMoveIntents>\n");
            builder.append("\t\t<totalRollDiceIntents rejected='").append(g.getNumberOfRejectedRollDiceIntents()).append("' successful='").append(g.getNumberOfSuccessfulRollDiceIntents()).append("'>").append(g.getNumberOfRollDiceIntents()).append("</totalRollDiceIntents>\n");
            builder.append("\t\t<totalPawnsThrown>").append(g.getNumberOfPawnsThrown()).append("</totalPawnsThrown>\n");
            for (Player p : players) {
                builder.append("\t\t<player ref='").append(p.getColor()).append("'>\n");
                int numberOfRejectedMoveIntentsPerPlayer=0;
                int numberOfSuccessfulMoveIntentsPerPlayer=0;
                int numberOfMoveIntentsPerPlayer = 0;
                int numberOfRejectedRollDiceIntentsPerPlayer = 0;
                int numberOfSuccessfulRollDiceIntentsPerPlayer = 0;
                int numberOfRollDiceIntentsPerPlayer = 0;
                int pawnsThrown=0;
                for (int i = 0; i < g.getNumberOfRounds(); i++) {
                    numberOfRejectedMoveIntentsPerPlayer+=g.getNumberOfRejectedMoveIntentsPerPlayerAndRound(p, i);
                    numberOfSuccessfulMoveIntentsPerPlayer+=g.getNumberOfSucceessfulMoveIntentsPerPlayerAndRound(p, i);
                    numberOfMoveIntentsPerPlayer+=g.getNumberOfMoveIntentsPerPlayerAndRound(p, i);
                    numberOfRejectedRollDiceIntentsPerPlayer+=g.getNumberOfRejectedRollDiceIntentsPerPlayerAndRound(p, i);
                    numberOfSuccessfulRollDiceIntentsPerPlayer+=g.getNumberOfSucceessfulRollDiceIntentsPerPlayerAndRound(p, i);
                    numberOfRollDiceIntentsPerPlayer+=g.getNumberOfRollDiceIntentsPerPlayerAndRound(p, i);
                    pawnsThrown+=g.getNumberOfPawnsThrownPerRoundAndPlayer(p, i);
                }
                builder.append("\t\t\t<moveIntents rejected='").append(numberOfRejectedMoveIntentsPerPlayer).append("' successful='").append(numberOfSuccessfulMoveIntentsPerPlayer).append("'>").append(numberOfMoveIntentsPerPlayer).append("</moveIntents>\n");
                builder.append("\t\t\t<rollDiceIntents rejected='").append(numberOfRejectedRollDiceIntentsPerPlayer).append("' successful='").append(numberOfSuccessfulRollDiceIntentsPerPlayer).append("'>").append(numberOfRollDiceIntentsPerPlayer).append("</rollDiceIntents>\n");
                builder.append("\t\t\t<pawnsThrown>").append(pawnsThrown).append("</pawnsThrown>\n");
                builder.append("\t\t</player>\n");
            }
            builder.append("\t</game>\n");
        }
        builder.append(ROOT_END);
        try {
            OutputStream os = new FileOutputStream(Preferences.getInstance().getProperty("XMLOutWriter.path"));
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write(builder.toString());
            osw.close();
        } catch (IOException ex) {
            Logger.getLogger(XMLOutWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
