package com.guru.selenium.steps;

import com.guru.selenium.pages.MenuPage;
import com.guru.selenium.utils.DriverFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.fail;

@Slf4j
public class PageNavigationSteps {
    private final WebDriver driver;
    private final MenuPage menuPage;
    private String currentPage;

    public PageNavigationSteps() {
        this.driver = DriverFactory.getInstance().getDriver();
        this.menuPage = new MenuPage();
        log.info("PageNavigationSteps initialized");
    }

    @When("I navigate to {string} page as guest")
    public void navigateToPageAsGuest(String pageName) {
        currentPage = pageName;
        menuPage.navigateToPage(pageName, true);
    }

    @When("I navigate to {string} page as logged in user")
    public void navigateToPageAsLoggedInUser(String pageName) {
        currentPage = pageName;
        menuPage.navigateToPage(pageName, false);
    }

    @Then("The page should load without errors")
    public void thePageShouldLoadWithoutErrors() {
        log.info("Verifying {} page loaded without errors", currentPage);

        try {
            boolean loaded = menuPage.isPageLoaded(currentPage);

            if (!loaded) {
                String currentUrl = driver.getCurrentUrl().toLowerCase();
                String pageNameLower = currentPage.toLowerCase().replace(" ", "");

                boolean urlLooksCorrect = currentUrl.contains(pageNameLower) ||
                        (pageNameLower.equals("actions") && currentUrl.contains("tasks")) ||
                        (pageNameLower.equals("guruai") && currentUrl.contains("agents"));

                if (!urlLooksCorrect) {
                    fail("Page " + currentPage + " failed to load properly");
                }
            }
        } catch (Exception e) {
            log.error("Verification error: {}", e.getMessage());
            fail("Exception during page verification: " + e.getMessage());
        }
    }
}