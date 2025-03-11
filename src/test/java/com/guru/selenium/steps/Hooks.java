package com.guru.selenium.steps;

import com.guru.selenium.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

@Slf4j
public class Hooks {

    @BeforeAll
    public static void setupBeforeAllTests() {
        log.info("Setting up test environment before all tests");
        DriverFactory.getInstance();
    }

    @Before
    public void setupTest(Scenario scenario) {
        log.info("Starting scenario: {}", scenario.getName());

        // Explicitly initialize the WebDriver and ensure browser is opened
        WebDriver driver = DriverFactory.getInstance().getDriver();

        // Verify driver is initialized
        if (driver != null) {
            log.info("WebDriver is initialized and browser should be open");
        } else {
            log.error("Failed to initialize WebDriver!");
            throw new RuntimeException("WebDriver initialization failed");
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        log.info("Finished scenario: {} with status: {}",
                scenario.getName(), scenario.getStatus());

        if (scenario.isFailed()) {
            captureScreenshot(scenario);
        }
    }

    private void captureScreenshot(Scenario scenario) {
        try {
            WebDriver driver = DriverFactory.getInstance().getDriver();
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot on failure");
            log.info("Captured screenshot for failed scenario: {}", scenario.getName());
        } catch (Exception e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }

    @AfterAll
    public static void tearDownAll() {
        log.info("Tearing down all tests");
        DriverFactory.getInstance().quitAllDrivers();
    }
}