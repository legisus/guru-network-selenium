package com.guru.selenium.pages;

import com.guru.selenium.utils.DriverFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MenuPage extends BasePage {
    // Menu item locators using data-tooltip-content
    private final By actionsMenu = By.xpath("//a[@data-tooltip-content='Actions']");
    private final By aiHubMenu = By.xpath("//a[@data-tooltip-content='AI Hub']");
    private final By analyticsMenu = By.xpath("//a[@data-tooltip-content='Analytics']");
    private final By tokensMenu = By.xpath("//a[@data-tooltip-content='Tokens']");
    private final By swapMenu = By.xpath("//a[@data-tooltip-content='Swap']");
    private final By leaderboardMenu = By.xpath("//a[@data-tooltip-content='Leaderboard']");
    private final By aboutMenu = By.xpath("//a[@data-tooltip-content='About']");

    // URL path segments for verification
    private final Map<String, String> urlPathSegments;

    // Page indicators to verify successful page load
    private final Map<String, By> pageIndicators;

    public MenuPage() {
        super();
        log.info("MenuPage initialized");

        // Initialize URL path segments for each page
        urlPathSegments = new HashMap<>();
        urlPathSegments.put("Actions", "tasks");
        urlPathSegments.put("AI Hub", "agents");
        urlPathSegments.put("Guru AI", "agents");
        urlPathSegments.put("Analytics", "analytics");
        urlPathSegments.put("Tokens", "tokens");
        urlPathSegments.put("Swap", "swap");
        urlPathSegments.put("Leaderboard", "leaderboards");
        urlPathSegments.put("Leaderboards", "leaderboards");
        urlPathSegments.put("About", "content");

        // Initialize page indicators for each page
        pageIndicators = new HashMap<>();
        pageIndicators.put("Actions", By.cssSelector(".TasksPage_container__r7VvT, .MainTasks_container__LNbdF"));
        pageIndicators.put("AI Hub", By.cssSelector(".AgentsPage_container__8zv44, .MainAgents_container__JfEMk"));
        pageIndicators.put("Guru AI", By.cssSelector(".AgentsPage_container__8zv44, .MainAgents_container__JfEMk"));
        pageIndicators.put("Analytics", By.cssSelector(".analytics_container__QEOdB"));
        pageIndicators.put("Tokens", By.cssSelector(".tokens_container__DtINW, .TokenList_container__bHYMP"));
        pageIndicators.put("Swap", By.cssSelector(".swap_container__NlPYI, .SwapWidget_container__YA4JW"));
        pageIndicators.put("Leaderboard", By.cssSelector(".leaderboards_container__a8Ey2"));
        pageIndicators.put("Leaderboards", By.cssSelector(".leaderboards_container__a8Ey2"));
        pageIndicators.put("About", By.cssSelector(".content_container__D1uEz"));
    }

    /**
     * Navigate to a specific page by clicking on its menu item
     *
     * @param pageName Name of the page to navigate to
     * @return true if navigation successful, false otherwise
     */
    public boolean navigateToPage(String pageName) {
        log.info("Navigating to {} page via menu", pageName);
        By menuLocator = getMenuLocatorByName(pageName);

        if (menuLocator == null) {
            log.error("No menu locator found for page: {}", pageName);
            return false;
        }

        try {
            // Wait for menu item to be clickable and click it
            WebElement menuItem = waitForElementToBeClickable(menuLocator, 10);
            if (menuItem == null) {
                log.error("Menu item for {} page not found or not clickable", pageName);
                return false;
            }

            clickElement(menuItem);
            log.info("Clicked on {} menu item", pageName);

            // Wait for page to load
            return waitForPageToLoadSuccessfully(pageName);
        } catch (Exception e) {
            log.error("Error navigating to {} page: {}", pageName, e.getMessage());
            return false;
        }
    }

    /**
     * Wait for the page to load successfully by checking URL and indicator element
     *
     * @param pageName Name of the page
     * @return true if page loaded successfully, false otherwise
     */
    private boolean waitForPageToLoadSuccessfully(String pageName) {
        // Get expected URL path segment
        String expectedPathSegment = urlPathSegments.get(pageName);
        if (expectedPathSegment == null) {
            log.error("No URL path segment found for page: {}", pageName);
            return false;
        }

        try {
            // Wait for page to load (document ready state)
            waitForPageToLoad();

            // Check URL path segment
            String currentUrl = driver.getCurrentUrl();
            boolean urlContainsPathSegment = currentUrl.contains("/" + expectedPathSegment);
            if (!urlContainsPathSegment) {
                log.error("URL does not contain expected path segment. Current: {}, Expected to contain: /{}",
                        currentUrl, expectedPathSegment);
                return false;
            }

            log.info("URL verified for {} page - contains path segment /{}", pageName, expectedPathSegment);

            // Check for indicator element
            By indicator = pageIndicators.get(pageName);
            if (indicator == null) {
                log.warn("No page indicator found for page: {}", pageName);
                // If no indicator defined, consider success based on URL only
                return true;
            }

            // Wait for specific page indicator to be visible
            WebElement pageElement = waitForElementToBeVisible(indicator, 15);
            boolean elementVisible = pageElement != null && pageElement.isDisplayed();

            if (elementVisible) {
                log.info("{} page loaded successfully with indicator element visible", pageName);
            } else {
                log.error("{} page did not load properly - indicator element not found", pageName);
            }

            return elementVisible;
        } catch (Exception e) {
            log.error("Error waiting for {} page to load: {}", pageName, e.getMessage());
            return false;
        }
    }

    /**
     * Get menu locator by page name
     *
     * @param pageName Name of the page
     * @return By locator for the menu item
     */
    private By getMenuLocatorByName(String pageName) {
        switch (pageName) {
            case "Actions":
                return actionsMenu;
            case "AI Hub":
            case "Guru AI": // Support both names
                return aiHubMenu;
            case "Analytics":
                return analyticsMenu;
            case "Tokens":
                return tokensMenu;
            case "Swap":
                return swapMenu;
            case "Leaderboard":
            case "Leaderboards": // Support both names
                return leaderboardMenu;
            case "About":
                return aboutMenu;
            default:
                log.warn("Unknown page name: {}", pageName);
                return null;
        }
    }

    /**
     * Check if page is loaded correctly by URL and indicator element
     *
     * @param pageName Name of the page to check
     * @return true if page is loaded correctly, false otherwise
     */
    public boolean isPageLoaded(String pageName) {
        // Check URL path segment
        String expectedPathSegment = urlPathSegments.get(pageName);
        if (expectedPathSegment == null) {
            return false;
        }

        String currentUrl = driver.getCurrentUrl();
        boolean urlContainsPathSegment = currentUrl.contains("/" + expectedPathSegment);

        if (!urlContainsPathSegment) {
            log.error("URL does not contain expected path segment for {}. Current: {}, Expected to contain: /{}",
                    pageName, currentUrl, expectedPathSegment);
            return false;
        }

        // Check indicator element if available
        By indicator = pageIndicators.get(pageName);
        if (indicator == null) {
            // If no indicator defined, consider success based on URL only
            return true;
        }

        return isElementDisplayed(indicator);
    }

    /**
     * Get all available menu items as a map of name to locator
     *
     * @return Map of menu item names to their locators
     */
    public Map<String, By> getAllMenuItems() {
        Map<String, By> menuItems = new HashMap<>();
        menuItems.put("Actions", actionsMenu);
        menuItems.put("AI Hub", aiHubMenu);
        menuItems.put("Analytics", analyticsMenu);
        menuItems.put("Tokens", tokensMenu);
        menuItems.put("Swap", swapMenu);
        menuItems.put("Leaderboard", leaderboardMenu);
        menuItems.put("About", aboutMenu);
        return menuItems;
    }
}