package com.guru.selenium.steps;

import com.guru.selenium.pages.AnalyticsPage;
import com.guru.selenium.utils.DriverFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

@Slf4j
public class AnalyticsSteps {
    private final AnalyticsPage analyticsPage;
    private final WebDriver driver;

    public AnalyticsSteps() {
        this.analyticsPage = new AnalyticsPage();
        this.driver = DriverFactory.getInstance().getDriver();
        log.info("AnalyticsSteps initialized");
    }

    @When("I navigate to analytics page")
    public void iNavigateToAnalyticsPage() {
        log.info("Navigating to analytics page");
        analyticsPage.navigateToAnalyticsPage();
    }

    @When("Click on assistant")
    public void clickOnAssistant() {
        log.info("Clicking on assistant button");
        analyticsPage.clickOnAssistantButton();
    }

    @Then("Guru AI was opened")
    public void guruAIWasOpened() {
        log.info("Verifying Guru AI was opened");
        boolean isGuruAiOpened = analyticsPage.isGuruAiOpened();
        try {
            log.info("Taking screenshot of opened Guru AI");
        } catch (Exception e) {
            log.warn("Failed to take screenshot: {}", e.getMessage());
        }
        assertTrue("Guru AI should be opened after clicking the assistant button", isGuruAiOpened);
        log.info("Guru AI opened verification completed with result: {}", isGuruAiOpened ? "PASS" : "FAIL");
    }
}