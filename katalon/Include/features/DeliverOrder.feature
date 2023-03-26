Feature: Deliver Order
  As a user with an order 
  I want to be able to see order details and the route
  So that I can deliver the order correctly

  Scenario: Deliver order with no problems
    Given that the user is logged in
    When the user is delivering an order
    And taps the button concluir
    Then the order is finished

  Scenario: Cancel order
    Given that the user is logged in
    When the user is delivering an order
    And taps the button cancelar
    And gives a reason 
    And taps ok button
    Then the order is canceled

  Scenario: Check Order Details
    Given that the user is logged in
    When the user is delivering an order
    And taps the button detalhes
    Then the app opens the order details

  Scenario: Tracking directions
    Given that the user is logged in
    When the user is delivering an order
    And taps the button directions
    Then the app opens the order directions
