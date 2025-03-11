package com.guru.selenium.steps;


import com.guru.selenium.pages.LoginPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.*;

@Slf4j
public class LoginSteps {
    private final LoginPage loginPage;

    public LoginSteps() {
        this.loginPage = new LoginPage();
    }

    @When("I click on signIn button")
    public void iClickOnSignInButton() {
        log.info("Clicking on signIn button");
        loginPage.clickLoginButton();
    }

    @And("Verify that popup appeared")
    public void verifyThatPopupAppeared() throws InterruptedException {
        log.info("Verify that popup appeared");
        loginPage.waitUntilPopupIsLoaded(5);
        assertTrue("Popup appeared", loginPage.isOnSignInPopup());
    }

    @And("Click on logIn with Telegram button")
    public void clickOnLogInWithTelegramButton() {
        loginPage.clickLoginWithTelegramButton();
    }


    @And("Enter phone {string} for login with Telegram")
    public void enterPhoneForLoginWithTelegram(String phone) {
        loginPage.switchToNewWindowAndEnterPhone(phone);
    }

}