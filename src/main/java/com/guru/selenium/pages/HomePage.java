package com.guru.selenium.pages;

import com.guru.selenium.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class HomePage extends BasePage {

    @FindBy(xpath = "//button[contains(@class, 'ProfileBar_toggle__AmjKN')]")
    private WebElement profileButton;

    private final String pageUrl;

    public HomePage() {
        super();
        Configuration config = Configuration.getInstance();
        this.pageUrl = config.getProperty("base.url") + "/tokens/top";
        log.info("HomePage initialized with URL: {}", pageUrl);
    }

    public boolean isProfileUploaded() {
        waitForElementToBeVisible(profileButton);
        log.info("Profile uploaded{}", isElementDisplayed(profileButton));
        return isElementDisplayed(profileButton);
    }

    public void navigateToHomePage() {
        log.info("Navigating to home page");
        navigateTo(pageUrl);
        waitForPageToLoad();

    }
}