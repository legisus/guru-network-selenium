package com.guru.selenium.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;


@Slf4j
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/smoke.feature",
        glue = {"com.guru.selenium.steps"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-pretty.html",
                "json:target/cucumber-reports/CucumberTestReport.json",
                "junit:target/cucumber-reports/CucumberTestReport.xml"
        },
        monochrome = true,
        dryRun = false,
        tags = "not @Ignore"
)
public class SmokeTestRunner {

    @BeforeClass
    public static void setup() {
        log.info("Setting up TestRunner");

        try {
            io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
            log.info("ChromeDriver set up successfully");
        } catch (Exception e) {
            log.error("Failed to set up ChromeDriver: {}", e.getMessage(), e);
        }

        log.info("Java version: {}", System.getProperty("java.version"));

        try {
            ProcessBuilder builder = new ProcessBuilder("google-chrome", "--version");
            Process process = builder.start();
            java.util.Scanner scanner = new java.util.Scanner(process.getInputStream());
            String version = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            log.info("Chrome version: {}", version);
        } catch (Exception e) {
            log.warn("Could not determine Chrome version: {}", e.getMessage());
        }
    }
}
