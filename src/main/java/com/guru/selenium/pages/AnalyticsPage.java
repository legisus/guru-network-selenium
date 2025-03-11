package com.guru.selenium.pages;

import com.guru.selenium.utils.DriverFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Slf4j
public class AnalyticsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By assistantButton = By.cssSelector("button.aichat_toggle__GfhU0");
    private final By guruAiContainer = By.id("page-aichat");
    private final By guruAiOpenClass = By.cssSelector(".aichat_open___aIT5");
    private final By guruAiHeader = By.cssSelector(".aichat_header__Kfkhu");
    private final By guruAiTitle = By.xpath("//span[contains(text(),'Guru AI')]");

    // Analytics page URL
    private static final String ANALYTICS_PAGE_URL = "https://app-guru-network-mono.dexguru.biz/analytics";

    public AnalyticsPage() {
        this.driver = DriverFactory.getInstance().getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        log.info("AnalyticsPage initialized");
    }

    /**
     * Navigate to the Analytics page
     */
    public void navigateToAnalyticsPage() {
        driver.get(ANALYTICS_PAGE_URL);
        wait.until(ExpectedConditions.urlContains("/analytics"));
        log.info("Navigated to Analytics page: {}", ANALYTICS_PAGE_URL);
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

    /**
     * Verify that Guru AI was opened
     * @return true if Guru AI container is displayed and has open class
     */
    public boolean isGuruAiOpened() {
        try {
            log.info("Checking if Guru AI is opened");

            // Wait for the container to be visible
            WebElement aiContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(guruAiContainer));

            // Check if it has the open class
            boolean hasOpenClass = !driver.findElements(guruAiOpenClass).isEmpty();

            // Check for the Guru AI title within the header
            boolean hasTitleVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(guruAiTitle)).isDisplayed();

            // Check for chat body elements
            boolean hasChatBody = !driver.findElements(By.cssSelector(".aichat_body__J5Kka")).isEmpty();

            // Log detailed status
            log.info("Guru AI container visible: {}", aiContainer.isDisplayed());
            log.info("Guru AI has open class: {}", hasOpenClass);
            log.info("Guru AI title visible: {}", hasTitleVisible);
            log.info("Guru AI chat body present: {}", hasChatBody);

            // All conditions must be true for the AI to be considered open
            boolean isFullyOpened = aiContainer.isDisplayed() && hasOpenClass && hasTitleVisible && hasChatBody;
            log.info("Guru AI opened status: {}", isFullyOpened);

            return isFullyOpened;
        } catch (Exception e) {
            log.error("Error while checking if Guru AI is opened: {}", e.getMessage());
            return false;
        }
    }
}
