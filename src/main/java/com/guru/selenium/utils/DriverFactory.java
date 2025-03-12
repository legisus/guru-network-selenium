package com.guru.selenium.utils;

import com.guru.selenium.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

@Slf4j
public class DriverFactory {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static DriverFactory instance;
    private final Configuration config;

    private DriverFactory() {
        this.config = Configuration.getInstance();
        log.info("DriverFactory initialized");
    }

    public static synchronized DriverFactory getInstance() {
        if (instance == null) {
            instance = new DriverFactory();
        }
        return instance;
    }

    public WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            log.info("No driver found in ThreadLocal, initializing new driver");
            initializeDriver();
        }
        return driverThreadLocal.get();
    }

    public void initializeDriver() {
        log.info("Initializing WebDriver");

        quitDriver();

        String browser = config.getProperty("browser", "chrome").toLowerCase();
        WebDriver driver;

        switch (browser) {
            case "chrome":
                driver = createChromeDriver();
                break;
            case "firefox":
                driver = createFirefoxDriver();
                break;
            case "edge":
                driver = createEdgeDriver();
                break;
            default:
                log.warn("Unsupported browser: {}. Defaulting to Chrome.", browser);
                driver = createChromeDriver();
        }

        driver.manage().timeouts().implicitlyWait(
                Duration.ofMillis(config.getIntProperty("timeouts.implicit", 10000)));
        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofMillis(config.getIntProperty("timeouts.pageLoad", 30000)));
        driver.manage().timeouts().scriptTimeout(
                Duration.ofMillis(config.getIntProperty("timeouts.script", 30000)));

        if (!config.getBooleanProperty("headless", false)) {
            driver.manage().window().maximize();
        }

        try {
            driver.get("about:blank");
            log.info("Successfully navigated to about:blank with new WebDriver instance");
        } catch (Exception e) {
            log.error("Failed initial navigation test: {}", e.getMessage());
            throw new RuntimeException("WebDriver initialization failed", e);
        }

        driverThreadLocal.set(driver);
        log.info("WebDriver initialized: {}", browser);
    }

    private WebDriver createChromeDriver() {
        log.debug("Creating Chrome WebDriver");

        try {
            log.info("Setting up ChromeDriver using WebDriverManager");
            io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
            log.info("ChromeDriver set up successfully");
        } catch (Exception e) {
            log.warn("Failed to set up ChromeDriver automatically: {}", e.getMessage());
            String chromeDriverPath = config.getProperty("webdriver.chrome.driver");
            if (chromeDriverPath != null && !chromeDriverPath.isEmpty()) {
                System.setProperty("webdriver.chrome.driver", chromeDriverPath);
                log.info("Using manually configured ChromeDriver path: {}", chromeDriverPath);
            } else {
                log.error("No ChromeDriver path configured and automatic setup failed");
                throw new RuntimeException("ChromeDriver setup failed", e);
            }
        }

        ChromeOptions options = new ChromeOptions();

        if (config.getBooleanProperty("headless", false)) {
            options.addArguments("--headless=new");
            log.debug("Chrome running in headless mode");
        }

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        String chromeArgs = config.getProperty("chrome.args", "");
        if (!chromeArgs.isEmpty()) {
            for (String arg : chromeArgs.split(",")) {
                options.addArguments(arg.trim());
                log.debug("Added Chrome argument: {}", arg.trim());
            }
        }

        options.setAcceptInsecureCerts(config.getBooleanProperty("acceptInsecureCerts", true));

        log.info("Creating new ChromeDriver instance with options");
        return new ChromeDriver(options);
    }

    private WebDriver createFirefoxDriver() {
        log.debug("Creating Firefox WebDriver");

        try {
            io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
            log.info("FirefoxDriver set up automatically");
        } catch (Exception e) {
            log.warn("Failed to set up FirefoxDriver automatically: {}", e.getMessage());
            String geckoDriverPath = config.getProperty("webdriver.gecko.driver");
            if (geckoDriverPath != null && !geckoDriverPath.isEmpty()) {
                System.setProperty("webdriver.gecko.driver", geckoDriverPath);
                log.info("Using manually configured FirefoxDriver path: {}", geckoDriverPath);
            }
        }

        FirefoxOptions options = new FirefoxOptions();

        if (config.getBooleanProperty("headless", false)) {
            options.addArguments("-headless");
            log.debug("Firefox running in headless mode");
        }

        options.setAcceptInsecureCerts(config.getBooleanProperty("acceptInsecureCerts", true));

        return new FirefoxDriver(options);
    }

    private WebDriver createEdgeDriver() {
        log.debug("Creating Edge WebDriver");

        try {
            io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup();
            log.info("EdgeDriver set up automatically");
        } catch (Exception e) {
            log.warn("Failed to set up EdgeDriver automatically: {}", e.getMessage());
            String edgeDriverPath = config.getProperty("webdriver.edge.driver");
            if (edgeDriverPath != null && !edgeDriverPath.isEmpty()) {
                System.setProperty("webdriver.edge.driver", edgeDriverPath);
                log.info("Using manually configured EdgeDriver path: {}", edgeDriverPath);
            }
        }

        EdgeOptions options = new EdgeOptions();

        if (config.getBooleanProperty("headless", false)) {
            options.addArguments("--headless");
            log.debug("Edge running in headless mode");
        }

        options.setAcceptInsecureCerts(config.getBooleanProperty("acceptInsecureCerts", true));

        return new EdgeDriver(options);
    }

    public void quitDriver() {
        log.info("Quitting WebDriver");
        WebDriver driver = driverThreadLocal.get();

        if (driver != null) {
            try {
                driver.quit();
                log.debug("WebDriver quit successfully");
            } catch (Exception e) {
                log.error("Error quitting WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    public void quitAllDrivers() {
        log.info("Quitting all WebDrivers");
        quitDriver();
    }
}