package com.guru.selenium.pages;

import com.guru.selenium.utils.Navigator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Slf4j
public class TasksPage extends BasePage {
    private final By tasksContainer = By.cssSelector(".content_body__1Ac9z");
    private final By taskCards = By.cssSelector(".TaskCard_container__RafuH");
    private final By pageTitle = By.xpath("//div[contains(@class, 'page_header__vyaGx')]//span[text()='Actions']");

    private final Navigator navigator;

    public TasksPage() {
        super();
        this.navigator = new Navigator();
        log.info("TasksPage initialized");
    }

    public void navigateToTasksPage() {
        log.info("Navigating to Tasks page");
        navigator.navigateViaMenu("Tasks");
        waitForElementToBeVisible(tasksContainer);
    }

    public boolean isTasksPageLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed();
    }
}