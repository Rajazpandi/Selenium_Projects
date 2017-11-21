package com.onesource.pages;

import org.openqa.selenium.WebDriver;
import com.onesource.utils.CommonUtil;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class Home extends CommonUtil {

	public Home(WebDriver driver, ExtentReports extent, ExtentTest test) {
		super(driver, extent, test);

	}
	
	public void selectCompany(String dropdownValue,String key,String value){
		
		
		clickLink("Home_change");
		selectDropDownByVisibleText("Home_country", dropdownValue);
		selectRadioButton_WE("Home_selectcountry", key, value);
		clickButton("Home_setdefault");
	}
	
	public void selectManageBluePrint(WebDriver driver,ExtentReports extent,ExtentTest test){
		
		mouseHover("Home_designquote");
		mouseHover("Home_configurator");
		mouseHoverAndClick("Home_manageblueprint");
	}

}
