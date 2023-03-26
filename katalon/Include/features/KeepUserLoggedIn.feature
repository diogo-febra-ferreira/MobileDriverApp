Feature: Keep user logged in
  As a user that is logged in
  I want to be able to kept logged in even if I close the app
  So that when I reopen the app I don't need to log in again.
  


  Scenario: User checks the keep me logged in checkbox
    Given that the user checks the logged in checkbox as he logs in
    When the user closes the app
    And opens it again
    Then the user is logged in
