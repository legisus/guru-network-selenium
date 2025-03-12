package com.guru.selenium.pages;

import com.guru.selenium.utils.DriverFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class GuruAIPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By guruAiContainer = By.id("page-aichat");
    private final By chatPromptButtons = By.cssSelector("button.AIChat_prompt__WYQFV");
    private final By chatInput = By.cssSelector("textarea[name='message']");
    private final By submitButton = By.cssSelector("button.AIChat_submit__ciifR");
    private final By chatMessages = By.cssSelector(".AIChat_list__1KKWq li");
    private final By aiResponses = By.cssSelector(".AIChatMessage_answer__LLofQ");
    private final By messageBodies = By.cssSelector(".Text_container__s3zN4 p");
    private final By loadingIndicator = By.cssSelector(".AIChat_service__piLWs");
    private final By guruAiOpenClass = By.cssSelector(".aichat_open___aIT5");

    private static final String SHRUG_EMOTICON = "¯_(ツ)_/¯";
    private static final String AGENT_FAILED = "AGENT_FAILED";

    public GuruAIPage() {
        this.driver = DriverFactory.getInstance().getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        log.info("GuruAIPage initialized");
    }

    /**
     * Verify if Guru AI chat is open
     * @return true if chat is open
     */
    public boolean isGuruAIOpen() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(guruAiContainer)).isDisplayed();
        } catch (Exception e) {
            log.error("Error checking if Guru AI is open: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Click on a specific prompt button by text
     * @param buttonText The text of the button to click
     * @return true if button was found and clicked
     */
    public boolean clickPromptButton(String buttonText) {
        log.info("Attempting to click on prompt button: '{}'", buttonText);

        wait.until(ExpectedConditions.visibilityOfElementLocated(chatPromptButtons));

        List<WebElement> buttons = driver.findElements(chatPromptButtons);
        log.info("Found {} prompt buttons", buttons.size());

        switch (buttonText.toLowerCase()) {
            case "generate a concise and engaging twitter post":
            case "twitter post":
                return clickButtonByIndex(buttons, 0, "Twitter post");

            case "give me a summary of this data":
            case "summary":
                boolean clickResult = clickButtonByIndex(buttons, 1, "Summary");
                waitForResponse(5);
                return clickResult;

            case "identify bullish or bearish trends":
            case "trends":
                return clickButtonByIndex(buttons, 2, "Trends");

            default:
                for (WebElement button : buttons) {
                    String actualText = button.findElement(By.cssSelector("span.Button_caption__baPq2")).getText();
                    if (actualText.equalsIgnoreCase(buttonText)) {
                        button.click();
                        log.info("Clicked on button with text: '{}'", actualText);
                        return true;
                    }
                }

                log.error("Button with text '{}' not found", buttonText);
                return false;
        }
    }

    /**
     * Click button by index with safety checks
     * @param buttons List of buttons
     * @param index Index to click
     * @param buttonName Name for logging
     * @return true if successful
     */
    private boolean clickButtonByIndex(List<WebElement> buttons, int index, String buttonName) {
        if (buttons.size() > index) {
            WebElement button = buttons.get(index);
            String actualText = button.findElement(By.cssSelector("span.Button_caption__baPq2")).getText();
            button.click();
            log.info("Clicked on {} button with text: '{}'", buttonName, actualText);
            return true;
        } else {
            log.error("{} button not found (index {})", buttonName, index);
            return false;
        }
    }

    /**
     * Enter text in chat input
     * @param text Text to enter
     */
    public void enterChatInput(String text) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(chatInput));
        input.clear();
        input.sendKeys(text);
        log.info("Entered text in chat input: '{}'", text);
    }

    /**
     * Submit the chat input
     */
    public void submitChat() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        button.click();
        log.info("Submitted chat input");
    }

    /**
     * Wait for AI response
     * @param timeoutSeconds Seconds to wait
     * @return true if new message appeared
     */
    public boolean waitForResponse(int timeoutSeconds) {
        try {
            int initialCount = driver.findElements(chatMessages).size();
            log.info("Current message count: {}", initialCount);

            WebDriverWait responseWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

            boolean received = responseWait.until(driver ->
                    driver.findElements(chatMessages).size() > initialCount);

            if (received) {
                log.info("New response received");
            } else {
                log.warn("No new response received after {} seconds", timeoutSeconds);
            }

            return received;
        } catch (Exception e) {
            log.error("Error waiting for response: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if Guru AI contributed a proper response to the latest message
     * @return true if a meaningful response is present
     */
    public boolean hasProperResponse() {
        try {
            log.info("Checking if Guru AI provided a proper response");

            waitForLoadingToComplete();

            List<WebElement> allMessages = driver.findElements(chatMessages);
            if (allMessages.isEmpty()) {
                log.warn("No messages found in the chat");
                return false;
            }

            WebElement lastMessage = allMessages.get(allMessages.size() - 1);

            boolean isAiResponse = !lastMessage.findElements(By.cssSelector(".AIChatMessage_answer__LLofQ")).isEmpty();
            if (!isAiResponse) {
                log.warn("Last message is not an AI response");
                return false;
            }

            String responseText = getMessageText(lastMessage);
            log.info("Last AI response: '{}'", responseText);

            if (isErrorResponse(responseText)) {
                log.warn("AI response contains error indicators");
                return false;
            }

            log.info("AI provided a proper response");
            return true;

        } catch (Exception e) {
            log.error("Error checking for AI response: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Wait for the loading indicator to disappear
     */
    private void waitForLoadingToComplete() {
        try {
            if (!driver.findElements(loadingIndicator).isEmpty()) {
                wait.until(driver -> {
                    WebElement loader = driver.findElement(loadingIndicator);
                    return loader.getText().trim().isEmpty();
                });
            }
        } catch (Exception e) {
            log.warn("Issue waiting for loading to complete: {}", e.getMessage());
        }
    }

    /**
     * Get text content from a message element
     * @param messageElement The message element
     * @return Text content
     */
    private String getMessageText(WebElement messageElement) {
        StringBuilder text = new StringBuilder();
        List<WebElement> paragraphs = messageElement.findElements(messageBodies);

        for (WebElement paragraph : paragraphs) {
            if (!text.isEmpty()) {
                text.append("\n");
            }
            text.append(paragraph.getText().trim());
        }

        return text.toString();
    }

    /**
     * Check if response text indicates an error
     * @param text Response text
     * @return true if it's an error response
     */
    private boolean isErrorResponse(String text) {
        if (text == null || text.isEmpty()) {
            return true;
        }

        return text.contains(SHRUG_EMOTICON) ||
                text.contains(AGENT_FAILED) ||
                text.trim().length() < 5;
    }

    /**
     * Get all AI responses as a list
     * @return List of response texts
     */
    public List<String> getAllResponses() {
        List<WebElement> responses = driver.findElements(aiResponses);
        return responses.stream()
                .map(this::getMessageText)
                .collect(Collectors.toList());
    }
}