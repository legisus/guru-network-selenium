Feature: Smoke Functionality
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
    And Enter phone "4244629816" for login with Telegram
    Then Verify profile uploaded successfully
    #2
    When I navigate to tokens page
    Then Check that it has same components like on dex guru tokens page
    #3
    When I navigate to analytics page
    And Click on assistant
    Then Guru AI was opened
    #4
#    When I enter request "make me analytics by dashboard" for Guru AI












