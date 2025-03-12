package com.guru.selenium.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.guru.selenium.utils.DriverFactory;

@Slf4j
public class TokensPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

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

    public TokensPage() {
        this.driver = DriverFactory.getInstance().getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        log.info("TokensPage initialized");
    }

    public void navigateToTokensPage() {
        waitForPageLoad();
        driver.get("https://app-guru-network-mono.dexguru.biz/tokens");
        log.info("Navigated to app-guru-network-mono tokens page");
        waitForPageLoad();
    }

    public void navigateToDexGuruTokensPage() {
        driver.get("https://dex.guru/tokens");
        log.info("Navigated to dex.guru tokens page");
        waitForPageLoad();
    }

    private void waitForPageLoad() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(tokenMarquee));
            log.info("Page loaded successfully");
        } catch (Exception e) {
            log.warn("Timeout waiting for page to load, continuing anyway: {}", e.getMessage());
        }
    }

    public Map<String, Boolean> compareComponentsWithDexGuru() {
        String currentUrl = driver.getCurrentUrl();

        log.info("Capturing components from app-guru-network-mono");
        Map<String, Boolean> componentsPresent = captureComponentsPresence();

        navigateToDexGuruTokensPage();
        log.info("Capturing components from dex.guru");
        Map<String, Boolean> dexGuruComponents = captureComponentsPresence();

        Map<String, Boolean> comparisonResult = new HashMap<>();

        for (String component : componentsPresent.keySet()) {
            boolean isMatching = componentsPresent.get(component) && dexGuruComponents.getOrDefault(component, false);
            comparisonResult.put(component, isMatching);
            log.info("Component '{}' match: {}", component, isMatching);
        }

        driver.get(currentUrl);

        return comparisonResult;
    }

    private Map<String, Boolean> captureComponentsPresence() {
        Map<String, Boolean> components = new HashMap<>();

        components.put("searchInput", isElementPresent(searchInput));
        components.put("tokenMarquee", isElementPresent(tokenMarquee));
        components.put("tokensList", isElementPresent(tokensList));
        components.put("tokenAssetsItems", isElementPresent(tokenAssetsItems));
        components.put("navigationLinks", isElementPresent(navigationLinks));
        components.put("categoryTabs", isElementPresent(categoryTabs));
        components.put("tokenTagsCloud", isElementPresent(tokenTagsCloud));
        components.put("tokenTagButtons", isElementPresent(tokenTagButtons));
        components.put("deltaValues", isElementPresent(deltaValues));
        components.put("footerNavigation", isElementPresent(footerNavigation));

        int tokenItemCount = driver.findElements(tokenAssetsItems).size();
        log.info("Number of token items found: {}", tokenItemCount);
        components.put("hasMultipleTokens", tokenItemCount > 5);

        return components;
    }

    private boolean isElementPresent(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            boolean result = !elements.isEmpty();
            log.info("Element with locator '{}' present: {}", locator, result);
            return result;
        } catch (Exception e) {
            log.error("Error checking if element is present: {}", e.getMessage());
            return false;
        }
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