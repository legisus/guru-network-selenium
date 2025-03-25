package com.guru.selenium.steps;

import com.guru.selenium.config.Configuration;
import com.guru.selenium.pages.*;
import com.guru.selenium.utils.DriverFactory;
import com.guru.selenium.utils.Navigator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Slf4j
public class PageNavigationSteps {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl;
    private final Navigator navigator;
    private final Map<String, String> pageUrls;

    // Page objects
    private final ActionsPage actionsPage;
    private final GuruAIAgentsPage guruAIAgentsPage;
    private final AnalyticsPage analyticsPage;
    private final TokensPage tokensPage;
    private final SwapPage swapPage;
    private final LeaderboardsPage leaderboardsPage;
    private final AboutPage aboutPage;
    private final LoginPage loginPage;
    private final HomePage homePage;

    public PageNavigationSteps(Navigator navigator) {
        this.navigator = navigator;
        this.driver = DriverFactory.getInstance().getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.baseUrl = Configuration.getInstance().getProperty("base.url");

        // Initialize page objects
        this.actionsPage = new ActionsPage();
        this.guruAIAgentsPage = new GuruAIAgentsPage();
        this.analyticsPage = new AnalyticsPage();
        this.tokensPage = new TokensPage();
        this.swapPage = new SwapPage();
        this.leaderboardsPage = new LeaderboardsPage();
        this.aboutPage = new AboutPage();
        this.loginPage = new LoginPage();
        this.homePage = new HomePage();

        // Initialize page URLs
        this.pageUrls = new HashMap<>();
        pageUrls.put("Home", baseUrl + "/tokens/top");
        pageUrls.put("Actions", baseUrl + "/tasks");
        pageUrls.put("Guru AI", baseUrl + "/agents");
        pageUrls.put("Analytics", baseUrl + "/analytics");
        pageUrls.put("Tokens", baseUrl + "/tokens");
        pageUrls.put("Swap", baseUrl + "/swap");
        pageUrls.put("Leaderboards", baseUrl + "/leaderboards");
        pageUrls.put("About", baseUrl + "/content/about");

        log.info("PageNavigationSteps initialized");
    }

    @Given("I am logged into the application")
    public void i_am_logged_into_the_application() {
        // Check if already logged in
        if (homePage.isProfileUploaded()) {
            log.info("User is already logged in");
            return;
        }

        log.info("User is not logged in yet, proceeding with login");

        // Navigate to home page
        homePage.navigateToHomePage();

        try {
            // Login process
            loginPage.clickLoginButton();
            loginPage.waitUntilPopupIsLoaded(5);
            loginPage.clickLoginWithTelegramButton();
            loginPage.switchToNewWindowAndEnterPhone("YOUR_PHONE_NUMBER"); // Replace with test phone number

            // Wait for profile to be uploaded
            assertTrue("Failed to log in", homePage.isProfileUploaded());
            log.info("Successfully logged in");
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
            log.info("Assuming user may already be logged in and continuing test");
        }
    }

    @When("I navigate to {string} page")
    public void i_navigate_to_page(String pageName) {
        log.info("Navigating to {} page", pageName);

        try {
            String menuItemName = navigator.getMenuItemName(pageName);
            boolean success = navigator.navigateViaMenu(menuItemName);

            assertTrue("Failed to navigate to " + pageName + " page", success);
            log.info("Navigated to {} page successfully", pageName);
        } catch (Exception e) {
            log.error("Error navigating to {} page: {}", pageName, e.getMessage());
            throw e;
        }
    }

    @Then("The page should load without errors")
    public void the_page_should_load_without_errors() {
        // Get the current URL to determine which page we're on
        String currentUrl = driver.getCurrentUrl();
        String pageName = getPageNameFromUrl(currentUrl);

        log.info("Verifying page loaded without errors: {}", pageName);

        try {
            // Check if page is properly loaded using page objects
            boolean pageLoaded = false;

            switch (pageName) {
                case "Actions":
                    pageLoaded = actionsPage.isActionsPageLoaded();
                    break;
                case "Guru AI":
                    pageLoaded = guruAIAgentsPage.isGuruAIAgentsPageLoaded();
                    break;
                case "Analytics":
                    pageLoaded = analyticsPage.isGuruAiOpened();
                    break;
                case "Tokens":
                    pageLoaded = tokensPage.verifyComponentsSimilarity();
                    break;
                case "Swap":
                    pageLoaded = swapPage.isSwapPageLoaded();
                    break;
                case "Leaderboards":
                    pageLoaded = leaderboardsPage.isLeaderboardsPageLoaded();
                    break;
                case "About":
                    pageLoaded = aboutPage.isAboutPageLoaded();
                    break;
                default:
                    // Fallback to home page
                    pageLoaded = homePage.isProfileUploaded();
            }

            assertTrue("Page " + pageName + " did not load properly", pageLoaded);
            log.info("Page {} loaded successfully without errors", pageName);
        } catch (Exception e) {
            log.error("Error checking page load status: {}", e.getMessage());
            throw e;
        }
    }

    private String getPageNameFromUrl(String url) {
        for (Map.Entry<String, String> entry : pageUrls.entrySet()) {
            if (url.contains(entry.getValue())) {
                return entry.getKey();
            }
        }
        return "Unknown";
    }
}