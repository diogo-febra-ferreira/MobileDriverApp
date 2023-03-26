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



class Dashboard {
	@Then("sees the available orders")
	@When("the user sees the available orders")
	public void sees_the_available_orders() {
		Mobile.verifyElementExist(findTestObject('android.widget.TextView - Available Orders'), 0)
	}


	@When("taps the assign button")
	public void taps_the_assign_button() {
		Mobile.delay(5)
		Mobile.tap(findTestObject('android.widget.Button - Assign'), 0)
	}

	@When("taps the unassign button")
	public void taps_the_unassign_button() {
		Mobile.delay(5)
		Mobile.tap(findTestObject('android.widget.Button - UNASSIGN'), 0)
	}

	@Then("the order shows on My Orders")
	public void the_order_shows_on_My_Orders() {
		Mobile.delay(5)
		Mobile.verifyElementExist(findTestObject('android.widget.Button - START'),0)
	}

	@Then("the order shows on Available Orders")
	public void the_order_shows_on_Available_Orders() {
		Mobile.delay(5)
		Mobile.verifyElementExist(findTestObject('android.widget.Button - Assign'),0)
	}

	@Then("sees my orders")
	@When("the user sees my orders")
	public void sees_my_orders() {
		Mobile.verifyElementExist(findTestObject('android.widget.TextView - My Orders'), 0)
	}


	@When("the user taps the account button")
	public void the_user_taps_the_account_button() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.tap(findTestObject('android.widget.Button - ACCOUNT'), 0)
	}

	@Then("opens his account page")
	public void opens_his_account_page() {
		Mobile.verifyElementExist(findTestObject('android.widget.Button - Statistics'), 0)
	}

	@When("taps the order switch")
	public void taps_the_order_switch() {
		Mobile.tap(findTestObject('android.widget.SpinnerAvailable'), 0)
	}

	@When("taps the closest orders option")
	public void taps_the_closest_orders_option() {
		Mobile.delay(1)
		Mobile.tap(findTestObject('android.widget.TextView - Closest Orders'),0)
	}

	@Then("the orders are ordered by closest")
	public void the_orders_are_ordered_by_closest() {
		Mobile.verifyElementExist(findTestObject('android.widget.TextView - Available Orders'), 0)
	}

	@When("taps the farthest orders option")
	public void taps_the_farthest_orders_option() {
		Mobile.delay(1)
		Mobile.tap(findTestObject('android.widget.TextView - Farthest Orders'),0)
	}

	@Then("the orders are ordered by farthest")
	public void the_orders_are_ordered_by_farthest() {
		Mobile.verifyElementExist(findTestObject('android.widget.TextView - Available Orders'), 0)
	}
}