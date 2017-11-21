package com.onesource.test;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.onesource.pages.Home;
import com.onesource.pages.Login;
import com.onesource.pages.ManageBluePrint;
import com.onesource.utils.CommonUtil;
import com.onesource.utils.ExcelUtil;
import com.onesource.utils.Ext_Report;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TC_ManageBluePrint_002 {

	private static final Logger LOGGER= Logger.getLogger(TC_ManageBluePrint_002.class);
	public static String DATA_FILE_PATH="./data/DataSheet.xlsx";
	public static String OBJECT_FILE_PATH="./Object/ObjectRepository.xlsx";
	public WebDriver driver;
	private ExtentReports extent;
	private ExtentTest test;
	public Map<String, String> dataMap;
	public Map<String, String> objectMap;
	public CommonUtil comUtil;
	ExcelUtil excel = new ExcelUtil();




	@BeforeMethod
	@Parameters({"UniqueDataId","UniqueObjectId","SheetName"})
	public void beforeMethod(String UniqueDataId,String UniqueObjectId,String SheetName) {
		
		try{
			excel.setExcelFile(DATA_FILE_PATH, "data");
			dataMap = excel.getSheetData(UniqueDataId, "data");
			
			excel.setExcelFile(OBJECT_FILE_PATH, "Login");
			objectMap = excel.getSheetDataObject(UniqueObjectId, SheetName);
			
		}catch(Exception e){
			LOGGER.error(e);
			System.out.println(e);
		}

		LOGGER.info("Browser is Going to Launch");
		extent = Ext_Report.instance("TC_ManageBluePrint_002");
		test = extent.startTest("Execution triggered for -TC_ManageBluePrint_002");
		comUtil=new CommonUtil(driver,extent,test);
		try{
			driver=comUtil.launchURL(dataMap.get("Browser"),dataMap.get("Url"));
			LOGGER.info("Browser successfully launched");
			test.log(LogStatus.PASS, "Browser successfully launched");

		}catch(Exception e){
			LOGGER.error("Error occured while launching browser: "+e);
			test.log(LogStatus.FAIL, "Error occured while launching browser: "+e);
			Assert.fail("Error occured while launching browser: "+e);
		}

	}

	@Test
	public void test() {

		try{
			//Step-1 Login the Avaya One source configurator
			Login l=new Login(driver,extent,test);		  
			l.doLogin(comUtil.GetConfigValue("username"),comUtil.GetConfigValue("password"));		  
			LOGGER.info("Application logged in successfully");
			test.log(LogStatus.PASS, "Application logged in successfully");

			//Step-2 Select the company 
			Home h=new Home(driver,extent,test);

			h.selectCompany(dataMap.get("country"), "company~version",
					dataMap.get("company")+"~"+dataMap.get("version"));

			h.selectManageBluePrint(driver,extent,test);

			//Step-3 Enter the Reference Id
			ManageBluePrint mb=new ManageBluePrint(comUtil.switchToWindow(driver),extent,test);
			mb.enterReferenceID(dataMap.get("refid"),driver, extent, test);


		}catch(Exception e){
			LOGGER.error("Error occured while doing login: "+e);
			test.log(LogStatus.FAIL, "Error occured while doing login: "+e);
			Assert.fail("Error occured while doing login: "+e);
		}

	}

	@AfterMethod
	public void afterMethod() {

		try {
			if(driver!=null)
				driver.quit();
			LOGGER.info("driver instance closed successfully");
			test.log(LogStatus.PASS, "driver instance closed successfully");
			extent.endTest(test);
			extent.flush();
		} catch (Exception e) {
			LOGGER.error("Error occured while closing driver instance: "+e);
			test.log(LogStatus.FAIL, "Error occured while closing driver instance: "+e);
			Assert.fail("Error occured while closing driver instance: "+e);
		}
	}

}
