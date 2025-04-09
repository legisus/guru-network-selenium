Feature: Smoke Functionality with signed in user
  As a user
  I want to be able to log in to the application
  Check main functionality


  Background:
    And I am on the home page

  Scenario: Successful login with valid credentials
    #1
    When I click on signIn button
    And Verify that popup appeared
    And Click on logIn with Telegram button
    And Enter phone '4244629816' for login with Telegram
    Then Verify profile uploaded successfully
    #2
    When I navigate to tokens page
    Then Check that it has same components like on dex guru tokens page
    #3
    When I navigate to analytics page
    And Click on assistant
    Then Guru AI was opened
    #4
    When I click on button 'Give me a summary of this data' for Guru AI
    Then I check that Guru AI contribute any response

  Scenario Outline: Verify pages load without errors
#    When I navigate to "<pageName>" page
    Then The page should load without errors

    Examples:
      | pageName     |
      | Actions      |
      | Guru AI      |
      | Analytics    |
      | Tokens       |
      | Swap         |
      | Leaderboards |
      | About        |