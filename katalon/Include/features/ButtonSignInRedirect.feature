Feature: Open Login Page
 	As a user
 	If I already have an account 
 	I want to login

  Scenario: Redirect from register to login page
    Given that the user has the app open
    When the user taps the sign in button
    Then the app opens the login page
