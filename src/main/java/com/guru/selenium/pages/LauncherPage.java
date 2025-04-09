package com.guru.selenium.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Slf4j
public class LauncherPage extends BasePage{
    private final By launcherMenuGuest = By.xpath("//span[contains(text(), 'Welcome')]");

    public boolean isLauncherPageLoaded(boolean isGuest) {
        if (isGuest) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(launcherMenuGuest)).isDisplayed();
        } else {
            return false; //TODO
        }
    }

}
