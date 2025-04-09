package com.guru.selenium.pages;

import com.guru.selenium.utils.Navigator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Slf4j
public class AnalyticsPage extends BasePage {
    private final WebDriverWait wait;

    private final By assistantButton = By.cssSelector("button.aichat_toggle__GfhU0");
    private final By guruAiContainer = By.id("page-aichat");
    private final By guruAiOpenClass = By.cssSelector(".aichat_open___aIT5");
//    private final By guruAiTitle = By.xpath("//span[contains(text(),'Guru AI')]");
    private final By analyticsContainer = By.cssSelector(".content_body__1Ac9z");
    private final By pageTitle = By.xpath("(//span[contains(@class, 'Caption_header__OVD2p')])[1]");

    private final Navigator navigator;

    public AnalyticsPage() {
        super();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.navigator = new Navigator();
        log.info("AnalyticsPage initialized");
    }

    /**
     * Navigate to the Analytics page
     */
    public void navigateToAnalyticsPage() {
        log.info("Navigating to Analytics page via menu");
        navigator.navigateViaMenu("Analytics");
        wait.until(ExpectedConditions.visibilityOfElementLocated(analyticsContainer));
        log.info("Navigated to Analytics page");
    }

    /**
     * Click on the Guru AI assistant button
     */
    public void clickOnAssistantButton() {
        log.info("Attempting to click on the AI assistant button");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(assistantButton));
        button.click();
        log.info("Clicked on AI assistant button");
    }

//    /**
//     * Verify that Guru AI was opened
//     * @return true if Guru AI container is displayed and has open class
//     */
//    public boolean isGuruAiOpened() {
//        try {
//            log.info("Checking if Guru AI is opened");
//
//            WebElement aiContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(guruAiContainer));
//
//            boolean hasOpenClass = !driver.findElements(guruAiOpenClass).isEmpty();
////            boolean hasTitleVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(guruAiTitle)).isDisplayed();
//            boolean hasChatBody = !driver.findElements(By.cssSelector(".aichat_body__J5Kka")).isEmpty();
//
//            log.info("Guru AI container visible: {}", aiContainer.isDisplayed());
//            log.info("Guru AI has open class: {}", hasOpenClass);
////            log.info("Guru AI title visible: {}", hasTitleVisible);
//            log.info("Guru AI chat body present: {}", hasChatBody);
//
////            boolean isFullyOpened = aiContainer.isDisplayed() && hasOpenClass && hasTitleVisible && hasChatBody;
////            log.info("Guru AI opened status: {}", isFullyOpened);
////
////            return isFullyOpened;
//        } catch (Exception e) {
//            log.error("Error while checking if Guru AI is opened: {}", e.getMessage());
//            return false;
//        }
//    }

    public boolean isHeadOfAnalyticsPage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed();
    }
}