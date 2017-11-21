package com.onesource.pages;

import org.openqa.selenium.WebDriver;

import com.onesource.utils.CommonUtil;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class Login extends CommonUtil {
	
	public Login(WebDriver driver,ExtentReports extent,ExtentTest test) {
		super(driver,extent,test);

	}

	public void doLogin(String username,String password){
		
		setText("Login_username", username);
		setText("Login_password", password);
		clickButton("Login_submit");
	}
	
	public void checkPage(String sheetName, String columnname,String fieldName){
		
	}

}
