package com.onesource.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("unused")
public class ExcelUtil implements ApplicationConstants{

	
	private XSSFSheet excelWSheet;
	private XSSFWorkbook excelWBook;
	private XSSFCell cellGlobal;
	private XSSFRow rowGlobal;
	int reqcellrowno;
	int reqcellcolno;

	private static final Logger LOGGER = Logger.getLogger(ExcelUtil.class.getName());

	public void setExcelFile(String path, String sheetName) throws Exception {
		try {
			// Open the Excel file
			FileInputStream excelFile = new FileInputStream(path);
			// Access the required test data sheet
			excelWBook = new XSSFWorkbook(excelFile);
			excelWSheet = excelWBook.getSheet(sheetName);
		} catch (Exception e) {
			LOGGER.error("Error while intializing the excel file " + e);
		}
	}


	@SuppressWarnings("deprecation")
	public String getExcelData(String sheetName, String columnname, String rowname) throws Exception {
		InputStream inp = new FileInputStream("./Object/ObjectRepository.xlsx");
		Workbook wb = WorkbookFactory.create(inp);
		//		Workbook wb = new HSSFWorkbook(inp);		
		//		Workbook wb = new XSSFWorkbook("./Object/Login.xlsx");
		Sheet sheet = wb.getSheet(sheetName);
		String cellData = "";
		int rowNum = 0;
		int colNum = 0;
		boolean colFound = false;
		boolean rowFound = false;
		try{
			Row row = sheet.getRow(0);

			for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
				Row rowLoop = sheet.getRow(i);
				if (rowLoop != null){
					Cell c = rowLoop.getCell(0);
					if (c != null) {
						c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
						String testcaserowname = c.getStringCellValue();
						if (rowname.equalsIgnoreCase(testcaserowname)) {
							rowNum = i;
							rowFound = true;
							break;
						}
					}
				}
			}
			if (rowFound) {
				for (int j = 0; j < row.getLastCellNum(); j++) {
					Cell c = row.getCell(j);
					if (c != null) {
						c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
						String testcasecellname = c.getStringCellValue();
						if (columnname.equalsIgnoreCase(testcasecellname)) {
							colNum = j;
							colFound = true;
							break;
						}
					}
				}
			}
			if (colFound) {
				Cell cell = sheet.getRow(rowNum).getCell(colNum);
				cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
				cellData = cell.getStringCellValue();
			}
			return cellData;
		}
		catch (Exception e) {
			System.out.println("Error while getting the cell data by getExcelData(Str1,Str2,Str3) for column : "+columnname+" and row : "+rowname+" Exception : " + e.getMessage());
			return cellData;
		}		
	}

	@SuppressWarnings("deprecation")
	public Map<String, String> getSheetData(String tcID, String sheetName) {
		final List<String> rowData = new ArrayList<>();
		final LinkedHashMap<String, String> rowVal = new LinkedHashMap<>();
		Object value;
		final Sheet sheet = getSheet(sheetName);
		final List<String> coulmnNames = getColumns(sheet);
		final int totalRows = sheet.getPhysicalNumberOfRows();
		final Row row = sheet.getRow(0);
		final int firstCellNum = row.getFirstCellNum();
		final int lastCellNum = row.getLastCellNum();
		for (int i = 1; i < totalRows; i++) {
			final Row rows = sheet.getRow(i);
			final String testLinkID = rows.getCell(0).getStringCellValue();
			if (tcID.equalsIgnoreCase(testLinkID)) {
				for (int j = firstCellNum; j < lastCellNum; j++) {
					final Cell cell = rows.getCell(j);
					if (cell == null
							|| cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
						rowData.add("");
					} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
						final Double val = cell.getNumericCellValue();
						value = val.intValue();
						rowData.add(value.toString());
					} else if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
						rowData.add(cell.getStringCellValue());
					} else if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
						rowData.add(cell.getStringCellValue());
					} else if (DateUtil.isCellDateFormatted(cell)) {
						rowData.add(cell.getDateCellValue().toString());
					} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN
							|| cell.getCellType() == XSSFCell.CELL_TYPE_ERROR
							|| cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
						try {
							throw new Exception(" Cell Type is not supported ");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					rowVal.put(coulmnNames.get(j), rowData.get(j).trim());
				}
				break;
			}

		}
		return rowVal;

	}

	private Sheet getSheet(String sheetName)
	{
		return excelWBook.getSheet(sheetName);
	}

	private List<String> getColumns(Sheet sheet) {
		final Row row = sheet.getRow(0);
		final List<String> columnValues = new ArrayList<>();
		final int firstCellNum = row.getFirstCellNum();
		final int lastCellNum = row.getLastCellNum();
		for (int i = firstCellNum; i < lastCellNum; i++) {
			final Cell cell = row.getCell(i);
			columnValues.add(cell.getStringCellValue());
		}
		return columnValues;
	}
	
	public Map<String, String> getSheetDataObject(String tcID, String sheetName) {
		final List<Row> rowData = new ArrayList<>();
		final LinkedHashMap<String, String> rowVal = new LinkedHashMap<>();
//		Object value;
		final Sheet sheet = getSheet(sheetName);
//		final List<String> coulmnNames = getColumns(sheet);
		final int totalRows = sheet.getPhysicalNumberOfRows();
		Row row = sheet.getRow(0);
//		final int firstCellNum = row.getFirstCellNum();
//		final int lastCellNum = row.getLastCellNum();
//		
		for (int i = 1; i < totalRows; i++) {
			row = sheet.getRow(i);
			rowVal.put(row.getCell(0).getStringCellValue(),row.getCell(1).getStringCellValue());
		}
		return rowVal;

	}

	public Map<String, String> getSheetDataObjects(String sheetName) {
		final LinkedHashMap<String, String> rowVal = new LinkedHashMap<>();
		final Sheet sheet = getSheet(sheetName);
		final int totalRows = sheet.getPhysicalNumberOfRows();
		Row row = sheet.getRow(0);
		
		for (int i = 1; i < totalRows; i++) {
			row = sheet.getRow(i);
			rowVal.put(row.getCell(0).getStringCellValue(),row.getCell(1).getStringCellValue());
		}
		return rowVal;

	}

} 
