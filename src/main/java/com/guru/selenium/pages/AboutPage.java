package com.guru.selenium.pages;

import com.guru.selenium.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Slf4j
public class AboutPage extends BasePage {
    private final By contentContainer = By.cssSelector(".content_body__1Ac9z");
    private final By aboutHeading = By.cssSelector("h1");
    private final By aboutSections = By.cssSelector(".ContentItem_section__CiLdP");
    private final By contentText = By.cssSelector(".Text_container__s3zN4 p");
    private final By pageTitle = By.xpath("//span[contains(@class, 'PageDashboardsByTag_title__NmhvS') and text()='About']");

    private final String pageUrl;

    public AboutPage() {
        super();
        this.pageUrl = Configuration.getInstance().getProperty("base.url") + "/content/about";
        log.info("AboutPage initialized with URL: {}", pageUrl);
    }

    public void navigateToAboutPage() {
        log.info("Navigating to About page");
        navigateTo(pageUrl);
        waitForPageToLoad();
        waitForElementToBeVisible(contentContainer);
    }

    public boolean isAboutPageLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed();
    }

    public String getPageTitle() {
        try {
            WebElement heading = waitForElementToBeVisible(aboutHeading);
            if (heading != null) {
                return heading.getText();
            }
            return "";
        } catch (Exception e) {
            log.error("Error getting About page title: {}", e.getMessage());
            return "";
        }
    }
}