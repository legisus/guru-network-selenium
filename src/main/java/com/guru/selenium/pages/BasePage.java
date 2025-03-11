package com.guru.selenium.pages;

import com.guru.selenium.utils.DriverFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Slf4j
public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected static final long defaultTimeoutSeconds = 30;

    protected BasePage() {
        this.driver = DriverFactory.getInstance().getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(defaultTimeoutSeconds));
        PageFactory.initElements(driver, this);
        log.info("Initialized BasePage with PageFactory");
    }

    public void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
        waitForPageToLoad();
    }

    protected void waitForPageToLoad() {
        log.debug("Waiting for page to load completely");
        wait.until(driver -> {
            String readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState").toString();
            log.debug("Current document.readyState: {}", readyState);
            return "complete".equals(readyState);
        });

        try {
            wait.until(driver -> {
                boolean jQueryDefined = (boolean) ((JavascriptExecutor) driver)
                        .executeScript("return typeof jQuery != 'undefined'");
                if (!jQueryDefined) {
                    return true; // jQuery is not used in the page, so no need to wait
                }
                return (boolean) ((JavascriptExecutor) driver)
                        .executeScript("return jQuery.active == 0");
            });
            log.debug("jQuery loading complete");
        } catch (Exception e) {
            log.debug("jQuery not available or error checking jQuery status: {}", e.getMessage());
        }

        try {
            wait.until(driver -> {
                boolean angularDefined = (boolean) ((JavascriptExecutor) driver)
                        .executeScript("return typeof angular != 'undefined'");
                if (!angularDefined) {
                    return true; // Angular is not used in the page, so no need to wait
                }
                return (boolean) ((JavascriptExecutor) driver)
                        .executeScript("return angular.element(document).injector().get('$http').pendingRequests.length === 0");
            });
            log.debug("Angular loading complete");
        } catch (Exception e) {
            log.debug("Angular not available or error checking Angular status: {}", e.getMessage());
        }

        log.debug("Page load wait complete");
    }

    protected WebElement waitForElementPresence(WebElement element, long timeoutInSeconds) {
        log.debug("Waiting for element presence, timeout: {} seconds", timeoutInSeconds);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(driver -> {
            try {
                // Check if element is present in DOM (may not be visible)
                if (element.isDisplayed() || !element.isDisplayed()) {
                    return element;
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        });
    }

    protected void waitForElementToBeVisible(WebElement element) {
        log.debug("Waiting for element to be visible");
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForElementToBeClickable(WebElement element) {
        log.debug("Waiting for element to be clickable");
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void clickElement(WebElement element) {
        log.info("Clicking element");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            log.warn("Standard click failed, trying JavaScript click: {}", e.getMessage());
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
        }
    }

    protected boolean isElementDisplayed(WebElement element) {
        log.debug("Checking if element is displayed");
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            log.debug("Element not displayed: {}", e.getMessage());
            return false;
        }
    }
}