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



class ButtonStartOrder {
	@Given("that the user is logged in")
	public void that_the_user_is_logged_in() {
		Mobile.startApplication('D:\\dev\\taes\\repos\\taes22_grupom_projeto\\app\\build\\outputs\\apk\\debug\\app-debug.apk', true)
		Mobile.tap(findTestObject('Object Repository/android.widget.Button - SIGN IN'), 0)
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - EmailLogin'), 'manu@gmail.com', 0)
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - PasswordLogin'), 'manu', 5)
		Mobile.tap(findTestObject('Object Repository/android.widget.Button - LOGIN'), 0)
	}


	@When("I click on button start")
	public void i_click_on_button_start() {
		Mobile.tap(findTestObject('Object Repository/android.widget.Button - START'), 0)
	}

	@Then("I open the map")
	public void i_open_the_map() {
		Mobile.verifyElementExist(findTestObject('Object Repository/android.widget.Button - Concluir'), 0)
	}
}