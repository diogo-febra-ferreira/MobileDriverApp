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



class KeepLoggedIn {
@Given("that the user checks the logged in checkbox as he logs in")
public void that_the_user_checks_the_logged_in_checkbox_as_he_logs_in() {
		Mobile.startApplication('D:\\dev\\taes\\repos\\taes22_grupom_projeto\\app\\build\\outputs\\apk\\debug\\app-debug.apk', false)
		Mobile.tap(findTestObject('Object Repository/android.widget.Button - SIGN IN'), 0)
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - EmailLogin'), 'manu@gmail.com', 0)
		Mobile.setText(findTestObject('Object Repository/android.widget.EditText - PasswordLogin'), 'manu', 5)
		Mobile.tap(findTestObject('Object Repository/android.widget.Switch - SwitchLogged'), 0)
		Mobile.tap(findTestObject('Object Repository/android.widget.Button - LOGIN'), 0)
		Mobile.delay(5)
}

@When("the user closes the app")
public void the_user_closes_the_app() {
    Mobile.closeApplication()
}

@When("opens it again")
public void opens_it_again() {
    Mobile.startExistingApplication('pt.pleiria.estg.myapplication')
}

@Then("the user is logged in")
public void the_user_is_logged_in() {
    Mobile.verifyElementExist(findTestObject('android.widget.TextView - Available Orders'),0)
}
}