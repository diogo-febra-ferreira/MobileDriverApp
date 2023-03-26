Feature: Opt out from app
  As a user that has an account
	I want to disable my account
	So that I can’t access it anymore


  Scenario: User opts out
    Given that the user is logged in
    When the user has his account page open
    And taps the delete button in his account page
    And taps ok option in confirmation pop up
    Then his account is deleted and he is logged out
