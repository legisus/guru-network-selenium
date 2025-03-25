package com.guru.selenium.pages;

import com.guru.selenium.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@Slf4j
public class AboutPage extends BasePage {
    private final By contentContainer = By.cssSelector(".content_body__1Ac9z");
    private final By aboutHeading = By.cssSelector("h1");
    private final By aboutSections = By.cssSelector(".ContentItem_section__CiLdP");
    private final By contentText = By.cssSelector(".Text_container__s3zN4 p");

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
        try {
            boolean isContentContainerVisible = isElementDisplayed(contentContainer);
            boolean isHeadingVisible = isElementDisplayed(aboutHeading);
            int sectionCount = driver.findElements(aboutSections).size();
            int textParagraphCount = driver.findElements(contentText).size();

            log.info("About page loaded: container visible: {}, heading visible: {}, section count: {}, paragraph count: {}",
                    isContentContainerVisible, isHeadingVisible, sectionCount, textParagraphCount);

            return isContentContainerVisible && isHeadingVisible && sectionCount > 0;
        } catch (Exception e) {
            log.error("Error checking if About page is loaded: {}", e.getMessage());
            return false;
        }
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