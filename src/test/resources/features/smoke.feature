Feature: Smoke Functionality
  As a user
  I want to be able to log in to the application
  Check main functionality


  Background:
    And I am on the home page

  Scenario: Successful login with valid credentials
    When I click on signIn button
    And Verify that popup appeared
    And Click on logIn with Telegram button
    And Enter phone "your_phone_here" for login with Telegram
    Then Verify profile uploaded successfully
