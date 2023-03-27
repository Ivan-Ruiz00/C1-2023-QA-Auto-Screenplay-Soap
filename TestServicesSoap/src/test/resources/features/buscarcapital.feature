Feature: Get the actual capital
  AS user
  I WANT TO look up for a country
  SO THAT I can I see capital

  @capital
  Scenario: List capital
    Given a user that wants to know the actual capital
    When the user sends the request to the api
    Then the user gets the capital