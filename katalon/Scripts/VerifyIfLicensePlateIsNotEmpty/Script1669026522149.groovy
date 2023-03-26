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

Mobile.setText(findTestObject('Object Repository/android.widget.EditText - First Name'), 'Edgar', 0)

Mobile.setText(findTestObject('Object Repository/android.widget.EditText - LastName'), 'Bagagem', 0)

Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Email'), 'edgarbagagem@mail.pt', 0)

Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Password'), 'edgar1', 0)

Mobile.setText(findTestObject('Object Repository/android.widget.EditText - Confirm Password'), 'edgar1', 0)

Mobile.setText(findTestObject('Object Repository/android.widget.EditText - PhoneNumber'), '918235784', 0)

Mobile.tap(findTestObject('Object Repository/android.widget.Button - REGISTER'), 0)

String textMsg = Mobile.getAttribute(findTestObject('Object Repository/android.widget.EditText - Matricula'), 'contentDescription', 0)

assert textMsg.equals("Preencha com a sua matrícula")

Mobile.closeApplication()

