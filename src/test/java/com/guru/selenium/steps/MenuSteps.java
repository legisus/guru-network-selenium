package com.guru.selenium.steps;

import com.guru.selenium.pages.*;
import com.guru.selenium.utils.DriverFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Slf4j
public class MenuSteps {
    private final MenuPage menuPage;
    private final ActionsPage actionsPage;
    private final GuruAIAgentsPage guruAIAgentsPage;
    private final AnalyticsPage analyticsPage;
    private final TokensPage tokensPage;
    private final SwapPage swapPage;
    private final LeaderboardsPage leaderboardsPage;
    private final AboutPage aboutPage;

    private String currentPage;
    private boolean isGuest;

    public MenuSteps(MenuPage menuPage, ActionsPage actionsPage, GuruAIAgentsPage guruAIAgentsPage, AnalyticsPage analyticsPage, TokensPage tokensPage, SwapPage swapPage, LeaderboardsPage leaderboardsPage, AboutPage aboutPage) {
        this.menuPage = menuPage;
        this.actionsPage = actionsPage;
        this.guruAIAgentsPage = guruAIAgentsPage;
        this.analyticsPage = analyticsPage;
        this.tokensPage = tokensPage;
        this.swapPage = swapPage;
        this.leaderboardsPage = leaderboardsPage;
        this.aboutPage = aboutPage;
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

        if(currentPage.equals("actions")) {
            assertTrue(actionsPage.isActionsPageLoaded());
        }

        if(currentPage.equals("guruai")) {
            assertTrue(guruAIAgentsPage.isGuruAIAgentsPageLoaded());
        }

        if(currentPage.equals("analytics")) {
            assertTrue(analyticsPage.isHeadOfAnalyticsPage());
        }

        if(currentPage.equals("tokens")) {
            assertTrue(tokensPage.isTokensPage());
        }

        if(currentPage.equals("swap")) {
            assertTrue(swapPage.isSwapPageLoaded(isGuest));
        }

        if(currentPage.equals("leaderboards")) {
            assertTrue(leaderboardsPage.isLeaderboardsPageLoaded());
        }

        if(currentPage.equals("about")) {
            assertTrue(aboutPage.isAboutPageLoaded());
        }

    }
}
