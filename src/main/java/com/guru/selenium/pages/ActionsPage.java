package com.guru.selenium.pages;

import com.guru.selenium.utils.Navigator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@Slf4j
public class ActionsPage extends BasePage {
    private final By tasksContainer = By.cssSelector(".content_body__1Ac9z");
    private final By taskCards = By.cssSelector(".TaskCard_container__RafuH");
    private final By pageTitle = By.xpath("//h1[contains(text(), 'Actions')]");

    private final Navigator navigator;

    public ActionsPage() {
        super();
        this.navigator = new Navigator();
        log.info("ActionsPage initialized");
    }

    public void navigateToActionsPage() {
        log.info("Navigating to Actions page");
        navigator.navigateViaMenu("Actions");
        waitForElementToBeVisible(tasksContainer);
    }

    public boolean isActionsPageLoaded() {
        try {
            boolean isTitleVisible = isElementDisplayed(pageTitle);
            boolean isTasksContainerVisible = isElementDisplayed(tasksContainer);
            int taskCount = driver.findElements(taskCards).size();

            log.info("Actions page loaded: title visible: {}, tasks container visible: {}, task count: {}",
                    isTitleVisible, isTasksContainerVisible, taskCount);

            return isTitleVisible && isTasksContainerVisible;
        } catch (Exception e) {
            log.error("Error checking if Actions page is loaded: {}", e.getMessage());
            return false;
        }
    }
}