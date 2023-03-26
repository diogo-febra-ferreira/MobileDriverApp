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

import org.apache.commons.lang3.StringUtils

import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When



class AccountBalance {
	String s

	@Then("checks his account balance")
	def check_account_balance() {
		Mobile.delay(5)
		Mobile.verifyElementExist(findTestObject('android.widget.TextView - Balance'), 0)
	}

	@When("the user finishes a delivery")
	public void the_user_finishes_a_delivery() {
		// GET PREVIOUS ACCOUNT BALANCE
		Mobile.delay(1)

		Mobile.tap(findTestObject('android.widget.Button - ACCOUNT'), 0)

		Mobile.delay(2)
		
		String txt = Mobile.getText(findTestObject('android.widget.TextView - Balance'), 0)

		s = StringUtils.remove(txt, '€');

		Mobile.delay(1)

		Mobile.tap(findTestObject('android.widget.Button - HOME'), 0)

		Mobile.tap(findTestObject('android.widget.Button - START'),0)
		Mobile.delay(5)
		Mobile.tap(findTestObject('android.widget.Button - CONCLUIR'),0)
	}

	@Then("his account balance goes up")
	public void his_account_balance_goes_up() {
		Mobile.delay(5)
		Mobile.tap(findTestObject('android.widget.Button - ACCOUNT'), 0)
		String txt = Mobile.getText(findTestObject('android.widget.TextView - Balance'), 0)

		String y = StringUtils.remove(txt, '€')

		BigDecimal numberS = new BigDecimal(s)

		BigDecimal numberY = new BigDecimal(y)

		Mobile.verifyGreaterThan(numberY, numberS)
	}
}