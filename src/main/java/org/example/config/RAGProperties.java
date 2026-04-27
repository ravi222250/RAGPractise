package org.example.config;

import java.io.IOException;
import java.util.Properties;


public class RAGProperties {

    private static RAGProperties instance;

    private final Properties properties;

    private RAGProperties() throws IOException {
        properties = new Properties();
        properties.load(RAGProperties.class.getClassLoader().getResourceAsStream("RAGProperties.properties"));
    }

    public static RAGProperties getInstance() throws IOException {
        if (instance == null) {
            instance = new RAGProperties();
        }
        return instance;
    }

    public Properties getProperties() {
        return properties;
    }


}
