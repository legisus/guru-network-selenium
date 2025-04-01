package com.guru.selenium.utils;

import com.guru.selenium.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Navigator class for menu-based navigation through the application
 */
@Slf4j
public class Navigator {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl;

    // Main menu locators
    private final By mainMenuContainer = By.id("main-menu");
    private final By menuItems = By.cssSelector(".MainMenu_item__2Bc14");
    private final By menuLinks = By.cssSelector(".MainMenu_link__ICVs0");
    private final By menuCaptions = By.cssSelector(".MainMenu_caption__5Xzp4");

    public Navigator() {
        this.driver = DriverFactory.getInstance().getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.baseUrl = Configuration.getInstance().getProperty("base.url");
        log.info("Navigator initialized with base URL: {}", baseUrl);
    }

    /**
     * Navigate to the home page
     */
    public void navigateToHome() {
        log.info("Navigating to home page");
        driver.get(baseUrl);
        waitForPageLoad();
        waitForMainMenuToLoad();
    }

    /**
     * Navigate to a specific page using the main menu
     *
     * @param menuItemName The name of the menu item to click (e.g. "Actions", "Tokens")
     * @return true if navigation was successful
     */
    public boolean navigateViaMenu(String menuItemName) {
        log.info("Navigating to {} via main menu", menuItemName);

        try {
            // Make sure we're on a page with the main menu
            waitForMainMenuToLoad();

            // Find all menu items
            List<WebElement> items = driver.findElements(menuLinks);
            log.info("Found {} menu items", items.size());

            // Click on the menu item with the matching caption
            for (WebElement item : items) {
                String caption = item.findElement(menuCaptions).getText();
                log.debug("Menu item caption: {}", caption);

                if (caption.equalsIgnoreCase(menuItemName)) {
                    log.info("Found menu item: {}", menuItemName);

                    // Wait for it to be clickable and click
                    wait.until(ExpectedConditions.elementToBeClickable(item));
                    item.click();

                    // Wait for navigation to complete
                    waitForPageLoad();

                    log.info("Successfully navigated to {}", menuItemName);
                    return true;
                }
            }

            log.warn("Menu item not found: {}", menuItemName);
            return false;

        } catch (Exception e) {
            log.error("Error navigating via menu: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Navigate directly to a page by URL path
     *
     * @param path The path part of the URL (e.g. "/tokens", "/analytics")
     */
    public void navigateToPath(String path) {
        String url = baseUrl + path;
        log.info("Navigating directly to URL: {}", url);
        driver.get(url);
        waitForPageLoad();
    }

    /**
     * Wait for the main menu to be loaded
     */
    private void waitForMainMenuToLoad() {
        try {
            log.debug("Waiting for main menu to load");
            wait.until(ExpectedConditions.visibilityOfElementLocated(mainMenuContainer));
            log.debug("Main menu loaded");
        } catch (Exception e) {
            log.warn("Error waiting for main menu: {}", e.getMessage());
        }
    }

    /**
     * Wait for page load to complete
     */
    private void waitForPageLoad() {
        try {
            // Wait for document ready state
            wait.until(driver -> {
                try {
                    return ((org.openqa.selenium.JavascriptExecutor) driver)
                            .executeScript("return document.readyState")
                            .equals("complete");
                } catch (Exception e) {
                    return false;
                }
            });

            // Additional wait for any dynamic content
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        } catch (Exception e) {
            log.warn("Error waiting for page load: {}", e.getMessage());
        }
    }

    /**
     * Map page name to menu item name
     *
     * @param pageName The name of the page (as used in test steps)
     * @return The corresponding menu item name
     */
    public String getMenuItemName(String pageName) {
        switch (pageName) {
            case "Actions":
                return "Actions";
            case "Guru AI":
                return "AI Hub";
            case "Analytics":
                return "Analytics";
            case "Tokens":
                return "Tokens";
            case "Swap":
                return "Swap";
            case "Leaderboards":
                return "Leaderboard";
            case "About":
                return "About";
            default:
                return pageName;
        }
    }
}