Feature: Smoke Functionality
  As a user
  Check main functionality before login


  Background:
    And I am on the home page

  Scenario Outline: Verify pages load without errors
    When I navigate to "<pageName>" page as guest
    Then The page should load without errors

    Examples:
      | pageName     |
      | Actions      |
      | Guru AI      |
      | Analytics    |
      | Tokens       |
      | Swap         |
      | Leaderboard  |
      | About        |