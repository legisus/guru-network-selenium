package com.guru.selenium.steps;

import com.guru.selenium.pages.*;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Slf4j
public class MenuSteps {
    private final MenuPage menuPage;
    private final TasksPage tasksPage;
    private final AgentsPage agentsPage;
    private final AnalyticsPage analyticsPage;
    private final TokensPage tokensPage;
    private final SwapPage swapPage;
    private final LeaderboardsPage leaderboardsPage;
    private final LauncherPage launcherPage;
    private final ContentPage contentPage;

    private String currentPage;
    private boolean isGuest;

    public MenuSteps(MenuPage menuPage, TasksPage tasksPage, AgentsPage agentsPage, AnalyticsPage analyticsPage,
                     TokensPage tokensPage, SwapPage swapPage, LeaderboardsPage leaderboardsPage,
                     LauncherPage launcherPage, ContentPage contentPage) {
        this.menuPage = menuPage;
        this.tasksPage = tasksPage;
        this.agentsPage = agentsPage;
        this.analyticsPage = analyticsPage;
        this.tokensPage = tokensPage;
        this.swapPage = swapPage;
        this.leaderboardsPage = leaderboardsPage;
        this.launcherPage = launcherPage;
        this.contentPage = contentPage;
        log.info("MenuSteps initialized");
    }

    @When("I navigate to {string} page as guest using side menu")
    public void navigateToPageAsGuest(String pageName) {
        currentPage = pageName.toLowerCase().trim().replaceAll(" ", "");
        isGuest = true;
        log.info("Page {} is opened: {}",
                pageName, menuPage.navigateToPage(pageName, true));
    }

//    @When("I navigate to {string} page as logged in user")
//    public void navigateToPageAsLoggedInUser(String pageName) {
//        currentPage = pageName;
//        menuPage.navigateToPage(pageName, false);
//    }

    @Then("The page should load without errors")
    public void thePageShouldLoadWithoutErrors() {
        log.info("Verifying {} page loaded without errors", currentPage);
        switch (currentPage) {
            case "tasks" -> assertTrue(tasksPage.isTasksPageLoaded());
            case "agents" -> assertTrue(agentsPage.isAgentsPageLoaded());
            case "analytics" -> assertTrue(analyticsPage.isHeadOfAnalyticsPage());
            case "tokens" -> assertTrue(tokensPage.isTokensPage());
            case "swap" -> assertTrue(swapPage.isSwapPageLoaded(isGuest));
            case "leaderboards" -> assertTrue(leaderboardsPage.isLeaderboardsPageLoaded());
            case "launcher" -> assertTrue(launcherPage.isLauncherPageLoaded(isGuest));
            case "content" -> assertTrue(contentPage.isAboutPageLoaded());
            default -> fail("Unknown page: " + currentPage);
        }

    }
}
