package com.onesource.pages;

import org.openqa.selenium.WebDriver;

import com.onesource.utils.CommonUtil;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class ManageBluePrint extends CommonUtil {

	public ManageBluePrint(WebDriver driver, ExtentReports extent, ExtentTest test) {
		super(driver, extent, test);
	}
	
	public void enterReferenceID(String refId,WebDriver driver,ExtentReports extent, ExtentTest test){
		
		setText("ManageBluePrint_refid",  refId);
		clickButton("ManageBluePrint_search");
	}
	
}
