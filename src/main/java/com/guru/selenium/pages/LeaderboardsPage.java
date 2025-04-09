package com.guru.selenium.pages;

import com.guru.selenium.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@Slf4j
public class LeaderboardsPage extends BasePage {
    private final By leaderboardContainer = By.cssSelector(".content_body__1Ac9z");
    private final By leaderboardTable = By.cssSelector("table.LeaderboardTable_table__ZrBqx");
    private final By leaderboardRows = By.cssSelector("tr.LeaderboardTable_row__VYYq0");
    private final By leaderboardTabs = By.cssSelector(".Tabs_tab__5q2_i");
    private final By pageTitle = By.xpath("(//span[contains(@class, 'Caption_container__nh_l0 Caption_header__OVD2p Caption_lg__aWlk2')])[1]");

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
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed();
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