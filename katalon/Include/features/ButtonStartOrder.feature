Feature: Start Order
  As a user
  I want to start an order
  So that i can see the map

  Scenario: Redirect from Dashboard to Map page
    Given that the user is logged in
    When I click on button start
    Then I open the map
