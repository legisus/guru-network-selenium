package com.guru.selenium.pages;

import com.guru.selenium.utils.Navigator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Slf4j
public class SwapPage extends BasePage {
    private final By swapContainer = By.cssSelector(".content_body__1Ac9z");
    private final By swapForm = By.cssSelector(".Swap_container__GUZeN");
    private final By tokenInputFrom = By.cssSelector(".TokenSelector_selector__s6EeG");
    private final By tokenInputTo = By.xpath("(//div[contains(@class, 'TokenSelector_selector__s6EeG')])[2]");
    private final By swapButton = By.xpath("//button[contains(@class, 'Button_button') and .//span[contains(text(), 'Swap')]]");

    private final By swapMenuGuest = By.xpath("//span[contains(text(), 'Welcome')]");

    private final Navigator navigator;

    public SwapPage() {
        super();
        this.navigator = new Navigator();
        log.info("SwapPage initialized");
    }

    public void navigateToSwapPage() {
        log.info("Navigating to Swap page");
        navigator.navigateViaMenu("Swap");
        waitForElementToBeVisible(swapContainer);
    }

    public boolean isSwapPageLoaded(boolean isGuest) {

        if (isGuest) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(swapMenuGuest)).isDisplayed();
        } else {
            try {
                boolean isSwapContainerVisible = isElementDisplayed(swapContainer);
                boolean isSwapFormVisible = isElementDisplayed(swapForm);
                boolean isFromInputVisible = isElementDisplayed(tokenInputFrom);
                boolean isToInputVisible = isElementDisplayed(tokenInputTo);
                boolean isSwapButtonVisible = isElementDisplayed(swapButton);

                log.info("Swap page loaded: container: {}, form: {}, from input: {}, to input: {}, swap button: {}",
                        isSwapContainerVisible, isSwapFormVisible, isFromInputVisible, isToInputVisible, isSwapButtonVisible);

                return isSwapContainerVisible && isSwapFormVisible &&
                        isFromInputVisible && isToInputVisible && isSwapButtonVisible;
            } catch (Exception e) {
                log.error("Error checking if Swap page is loaded: {}", e.getMessage());
                return false;
            }
        }
    }
}