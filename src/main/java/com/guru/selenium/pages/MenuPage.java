package com.guru.selenium.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

@Slf4j
public class MenuPage extends BasePage {
    private final By tasksMenu = By.cssSelector("a[href='/tasks']");
    private final By agentsMenu = By.cssSelector("a[href='/agents']");
    private final By analyticsMenu = By.cssSelector("a[href='/analytics']");
    private final By tokensMenu = By.cssSelector("a[href='/tokens']");
    private final By swapMenu = By.cssSelector("a[href='/swap']");
    private final By leaderboardsMenu = By.cssSelector("a[href='/leaderboards']");
    private final By contentMenu = By.cssSelector("a[href='/content']");
    private final By launcherMenu = By.cssSelector("a[href='/launcher']");



    public boolean navigateToPage(String pageName, boolean isGuest) {
        log.info("Navigating to {} page as {}", pageName, isGuest ? "guest" : "logged in user");
        waitForPageToLoad();
            By menuLocator = getMenuLocatorByName(pageName);
            log.info("Navigating to {}", menuLocator);

            if (menuLocator == null) {
                log.error("No menu locator found for page: {}", pageName);
                return false;
            }

            WebElement menuItem = waitForElementToBeClickable(menuLocator, 10);

            try {
                menuItem.click();
                return true;
            }catch (Exception e) {
                log.error("Error while clicking on menu item {}", menuLocator, e);
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", menuItem);
                log.info("Clicked on {} menu item using JavaScript", pageName);
                waitForPageToLoad();
                return true;
            }
    }

    private By getMenuLocatorByName(String pageName) {
        return switch (pageName) {
            case "tasks" -> tasksMenu;
            case "agents" -> agentsMenu;
            case "analytics" -> analyticsMenu;
            case "tokens" -> tokensMenu;
            case "swap" -> swapMenu;
            case "leaderboards" -> leaderboardsMenu;
            case "content" -> contentMenu;
            case "launcher" -> launcherMenu;
            default -> {
                log.warn("Unknown page name: {}", pageName);
                yield null;
            }
        };
    }

}