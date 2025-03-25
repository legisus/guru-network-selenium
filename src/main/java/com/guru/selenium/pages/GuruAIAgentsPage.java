package com.guru.selenium.pages;

import com.guru.selenium.utils.Navigator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@Slf4j
public class GuruAIAgentsPage extends BasePage {
    private final By agentsContainer = By.cssSelector(".content_body__1Ac9z");
    private final By agentCards = By.cssSelector(".AgentCard_container__FdMw4");
    private final By pageTitle = By.xpath("//h1[contains(text(), 'Guru AI')]");
    private final By agentTitles = By.cssSelector(".AgentCard_title__0JRGK");

    private final Navigator navigator;

    public GuruAIAgentsPage() {
        super();
        this.navigator = new Navigator();
        log.info("GuruAIAgentsPage initialized");
    }

    public void navigateToGuruAIAgentsPage() {
        log.info("Navigating to Guru AI Agents page");
        navigator.navigateViaMenu("AI Hub");
        waitForElementToBeVisible(agentsContainer);
    }

    public boolean isGuruAIAgentsPageLoaded() {
        try {
            boolean isTitleVisible = isElementDisplayed(pageTitle);
            boolean isAgentsContainerVisible = isElementDisplayed(agentsContainer);
            int agentCount = driver.findElements(agentCards).size();

            log.info("Guru AI Agents page loaded: title visible: {}, agents container visible: {}, agent count: {}",
                    isTitleVisible, isAgentsContainerVisible, agentCount);

            return isTitleVisible && isAgentsContainerVisible && agentCount > 0;
        } catch (Exception e) {
            log.error("Error checking if Guru AI Agents page is loaded: {}", e.getMessage());
            return false;
        }
    }

    public List<String> getAvailableAgentNames() {
        List<WebElement> titles = driver.findElements(agentTitles);
        return titles.stream().map(WebElement::getText).toList();
    }
}