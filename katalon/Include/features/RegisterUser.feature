Feature: Register in app
  As a user
  I want to have an account in Fastuga
  So that I can access it

 
 Scenario: Register an account without first name
		Given the user has the app open
		When user fills his information 
 		And doesn't fill his first name
 		Then taps the register button
		And first name field receives the error "Preencha o seu nome"

Scenario: Register an account without last name
 		Given the user has the app open
 		When user fills his information 
 		And doesn't fill his last name
 		Then taps the register button
 		And last name field receives the error "Preencha o seu último nome"
 		
Scenario: Register an account without email
 		Given the user has the app open
 		When user fills his information 
 		And doesn't fill his email
 		Then taps the register button
 		And email field receives the error "Preencha com o seu email"
 		
Scenario: Register an account without password
 		Given the user has the app open
 		When user fills his information 
 		And doesn't fill his password
 		Then taps the register button
 		And password field receives the error "Preencha com a sua password"
 		
Scenario: Register an account without confirming password
 		Given the user has the app open
 		When user fills his information 
 		And doesn't fill his confirm password
 		Then taps the register button
 		And confirm password field receives the error "Confirme a sua password"
 		
Scenario: Register an account without phone number
 		Given the user has the app open
 		When user fills his information 
 		And doesn't fill his phone number
 		Then taps the register button
 		And phone number field receives the error "Preencha com o seu número de telefone"
 		
Scenario: Register an account without license plate
 		Given the user has the app open
 		When user fills his information 
 		And doesn't fill his license plate
 		Then taps the register button
 		And license plate field receives the error "Preencha com a sua matrícula"
 		
Scenario: Register an account with a non unique email
		Given the user has the app open
		When user fills his information
		And the email is not unique
		Then taps the register button
		And email field receives the error "Já existe um user com este email"
    
Scenario: Register an account with a invalid email
		Given the user has the app open
		When user fills his information
		And the email is not valid
		Then taps the register button
		And email field receives the error "Email inválido"
		
Scenario: Register an account where passwords don't match
		Given the user has the app open
		When user fills his information
		And the confirmation password does not match with password
		Then taps the register button
		And confirm password field receives the error "Passwords não coincidem"
		
Scenario: Register an account when license plate is not unique
		Given the user has the app open
		When user fills his information
		And the license plate is not unique
		Then taps the register button
		And license plate field receives the error "Já existe um user com esta matrícula"


Scenario: Register an account with invalid license plate
		Given the user has the app open
		When user fills his information
		And the license plate is invalid
		Then taps the register button
 		And license plate field receives the error "Formato Inválido E.g: A1-3B-2C"

Scenario: Register an account with unique phone number
		Given the user has the app open 
		When user fills his information
		And the phone number is not unique
		Then taps the register button
		And phone number field receives the error "Já existe um user com este número de telefone"

Scenario: Register an account with valid inputs
	  Given the user has the app open
	  When user fills his information correctly
	  Then taps the register button
	  And the app shows the dashboard