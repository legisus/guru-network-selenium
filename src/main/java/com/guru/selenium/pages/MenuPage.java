package com.guru.selenium.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MenuPage extends BasePage {
    private static final String BASE_URL = "https://app-guru-network-mono.dexguru.biz";

    private final By actionsMenu = By.xpath("//a[@data-tooltip-content='Actions']");
    private final By aiHubMenu = By.xpath("//a[@data-tooltip-content='AI Hub']");
    private final By analyticsMenu = By.xpath("//a[@data-tooltip-content='Analytics']");
    private final By tokensMenu = By.xpath("//a[@data-tooltip-content='Tokens']");
    private final By swapMenu = By.xpath("//a[@data-tooltip-content='Swap']");
    private final By leaderboardMenu = By.xpath("//a[@data-tooltip-content='Leaderboard']");
    private final By aboutMenu = By.xpath("//a[@data-tooltip-content='About']");

    private Map<String, String> pageContentIdentifiers;
    private boolean isGuest;

    public void initializePageContentIdentifiers(boolean isGuest) {
        this.isGuest = isGuest;
        pageContentIdentifiers = new HashMap<>();

        if (isGuest) {
            initializeGuestPageIdentifiers();
        } else {
            initializeLoggedInPageIdentifiers();
        }
    }

    private void initializeGuestPageIdentifiers() {
        log.info("Initializing page identifiers for guest user");
        pageContentIdentifiers.put("Actions", "task,action");
        pageContentIdentifiers.put("AI Hub", "AI,agent");
        pageContentIdentifiers.put("Guru AI", "AI,agent");
        pageContentIdentifiers.put("Analytics", "analytics,chart");
        pageContentIdentifiers.put("Tokens", "token");
        pageContentIdentifiers.put("Swap", "welcome,guru,network");
        pageContentIdentifiers.put("Leaderboard", "leaderboard");
        pageContentIdentifiers.put("About", "about");
    }

    private void initializeLoggedInPageIdentifiers() {
        log.info("Initializing page identifiers for logged-in user");
        pageContentIdentifiers.put("Actions", "task,action");
        pageContentIdentifiers.put("AI Hub", "AI,agent");
        pageContentIdentifiers.put("Guru AI", "AI,agent");
        pageContentIdentifiers.put("Analytics", "analytics,chart");
        pageContentIdentifiers.put("Tokens", "token");
        pageContentIdentifiers.put("Swap", "swap");
        pageContentIdentifiers.put("Leaderboard", "leaderboard");
        pageContentIdentifiers.put("About", "about");
    }

    public boolean navigateToPage(String pageName, boolean isGuest) {
        initializePageContentIdentifiers(isGuest);
        log.info("Navigating to {} page as {}", pageName, isGuest ? "guest" : "logged in user");

        try {
            String originalUrl = driver.getCurrentUrl();
            By menuLocator = getMenuLocatorByName(pageName);

            if (menuLocator == null) {
                log.error("No menu locator found for page: {}", pageName);
                return navigateDirectly(pageName);
            }

            WebElement menuItem = waitForElementToBeClickable(menuLocator, 10);
            if (menuItem == null) {
                log.error("Menu item for {} page not found or not clickable", pageName);
                return navigateDirectly(pageName);
            }

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", menuItem);
            log.info("Clicked on {} menu item using JavaScript", pageName);
            waitForPageToLoad();

            return verifyPageNavigated(pageName, originalUrl);
        } catch (Exception e) {
            log.error("Error navigating to {} page: {}", pageName, e.getMessage());
            return navigateDirectly(pageName);
        }
    }

    private By getMenuLocatorByName(String pageName) {
        return switch (pageName) {
            case "Actions" -> actionsMenu;
            case "Guru AI" -> aiHubMenu;
            case "Analytics" -> analyticsMenu;
            case "Tokens" -> tokensMenu;
            case "Swap" -> swapMenu;
            case "Leaderboard" -> leaderboardMenu;
            case "About" -> aboutMenu;
            default -> {
                log.warn("Unknown page name: {}", pageName);
                yield null;
            }
        };
    }

    private boolean navigateDirectly(String pageName) {
        try {
            String url = getDirectUrlForPage(pageName);
            log.info("Attempting direct navigation to {}: {}", pageName, url);
            driver.get(url);
            waitForPageToLoad();
            return verifyPageContent(pageName);
        } catch (Exception e) {
            log.error("Direct navigation to {} failed: {}", pageName, e.getMessage());
            return false;
        }
    }

    private String getDirectUrlForPage(String pageName) {
        if (isGuest) {
            return switch(pageName.toLowerCase()) {
                case "actions" -> BASE_URL + "/tasks";
                case "guru ai" -> BASE_URL + "/agents";
                case "analytics" -> BASE_URL + "/analytics";
                case "tokens" -> BASE_URL + "/tokens";
                case "swap" -> BASE_URL + "/login";
                case "leaderboard" -> BASE_URL + "/leaderboards";
                case "about" -> BASE_URL + "/content/about";
                default -> BASE_URL;
            };
        } else {
            return switch(pageName.toLowerCase()) {
                case "actions" -> BASE_URL + "/tasks";
                case "guru ai" -> BASE_URL + "/agents";
                case "analytics" -> BASE_URL + "/analytics";
                case "tokens" -> BASE_URL + "/tokens";
                case "swap" -> BASE_URL + "/swap";
                case "leaderboard" -> BASE_URL + "/leaderboards";
                case "about" -> BASE_URL + "/content/about";
                default -> BASE_URL;
            };
        }
    }

    private boolean verifyPageNavigated(String pageName, String originalUrl) {
        String currentUrl = driver.getCurrentUrl();
        boolean urlChanged = !currentUrl.equals(originalUrl);

        if (!urlChanged) {
            log.warn("URL didn't change after navigation to {}", pageName);
            return false;
        }

        if (isGuest) {
            if (currentUrl.contains("/login?redirect=")) {
                String redirectPath = currentUrl.substring(currentUrl.indexOf("redirect=") + 9);
                String expectedPath = getDirectUrlForPage(pageName).replace(BASE_URL, "");

                if (redirectPath.contains(expectedPath)) {
                    log.info("Guest mode: {} page redirected to login as expected: {}", pageName, currentUrl);
                    return true;
                }
            } else if (pageName.equalsIgnoreCase("About") && currentUrl.contains("/content/about")) {
                return true;
            }
        } else {
            String expectedUrl = getDirectUrlForPage(pageName);
            String currentUrlLower = currentUrl.toLowerCase();
            String expectedUrlLower = expectedUrl.toLowerCase();

            if (currentUrlLower.contains(expectedUrlLower) ||
                    (currentUrlLower.startsWith(BASE_URL) && expectedUrlLower.substring(BASE_URL.length()).contains(currentUrlLower.substring(BASE_URL.length())))) {
                log.info("{} page verified based on URL: {}", pageName, currentUrl);
                return true;
            }
        }

        return verifyPageContent(pageName);
    }

    private boolean verifyPageContent(String pageName) {
        if (pageContentIdentifiers == null) {
            log.error("Page content identifiers not initialized");
            return false;
        }

        String contentIdentifiers = pageContentIdentifiers.get(pageName);
        if (contentIdentifiers == null) {
            return false;
        }

        String[] identifiers = contentIdentifiers.split(",");
        for (String identifier : identifiers) {
            By textLocator = By.xpath("//*[contains(text(), '" + identifier + "')]");
            By classLocator = By.xpath("//*[contains(@class, '" + identifier + "')]");

            if (getElementCount(textLocator) > 0 || getElementCount(classLocator) > 0) {
                log.info("{} page verified by finding '{}' content", pageName, identifier);
                return true;
            }
        }

        log.warn("Could not verify {} page content", pageName);
        return false;
    }

    public boolean isPageLoaded(String pageName) {
        if ("Analytics".equals(pageName) && driver.getCurrentUrl().contains("/analytics")) {
            return true;
        }
        return verifyPageContent(pageName);
    }
}