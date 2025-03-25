package com.guru.selenium.pages;

import com.guru.selenium.utils.Navigator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TokensPage extends BasePage {
    // Locators
    private final By searchInput = By.cssSelector("input[placeholder*='Search']");
    private final By tokenMarquee = By.cssSelector(".Marquee_container__n8pSR");
    private final By tokensList = By.cssSelector(".content_body__1Ac9z");
    private final By tokenAssetsItems = By.cssSelector(".TokenAsset_container__q260a");
    private final By navigationLinks = By.cssSelector(".MainMenu_link__ICVs0");
    private final By categoryTabs = By.cssSelector(".Tabs_tab__5q2_i");
    private final By tokenTagsCloud = By.cssSelector(".cloud_list__6D2fs");
    private final By tokenTagButtons = By.cssSelector(".cloud_tag___wQrd");
    private final By deltaValues = By.cssSelector(".Delta_container__fMWhH");
    private final By footerNavigation = By.cssSelector(".layout_footer__Koz5Z");

    // URLs for comparison
    private static final String DEX_TOKENS_URL = "https://dex.guru/tokens";

    private final Navigator navigator;

    public TokensPage() {
        super();
        this.navigator = new Navigator();
        log.info("TokensPage initialized");
    }

    public void navigateToTokensPage() {
        log.info("Navigating to tokens page");
        navigator.navigateViaMenu("Tokens");
        waitForElementToBeVisible(tokenMarquee);
    }

    public void navigateToDexGuruTokensPage() {
        log.info("Navigating to dex.guru tokens page");
        navigateToAndWaitForElement(DEX_TOKENS_URL, tokenMarquee);
    }

    public Map<String, Boolean> compareComponentsWithDexGuru() {
        // Create map of components to check
        Map<String, By> componentsToCheck = new HashMap<>();
        componentsToCheck.put("searchInput", searchInput);
        componentsToCheck.put("tokenMarquee", tokenMarquee);
        componentsToCheck.put("tokensList", tokensList);
        componentsToCheck.put("tokenAssetsItems", tokenAssetsItems);
        componentsToCheck.put("navigationLinks", navigationLinks);
        componentsToCheck.put("categoryTabs", categoryTabs);
        componentsToCheck.put("tokenTagsCloud", tokenTagsCloud);
        componentsToCheck.put("tokenTagButtons", tokenTagButtons);
        componentsToCheck.put("deltaValues", deltaValues);
        componentsToCheck.put("footerNavigation", footerNavigation);

        // Use BasePage method to compare with other page
        Map<String, Boolean> comparisonResult = compareElementsWithOtherPage(componentsToCheck, DEX_TOKENS_URL);

        // Add token count comparison
        String currentUrl = driver.getCurrentUrl();

        // Check token count on app page
        navigateToTokensPage();
        int appTokenCount = getElementCount(tokenAssetsItems);
        log.info("Number of token items found on app page: {}", appTokenCount);

        // Check token count on dex.guru page
        navigateToDexGuruTokensPage();
        int dexTokenCount = getElementCount(tokenAssetsItems);
        log.info("Number of token items found on dex.guru page: {}", dexTokenCount);

        // Compare token counts
        boolean hasMultipleTokens = appTokenCount > 5 && dexTokenCount > 5;
        comparisonResult.put("hasMultipleTokens", hasMultipleTokens);

        // Return to original URL
        driver.get(currentUrl);

        return comparisonResult;
    }

    public boolean verifyComponentsSimilarity() {
        Map<String, Boolean> comparisonResult = compareComponentsWithDexGuru();

        comparisonResult.forEach((key, value) ->
                log.info("Component comparison - {}: {}", key, value ? "MATCH" : "MISMATCH"));

        long matchingCount = comparisonResult.values().stream().filter(value -> value).count();
        double similarityPercentage = (double) matchingCount / comparisonResult.size() * 100;

        log.info("Component similarity percentage: {}%", similarityPercentage);

        return similarityPercentage >= 70;
    }

    public void captureScreenshotOfBothSites() {
        navigateToTokensPage();
        navigateToDexGuruTokensPage();
    }
}