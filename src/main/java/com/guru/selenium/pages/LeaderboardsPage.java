package com.guru.selenium.pages;

import com.guru.selenium.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@Slf4j
public class LeaderboardsPage extends BasePage {
    private final By leaderboardContainer = By.cssSelector(".content_body__1Ac9z");
    private final By leaderboardTable = By.cssSelector("table.LeaderboardTable_table__ZrBqx");
    private final By leaderboardRows = By.cssSelector("tr.LeaderboardTable_row__VYYq0");
    private final By leaderboardTabs = By.cssSelector(".Tabs_tab__5q2_i");
    private final By pageTitle = By.xpath("//h1[contains(text(), 'Leaderboard')]");

    private final String pageUrl;

    public LeaderboardsPage() {
        super();
        this.pageUrl = Configuration.getInstance().getProperty("base.url") + "/leaderboards";
        log.info("LeaderboardsPage initialized with URL: {}", pageUrl);
    }

    public void navigateToLeaderboardsPage() {
        log.info("Navigating to Leaderboards page");
        navigateTo(pageUrl);
        waitForPageToLoad();
        waitForElementToBeVisible(leaderboardContainer);
    }

    public boolean isLeaderboardsPageLoaded() {
        try {
            boolean isTitleVisible = isElementDisplayed(pageTitle);
            boolean isLeaderboardContainerVisible = isElementDisplayed(leaderboardContainer);
            boolean isLeaderboardTableVisible = isElementDisplayed(leaderboardTable);
            int rowCount = driver.findElements(leaderboardRows).size();

            log.info("Leaderboards page loaded: title visible: {}, container visible: {}, table visible: {}, row count: {}",
                    isTitleVisible, isLeaderboardContainerVisible, isLeaderboardTableVisible, rowCount);

            return isTitleVisible && isLeaderboardContainerVisible && isLeaderboardTableVisible;
        } catch (Exception e) {
            log.error("Error checking if Leaderboards page is loaded: {}", e.getMessage());
            return false;
        }
    }

    public void switchTab(String tabName) {
        try {
            List<WebElement> tabs = driver.findElements(leaderboardTabs);
            for (WebElement tab : tabs) {
                if (tab.getText().trim().equalsIgnoreCase(tabName)) {
                    clickElement(tab);
                    log.info("Switched to Leaderboard tab: {}", tabName);
                    return;
                }
            }
            log.warn("Tab with name '{}' not found", tabName);
        } catch (Exception e) {
            log.error("Error switching leaderboard tab: {}", e.getMessage());
        }
    }
}