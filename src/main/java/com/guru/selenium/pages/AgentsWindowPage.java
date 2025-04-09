package com.guru.selenium.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AgentsWindowPage extends BasePage {
    // Locators
    private final By guruAiContainer = By.id("page-aichat");
    private final By chatPromptButtons = By.cssSelector("button.AIChat_prompt__WYQFV");
    private final By chatInput = By.cssSelector("textarea[name='message']");
    private final By submitButton = By.cssSelector("button.AIChat_submit__ciifR");
    private final By chatMessages = By.cssSelector(".AIChat_list__1KKWq li");
    private final By aiResponses = By.cssSelector(".AIChatMessage_answer__LLofQ");
    private final By messageBodies = By.cssSelector(".Text_container__s3zN4 p");
    private final By loadingIndicator = By.cssSelector(".AIChat_service__piLWs");
    private final By guruAiOpenClass = By.cssSelector(".aichat_open___aIT5");

    // Error response indicators
    private static final String SHRUG_EMOTICON = "¯_(ツ)_/¯";
    private static final String AGENT_FAILED = "AGENT_FAILED";

    public AgentsWindowPage() {
        super();
        log.info("AgentsWindowPage initialized");
    }

    /**
     * Verify if Guru AI chat is open
     * @return true if chat is open
     */
    public boolean isAgentWindowOpen() {
        log.info("Checking if Guru AI is open");
        try {
            WebElement container = waitForElementToBeVisible(guruAiContainer);
            return container != null && container.isDisplayed();
        } catch (Exception e) {
            log.error("Error checking if Guru AI is open: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Click on a specific prompt button by text and wait 30 seconds
     * @param buttonText The text of the button to click
     * @return true if button was found and clicked
     */
    public boolean clickPromptButton(String buttonText) {
        log.info("Attempting to click on prompt button: '{}'", buttonText);

        // Wait for any prompt button to be visible
        waitForElementToBeVisible(chatPromptButtons);

        List<WebElement> buttons = driver.findElements(chatPromptButtons);
        log.info("Found {} prompt buttons", buttons.size());

        boolean clickSuccess = false;

        switch (buttonText.toLowerCase()) {
            case "generate a concise and engaging twitter post":
            case "twitter post":
                clickSuccess = clickButtonByIndex(buttons, 0, "Twitter post");
                break;

            case "give me a summary of this data":
            case "summary":
                clickSuccess = clickButtonByIndex(buttons, 1, "Summary");
                break;

            case "identify bullish or bearish trends":
            case "trends":
                clickSuccess = clickButtonByIndex(buttons, 2, "Trends");
                break;

            default:
                for (WebElement button : buttons) {
                    String actualText = button.findElement(By.cssSelector("span.Button_caption__baPq2")).getText();
                    if (actualText.equalsIgnoreCase(buttonText)) {
                        clickElement(button);
                        log.info("Clicked on button with text: '{}'", actualText);
                        clickSuccess = true;
                        break;
                    }
                }

                if (!clickSuccess) {
                    log.error("Button with text '{}' not found", buttonText);
                    return false;
                }
        }

        if (clickSuccess) {
            log.info("Waiting 30 seconds after clicking '{}'", buttonText);

            // Record start time
            long startTime = System.currentTimeMillis();

            // Try to wait for loading indicator and then response
            try {
                // Wait for loading indicator to disappear
                waitForLoadingToComplete(loadingIndicator, 15000);

                // Try to detect new messages
                waitForResponse(10);
            } catch (Exception e) {
                log.debug("Exception while waiting for response: {}", e.getMessage());
            }

            // Calculate elapsed time
            long elapsedTime = System.currentTimeMillis() - startTime;
            long remainingTime = 30000 - elapsedTime;

            // If we've waited less than 30 seconds total, wait the remaining time
            if (remainingTime > 0) {
                try {
                    log.debug("Waiting additional {} ms to complete 30 second wait", remainingTime);
                    Thread.sleep(remainingTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("Wait interrupted: {}", e.getMessage());
                }
            }

            log.info("Completed 30 second wait after clicking '{}'", buttonText);
        }

        return clickSuccess;
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
            clickElement(button);
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
        log.info("Entering text in chat input: '{}'", text);
        WebElement input = waitForElementToBeClickable(chatInput);
        if (input != null) {
            input.clear();
            input.sendKeys(text);
            log.info("Text entered successfully");
        } else {
            log.error("Chat input field not found or not clickable");
        }
    }

    /**
     * Submit the chat input
     */
    public void submitChat() {
        log.info("Submitting chat input");
        clickElement(submitButton);
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

            // Use BasePage method for waiting for element count to increase
            boolean received = waitForElementCountToIncrease(chatMessages, initialCount, timeoutSeconds);

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
     * Check if Guru AI contributed a proper response
     * @return true if a meaningful response is present
     */
    public boolean hasProperResponse() {
        try {
            log.info("Checking if Guru AI provided a proper response");

            // Wait for loading to complete
            waitForLoadingToComplete(loadingIndicator, 30);

            // Get all AI responses
            List<String> responses = getAllResponses();
            log.info("Found {} AI responses in total", responses.size());

            // If we have any responses at all, check them
            if (!responses.isEmpty()) {
                // Check if at least one response is meaningful
                for (String response : responses) {
                    // Ignore null/empty responses
                    if (response == null || response.trim().isEmpty()) {
                        continue;
                    }

                    // Check if it's a substantial response (longer than 50 chars)
                    if (response.length() > 50) {
                        log.info("Found substantial AI response (length: {})", response.length());
                        return true;
                    }

                    // Check if it's not an error response and has reasonable content
                    if (!isErrorResponse(response) && response.length() > 10) {
                        log.info("Found meaningful AI response");
                        return true;
                    }
                }
            }

            log.warn("No meaningful AI responses found");
            return false;

        } catch (Exception e) {
            log.error("Error checking for AI response: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if response text indicates an error
     * @param text Response text
     * @return true if it's an error response
     */
    private boolean isErrorResponse(String text) {
        if (text == null || text.trim().isEmpty()) {
            return true;
        }

        // Check for known error indicators only
        return text.contains(SHRUG_EMOTICON) ||
                text.contains(AGENT_FAILED);
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