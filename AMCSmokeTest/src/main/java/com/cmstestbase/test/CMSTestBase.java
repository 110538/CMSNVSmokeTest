package com.cmstestbase.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.Pattern;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.objects.test.ObjectRepository;
import ru.yandex.qatools.allure.annotations.Step;

public class CMSTestBase {

	public XSSFWorkbook workbook;
	public XSSFSheet sheet;
	public XSSFRow row = null;
	public XSSFCell cell = null;
	public String[][] excelData = null;
	public int colCount = 0;
	public int rowCount = 0;
	public int lastRow;
	public String[][] FDIDNumbers;
	public String path = System.getProperty("user.dir");
	public String databaseURL;
	public String user;
	public String passWord;
	public int linenumber;
	public String data;
	public static ArrayList<String> ActualEvents = new ArrayList<String>();
	public static ArrayList<String> listEvents = new ArrayList<String>();
	public static ArrayList<String> errors = new ArrayList<String>();
	public static ArrayList<String> recordon = new ArrayList<String>();
	public static ArrayList<String> armeta = new ArrayList<String>();
	public String FDID;
	public static WebDriver driver;
	private Properties prop = null;
	public static ThreadLocal<Properties> propHandler = new ThreadLocal<Properties>();
	public static ThreadLocal<XSSFWorkbook> excelWorkBook = new ThreadLocal<XSSFWorkbook>();
	protected XSSFWorkbook workBook;

	public Logger logger = Logger.getLogger(CMSTestBase.class);

	// ============Method For Reading Excel File and data================
	public String[][] readingexcelFiles(String sheetname) throws Exception {

		try {

			String FilePath = path + "/ExcelFile/API_inputs.xlsx";

			FileInputStream finputStream = new FileInputStream(new File(FilePath));

			workbook = new XSSFWorkbook(finputStream);

			sheet = workbook.getSheet(sheetname);

			colCount = sheet.getRow(0).getPhysicalNumberOfCells();
			// System.out.println("Columns"+ colCount);

			rowCount = sheet.getPhysicalNumberOfRows();
			// System.out.println("Rows"+ rowCount);

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

		}

		return excelData;
	}

	// ========Method For Reading Excel Sheet Based on SheetName=========

	public String[][] readingExcel(String sheetName) {

		logStep("Started reading data from Excel");

		try {

			FDIDNumbers = readingexcelFiles(sheetName);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return FDIDNumbers;
	}

	public Properties readPropertyFile() {
		prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(System.getProperty("user.dir") + "\\" + "ConfigProperties");
			// load a properties file
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
			workBook = new XSSFWorkbook(files);
		} catch (IOException e) {
			e.printStackTrace();
		}
		excelWorkBook.set(workBook);
		return workBook;
	}

	public String getDataFromExcel(int rownum, int colnum, String sheetName) throws Exception {
		workBook = excelWorkBook.get();
		sheet = workBook.getSheet(sheetName);
		XSSFCell cell = sheet.getRow(rownum).getCell(colnum);
		DataFormatter df = new DataFormatter();
		String data = df.formatCellValue(cell);
		return data;
	}

	// Function to load excel data in hashmap
	public HashMap<String, Integer> storeExcelDataInHashMap() {
		propHandler.set(readPropertyFile());
		HashMap<String, Integer> testCaseRailID = new HashMap<String, Integer>();
		initializeExcelSheet(prop.getProperty("TestRailExcelFile"));
		String sheetName = "ExecutionModel";
		workBook = excelWorkBook.get();
		sheet = workBook.getSheet(sheetName);
		int maxcount = sheet.getLastRowNum();
		System.out.println(maxcount);
		System.out.println(workBook);
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

	public void ConnectDBandExecuteESBQuery(int appNumber) throws Exception {

		readDatabaseCredentials(1);

		String[][] FDidList = readingExcel("FDIDNUMBERS");

		int lastRowexcel = lastRow;

		for (int i = 0; i <= lastRowexcel; i++) {

			FDID = FDidList[i][0].toString();

			System.out.println(FDID);

			String finalQuery = BuildQuery(FDID, appNumber);

			ConnectToESBDatabase(finalQuery, FDID);

			System.out.println("======================");

		}

	}

	public String BuildQuery(String FDID, int appNumber) {

		String queryString = sqlQueries(appNumber);

		String[] querySplits = queryString.split("IDNUMBER");

		String finalQuery = querySplits[0].toString() + FDID + querySplits[1].toString();

		Date date = new Date();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String currentDate = dateFormat.format(date);

		finalQuery = finalQuery.replaceAll("%s", currentDate);

		return finalQuery;

	}

	public void ConnectToESBDatabase(String queryString, String FDID)
			throws SQLException, ClassNotFoundException, Exception {

		Connection con = null;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");

			String URL = databaseURL;
			String Username = user;
			String Password = passWord;

			System.out.println("Driver Loaded");

			logStep("Driver Loaded");

			con = DriverManager.getConnection(URL, Username, Password);

			System.out.println(con);

			if (con != null) {
				// System.out.println("Connected to the Database...");
				logStep("Connected to the Database..");
				logger("Connected to the Database..");
			}

			Statement stmt = con.createStatement();

			System.out.println("Connection successfull" + stmt.toString());

		}

		catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e1) {
			e1.getMessage();
		}

