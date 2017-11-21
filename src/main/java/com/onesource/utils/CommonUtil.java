package com.onesource.utils;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class CommonUtil implements ApplicationConstants{

	public WebDriver driver;
	private ExtentReports extent;
	private ExtentTest test;
	public static String OBJECT_FILE_PATH="./Object/ObjectRepository.xlsx";
	//	public static String UniqueObjectId="ObjectName";
	Map<String, String> objectsMap = readAllObjects();

	Map<String, String> objectMap;

	public CommonUtil(WebDriver driver,ExtentReports extent,ExtentTest test) {
		this.driver=driver;
		this.extent=extent;
		this.test=test;
	}

	private static final Logger LOGGER = Logger.getLogger(CommonUtil.class.getName());
	Capabilities capability;

	static Map<String, String>  readAllObjects()
	{
		ExcelUtil excelUtl = new ExcelUtil();
		try {
			excelUtl.setExcelFile(OBJECT_FILE_PATH, "Objects");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return excelUtl.getSheetDataObjects("Objects");

	}

	@SuppressWarnings("deprecation")
	public WebDriver launchURL(String browserName,String url){

		switch(browserName.toLowerCase()){

		case "chrome":
			System.setProperty("webdriver.chrome.driver", DRIVER_PATH + "chromedriver.exe");
			ChromeOptions chromeoptions = new ChromeOptions();
			//here "--start-maximized" argument is responsible to maximize chrome browser
			chromeoptions.addArguments("--start-maximized");
			driver=new ChromeDriver(chromeoptions);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			capability = ((RemoteWebDriver) driver).getCapabilities(); 
			extent.addSystemInfo("BrowserName",capability.getBrowserName());
			extent.addSystemInfo("Browser Version", capability.getVersion());

			try{
				driver.get(url);
				LOGGER.info("URL Launched Successfully");
				getScreenShot("URL Launched Successfully");
				test.log(LogStatus.PASS, "URL: "+url+" Launched Successfully");
				System.out.println("URL Launched Successfully");
			}catch(Exception e){
				LOGGER.error("URL Launch Falied"+e);
				getScreenShot("URL Launch Falied");
				test.log(LogStatus.FAIL, "URL: "+url+" could not be launched -"+e);
				Assert.fail("Error occured while launching url: "+e);	
			}
			break;
		case "firefox":
			System.setProperty("webdriver.gecko.driver", DRIVER_PATH + "geckodriver_old.exe");
			driver=new FirefoxDriver();
			driver.manage().window().maximize();
			capability = ((RemoteWebDriver) driver).getCapabilities(); 
			//			extent.addSystemInfo("BrowserName",capability.getBrowserName());
			//			extent.addSystemInfo("Browser Version", capability.getVersion());
			try{
				driver.get(url);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				LOGGER.info("URL Launched Successfully");
				//				getScreenShot("URL Launched Successfully");
				//				test.log(LogStatus.PASS, "URL: "+url+" Launched Successfully");
				System.out.println("URL Launched Successfully");
			}catch(Exception e){
				LOGGER.error("URL Launch Falied"+e);
				//				getScreenShot("URL Launch Falied");
				//				test.log(LogStatus.FAIL, "URL: "+url+" could not be launched -"+e);
				Assert.fail("Error occured while launching url: "+e);	
			}
			break;
		case "ie":
			System.setProperty("webdriver.ie.driver", DRIVER_PATH + "IEDriverServer.exe");
			DesiredCapabilities returnCapabilities = DesiredCapabilities.internetExplorer();
			returnCapabilities.setCapability("ignoreProtectedModeSettings", true);
			returnCapabilities.setCapability("ie.ensureCleanSession", true);
			returnCapabilities.setCapability("requireWindowFocus", true);
			returnCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS,true);
			returnCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			driver = new InternetExplorerDriver(returnCapabilities);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			capability = ((RemoteWebDriver) driver).getCapabilities(); 
			//			extent.addSystemInfo("BrowserName",capability.getBrowserName());
			//			extent.addSystemInfo("Browser Version", capability.getVersion());
			try{
				driver.get(url);
				LOGGER.info("URL Launched Successfully");
				//				getScreenShot("URL Launched Successfully");
				//				test.log(LogStatus.PASS, "URL: "+url+" Launched Successfully");
				System.out.println("URL Launched Successfully");
			}catch(Exception e){
				LOGGER.error("URL Launch Falied"+e);
				//				getScreenShot("URL Launch Falied");
				//				test.log(LogStatus.FAIL, "URL: "+url+" could not be launched -"+e);
				Assert.fail("Error occured while launching url: "+e);	
			}
			break;
		}
		return driver;
	}

	public void killBrowserInstance(WebDriver driver){
		try{
			driver.quit();
			LOGGER.info("Browser Instance successfully closed");
		}catch(Exception e){
			LOGGER.error("Error occured while closing webdriver instance: "+e);
			Assert.fail("Error occured while closing webdriver instance: "+e);
		}
	}

	public By getObject(String objName)
	{
		By ret = null;
		String c=null;
		try{
			//			setExcelFile(OBJECT_FILE_PATH, "data");
			//			objectMap = getSheetDataObject("ObjectName", "Login");
			//			
			////			c = getExcelData(sheetName, columnName, rowname);
			//			c=objectMap.get(objName);

			c=objectsMap.get(objName);
			System.out.println();
		}catch(Exception e){
			LOGGER.error("Error occured while getting data from excel: "+e);
			Assert.fail("Error occured while getting data from excel: "+e);
		}

		String[] keyVal = c.split("~");
		String key = keyVal[0].trim();
		String value = keyVal[1].trim();

		switch(key.toLowerCase())
		{
		case "class":
			ret = By.className(value);
			break;
		case "css":	
			ret = By.cssSelector(value);
			break;
		case "id":
			ret = By.id(value);
			break;
		case "link":
			ret = By.linkText(value);
			break;
		case "name":
			ret = By.name(value);
			break;
		case "partiallink":
			ret = By.partialLinkText(value);
			break;
		case "tagname":
			ret = By.tagName(value);
			break;
		case "xpath":
			ret = By.xpath(value);
			break;
		}

		return ret;
	}

	public By getObjectReplace(String objName,String rKey,String rValue)
	{
		By ret = null;
		String c=objectsMap.get(objName);

		String[] keyVal = c.split("\\~");
		String[] replaceKey = rKey.split("\\~");
		String[] replaceValue = rValue.split("\\~");

		for (int i = 0; i < replaceKey.length; i++)
			keyVal[1] = keyVal[1].replace(replaceKey[i], replaceValue[i]);

		String value=keyVal[1];

		switch(keyVal[0].toLowerCase())
		{
		case "class":
			ret = By.className(value);
			break;
		case "css":	
			ret = By.cssSelector(value);
			break;
		case "id":
			ret = By.id(value);
			break;
		case "link":
			ret = By.linkText(value);
			break;
		case "name":
			ret = By.name(value);
			break;
		case "partiallink":
			ret = By.partialLinkText(value);
			break;
		case "tagname":
			ret = By.tagName(value);
			break;
		case "xpath":
			ret = By.xpath(value);
			break;
		}

		return ret;
	}



	public String GetConfigValue(String key) {
		Properties props = new Properties();
		File file = new File(".//data//Input_Data.properties");
		FileReader fileR = null;
		String retVal=null;
		try {
			fileR = new FileReader(file);
			props.load(fileR);
			retVal = props.getProperty(key);
			LOGGER.debug("Key: "+key+" "+"Value: "+retVal);

			fileR.close();
		} catch(Exception e){
			LOGGER.error("Error occured while getting value from propertie file: "+e);
			Assert.fail("Error occured while getting value from propertie file: "+e);
		}
		return retVal;
	}


	public void setText(String objName,String value){
		try{
			By locator;
			locator=getObject(objName);
			driver.findElement(locator).sendKeys(value);
			LOGGER.info("The text "+value +" entered successfully");
			getScreenShot("login");
			test.log(LogStatus.PASS,"The text "+ value +" entered successfully");


		}catch(Exception e){
			LOGGER.error("Error occured while entering value in textbox: "+e);
			test.log(LogStatus.FAIL, "Error occured while entering value in textbox: "+e);
			Assert.fail("Error occured while entering value in textbox: "+e);
		}
	}

	public void clickButton(String objName){
		try{
			By locator;
			locator=getObject(objName);
			driver.findElement(locator).click();
			LOGGER.info("Button successfully clicked");
			test.log(LogStatus.PASS, "Button successfully clicked");

		}catch(Exception e){
			LOGGER.error("Error occured while clicking button: "+e);
			test.log(LogStatus.FAIL, "Error occured while clicking button: "+e);
			Assert.fail("Error occured while clicking button: "+e);
		}
	}


	public boolean selectRadioButton_WE(String objName,String key,String value) {

		try {			
			WebElement ele = driver.findElement(getObjectReplace(objName,key, value));
			if (!ele.isSelected())
			{
				ele.click();
				LOGGER.info(objName + " has been selected");
				test.log(LogStatus.PASS, "EXPECTECD: Element " + objName + " should be selected",
						"Usage: <span style='font-weight:bold;'>ACTUAL:: " + objName
						+ " has been selected</span>");
			}
			else
			{
				LOGGER.info(objName + " radio button already checked");
				test.log(LogStatus.INFO, "EXPECTECD: Element " + objName + " already checked",
						"Usage: <span style='font-weight:bold;'>ACTUAL:: " + objName
						+ " already checked</span>");
			}
			return true;

		} catch (Exception e) {
			getScreenShot("selecting radio button " + objName);
			LOGGER.error("Selecting radio button failed..." + e);
			LOGGER.error("Selecting radio button failed..." + e);
			test.log(LogStatus.FAIL, "Selecting radio button failed", "Selecting radio button failed  because  -" + e);
			Assert.fail("Selecting radio button failed..." + e);
			return false;
		}
	}

	public boolean selectDropDownByVisibleText(String objName, String value) {
		try {
			if (value != null && value.trim().length() > 0) {
				WebElement supplierName = driver.findElement(getObject(objName));

				boolean isOptionFound = false;
				Select oSelect = new Select(supplierName);
				List<WebElement> optionelems = oSelect.getOptions();
				for(int i=0; i<optionelems.size(); i++)				
					if(optionelems.get(i).getText().trim().equals(value.trim()))	
					{
						oSelect.selectByIndex(i);
						isOptionFound = true;
						break;
					}
				if(isOptionFound)
				{
					LOGGER.info("Value -'" + value + "'- selected from Drop down "+objName+" sucessfully");
					test.log(LogStatus.PASS, "Drop down should be selected",
							"Value -'" + value + "'- selected from Drop down "+objName+" sucessfully");
				}
				else
				{
					getScreenShot("Unable to find Value -'" + value + "'- from Drop down "+objName);
					LOGGER.info("Unable to find Value -'" + value + "'- from Drop down "+objName);
					test.log(LogStatus.FAIL, "Drop down should be selected",
							"Unable to find Value -'" + value + "'- from Drop down "+objName);
					Assert.fail("Unable to find Value -'" + value + "'- from Drop down "+objName);
				}
			}
			return true;
		} catch (Exception e) {
			getScreenShot("Selecting  " + value);
			LOGGER.error("Dropdown selection failed..." + e);
			test.log(LogStatus.FAIL, "Dropdown selection failed..." + e);
			Assert.fail("Dropdown selection failed..." + e);
			return false;
		}
	}



	public void clickLink(String objName){
		try{
			By locator;
			locator=getObject(objName);
			driver.findElement(locator).click();
			LOGGER.info("Link successfully clicked");
			test.log(LogStatus.PASS, "Link successfully clicked");

		}catch(Exception e){
			LOGGER.error("Error occured while clicking Link: "+e);
			test.log(LogStatus.FAIL, "Error occured while clicking Link: "+e);
			Assert.fail("Error occured while clicking Link: "+e);
		}
	}

	public void mouseHover(String objName) {
		try {

			WebElement elem = driver.findElement(getObject(objName));
			Actions actions = new Actions(driver);
			actions.moveToElement(elem).click().perform();
			LOGGER.info(objName + " element is clicked");
			test.log(LogStatus.PASS, objName + " element is clicked");

		} catch (Exception e) {
			LOGGER.info("Exception occured while looking for element : " + objName + "  - " + e);
			LOGGER.error("Exception occured while looking for element : " + objName + "  - " + e);
			test.log(LogStatus.FAIL, objName + " element is not clicked");

		}
	}

	public void mouseHoverAndClick(String objName) {
		try {

			WebElement elem = driver.findElement(getObject(objName));
			Actions actions = new Actions(driver);
			actions.moveToElement(elem).click().perform();
			LOGGER.info(objName + " element is clicked");
			test.log(LogStatus.PASS, objName + " element is clicked");

		} catch (Exception e) {
			LOGGER.error("Exception occured while looking for element : " + objName + "  - " + e);
			LOGGER.error("Exception occured while looking for element : " + objName + "  - " + e);
			test.log(LogStatus.FAIL, objName + " element is not clicked");

		}
	}

	public void getScreenShot(String testStep) {
		try {
			String screenshotB64 = "data:image/png;base64,"+((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);    

			test.log(LogStatus.INFO, "Snapshot for  " + testStep + "  : "+ test.addBase64ScreenShot(screenshotB64));

		} catch (Exception e) {
			LOGGER.info("ERROR IN SCREENSHOT." + e);
			Assert.fail("ERROR IN SCREENSHOT." + e);
		}
	}

	public WebDriver switchToWindow(WebDriver driver){

		try{

			String parentWindowHandle=driver.getWindowHandle();
			Set<String> windowId=driver.getWindowHandles();

			for(String id:windowId){

				if(!id.equalsIgnoreCase(parentWindowHandle)){
					driver.switchTo().window(id);
				}
				LOGGER.info("Window Switched successfully");
			}

		}catch(Exception e){
			LOGGER.error("Error Occured while switching window: "+e);
		}
		return driver;
	}

	public String getKey(Map<String,String> map,String value){

		Set<String> keys = map.keySet();
		String res=null;
		for(String myKeys: keys){
			if(myKeys.equalsIgnoreCase(value)){
				res=myKeys;
			}
		}
		return res;
	}
}

