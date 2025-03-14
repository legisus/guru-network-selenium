package com.guru.selenium.pages;

import com.guru.selenium.utils.DriverFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected static final long DEFAULT_TIMEOUT_SECONDS = 30;

    protected BasePage() {
        this.driver = DriverFactory.getInstance().getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
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
                    return true;
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

    protected void waitForElementPresence(WebElement element, long timeoutInSeconds) {
        log.debug("Waiting for element presence, timeout: {} seconds", timeoutInSeconds);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(driver -> {
            try {
                if (element.isDisplayed() || !element.isDisplayed()) {
                    return element;
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        });
    }

    /**
     * Wait for element to be present in DOM using By locator
     * @param locator The By locator
     * @param timeoutInSeconds Timeout in seconds
     * @return WebElement if found, null otherwise
     */
    protected WebElement waitForElementPresence(By locator, long timeoutInSeconds) {
        log.debug("Waiting for element presence by locator: {}, timeout: {} seconds", locator, timeoutInSeconds);
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            log.warn("Element not present within timeout: {}", e.getMessage());
            return null;
        }
    }

    protected void waitForElementToBeVisible(WebElement element) {
        log.debug("Waiting for element to be visible");
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for element to be visible using By locator
     * @param locator The By locator
     * @return WebElement if visible, null otherwise
     */
    protected WebElement waitForElementToBeVisible(By locator) {
        log.debug("Waiting for element to be visible by locator: {}", locator);
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            log.warn("Element not visible within timeout: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Wait for element to be visible using By locator with custom timeout
     * @param locator The By locator
     * @param timeoutInSeconds Timeout in seconds
     * @return WebElement if visible, null otherwise
     */
    protected WebElement waitForElementToBeVisible(By locator, long timeoutInSeconds) {
        log.debug("Waiting for element to be visible by locator: {}, timeout: {} seconds", locator, timeoutInSeconds);
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            log.warn("Element not visible within timeout: {}", e.getMessage());
            return null;
        }
    }

    protected void waitForElementToBeClickable(WebElement element) {
        log.debug("Waiting for element to be clickable");
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait for element to be clickable using By locator
     * @param locator The By locator
     * @return WebElement if clickable, null otherwise
     */
    protected WebElement waitForElementToBeClickable(By locator) {
        log.debug("Waiting for element to be clickable by locator: {}", locator);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            log.warn("Element not clickable within timeout: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Wait for element to be clickable using By locator with custom timeout
     * @param locator The By locator
     * @param timeoutInSeconds Timeout in seconds
     * @return WebElement if clickable, null otherwise
     */
    protected WebElement waitForElementToBeClickable(By locator, long timeoutInSeconds) {
        log.debug("Waiting for element to be clickable by locator: {}, timeout: {} seconds", locator, timeoutInSeconds);
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            return customWait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            log.warn("Element not clickable within timeout: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Wait for a loading indicator to disappear or become empty
     * @param loadingIndicator The By locator for the loading indicator
     * @param timeoutMillis Timeout in milliseconds
     */
    protected void waitForLoadingToComplete(By loadingIndicator, long timeoutMillis) {
        try {
            log.debug("Waiting up to {} ms for loading indicator to complete", timeoutMillis);

            // First check if loading indicator is present
            if (!driver.findElements(loadingIndicator).isEmpty()) {
                // Create a new wait with the specified timeout
                WebDriverWait customWait = new WebDriverWait(driver, Duration.ofMillis(timeoutMillis));

                // Wait for the loading indicator to have empty text or disappear
                customWait.until(driver -> {
                    try {
                        List<WebElement> loaders = driver.findElements(loadingIndicator);
                        if (loaders.isEmpty()) {
                            log.debug("Loading indicator no longer present");
                            return true;
                        }

                        WebElement loader = loaders.get(0);
                        boolean isComplete = loader.getText().trim().isEmpty();
                        if (isComplete) {
                            log.debug("Loading completed successfully (empty text)");
                        }
                        return isComplete;
                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        // If element is no longer present, consider loading complete
                        log.debug("Loading indicator no longer present (element not found)");
                        return true;
                    }
                });
                log.debug("Loading complete");
            } else {
                log.debug("No loading indicator found, assuming already loaded");
            }
        } catch (org.openqa.selenium.TimeoutException e) {
            log.warn("Loading indicator still present after {} ms timeout", timeoutMillis);
        } catch (Exception e) {
            log.warn("Issue waiting for loading to complete after {} ms: {}", timeoutMillis, e.getMessage());
        }
    }

    /**
     * Wait for a loading indicator to disappear or become empty using default timeout
     * @param loadingIndicator The By locator for the loading indicator
     */
    protected void waitForLoadingToComplete(By loadingIndicator) {
        // Use default timeout (convert seconds to milliseconds)
        waitForLoadingToComplete(loadingIndicator, DEFAULT_TIMEOUT_SECONDS * 1000);
    }

    /**
     * Wait for a loading indicator to disappear with a custom timeout
     * @param loadingIndicator The By locator for the loading indicator
     * @param timeoutSeconds Timeout in seconds
     */
    protected void waitForLoadingToComplete(By loadingIndicator, int timeoutSeconds) {
        // Convert seconds to milliseconds
        waitForLoadingToComplete(loadingIndicator, timeoutSeconds * 1000L);
    }

    /**
     * Wait for a new count of elements (used for waiting for new messages)
     * @param locator By locator for elements
     * @param initialCount Initial count of elements
     * @param timeoutSeconds Timeout in seconds
     * @return true if count increased, false otherwise
     */
    protected boolean waitForElementCountToIncrease(By locator, int initialCount, int timeoutSeconds) {
        log.debug("Waiting for element count to increase from {}, timeout: {} seconds", initialCount, timeoutSeconds);
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return customWait.until(driver -> {
                int currentCount = driver.findElements(locator).size();
                log.debug("Current count: {}, Initial count: {}", currentCount, initialCount);
                return currentCount > initialCount;
            });
        } catch (Exception e) {
            log.warn("Element count did not increase within timeout: {}", e.getMessage());
            return false;
        }
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

    /**
     * Click element found by locator
     * @param locator By locator for the element
     * @return true if clicked successfully, false otherwise
     */
    protected boolean clickElement(By locator) {
        log.info("Clicking element by locator: {}", locator);
        try {
            WebElement element = waitForElementToBeClickable(locator);
            if (element != null) {
                element.click();
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("Standard click failed, trying JavaScript click: {}", e.getMessage());
            try {
                WebElement element = driver.findElement(locator);
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", element);
                return true;
            } catch (Exception jsException) {
                log.error("JavaScript click also failed: {}", jsException.getMessage());
                return false;
            }
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

    /**
     * Check if element is displayed by locator
     * @param locator By locator for the element
     * @return true if displayed, false otherwise
     */
    protected boolean isElementDisplayed(By locator) {
        log.debug("Checking if element is displayed by locator: {}", locator);
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            log.debug("Element not displayed: {}", e.getMessage());
            return false;
        }
    }
    /**
     * Wait for page to load by checking for a specific element
     * @param locator By locator for element that indicates page is loaded
     * @param timeoutInSeconds Timeout in seconds
     */
    protected void waitForPageLoadByElement(By locator, long timeoutInSeconds) {
        log.debug("Waiting for page to load by element: {}, timeout: {} seconds", locator, timeoutInSeconds);
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
            log.info("Page loaded successfully - element found: {}", locator);
        } catch (Exception e) {
            log.warn("Timeout waiting for page to load, continuing anyway: {}", e.getMessage());
        }
    }

    /**
     * Wait for page to load by checking for a specific element using default timeout
     * @param locator By locator for element that indicates page is loaded
     */
    protected void waitForPageLoadByElement(By locator) {
        waitForPageLoadByElement(locator, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Check if elements matching the locator are present in DOM (regardless of visibility)
     * @param locator By locator to find elements
     * @return true if at least one element is present, false otherwise
     */
    protected boolean isElementPresent(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            boolean result = !elements.isEmpty();
            log.debug("Element with locator '{}' present: {}", locator, result);
            return result;
        } catch (Exception e) {
            log.error("Error checking if element is present: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get number of elements matching the locator
     * @param locator By locator to find elements
     * @return count of elements found
     */
    protected int getElementCount(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            int count = elements.size();
            log.debug("Element count for locator '{}': {}", locator, count);
            return count;
        } catch (Exception e) {
            log.error("Error getting element count: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Navigate to URL and wait for page to load by specific element
     * @param url URL to navigate to
     * @param indicatorElement By locator for element that indicates page is loaded
     */
    public void navigateToAndWaitForElement(String url, By indicatorElement) {
        log.info("Navigating to: {} and waiting for element: {}", url, indicatorElement);
        driver.get(url);

        // First wait for document to be ready
        waitForPageToLoad();

        // Then wait for specific element
        waitForPageLoadByElement(indicatorElement);
    }

    /**
     * Compare whether elements present on current page match those on another page
     * @param elementsToCompare Map of component names to locators
     * @param otherPageUrl URL of page to compare with
     * @return Map of component names to boolean indicating if they match
     */
    protected Map<String, Boolean> compareElementsWithOtherPage(Map<String, By> elementsToCompare, String otherPageUrl) {
        // Store current URL to return to later
        String currentUrl = driver.getCurrentUrl();

        // Capture current page elements
        log.info("Capturing elements on current page: {}", currentUrl);
        Map<String, Boolean> currentPageElements = new HashMap<>();
        for (Map.Entry<String, By> entry : elementsToCompare.entrySet()) {
            currentPageElements.put(entry.getKey(), isElementPresent(entry.getValue()));
        }

        // Navigate to other page
        log.info("Navigating to comparison page: {}", otherPageUrl);
        driver.get(otherPageUrl);
        waitForPageToLoad();

        // Capture other page elements
        Map<String, Boolean> otherPageElements = new HashMap<>();
        for (Map.Entry<String, By> entry : elementsToCompare.entrySet()) {
            otherPageElements.put(entry.getKey(), isElementPresent(entry.getValue()));
        }

        // Compare elements
        Map<String, Boolean> comparisonResult = new HashMap<>();
        for (String component : elementsToCompare.keySet()) {
            boolean isMatching = currentPageElements.get(component) && otherPageElements.get(component);
            comparisonResult.put(component, isMatching);
            log.info("Component '{}' match: {}", component, isMatching);
        }

        // Return to original page
        driver.get(currentUrl);
        waitForPageToLoad();

        return comparisonResult;
    }
}