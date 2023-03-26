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



class Login {
	@Given("the user has the app open on the login page")
	public void the_user_has_the_app_open_on_the_login_page() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.startApplication('D:\\dev\\taes\\repos\\taes22_grupom_projeto\\app\\build\\outputs\\apk\\debug\\app-debug.apk', true)
		Mobile.tap(findTestObject('Object Repository/android.widget.Button - SIGN IN'), 0)
	}

	@When("the user does not fill his login credentials")
	public void the_user_does_not_fill_his_login_information() {
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - EmailLogin'), '', 0)
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - PasswordLogin'), '', 0)
	}

	@Then("taps the login button")
	public void taps_the_login_button() {
		Mobile.tap(findTestObject('Object Repository/android.widget.Button - LOGIN'), 0)
	}

	@Then("email login field receives the error {string}")
	public void email_login_field_receives_the_error(String string) {
		Mobile.delay(5)
		String textMsg = Mobile.getAttribute(findTestObject('Object Repository/android.widget.EditText - EmailLogin'), 'contentDescription', 0)

		assert textMsg.equals(string)
	}

	@When("the password is invalid")
	public void password_is_invalid() {
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - PasswordLogin'), '1nv4l1dp4ssw0rd', 0)
	}

	@Then("password login field receives the error {string}")
	public void password_login_field_receives_the_error(String string) {
		Mobile.delay(5)
		String textMsg = Mobile.getAttribute(findTestObject('Object Repository/android.widget.EditText - PasswordLogin'), 'contentDescription', 0)

		assert textMsg.equals(string)
	}


	@When("the user fills his login credentials")
	public void the_user_fills_his_login_credentials() {
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - EmailLogin'), 'TODO:create mail', 0)
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - PasswordLogin'), 'TODO: assign password', 0)
	}

	@When("the email does not exist")
	public void the_email_does_not_exist() {
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - EmailLogin'), 'n0n3x1st3nt3m41l@mail.pt', 0)
	}

	@When("the user taps the create new account button")
	public void taps_create_account() {
		Mobile.tap(findTestObject('Object Repository/android.widget.TextView - Create New Account'),0)
	}

	@Then("the app opens the register page")
	public void app_at_register_page() {
		Mobile.verifyElementExist(findTestObject('android.widget.Button - REGISTER'), 0)
	}
}
