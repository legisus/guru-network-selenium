package com.guru.selenium.pages;

import com.guru.selenium.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Slf4j
public class LoginPage extends BasePage {
    @FindBy(xpath = "//div[contains(@class, 'login_number_field_wrap')]//input[@id='login-phone']")
    private WebElement phoneField;

    @FindBy(xpath = "//span[@class='Button_caption__baPq2' and text()='Sign In']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[contains(@class, 'Dialog_container__sZ9Tc')]")
    private WebElement popup;

    @FindBy(xpath = "//button[@class='btn tgme_widget_login_button' and contains(text(), 'Log in with Telegram')]")
    private WebElement loginWithTelegramButton;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement nextButton;

    @FindBy(xpath = "//iframe[@id='telegram-login-guru_network_stage_bot']")
    private WebElement telegramLoginIframe;

    private final String pageUrl;

    public LoginPage() {
        super();
        Configuration config = Configuration.getInstance();
        this.pageUrl = config.getProperty("base.url");
        log.info("LoginPage initialized with URL: {}", pageUrl);
    }

    public void navigateToLoginPage() {
        log.info("Navigating to login page");
        navigateTo(pageUrl);
    }

    public void waitUntilPopupIsLoaded(long timeoutSeconds) {
        waitForElementPresence(popup, timeoutSeconds);
    }

    public boolean isOnSignInPopup() {
        log.info("Checking if on signIn popup");
        waitForElementPresence(popup, 5);
        return isElementDisplayed(popup);
    }

    public void clickLoginButton() {
        log.info("Clicking login button");
        waitForElementToBeClickable(loginButton);
        clickElement(loginButton);
        log.info("Login button clicked");
    }

    public void clickLoginWithTelegramButton() {
        log.info("Clicking on Login with Telegram button");

        int initialWindowCount = driver.getWindowHandles().size();
        log.info("Initial window count: {}", initialWindowCount);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(telegramLoginIframe));
        driver.switchTo().frame(telegramLoginIframe);

        try {
            WebElement loginButton = null;
            try {
                loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".login_telegram_photo_wrap")));
                log.info("Found Telegram login photo/icon");
            } catch (Exception e) {
                log.warn("Could not find Telegram login photo, looking for buttons");
                try {
                    loginButton = driver.findElement(By.cssSelector(".tgme_widget_login_button"));
                } catch (Exception e2) {
                    log.warn("Could not find tgme button, looking for any login-related element");
                    try {
                        List<WebElement> buttons = driver.findElements(By.tagName("button"));
                        for (WebElement button : buttons) {
                            if (button.getText().toLowerCase().contains("log") ||
                                    button.getText().toLowerCase().contains("sign")) {
                                loginButton = button;
                                break;
                            }
                        }
                    } catch (Exception e3) {
                        log.error("Could not find any login button");
                    }
                }
            }

            if (loginButton != null) {
                log.info("Clicking login trigger element");
                loginButton.click();
            } else {
                log.info("No login button found, the iframe might already be showing the phone form");
            }
        } finally {
            driver.switchTo().defaultContent();
        }

        try {
            wait.until(driver -> driver.getWindowHandles().size() > initialWindowCount);
            log.info("New window detected after login button click");
        } catch (Exception e) {
            log.info("No new window opened, continuing with iframe interaction");
        }
    }

    public void switchToNewWindowAndEnterPhone(String phone) {
        log.info("Switching to new window and entering phone: {}", phone);

        String originalWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        if (allWindows.size() <= 1) {
            log.warn("No new windows detected, staying in current window");
            return;
        }

        String newWindowHandle = null;
        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(originalWindow)) {
                newWindowHandle = windowHandle;
                break;
            }
        }

        if (newWindowHandle == null) {
            log.error("Could not find new window handle");
            return;
        }

        driver.switchTo().window(newWindowHandle);
        log.info("Switched to new window: {}", driver.getTitle());

        try {
            Thread.sleep(2000);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement phoneInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("input#login-phone")));

            log.info("Found phone input, entering number");
            phoneInput.click();
            phoneInput.clear();
            phoneInput.sendKeys(phone);

            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.button-item[type='submit']")));

            log.info("Found Next button, clicking");
            nextButton.click();

            log.info("Please complete the manual verification in Telegram app");

            try {
                WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));
                WebElement acceptButton = longWait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[.//span[contains(text(), 'Accept')]]")));

                log.info("Accept button found, clicking");
                acceptButton.click();

                Thread.sleep(3000);

            } catch (Exception e) {
                log.warn("Could not find or click Accept button: {}", e.getMessage());
            }

        } catch (Exception e) {
            log.error("Error interacting with new window: {}", e.getMessage());
        } finally {
            try {
                try {
                    String currentTitle = driver.getTitle();
                    log.info("Window is still open with title: {}", currentTitle);

                    driver.close();
                    log.info("Closed the authentication window");
                } catch (Exception e) {
                    log.info("Authentication window appears to have closed itself");
                }

                driver.switchTo().window(originalWindow);
                log.info("Switched back to original window");

            } catch (Exception e) {
                log.error("Error handling window switching: {}", e.getMessage());
            }
        }
    }


}