package com.guru.selenium.steps;

import com.guru.selenium.pages.TokensPage;
import com.guru.selenium.utils.DriverFactory;
import com.guru.selenium.utils.VisualComparisonUtil;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import static org.junit.Assert.assertTrue;

@Slf4j
public class TokensSteps {
    private final TokensPage tokensPage;
    private final WebDriver driver;

    public TokensSteps() {
        this.tokensPage = new TokensPage();
        this.driver = DriverFactory.getInstance().getDriver();
        log.info("TokensSteps initialized");
    }

    @When("I navigate to tokens page")
    public void iNavigateToTokensPage() {
        log.info("Navigating to tokens page");
        tokensPage.navigateToTokensPage();
    }

    @Then("Check that it has same components like on dex guru tokens page")
    public void checkThatItHasSameComponentsLikeOnDexGuruTokensPage() {
        log.info("Checking that components are similar to dex guru tokens page");

        // Method 1: Use our custom component comparison
        boolean areSimilar = tokensPage.verifyComponentsSimilarity();

        // Method 2: Use the utility for a more detailed comparison
        Map<String, Object> comparisonReport = VisualComparisonUtil.compareWebsites(
                driver,
                "https://app-guru-network-mono.dexguru.biz/tokens",
                "https://dex.guru/tokens"
        );

        // Log the detailed report
        log.info("Comparison report: {}", comparisonReport);

        // Get the similarity score from the utility
        double similarityScore = (double) comparisonReport.getOrDefault("similarityScore", 0.0);
        log.info("Similarity score: {}%", similarityScore);

        // Check if the sites are considered similar (using both methods)
        boolean isVisualComparisonPassed = similarityScore >= 70.0;
        boolean finalVerdict = areSimilar && isVisualComparisonPassed;

        assertTrue("Components should be similar to dex.guru tokens page", finalVerdict);
        log.info("Components similarity verification completed with result: {}", finalVerdict ? "PASS" : "FAIL");

        // Additional debugging for element selectors if needed
        if (!finalVerdict) {
            log.info("Performing additional selector analysis for debugging purposes:");
            VisualComparisonUtil.suggestSelectors(driver, "https://app-guru-network-mono.dexguru.biz/tokens");
            VisualComparisonUtil.suggestSelectors(driver, "https://dex.guru/tokens");
        }
    }
}