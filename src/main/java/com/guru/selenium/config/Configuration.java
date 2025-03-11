package com.guru.selenium.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class Configuration {
    private static Configuration instance;
    private final Properties properties;

    private Configuration() {
        properties = new Properties();
        loadProperties();
        log.info("Configuration initialized");
    }

    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                log.warn("Unable to find config.properties file, using default values");
                return;
            }

            properties.load(input);
            log.info("Loaded {} properties from config file", properties.size());
        } catch (IOException e) {
            log.error("Error loading properties: {}", e.getMessage(), e);
        }

        for (String key : properties.stringPropertyNames()) {
            String envKey = key.replace(".", "_").toUpperCase();
            String envValue = System.getenv(envKey);
            if (envValue != null && !envValue.isEmpty()) {
                properties.setProperty(key, envValue);
                log.debug("Overriding property from environment: {} = {}", key, envValue);
            }
        }

        for (String key : properties.stringPropertyNames()) {
            String sysValue = System.getProperty(key);
            if (sysValue != null && !sysValue.isEmpty()) {
                properties.setProperty(key, sysValue);
                log.debug("Overriding property from system properties: {} = {}", key, sysValue);
            }
        }
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        log.trace("Getting property: {} = {}", key, value);
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        log.trace("Getting property with default: {} = {}", key, value);
        return value;
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        log.debug("Set property: {} = {}", key, value);
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid integer property {}: {}", key, value);
            return defaultValue;
        }
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }

        return Boolean.parseBoolean(value);
    }
}