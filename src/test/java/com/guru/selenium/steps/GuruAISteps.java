package com.guru.selenium.steps;

import com.guru.selenium.pages.GuruAIPage;
import com.guru.selenium.utils.DriverFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;
import org.junit.Assert;

@Slf4j
public class GuruAISteps {
    private final GuruAIPage guruAIPage;
    private final WebDriver driver;

    public GuruAISteps() {
        this.guruAIPage = new GuruAIPage();
        this.driver = DriverFactory.getInstance().getDriver();
        log.info("GuruAISteps initialized");
    }

    @Then("I click on button {string} for Guru AI")
    public void iClickOnButtonForGuruAI(String buttonText) {
        log.info("Clicking on Guru AI button: '{}'", buttonText);
        assertTrue("Guru AI should be open before clicking buttons", guruAIPage.isGuruAIOpen());

        boolean clicked = guruAIPage.clickPromptButton(buttonText);

        assertTrue("Should be able to click on button: " + buttonText, clicked);
        log.info("Successfully clicked on Guru AI button: '{}'", buttonText);
    }

    @When("I enter {string} in Guru AI")
    public void iEnterTextInGuruAI(String text) {
        log.info("Entering text in Guru AI: '{}'", text);
        guruAIPage.enterChatInput(text);
    }

    @When("I submit the Guru AI message")
    public void iSubmitGuruAIMessage() {
        log.info("Submitting Guru AI message");
        guruAIPage.submitChat();
    }

    @Then("I should see a response from Guru AI")
    public void iShouldSeeResponseFromGuruAI() {
        log.info("Waiting for Guru AI response");
        boolean responseReceived = guruAIPage.waitForResponse(15); // 15 second timeout

        assertTrue("Should receive a response from Guru AI", responseReceived);
        log.info("Successfully received response from Guru AI");
    }

    @Then("I check that Guru AI contribute any response")
    public void iCheckThatGuruAIContributeAnyResponse() {
        log.info("Checking if Guru AI contributed a proper response");

        try {
            boolean hasProperResponse = guruAIPage.hasProperResponse();
            log.info("All AI responses: {}", guruAIPage.getAllResponses());
            log.info("Guru AI response verification result: {}", hasProperResponse ? "PASS" : "FAIL");
            if (!hasProperResponse) {
                log.error("Guru AI response check failed - AI did not provide a proper response");
                Assert.fail("Guru AI should provide a proper response (not empty, not an error message, and not just a shrug emoticon)");
            }
        } catch (Exception e) {
            log.error("Exception during Guru AI response check: {}", e.getMessage());
            Assert.fail("Exception during Guru AI response check: " + e.getMessage());
        }
    }
}