package com.guru.selenium.steps;

import com.guru.selenium.pages.HomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertTrue;

@Slf4j
public class HomeSteps {
    private final HomePage homePage;

    public HomeSteps() {
        this.homePage = new HomePage();
        log.info("HomeSteps initialized");
    }

    @Given("I am on the home page")
    public void iAmOnTheHomePage() {
        log.info("Navigating to home page");
        homePage.navigateToHomePage();
    }

    @Then("Verify profile uploaded successfully")
    public void verifyProfileUploadedSuccessfully() {
        assertTrue(homePage.isProfileUploaded());
    }
}