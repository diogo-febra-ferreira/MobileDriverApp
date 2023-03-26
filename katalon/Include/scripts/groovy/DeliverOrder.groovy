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



class DeliverOrder {
	@When("the user is delivering an order")
	public void the_user_is_delivering_an_order() {
		Mobile.tap(findTestObject('android.widget.Button - START'),0)
	}

	@When("taps the button concluir")
	public void taps_the_button_concluir() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.delay(5)
		Mobile.tap(findTestObject('android.widget.Button - CONCLUIR'),0)
	}

	@When("taps the button cancelar")
	public void taps_the_button_cancelar() {
		// Write code here that turns the phrase above into concrete actions
		Mobile.delay(5)
		Mobile.tap(findTestObject('android.widget.Button - CANCELAR'),0)
	}

	@When("taps the button detalhes")
	public void taps_the_button_detalhes() {
		Mobile.delay(5)
		Mobile.tap(findTestObject('android.widget.Button - DETALHES'),0)
	}

	@When("taps the button directions")
	public void taps_the_button_directions() {
		Mobile.delay(5)
		Mobile.tap(findTestObject('android.widget.Button - DIRECTIONS'),0)
	}
	
	@When("gives a reason")
	public void gives_reason() {
		Mobile.setText(findTestObject('android.widget.EditText - MotivoCancelar'), 'Razão Cancelamento', 0)
	}
	
	@When("taps ok button")
	public void taps_ok() {
		Mobile.tap(findTestObject('android.widget.Button - OKCANCELAR'),0)
	}

	@Then("the app opens the order details")
	public void open_order_details() {
		Mobile.verifyElementExist(findTestObject('android.widget.TextView - Price'), 0)
	}

	@Then("the app opens the order directions")
	public void open_order_directions() {
		Mobile.verifyElementExist(findTestObject('android.widget.LinearLayout - Notifications'), 0)
	}

	@Then("the order is finished")
	public void the_order_is_finished() {
		Mobile.verifyElementExist(findTestObject('android.widget.TextView - Welcome'), 0)
	}

	@Then("the order is canceled")
	public void the_order_is_canceled() {
		Mobile.verifyElementExist(findTestObject('android.widget.TextView - Welcome'), 0)
	}
}