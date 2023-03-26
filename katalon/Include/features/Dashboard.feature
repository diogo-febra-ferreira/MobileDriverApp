Feature: Dashboard
  As a user logged in an account
	I want to be able to see available orders, my orders, to assign myself a order, and to start a delivery.
	So that I can start to deliver a order.

  Scenario: See available orders
    Given that the user is logged in
    Then sees the available orders
    
 Scenario: Assign an order
 		Given that the user is logged in
 		When the user sees the available orders
 		And taps the assign button
 		Then the order shows on My Orders
 		
	Scenario: Unassign an order
		Given that the user is logged in
 		When the user sees my orders
 		And taps the unassign button
 		Then the order shows on Available Orders
 		
 	Scenario: Order orders by closest
 		Given that the user is logged in
 		When the user sees the available orders
 		And taps the order switch
 		And taps the closest orders option
 		Then the orders are ordered by closest
	
	 Scenario: Order orders by farthest
 		Given that the user is logged in
 		When the user sees the available orders
 		And taps the order switch
 		And taps the farthest orders option
 		Then the orders are ordered by farthest
 		
 Scenario: See My orders
    Given that the user is logged in
    Then sees my orders
    
Scenario: Access user page
		Given that the user is logged in
		When the user taps the account button
		Then opens his account page