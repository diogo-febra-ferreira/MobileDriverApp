Feature: Notifications
  As a user that is logged in
  I want to see notifications about the orders I am assigned to and new orders available
  So that I can be informed about the status of orders

  Scenario: User checks his notifications
    Given that the user is logged in
    When the user taps the notifications button
    Then the notifications page opens
