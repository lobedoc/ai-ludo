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
package de.uni_mannheim.informatik.ai.ludo.model.preferences;

import de.uni_mannheim.informatik.ai.ludo.view.View;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides convinient access to the preferences.properties file.
 * This file entails all the additional information which are necessary to run
 * this application but should not be inserted by the user.
 * These informations are the artificial gamer class names, the class name of a
 * concrete StatisticsWriter and the output location of the statisticsfile.
 * Further, it provides access to internationalized messages which can be accessed
 * by any interrested party.
 * @author gtrefs
 */
public class Preferences {

    private static String MAX_ROUND = "ludo.game.maxRound";
    private static String MODE = "ludo.game.mode";
    private static String SIMULATION_MODE = "simulation";
    private static String LEARNING_MODE = "learning";
    private static String DEFAULT_VIEW_LABEL = "ludo.view.defaultViewLabel";
    private static String VIEW_PROPERTY = "ludo.view.views";
    private static char VIEW_SEPARATOR = ';';
    private static char IDENTIFIER_SEPARATOR = ':';
    private String loadedMode;
    private int loadedMaxRound = -1;
    private Properties props;
    private String propertiesPath = "Preferences.properties";
    // Load Views
    private ViewRegistry viewRegistry;

    public int getMaxRound() {
        if (loadedMaxRound == -1) {
            loadedMaxRound = Integer.parseInt(getProperty(MAX_ROUND));
        }
        return loadedMaxRound;
    }

    public boolean isInSimulationMode() {
        if (loadedMode == null) {
            loadedMode = getProperty(MODE);
        }
        return loadedMode.equals(SIMULATION_MODE);
    }
    public boolean isInLearningMode() {
        if(loadedMode == null) {
            loadedMode = getProperty(MODE);
        }
        return loadedMode.equals(LEARNING_MODE);
    }

    // START - Singleton
    private static Preferences instance = new Preferences();

    public static Preferences getInstance() {
        return instance;
    }

    private Preferences() {
        loadProps();
        initViewRegistry();
    }

    private void initViewRegistry() {
        viewRegistry = new ViewRegistry();
        viewRegistry.registerViewsFromProperties(props, VIEW_PROPERTY, VIEW_SEPARATOR, IDENTIFIER_SEPARATOR);
        viewRegistry.registerViewsFromProperties(System.getProperties(), VIEW_PROPERTY, VIEW_SEPARATOR, VIEW_SEPARATOR);
    }

    private void loadProps() {
        props = new Properties();
        try {
            props.load(getClass().getResourceAsStream(propertiesPath));
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized String getProperty(String arg0) {
        if (props == null) {
            loadProps();
        }
        // The user can alter the behaviour by entering the values directly on the prompt
        String ret = System.getProperty(arg0) == null ? props.getProperty(arg0) : System.getProperty(arg0);
        return ret;
    }

    public View loadDefaultView() {
        return loadView(getProperty(DEFAULT_VIEW_LABEL));
    }

    public View loadView(String viewClassLabel) {
        return loadViewAsClass(View.class, viewClassLabel);
    }

    public <T> T loadViewAsClass(Class<T> clazz, String viewClassLabel) {
        return viewRegistry.getView(clazz, viewClassLabel);
    }

    private class ViewRegistry {

        private Map<String, String> labelWithClassName;

        private ViewRegistry() {
            labelWithClassName = new HashMap<String, String>();
        }

        public void registerViewsFromProperties(Properties props, String viewProperty, char viewSeparator, char identifierSeparator) {
            String views = props.getProperty(viewProperty);
            if (views == null) {
                return;
            }
            String[] viewClassNames = views.split(String.valueOf(viewSeparator));
            for (int i = 0; i < viewClassNames.length; i++) {
                String[] identfierWithClassName = viewClassNames[i].split(String.valueOf(identifierSeparator));
                if (identfierWithClassName == null) {
                    continue;
                }
                labelWithClassName.put(identfierWithClassName[0], identfierWithClassName[1]);
            }
        }

        public <T> T getView(Class<T> clazz, String viewClassLabel) {
            String className = labelWithClassName.get(viewClassLabel);
            try {
                return (T) ClassLoader.getSystemClassLoader().loadClass(className).newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        public String getViewClassName(String propertyName) {
            return labelWithClassName.get(propertyName);
        }
    }
}
