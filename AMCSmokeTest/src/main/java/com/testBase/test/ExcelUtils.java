package com.testBase.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils extends TestBase {
	
	public static XSSFWorkbook workbook;
	public XSSFSheet sheet;
	public XSSFRow row = null;
	public XSSFCell cell = null;
	public String[][] excelData = null;
	public int colCount = 0;
	public int rowCount = 0;
	public int lastRow;
	public String path = System.getProperty("user.dir");
	public String databaseURL;
	public String passWord;
	public String user;
	public String[][] FDIDNumbers;
	public String data;
	private Properties prop = null;
	public static ThreadLocal<Properties> propHandler = new ThreadLocal<Properties>();
	public ThreadLocal<XSSFWorkbook> excelWorkBook = new ThreadLocal<XSSFWorkbook>();
	
	
	// Method For Reading Excel File and Data

		public String[][] readingexcelFiles(String sheetname) throws Exception {

			try {

				String FilePath = path + "\\ExcelFile\\API_inputs.xlsx";

				FileInputStream finputStream = new FileInputStream(new File(FilePath));

				workbook = new XSSFWorkbook(finputStream);

				sheet = workbook.getSheet(sheetname);

				colCount = sheet.getRow(0).getPhysicalNumberOfCells();

				rowCount = sheet.getPhysicalNumberOfRows();

				lastRow = sheet.getLastRowNum();

				excelData = new String[rowCount][colCount];

				for (int Nrow = 0; Nrow < rowCount; Nrow++) {

					row = sheet.getRow(Nrow);

					for (int Ncolumn = 0; Ncolumn < colCount; Ncolumn++) {

						cell = sheet.getRow(Nrow).getCell(Ncolumn);

						DataFormatter df = new DataFormatter();

						excelData[Nrow][Ncolumn] = df.formatCellValue(cell);

					}
				}

			} catch (Exception e) {

				System.out.println(e.getMessage());
			}

			return excelData;
		}

		// Reading Excel Sheet Based on SheetName

		public String[][] readingExcel(String sheetName) {

			//logStep("Started reading data from Excel");

			try {

				FDIDNumbers = readingexcelFiles(sheetName);

			} catch (Exception e) {

				e.printStackTrace();
			}

			return FDIDNumbers;
		}

		
		// Finds the Excel sheet path
		// Loads the Excel workbook

		public XSSFWorkbook initializeExcelSheet(String fileName) {

			prop = readPropertyFile();

			File file = new File(System.getProperty("user.dir") + prop.getProperty("workSpaceExcelPath") + "\\" + fileName);

			FileInputStream files = null;

			try {

				files = new FileInputStream(file);

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}

			try {

				workbook = new XSSFWorkbook(files);

			} catch (IOException e) {

				e.printStackTrace();
			}

			excelWorkBook.set(workbook);

			return workbook;
		}

		
		// Finds the ConfigProperties file
		// Reads the data from ConfigProperties file

		public Properties readPropertyFile() {

			prop = new Properties();

			InputStream input = null;

			try {

				input = new FileInputStream(System.getProperty("user.dir") + "\\" + "ConfigProperties");

				prop.load(input);

			} catch (IOException ex) {

				ex.printStackTrace();

			} finally {

				if (input != null) {

					try {

						input.close();

					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}

			return prop;
		}

		
		
		
		
		// Reads the Data from Excel sheet.

		public String getDataFromExcel(int rownum, int colnum, String sheetName) throws Exception {

			workbook = excelWorkBook.get();

			sheet = workbook.getSheet(sheetName);

			XSSFCell cell = sheet.getRow(rownum).getCell(colnum);

			DataFormatter df = new DataFormatter();

			String data = df.formatCellValue(cell);

			return data;
		}

		
		// Reads the Data From Excel WorkBook

		public String[][] getDataFromExcel(XSSFWorkbook workbook, String sheetName) {

			String[][] excelData = null;

			try {

				int colCount = 0, rowCount = 0;

				sheet = workbook.getSheet(sheetName);

				// Reads the Column Count
				colCount = sheet.getRow(0).getPhysicalNumberOfCells();

				// Reads the Row Cout
				rowCount = sheet.getPhysicalNumberOfRows();

				excelData = new String[rowCount][colCount];

				for (int Nrow = 0; Nrow < rowCount; Nrow++) {

					sheet.getRow(Nrow);

					for (int Ncolumn = 0; Ncolumn < colCount; Ncolumn++) {

						cell = sheet.getRow(Nrow).getCell(Ncolumn);

						DataFormatter df = new DataFormatter();

						excelData[Nrow][Ncolumn] = df.formatCellValue(cell);
					}
				}
			} catch (Exception e) {

				System.out.println(e.getMessage());
			}
			return excelData;
		}

		
		// Database Credentials Reading From the Sheet

				public void readDatabaseCredentials(int linenumber) {

					String[][] databaseCredentials = readingExcel("DatabaseCredentials");

					for (int inum = 1; inum <= lastRow; inum++) {

						if (linenumber == inum) {

							databaseURL = databaseCredentials[linenumber][0];

							user = databaseCredentials[linenumber][1];

							passWord = databaseCredentials[linenumber][2];

						}

					}

				}
				
				
				// Reads the Queries from Excel sheet based on Row Number

				public String sqlQueries(int linenumber) {

					String[][] querieString = readingExcel("Queries");

					for (int num = 1; num <= lastRow; num++) {

						if (linenumber == num) {

							data = querieString[linenumber][0];
						}

					}

					return data;

				}
		
				
				public HashMap<String, Integer> storeExcelDataInHashMap() {

					propHandler.set(readPropertyFile());

					HashMap<String, Integer> testCaseRailID = new HashMap<String, Integer>();

					initializeExcelSheet(prop.getProperty("TestRailExcelFile"));

					String sheetName = "ExecutionModel";

					workbook = excelWorkBook.get();

					sheet = workbook.getSheet(sheetName);

					int maxcount = sheet.getLastRowNum();

					try {

						for (int testcase = 1; testcase <= maxcount; testcase++) {

							String testCaseName = getDataFromExcel(testcase, 0, sheetName);

							int testCaseID = Integer.parseInt(getDataFromExcel(testcase, 2, sheetName));

							testCaseRailID.put(testCaseName, testCaseID);
						}

					} catch (Exception e) {

						System.out.println(e.getMessage());

					}

					System.out.println(testCaseRailID);

					return testCaseRailID;

				}

}
