Feature: Account Balance
	As a user logged in the app
	I want to be able to see my balance
	

  Scenario: Balance updates after the user delivers an order
    Given that the user is logged in
    When the user finishes a delivery
    Then his account balance goes up
    
  Scenario: Check Account Balance
  	Given that the user is logged in
  	When the user taps the account button
  	Then checks his account balance