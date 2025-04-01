package com.guru.selenium.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

@Slf4j
public class MenuPage extends BasePage {
    private final By actionsMenu = By.xpath("//a[@data-tooltip-content='Actions']");
    private final By aiHubMenu = By.xpath("//a[@data-tooltip-content='AI Hub']");
    private final By analyticsMenu = By.xpath("//a[@data-tooltip-content='Analytics']");
    private final By tokensMenu = By.xpath("//a[@data-tooltip-content='Tokens']");
    private final By swapMenu = By.xpath("//a[@data-tooltip-content='Swap']");
    private final By leaderboardMenu = By.xpath("//a[@data-tooltip-content='Leaderboard']");
    private final By aboutMenu = By.xpath("//a[@href='/content' and @data-tooltip-content='About']");

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

}