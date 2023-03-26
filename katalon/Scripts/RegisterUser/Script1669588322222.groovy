import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

Mobile.startApplication('C:\\dev\\taes\\repos\\taes22_grupom_projeto\\app\\build\\outputs\\apk\\debug\\app-debug.apk', true)

Mobile.setText(findTestObject('android.widget.EditText - First Name'), 'JoaoTeste', 0)

Mobile.setText(findTestObject('android.widget.EditText - LastName'), 'MenesesTeste', 0)

Mobile.setText(findTestObject('android.widget.EditText - Email'), 'joaoteste@gmail.com', 0)

Mobile.setText(findTestObject('android.widget.EditText - Password'), '123', 0)

Mobile.setText(findTestObject('android.widget.EditText - Confirm Password'), '123', 0)

Mobile.setText(findTestObject('android.widget.EditText - PhoneNumber'), '962321928', 0)

Mobile.setText(findTestObject('android.widget.EditText - Matricula'), 'AL-10-AL', 0)

Mobile.verifyElementExist(findTestObject('android.widget.Button - REGISTER'), 0)

Mobile.tapAndHold(findTestObject('android.widget.Button - REGISTER'), 0, 0)

Mobile.delay(5)

Mobile.verifyElementExist(findTestObject('android.widget.TextView - Welcome'), 0)

Mobile.closeApplication()

