Feature: Smoke Functionality
  As a user
  Check main functionality before login

  Background:
    Given I am on the home page

  Scenario Outline: Verify pages load without errors
    When I navigate to "<pageName>" page as guest using side menu
    Then The page should load without errors

    Examples:
      | pageName     |
      | Actions      |
      | Guru AI      |
      | Analytics    |
      | Tokens       |
      | Swap         |
      | About        |
      | Leaderboard  |