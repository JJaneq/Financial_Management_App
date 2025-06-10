package com.edp.projekt.service;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceManager {
    private static final String CONFIG_FILEPATH = "config.properties";
    private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());

    public static void saveLastUserId(int userID) {
        Properties properties = new Properties();

        try (FileInputStream in = new FileInputStream(CONFIG_FILEPATH)) {
            properties.load(in);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Cannot load existing config: " + e.getMessage(), e);
        }

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

    public static String getPreferredStock() {
        Properties properties = new Properties();
        String preferredStock ="Brak danych";

        try (FileInputStream in = new FileInputStream((CONFIG_FILEPATH))) {
            properties.load(in);
            preferredStock = properties.getProperty("preferredStock", "Brak danych");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot load stock: " + e.getMessage(), e);
        }
        return preferredStock;
    }

    public static Dimension getPreferredResolution() {
        Properties properties = new Properties();
        Dimension preferredResolution = new Dimension();

        try (FileInputStream in = new FileInputStream(CONFIG_FILEPATH)) {
            properties.load(in);
            int width = Integer.parseInt(properties.getProperty("preferredWidth", "1080"));
            int height = Integer.parseInt(properties.getProperty("preferredHeight", "720"));
            preferredResolution.width = width;
            preferredResolution.height = height;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot load resolution: " + e.getMessage(), e);
            //TODO: obsługa tworzenia pliku
        }
        return preferredResolution;
    }

    public static String getPreferredTheme() {
        Properties properties = new Properties();
        String preferredTheme ="light-theme";

        try (FileInputStream in = new FileInputStream((CONFIG_FILEPATH))) {
            properties.load(in);
            preferredTheme = properties.getProperty("theme", "light-theme");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot load theme: " + e.getMessage(), e);
        }
        return preferredTheme;
    }

    public static void setNewPreferences(Dimension newResolution, String newTheme, String newStockSymbol) {
        setPreferredResolution(newResolution);
        setPreferredTheme(newTheme);
        setPreferredStock(newStockSymbol);
    }

    private static void setPreferredResolution(Dimension preferredResolution) {
        Properties properties = new Properties();

        try (FileInputStream in = new FileInputStream(CONFIG_FILEPATH)) {
            properties.load(in);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Cannot load existing config: " + e.getMessage(), e);
        }

        properties.setProperty("preferredWidth", String.valueOf(preferredResolution.width));
        properties.setProperty("preferredHeight", String.valueOf(preferredResolution.height));

        try (FileOutputStream out = new FileOutputStream(CONFIG_FILEPATH)) {
            properties.store(out, null);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot save preferred resolution: " + e.getMessage(), e);
        }
    }

    private static void setPreferredTheme(String preferredTheme) {
        Properties properties = new Properties();

        try (FileInputStream in = new FileInputStream(CONFIG_FILEPATH)) {
            properties.load(in);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Cannot load existing config: " + e.getMessage(), e);
        }

        properties.setProperty("theme", preferredTheme);

        try (FileOutputStream out = new FileOutputStream(CONFIG_FILEPATH)) {
            properties.store(out, null);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot save preferred theme: " + e.getMessage(), e);
        }
    }

    private static void setPreferredStock(String preferredStock) {
        Properties properties = new Properties();

        try (FileInputStream in = new FileInputStream(CONFIG_FILEPATH)) {
            properties.load(in);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Cannot load existing config: " + e.getMessage(), e);
        }

        properties.setProperty("preferredStock", preferredStock);

        try (FileOutputStream out = new FileOutputStream(CONFIG_FILEPATH)) {
            properties.store(out, null);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot save preferred stock symbol: " + e.getMessage(), e);
        }
    }
}