		// step3 create the statement object
		Statement stmt = con.createStatement();
		// step4 execute query
		ResultSet rs = null;

		rs = stmt.executeQuery(queryString);
		logStep("Query executed successfully");
		logger("Query executed successfully");

		while (rs.next()) {

			ResultSetMetaData metaData = rs.getMetaData();

			int count = metaData.getColumnCount(); // number of column

			String columnName[] = new String[count];
			int line = 0;
			for (int data = 1; data <= count; data++) {

				columnName[data - 1] = metaData.getColumnLabel(data);

				armeta.add(columnName[data - 1]);
				listEvents.add(rs.getString(data));

				line = line + 1;

				if (line == 4) {
					ActualEvents.add(listEvents.toString());
					line = 0;
					listEvents.clear();
				}
				// errors.add(e)

			}

		}

		con.close();

	}

	public void logger(String string) {
		logger.info(string);
	}

	// ===========Method For Database Credentials Reading From the Sheet=========

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

	public String sqlQueries(int linenumber) {

		String[][] querieString = readingExcel("Queries");

		for (int num = 1; num <= lastRow; num++) {
			if (linenumber == num) {
				data = querieString[linenumber][0];

			}

		}
		return data;

	}

	@Step("{0}")

	public void logStep(String stepName) {

	}

	public WebDriver nonProdPortalLaunch() {

		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\Drivers\\chromedriver.exe");

		driver = new ChromeDriver();

		driver.manage().window().maximize();

		driver.get("https://nonprodportal.amcnetworks.com/Citrix/AppsWeb/");

		System.out.println(driver);

		return driver;

	}

	public void timing(WebElement accept) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOf(accept));

	}

	public Pattern fileReaders(String filename) {
		filename = System.getProperty("user.dir") + "\\" + filename;
		Pattern pattern = new Pattern(filename);
		return pattern;
	}

	@BeforeSuite(enabled = true)

	public void puhingRecords() throws Exception {
		System.out.println("Started Smkoe Test for CMSNV");

		WebDriver driver = nonProdPortalLaunch();

		ObjectRepository obr = new ObjectRepository(driver);

		obr.WelcomeAccept();

		logStep("Accepted Welcome screen");

		logger("Accepted Welcome screen");

		obr.detectReceiver();

		logStep("Selected detectReceiver");

		logger("Selected detectReceiver");

		obr.openCitrixLancher();

		logStep("Closed the Citrix Launcher");

		logger("Closed the Citrix Launcher");

		obr.alreadyInstalled();

		logStep("Closed the Citrix Launcher");

		logger("Closed the Citrix Launcher");

		obr.amcCredentials(1, "NonProdLoginCredentials");

		logStep("Entered the AMC Credentials");

		logger("Entered the AMC Credentials");

		obr.allappsbtn();

		logStep("Selected the all apps button");

		logger("Selected the all apps button");

		obr.cmsTest();

		logStep("Clicked on the CMS test application");

		logger("Clicked on the CMS test application");

		obr.cmsFileOpen();

		logStep("Selected the CMS file");

		logger("Selected the CMS file");

		obr.cmsCredentials();

		logStep("Entered the CMS credntials");

		logger("Entered the CMS credntials");

		obr.titleVersionSearch();

		logStep("Clicked the title verion button");

		logger("Clicked the title verion button");

		obr.serialNumberInCMS(0, "FDIDNUMBERS");

		logStep("FDID reading from excel sheet");

		logger("FDID reading from excel sheet");

		logStep("Entered FDID");

		logger("Entered FDID");

		obr.accepted();

		logStep("Accepted the title record");

		logger("Accepted the title record");

		obr.datMigrationTab();

		logStep("Navigated to data migration tab");

		logger("Navigated to data migration tab");

		obr.pushToMigrate();

		logStep("Pushed the record");

		logger("Pushed the record");

		obr.migrationRequest();

		logStep("Accepted the migration request");

		logger("Accepted the migration request");

		try {
			obr.migrationDate();

			logStep("Navigated to migration date");

			logger("Navigated to migration date");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		} finally {

			obr.mainMenu();

			logStep("Selected the main menu in cms file maker");

			logger("Selected the main menu in cms file maker");

			obr.quitFileMaker();

			logStep("Quit the file maker");

			logger("Quit the file maker");

			obr.acknowledgeFileMakerQuit();

			logStep("Acckonwledged the file maker ");

			logger("Acckonwledged the file maker ");

			obr.closingFilemaker();

			logStep("Closed the file maker");

			logger("Closed the file maker");

			obr.closingMainWindowFileMaker();

			logStep("Closed the file maker main window");

			logger("Closed the file maker main window");

			logStep("Waiting some time before fecching ESB events ");

			logger("Waiting some time before fecching ESB events ");

			Thread.sleep(60000);

			System.out.println("******************************");

			logger("Waiting Time is completed to fetch the ESB events ");
		}

	}

	@AfterSuite(enabled = true)

	public void closingbrowser() throws Exception {

		ObjectRepository obr = new ObjectRepository(driver);

		obr.logoutNonProdPortal();

		logStep("Nonprod portal logged out successfully ");

		logger("Nonprod portal logged out successfully ");

		browserClose();

		logger("Browser is Closed.. ");
	}

	public void browserClose() {

		driver.close();
	}

}
