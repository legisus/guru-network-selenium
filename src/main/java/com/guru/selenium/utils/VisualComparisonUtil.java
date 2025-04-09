package com.guru.selenium.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to help with visual comparison between two websites
 */
@Slf4j
public class VisualComparisonUtil {

    /**
     * Analyzes common UI elements between two URLs
     * This can be used to identify matching selectors across different sites
     */
    public static Map<String, Object> compareWebsites(WebDriver driver,
                                                      String firstUrl,
                                                      String secondUrl) {
        Map<String, Object> report = new HashMap<>();
        Map<String, Object> similarities = new HashMap<>();
        Map<String, Object> differences = new HashMap<>();

        try {
            driver.get(firstUrl);
            waitForPageLoad(driver);
            Map<String, Object> firstSiteData = collectSiteData(driver);

            driver.get(secondUrl);
            waitForPageLoad(driver);
            Map<String, Object> secondSiteData = collectSiteData(driver);

            if (firstSiteData.containsKey("searchInput") && secondSiteData.containsKey("searchInput")) {
                similarities.put("searchInput", true);
            } else {
                differences.put("searchInput", false);
            }

            if (firstSiteData.containsKey("tokenElements") && secondSiteData.containsKey("tokenElements")) {
                int firstCount = (int) firstSiteData.get("tokenCount");
                int secondCount = (int) secondSiteData.get("tokenCount");

                similarities.put("hasTokenList", true);
                similarities.put("tokenCountFirst", firstCount);
                similarities.put("tokenCountSecond", secondCount);

                if (firstCount > 5 && secondCount > 5) {
                    similarities.put("hasMultipleTokens", true);
                }
            } else {
                differences.put("hasTokenList", false);
            }

            if (firstSiteData.containsKey("navElements") && secondSiteData.containsKey("navElements")) {
                similarities.put("hasNavigation", true);
            } else {
                differences.put("hasNavigation", false);
            }

            if (firstSiteData.containsKey("footerElements") && secondSiteData.containsKey("footerElements")) {
                similarities.put("hasFooter", true);
            } else {
                differences.put("hasFooter", false);
            }

            report.put("similarities", similarities);
            report.put("differences", differences);

            double totalChecks = similarities.size() + differences.size();
            double matchCount = similarities.size();
            double similarityScore = (matchCount / totalChecks) * 100;

            report.put("similarityScore", similarityScore);
            report.put("recommendation", similarityScore >= 70 ? "PASS" : "FAIL");

            log.info("Comparison complete. Similarity score: {}%", similarityScore);

        } catch (Exception e) {
            log.error("Error during website comparison: {}", e.getMessage());
            report.put("error", e.getMessage());
        }

        return report;
    }

    private static Map<String, Object> collectSiteData(WebDriver driver) {
        Map<String, Object> data = new HashMap<>();

        List<WebElement> searchInputs = driver.findElements(By.cssSelector("input[type='search'], input[placeholder*='Search']"));
        if (!searchInputs.isEmpty()) {
            data.put("searchInput", searchInputs.get(0));
        }

        List<WebElement> tokenElements = driver.findElements(By.cssSelector(
                ".TokenAsset_container__q260a, .token-item, .TokenItem, .Marquee_item__6sQZ_, [class*='token']"));
        if (!tokenElements.isEmpty()) {
            data.put("tokenElements", tokenElements);
            data.put("tokenCount", tokenElements.size());
        }

        List<WebElement> navElements = driver.findElements(By.cssSelector(
                ".MainMenu_container__Uuptt, .navigation, .nav, [class*='menu']"));
        if (!navElements.isEmpty()) {
            data.put("navElements", navElements);
        }

        List<WebElement> footerElements = driver.findElements(By.cssSelector(
                ".layout_footer__Koz5Z, footer, .footer"));
        if (!footerElements.isEmpty()) {
            data.put("footerElements", footerElements);
        }

        return data;
    }

    static void waitForPageLoad(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("return document.readyState").equals("complete");

            Thread.sleep(2000);
        } catch (Exception e) {
            log.warn("Error waiting for page load: {}", e.getMessage());
        }
    }

    /**
     * Suggest CSS selectors that might be useful for comparison
     */
    public static void suggestSelectors(WebDriver driver, String url) {
        try {
            driver.get(url);
            waitForPageLoad(driver);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            String script =
                    "const suggestions = [];" +
                            "document.querySelectorAll('[class]').forEach(el => {" +
                            "  if (el.className && typeof el.className === 'string') {" +
                            "    const classes = el.className.split(' ');" +
                            "    classes.filter(c => c.includes('token') || c.includes('list') || c.includes('card') || c.includes('menu') || c.includes('search'))" +
                            "      .forEach(cls => suggestions.push({'selector': '.' + cls, 'element': el.tagName}));" +
                            "  }" +
                            "});" +
                            "return suggestions.slice(0, 20);";

            List<Map<String, Object>> suggestions = (List<Map<String, Object>>) js.executeScript(script);

            log.info("SUGGESTED SELECTORS FOR: {}", url);
            for (Map<String, Object> suggestion : suggestions) {
                log.info("{}: {}", suggestion.get("selector"), suggestion.get("element"));
            }

        } catch (Exception e) {
            log.error("Error getting selector suggestions: {}", e.getMessage());
        }
    }
}