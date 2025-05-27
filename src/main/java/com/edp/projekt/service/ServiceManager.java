package com.edp.projekt.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceManager {
    private static final String CONFIG_FILEPATH = "config.properties";
    private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());

    public static void saveLastUserId(int userID) {
        Properties properties = new Properties();
        properties.setProperty("lastUserID", String.valueOf(userID));

        try (FileOutputStream out = new FileOutputStream(CONFIG_FILEPATH)) {
            properties.store(out, null);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot save user id: " + e.getMessage(), e);
        }
    }

    public static int loadLastUserId() {
        Properties properties = new Properties();

        try (FileInputStream in = new FileInputStream(CONFIG_FILEPATH)) {
            properties.load(in);
            return Integer.parseInt(properties.getProperty("lastUserID", "-1"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot load user id: " + e.getMessage(), e);
            //TODO: obsługa tworzenia pliku
            return -1;
        }
    }
}
