package com.guru.selenium.pages;

import com.guru.selenium.config.Configuration;
import com.guru.selenium.utils.Navigator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;

@Slf4j
public class HomePage extends BasePage {

    @FindBy(xpath = "//button[contains(@class, 'ProfileBar_toggle__AmjKN')]")
    private WebElement profileButton;

    private final By mainMenuContainer = By.id("main-menu");
    private final Navigator navigator;

    public HomePage() {
        super();
        this.navigator = new Navigator();
        log.info("HomePage initialized");
    }

    public boolean isProfileUploaded() {
        waitForElementToBeVisible(profileButton);
        log.info("Profile uploaded: {}", isElementDisplayed(profileButton));
        return isElementDisplayed(profileButton);
    }

    public void navigateToHomePage() {
        log.info("Navigating to home page");
        navigator.navigateToHome();
        waitForElementPresence(mainMenuContainer, 10);
    }

    /**
     * Navigate to a specific page using the main menu
     * @param pageName name of the page to navigate to
     * @return true if navigation was successful
     */
    public boolean navigateToPage(String pageName) {
        return navigator.navigateViaMenu(pageName);
    }
}