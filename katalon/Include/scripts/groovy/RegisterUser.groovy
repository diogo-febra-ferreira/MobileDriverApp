import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When



class RegisterUser {
	@Given("the user has the app open")
	public void the_user_has_the_app_open() {
		Mobile.startApplication('D:\\dev\\taes\\repos\\taes22_grupom_projeto\\app\\build\\outputs\\apk\\debug\\app-debug.apk', true)
	}

	@When("user fills his information")
	public void user_fills_his_information() {
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - First Name'), 'Manel', 0)

		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - LastName'), 'alves', 0)

		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Email'), 'alves@mail.pt', 0)

		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Password'), 'alves1', 0)

		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Confirm Password'), 'alves1', 0)

		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - PhoneNumber'), '918234837', 0)

		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Matricula'), 'KM-24-BC', 0)
	}

	@When("doesn't fill his first name")
	public void doesn_t_fill_his_first_name() {
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - First Name'), '', 0)
	}

	@Then("taps the register button")
	public void taps_the_register_button() {
		Mobile.tap(findTestObject('Object Repository/android.widget.Button - REGISTER'), 0)
	}


	@When("doesn't fill his last name")
	public void doesn_t_fill_his_last_name() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - LastName'), '', 0)
	}

	@When("doesn't fill his email")
	public void doesn_t_fill_his_email() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Email'), '', 0)
	}

	@When("doesn't fill his password")
	public void doesn_t_fill_his_password() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Password'), '', 0)
	}

	@When("doesn't fill his confirm password")
	public void doesn_t_fill_his_confirm_password() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Confirm Password'), '', 0)
	}

	@When("doesn't fill his phone number")
	public void doesn_t_fill_his_phone_number() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - PhoneNumber'), '', 0)
	}

	@When("doesn't fill his license plate")
	public void doesn_t_fill_his_license_plate() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Matricula'), '', 0)
	}

	@When("the email is not unique")
	public void email_is_not_unique() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Email'), 'manuela@gmail.com', 0)
	}

	@When("the email is not valid")
	public void email_is_not_valid() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Email'), 'manmangmail.com', 0)
	}

	@When("the confirmation password does not match with password")
	public void passwords_do_not_match() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Confirm Password'), 'alves2', 0)
	}

	@When("the license plate is not unique")
	public void license_plate_is_not_unique() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Matricula'), 'EE-33-GG', 0)
	}

	@When("the license plate is invalid")
	public void license_plate_is_not_valid() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Matricula'), 'EEE-33-GG', 0)
	}

	@When("the phone number is not unique")
	public void phone_number_is_not_unique() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - PhoneNumber'), '911456789', 0)
	}

	@Then("first name field receives the error {string}")
	public void first_name_field_receives_the_error(String string) {
		// Write code here that turns the phrase above into concrete actions
		String textMsg = Mobile.getAttribute(findTestObject('Object Repository/android.widget.EditText - First Name'), 'contentDescription', 0)

		assert textMsg.equals(string)
	}

	@Then("last name field receives the error {string}")
	public void last_name_field_receives_the_error(String string) {
		// Write code here that turns the phrase above into concrete actions
		String textMsg = Mobile.getAttribute(findTestObject('Object Repository/android.widget.EditText - LastName'), 'contentDescription', 0)

		assert textMsg.equals(string)
	}

	@Then("email field receives the error {string}")
	public void email_field_receives_the_error(String string) {
		// Write code here that turns the phrase above into concrete actions
		Mobile.delay(5)
		String textMsg = Mobile.getAttribute(findTestObject('Object Repository/android.widget.EditText - Email'), 'contentDescription', 0)

		assert textMsg.equals(string)
	}

	@Then("password field receives the error {string}")
	public void password_field_receives_the_error(String string) {
		// Write code here that turns the phrase above into concrete actions
		String textMsg = Mobile.getAttribute(findTestObject('Object Repository/android.widget.EditText - Password'), 'contentDescription', 0)

		assert textMsg.equals(string)
	}

	@Then("confirm password field receives the error {string}")
	public void confirm_password_field_receives_the_error(String string) {
		// Write code here that turns the phrase above into concrete actions
		String textMsg = Mobile.getAttribute(findTestObject('Object Repository/android.widget.EditText - Confirm Password'), 'contentDescription', 0)

		assert textMsg.equals(string)
	}

	@Then("phone number field receives the error {string}")
	public void phone_number_field_receives_the_error(String string) {
		// Write code here that turns the phrase above into concrete actions
		Mobile.delay(5)
		String textMsg = Mobile.getAttribute(findTestObject('Object Repository/android.widget.EditText - PhoneNumber'), 'contentDescription', 0)

		assert textMsg.equals(string)
	}

	@Then("license plate field receives the error {string}")
	public void license_plate_field_receives_the_error(String string) {
		// Write code here that turns the phrase above into concrete actions
		Mobile.delay(5)
		String textMsg = Mobile.getAttribute(findTestObject('Object Repository/android.widget.EditText - Matricula'), 'contentDescription', 0)

		assert textMsg.equals(string)
	}

	@When("user fills his information correctly")
	public void user_fills_info_valid() {
		Mobile.setText(findTestObject('android.widget.EditText - First Name'), 'JoaoTeste', 0)

		Mobile.setText(findTestObject('android.widget.EditText - LastName'), 'MenesesTeste', 0)

		Mobile.setText(findTestObject('android.widget.EditText - Email'), 'joaoteste@gmail.com', 0)

		Mobile.setText(findTestObject('android.widget.EditText - Password'), '123', 0)

		Mobile.setText(findTestObject('android.widget.EditText - Confirm Password'), '123', 0)

		Mobile.setText(findTestObject('android.widget.EditText - PhoneNumber'), '962321928', 0)

		Mobile.setText(findTestObject('android.widget.EditText - Matricula'), 'AL-10-AL', 0)
	}

	@Then("the app shows the dashboard")
	public void the_app_shows_the_dashboard() {
		Mobile.delay(5)
		Mobile.verifyElementExist(findTestObject('android.widget.TextView - Welcome'), 0)
	}
}