package com.guru.selenium.pages;

import com.guru.selenium.utils.Navigator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@Slf4j
public class GuruAIAgentsPage extends BasePage {
    private final By agentsContainer = By.cssSelector(".content_body__1Ac9z");
    private final By agentCards = By.cssSelector(".AgentCard_container__FdMw4");
    private final By pageTitle = By.xpath("//div[contains(@class, 'layout_header__Aaszh')]//span[contains(text(), 'Guru AI')]");
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
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed();
    }

    public List<String> getAvailableAgentNames() {
        List<WebElement> titles = driver.findElements(agentTitles);
        return titles.stream().map(WebElement::getText).toList();
    }
}