Feature: Statistics
	As a user that has an account
	I want to see the statistics of my account and deliveries
	So that I can know my respective statistics

  Scenario: User checks his statistics
    Given that the user is logged in
    When the user has his account page open
    And taps the statistics button
    Then he can see his statistics
