package com.onesource.utils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.relevantcodes.extentreports.ExtentReports;

public class Ext_Report {
	
	@SuppressWarnings("unused")
	public static ExtentReports instance(String ModuleName){
		ExtentReports extent;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		String fileName =  calendar.get(Calendar.DATE)
		+ "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR) + "_" + calendar.get(Calendar.HOUR_OF_DAY)
		+ "-" + calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.SECOND);
//		String Path =".\\reports\\"+ModuleName+"_"+fileName+".html";
		String Path =".\\reports\\"+ModuleName+".html";
		extent = new ExtentReports(Path, true);
		extent.loadConfig(new File(".\\extentconfig.xml"));
		return extent;
	 }

}
