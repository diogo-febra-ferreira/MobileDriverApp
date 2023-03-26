Feature: Login in app
  As a user
  I already have an account in Fastuga
  So I want to sign in

  Scenario: Log in with empty credentials
    Given the user has the app open on the login page
    When the user does not fill his login credentials
    Then taps the login button
    And email login field receives the error "Deve preencher o email"
    And password login field receives the error "Deve preencher a password"

  Scenario: Log in with an email that doesn't have an account
    Given the user has the app open on the login page
    When the user fills his login credentials
    And the email does not exist
    Then taps the login button
    And email login field receives the error "Não existe conta com este email"

  Scenario: Log in with an invalid password
    Given the user has the app open on the login page
    When the user fills his login credentials
    And the password is invalid
    Then taps the login button
    And password login field receives the error "password Inválida"
    
  Scenario: Log in with valid credentials
    Given the user has the app open on the login page
    When the user fills his login credentials
    Then taps the login button
	  And the app shows the dashboard
	  
	Scenario: Want to create new account
		Given the user has the app open on the login page
		When the user taps the create new account button
		Then the app opens the register page
