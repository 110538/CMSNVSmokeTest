package com.cmstestbase.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
//import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.Pattern;
import org.testng.Assert;
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
	public String jsonPrettyPrintString = null;
	public String passWord;
	public int linenumber;
	public String data;
	public static ArrayList<String> ActualEvents = new ArrayList<String>();
	public static ArrayList<String> SourceEvents = new ArrayList<String>();
	public static ArrayList<String> listEvents = new ArrayList<String>();
	public static ArrayList<String> sourceList = new ArrayList<String>();
	public static ArrayList<String> errors = new ArrayList<String>();
	public static ArrayList<String> armeta = new ArrayList<String>();
	public String FDID;
	public static WebDriver driver;
	private Properties prop = null;
	public static ThreadLocal<Properties> propHandler = new ThreadLocal<Properties>();
	public static ThreadLocal<XSSFWorkbook> excelWorkBook = new ThreadLocal<XSSFWorkbook>();
	public JSONObject jsonObj;
	public static JSONObject mpJsonObj;
	public JSONObject jsonObjEmpty = new JSONObject();
	public static JSONObject wopJsonObj;
	public static JSONObject sourceJsonObj;
	public static JSONObject metadata;
	public String apirequest = "no request";
	public StringBuffer textView = new StringBuffer();
	public StringBuffer textView_1 = new StringBuffer();
	public StringBuffer textView_2 = new StringBuffer();
	public StringBuffer sourceText = new StringBuffer();
	public String text2 = null;
	public String text1 = null;
	public String text = null;
	public int testline;
	public static String accessToken = "no value";
	public String token = "no value";
	public String paramText;
	public String paramName;
	public String pName;
	public ArrayList<String> paramvalues = new ArrayList<String>();
	public static ArrayList<String> wopParamvalues = new ArrayList<String>();
	public static ArrayList<String> mpParamvalues = new ArrayList<String>();
	public static ArrayList<String> SourceParamvalues = new ArrayList<String>();
	public ArrayList<NameValuePair> postParameters;
	public List<NameValuePair> nameValuePairs;
	public String[] apirequests;
	public String excelSheet;
	public static String externalFileName;
	public static String[][] dataBook;
	public static String RLID;
	public static int finalApiType;
	public static int finalApiCount;
	//public Logger logger = Logger.getLogger(CMSTestBase.class);

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

		logStep("Started reading data from Excel");

		try {

			FDIDNumbers = readingexcelFiles(sheetName);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return FDIDNumbers;
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

	// Reads the Data from Excel sheet.

	public String getDataFromExcel(int rownum, int colnum, String sheetName) throws Exception {

		workbook = excelWorkBook.get();

		sheet = workbook.getSheet(sheetName);

		XSSFCell cell = sheet.getRow(rownum).getCell(colnum);

		DataFormatter df = new DataFormatter();

		String data = df.formatCellValue(cell);

		return data;
	}

	// Loads the Excel Workbook
	// Loads the ExecutionModel sheet and Reads the Data

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

	// Reading FDID Numbers From Excel Sheet and Passing Into BuildQuery Method
	// Giving finalQuery With FDID Numbers into ConnectToESBDatabase Method

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

	// FDID Numbers are Passed to parameterised ESB Query
	// Giving System Current Date to parameterised ESB Query
	// Changing IST Time to EST Time 

	public String BuildQuery(String FDID, int appNumber) throws Exception {

		String queryString = sqlQueries(appNumber);

		String[] querySplits = queryString.split("IDNUMBER");

		String finalQuery = querySplits[0].toString() + FDID + querySplits[1].toString();

		Date date = new Date();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String currentDate = dateFormat.format(date);

		String dateInString = currentDate;

		Date datenew = dateFormat.parse(dateInString);        

		// To TimeZone America/New_York

		SimpleDateFormat sdfAmerica = new SimpleDateFormat("yyyy-MM-dd");

		TimeZone tzInAmerica = TimeZone.getTimeZone("America/New_York");

		sdfAmerica.setTimeZone(tzInAmerica);

		String sDateInAmerica = sdfAmerica.format(datenew); // Convert to String first

		Date dateInAmerica = dateFormat.parse(sDateInAmerica); // Create a new Date object

		String newcurrentDate = dateFormat.format(dateInAmerica);


		finalQuery = finalQuery.replaceAll("%s", newcurrentDate);
		

		return finalQuery;

	}

	// DB Connection
	// Query Execution
	// Events Storing into ActualEvents Array.

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

				logStep("Connected to the Database..");

				//logger("Connected to the Database..");
			}

			Statement stmt = con.createStatement();

			System.out.println("Connection successfull" + stmt.toString());

		}

		catch (SQLException e) {

			System.out.println(e.getMessage());

		} catch (ClassNotFoundException e1) {

			e1.getMessage();
		}

		Statement stmt = con.createStatement();

		ResultSet rs = null;

		rs = stmt.executeQuery(queryString.trim());

		logStep("Query executed successfully");

		//logger("Query executed successfully");

		int armetacount=0;
		int loop = 2;
		outer:
			while (rs.next()) {
				armetacount=armetacount+1;
				ResultSetMetaData metaData = rs.getMetaData();

				int count = metaData.getColumnCount(); // number of column

				String columnName[] = new String[count];

				int line = 0;

				for (int data = 1; data <= count; data++) {

					columnName[data - 1] = metaData.getColumnLabel(data);

					if(armetacount==1) {

						armeta.add(columnName[data - 1]);
					}
					// Adding the ESB Events in listEvents ArryList
					if(data!=3) {
						listEvents.add(rs.getString(data));
					}
					if(data>2) {
						sourceList.add(rs.getString(data));
					}
					line = line + 1;



					if (line == 4) {

						// Adding the lisEvents to ActualEvents ArryList

						ActualEvents.add(listEvents.toString());
						listEvents.clear();
						if (sourceList.contains("AMCN.LCS.EVENT.GENERATOR.TO.XSLT.STAGE1") && loop == 2) {

							SourceEvents.add(sourceList.toString());

							// Storing the CMSNV ESB Payload Into a sourceData varaible
							sourceList.clear();

							String sourceData = SourceEvents.get(SourceEvents.size() - 1);

							SourceEvents.clear();

							listEvents.clear();

							if(loop==2) {
								String start = "<NS1:payload>";

								sourceData = sourceData.substring(sourceData.indexOf("<NS1:payload>") + start.length(),
										sourceData.indexOf("</NS1:payload>"));


								System.out.println("Source xml  is :- " + sourceData);

								sourceJsonObj = new JSONObject(sourceData.toString());
								sourceData = null;
								readingexcelFiles("source");

								dataBook = getDataFromExcel(workbook, "source");

								XSSFSheet sheet = workbook.getSheet("source");

								int rowCount = sheet.getPhysicalNumberOfRows();

								for (int i = 1; i <= rowCount - 1; i++) {

									// Calling the GetValue Method to Read the CMSNV payload fields From Excel sheet

									getValue("source", dataBook[i][0].toString());

								}
							}
							loop = loop + 1;

							line = 0;


							//break outer;
						}else {
							sourceList.clear();
						}
					}

				}

			}

		con.close();

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

	// ========Method For API Get Call==============

	public JSONObject getapiExecute(String lineNumbers, String sheetname, String RLID) throws Exception {

		textView.delete(0, textView.length());

		textView_1.delete(0, textView_1.length());

		// Local Variables
		String[] cTypes;

		String[] hTypes;

		logStep("Started executing API Testing");

		String[] lines;

		if (lineNumbers.contains(",")) {

			lines = lineNumbers.split(",");

		} else {

			lines = lineNumbers.split(" ");
		}

		for (int lin = 1; lin <= lines.length; lin++) {

			int lineNumber = Integer.parseInt(lines[lin - 1]);

			testline = lineNumber;

			readingExcel(sheetname);

			logStep("Verify Api Type is Get  or Post");

			if (excelData[lineNumber][1].toString().equalsIgnoreCase("GET")) {

				logStep("Given url is Get API continue execution");

				HttpClient client = HttpClients.createDefault();

				logStep("Given API is : " + excelData[lineNumber][0]);

				HttpGet request = new HttpGet(excelData[lineNumber][0]);

				String cType = excelData[lineNumber][2].toString();

				String hType = excelData[lineNumber][3].toString().replace("%s", RLID);

				if (cType.contains(",")) {

					cTypes = cType.split(",");

					hTypes = hType.split(",");
				} else {

					cTypes = cType.split(" ");

					hTypes = hType.split(" ");
				}
				for (int i = 0; i <= cTypes.length - 1; i++) {

					if (cTypes[i].trim().contains("Authorization")) {

						logStep("Using Access tokena as Bearer : " + accessToken);

						logStep("Headers are: : " + cTypes[i] + "------" + hTypes[i] + " " + accessToken);

						request.addHeader(cTypes[i], hTypes[i] + " " + accessToken);
					} else if (cTypes[i].trim().contains("auth2")) {

						logStep("Using Access tokena as Bearer : " + token);

						logStep("Headers are: : " + cTypes[i] + "------" + hTypes[i] + " " + token);

						request.addHeader(cTypes[i], hTypes[i] + " " + token);
					} else {

						logStep("Headers are: : " + cTypes[i] + "------" + hTypes[i]);

						request.addHeader(cTypes[i], hTypes[i]);
					}
					System.out.println("Headers are: : " + cTypes[i] + "------" + hTypes[i]);
				}

				HttpResponse response = client.execute(request);

				logStep("Execution Completed and reading response now");

				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				logStep("Verify Status code in response");

				if (response.getStatusLine().getStatusCode() != 200) {

					RuntimeException error = new RuntimeException(

							"Failed : HTTP error code : " + response.getStatusLine().getStatusCode());

					System.out.println(error);

					logStep(error.toString());

					throw new RuntimeException(
							"Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
				} else {

					String line = "";

					while ((line = rd.readLine()) != null) {

						textView_1.append(line);

					}
					if (textView_1.toString().contains("xml")) {

						text1 = convertxmltoJson(textView_1.toString());

					} else if (!textView_1.toString().contains("xml")) {

						jsonObj = new JSONObject();

						text1 = textView_1.toString();

						if (text1.startsWith("[")) {

							textView_1.deleteCharAt(0);

							textView_1.deleteCharAt(textView_1.length() - 1);

							text1 = textView_1.toString();
						}

						System.out.println("response  is : =========" + text1.toString());
					}

					jsonObj = new JSONObject(text1.toString());

					System.out.println(jsonObj.toString());

					if (sheetname.contains("APIMP")) {

						mpJsonObj = jsonObj;

					} else if (sheetname.contains("WOPAPI")) {

						wopJsonObj = jsonObj;

					}

				}

			}
		}

		// Reads the WOPData based the WOP sheetname
		// Reads the MPData bases on the MP sheetname

		if (finalApiType == finalApiCount) {

			if (sheetname.contains("WOP")) {

				sheetname = "wopData";

				sourceJsonObj = wopJsonObj;

			} else if (sheetname.contains("MP")) {

				sheetname = "mpData";

				sourceJsonObj = mpJsonObj;
			}

			readingexcelFiles(sheetname);

			dataBook = getDataFromExcel(workbook, sheetname);

			XSSFSheet sheet = workbook.getSheet(sheetname);

			int rowCount = sheet.getPhysicalNumberOfRows();

			for (int i = 1; i <= rowCount - 1; i++) {

				// Calling the getValue Method to Read the WOPData
				// Calling the getValue Method to Read the MPData

				getValue(sheetname, dataBook[i][3].toString());

			}
		}

		return jsonObj;
	}

	// Clearing the JSon Object

	public void clearJSon() {

		jsonObj = jsonObjEmpty;

		textView_1 = textView_1.delete(0, textView_1.length());

		paramvalues.clear();
	}

	// Method For Clearing the JSon Object

	public void clearJSonObj() {

		mpJsonObj = jsonObjEmpty;

		wopJsonObj = jsonObjEmpty;

	}

	// ========Method For API Post Call===========

	public JSONObject testhttpclientforPost(int lineNumber, String sheetname, String RLID) throws Exception {

		readingExcel(sheetname);

		clearJSon();

		logStep("Get the details from Excel");

		String reqExcel = excelData[lineNumber][4].toString();

		addparamvaluesinRequest(lineNumber, excelData[lineNumber][4].toString(), sheetname, RLID);

		logStep("Http Client service is invoked");

		HttpClient client = HttpClients.createDefault();

		// Executing the API POST Method using URL , Content Type, Header , XML Request

		HttpPost post = new HttpPost(excelData[lineNumber][0].toString());

		String[] cTypes;

		String[] hTypes;
		try {
			if (apirequest.equals("no request")) {

				apirequest = excelData[lineNumber][4].toString();

				//logStep("apirequest is  : --" + apirequest);

			} else if (apirequest.contains("&") && !reqExcel.equalsIgnoreCase("externalFile")) {

				logStep("apirequest is  : --" + apirequest);

				nameValuePairs = new ArrayList<NameValuePair>();

				postParameters = new ArrayList<NameValuePair>();

				String[] params;

				apirequests = apirequest.split("&");

				for (String param : apirequests) {

					if (param.contains("=")) {

						params = param.split("=");

						nameValuePairs.add(new BasicNameValuePair(params[0], params[1]));

						postParameters.add(new BasicNameValuePair(params[0], params[1]));

					}
				}
				//logStep("Executing request with http Post method");

				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} else {

				//logStep("apirequest is  : --" + apirequest);

				StringEntity entity = new StringEntity(apirequest);

				post.setEntity(entity);

				String cType = excelData[lineNumber][2].toString();

				String hType = excelData[lineNumber][3].toString();

				if (cType.contains(",")) {

					cTypes = cType.split(",");

					hTypes = hType.split(",");

				} else {

					cTypes = cType.split(" ");

					hTypes = hType.split(" ");
				}
				for (int i = 0; i <= cTypes.length - 1; i++) {

					//logStep("Using Access tokena as Bearer : " + token);

					if (cTypes[i].trim().contains("Authorization")) {

						//logStep("Using Access tokena as Bearer : " + accessToken);

						//logStep("Headers are: : " + cTypes[i] + "------" + hTypes[i] + " " + accessToken);

						System.out.println(cTypes[i] + "," + hTypes[i] + "-- " + accessToken);

						post.addHeader(cTypes[i], hTypes[i] + " " + accessToken);

					} else if (cTypes[i].trim().contains("auth2")) {

						//logStep("Using Access tokena as Bearer : " + token);

						//logStep("Headers are: : " + cTypes[i] + "------" + hTypes[i] + " " + token);

						post.addHeader(cTypes[i], hTypes[i] + " " + token);

					} else {

						logStep("Headers are: : " + cTypes[i] + "------" + hTypes[i]);

						post.addHeader(cTypes[i], hTypes[i]);
					}
				}

				//logStep("Executing post API with Http Post method");
			}

			HttpResponse response = client.execute(post);

			//logStep("Response is : " + response);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			// Prints the API Response Status
			if (response.getStatusLine().getStatusCode() != 200) {

				RuntimeException error = new RuntimeException(
						"Failed : HTTP error code : " + response.getStatusLine().getStatusCode());

				System.out.println(error);

				logStep(error.toString());

				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());

			} else {

				logStep("API Executed Successully : ");

				String line = "";

				while ((line = rd.readLine()) != null) {

					textView_1.append(line);

				}
				if (textView_1.toString().contains("xml")) {

					text1 = convertxmltoJson(textView_1.toString());

				} else if (!textView_1.toString().contains("xml")) {

					jsonObj = new JSONObject();

					text1 = textView_1.toString();

					if (text1.startsWith("[")) {

						textView_1.deleteCharAt(0);

						textView_1.deleteCharAt(textView_1.length() - 1);

						text1 = textView_1.toString();
					}

					System.out.println("response  is : =========" + textView_1.toString());
				}
				jsonObj = new JSONObject(text1);

				System.out.println(jsonObj.toString());

				if (sheetname.contains("MP")) {

					mpJsonObj = jsonObj;

				} else if (sheetname.contains("WOP")) {

					wopJsonObj = jsonObj;
				}
			}

			logStep("API Esecuted Successfully");

			System.out.println("Api Executed successfully");

			logStep(jsonObj.toString());

		} catch (IOException e) {

			e.printStackTrace();
		}

		// Reads the WOPData based the WOP sheetname
		// Reads the MPData bases on the MP sheetname

		if (finalApiType == finalApiCount) {

			if (sheetname.contains("WOP")) {

				sheetname = "wopData";

				sourceJsonObj = wopJsonObj;

			} else if (sheetname.contains("MP")) {

				sheetname = "mpData";

				sourceJsonObj = mpJsonObj;
			}

			readingexcelFiles(sheetname);

			dataBook = getDataFromExcel(workbook, sheetname);

			XSSFSheet sheet = workbook.getSheet(sheetname);

			int rowCount = sheet.getPhysicalNumberOfRows();

			for (int i = 1; i <= rowCount - 1; i++) {

				getValue(sheetname, dataBook[i][3].toString());

			}
		}

		return jsonObj;
	}

	// Convert XML response to JSON Response

	public String convertxmltoJson(String xmlString) {

		try {

			JSONObject xmlJSONObj = XML.toJSONObject(xmlString);

			jsonPrettyPrintString = xmlJSONObj.toString();

			System.out.println(jsonPrettyPrintString);

		} catch (JSONException je) {

			System.out.println(je.toString());
		}

		return jsonPrettyPrintString;
	}

	// Method For File Reader

	public void readFile(String path) throws IOException {

		File file = new File(path);

		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String line = "";

			while ((line = br.readLine()) != null) {

				sb.append(line);

				sb.append("\n");
			}
		}

		apirequest = convertxmltoJson(sb.toString());

	}

	// Add Parameter Values in Request Of Next API Call.

	public void addparamvaluesinRequest(int lineNumber, String jsonValue, String sheetName, String RLID)
			throws IOException {

		readingExcel(excelSheet);

		apirequest = excelData[lineNumber][4].toString().replaceAll("%s", RLID);

		if (apirequest.equalsIgnoreCase("externalFile")) {

			if (externalFileName.contains("Media_Asset")) {

				readFile(path + "\\Media_Asset.xml");

			} else if (externalFileName.contains("Component_Data")) {

				readFile(path + "\\Component_Data.xml");

			} else {

				System.out.println("Api Request from Excel");

				//logStep("Checkes request details from Excel");

				try {

					//JSONParser parser = new JSONParser();

					JSONObject jObject = new JSONObject(apirequest);

					logStep("Request in Json formate");

					JSONObject maintag = (JSONObject) jObject.getJSONObject("request");

					metadata = null;

					String[] metaValues;

					if (jsonValue.contains(",")) {

						metaValues = jsonValue.split(",");

					} else {

						metaValues = jsonValue.split(" ");
					}

					//logStep("Try ing to add values to request");

					for (int i = 0; i <= metaValues.length - 1; i++) {

						if (accessToken.equals("no value") || token.equals("no value")) {

							System.out.println("No Token values are present");

						} else {

							if (jsonValue.contains(metaValues[i])) {

								metadata = maintag.getJSONObject("metadata").put(metaValues[i], accessToken);

							} else if (jsonValue.contains("token")) {

								metadata = maintag.getJSONObject("metadata").put(metaValues[i], token);
							}
						}
					}

					apirequest = jObject.toString();

					//logStep("Edited request is : ----" + apirequest);

				} catch (Exception e) {

					logStep("request is not in JSON formate : --" + apirequest);

				}
			}
		}

		System.out.println("metadata -- :" + apirequest);
	}

	// Reads the JSON response

	public void readJson(int lineNumber, String getValue) {

		if (getValue.equalsIgnoreCase("access_token")) {

			accessToken = (String) jsonObj.get("access_token");

			//logStep(getValue + " value is :" + accessToken);

			System.out.println(getValue + " value is :" + accessToken);

		} else if (getValue.equalsIgnoreCase("token")) {

			token = (String) jsonObj.get("token");

			//logStep(getValue + " value is :" + token);

			System.out.println(getValue + " value is :" + token);
		}
	}

	// Reading JsonType From Excel Sheet
	// Reading API Parameters From Excel Sheet

	public void getValue(String JsonType, String Param) throws Exception, IOException {

		readingexcelFiles(JsonType);

		String[][] dataBook = getDataFromExcel(workbook, JsonType);

		//logger("Started reading Responses from " + JsonType + " API ");

		String paramvalue;

		String jsontypes = null;

		String[] jTypes;

		if (JsonType.equalsIgnoreCase("MPData")) {

			jsonObj = mpJsonObj;

		} else if (JsonType.equalsIgnoreCase("WOPData")) {

			jsonObj = wopJsonObj;

		} else if (JsonType.equalsIgnoreCase("source")) {

			jsonObj = sourceJsonObj;
		}

		XSSFSheet sheet = workbook.getSheet(JsonType);

		int rowCount = sheet.getPhysicalNumberOfRows();

		for (int excellist = 1; excellist <= rowCount - 1; excellist++) {

			paramName = dataBook[excellist][3].toString();

			paramvalue = dataBook[excellist][0].toString();

			if (Param.equalsIgnoreCase(paramName)) {

				//logger("Validating " + Param + " param name in " + JsonType + " API");

				pName = paramName;

				jsontypes = dataBook[excellist][2].toString();

				if (jsontypes.contains(",")) {

					jTypes = jsontypes.split(",");

				} else {

					jTypes = jsontypes.split("  ");
				}

				for (int i = 0; i <= jTypes.length - 1; i++) {

					if (jTypes[i].toString().contains("direct")) {

						paramText = jsonObj.get(paramvalue).toString();

						break;

					} else {

						Object obj_reult = jsonObj.get(jTypes[i].toString());

						JSONArray Obj_resultArray = null;

						if (obj_reult instanceof JSONObject) {

							jsonObj = (JSONObject) obj_reult;

						} else {

							Obj_resultArray = (JSONArray) jsonObj.get(jTypes[i].toString());

							i = i + 1;

							jsonObj = (JSONObject) Obj_resultArray.get(Integer.parseInt(jTypes[i].toString()));

						}
					}
				}

				paramText = jsonObj.get(paramvalue).toString();

				if (paramName.equalsIgnoreCase("RLA_RL_ASSET_ID")) {

					RLID = paramText;
				}

				int loopcount = 0;

				if (JsonType.equals("source")) {

					SourceParamvalues.add(paramText);

					// logger("Source Param values are ------ : " + SourceParamvalues);

				} else if (JsonType.equalsIgnoreCase("wopdata")) {

					for (String source : SourceParamvalues) {
						if(paramText.equals("")) {
							paramText="null";
						}
						if (paramText.equals(source)) {

							logStep("CMSNV " + paramName + " ( " + source + " ) is Matched with WOP " + paramName
									+ " ( " + paramText + " )");
							//logger("CMSNV " + paramName + " ( " + source + " ) is Matched with WOP " + paramName + " ( "
							//+ paramText + " )");

							System.out.println("CMSNV " + paramName + " ( " + source + " ) is Matched with WOP "
									+ paramName + " ( " + paramText + " )");
							break;

						} else {

							loopcount = loopcount + 1;

							if (loopcount == SourceParamvalues.size()) {

								logStep("CMSNV " + paramName + " ( " + source + " ) is not Matched with WOP "
										+ paramName + " ( " + paramText + " )");

								Assert.fail("CMSNV " + paramName + " ( " + source + " ) is not Matched with WOP "
										+ paramName + " ( " + paramText + " )");
							}
						}
					}

					wopParamvalues.add(paramText);

				} else if (JsonType.equalsIgnoreCase("mpdata")) {

					for (String source : SourceParamvalues) {

						if (paramText.equals(source)) {

							logStep("CMSNV " + paramName + " ( " + source + " ) is Matched with MP " + paramName + " ( "
									+ paramText + " )");

							//logger("CMSNV " + paramName + " ( " + source + " ) is Matched with MP " + paramName + " ( "
							//	+ paramText + " )");

							System.out.println("CMSNV " + paramName + " ( " + source + " ) is Matched with MP "
									+ paramName + " ( " + paramText + " )");
							break;

						} else {

							loopcount = loopcount + 1;

							if (loopcount == SourceParamvalues.size()) {

								logStep("CMSNV " + paramName + " ( " + source + " ) is not Matched with MP " + paramName
										+ " ( " + paramText + " )");

								Assert.fail("CMSNV " + paramName + " ( " + source + " ) is not Matched with MP "
										+ paramName + " ( " + paramText + " )");
							}
						}
					}

					mpParamvalues.add(paramText);

				} else {

					paramvalues.add(paramText);

				}

				break;

			}
		}

	}

	// Print the Console Logs

	public void logger(String string) {

		//logger.info(string);
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

	// Generates the Allure Reporting

	@Step("{0}")

	public void logStep(String stepName) {

	}

	// Browser Launching
	// Hits the nonprodportal.amcnetworks.com

	public WebDriver nonProdPortalLaunch() {

		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\Drivers\\chromedriver.exe");

		driver = new ChromeDriver();

		driver.manage().window().maximize();

		driver.get("https://nonprodportal.amcnetworks.com/Citrix/AppsWeb/");

		System.out.println(driver);

		return driver;

	}

	// Explicit Wait to balance between the Browser and WebElements

	public void timing(WebElement accept) {

		WebDriverWait wait = new WebDriverWait(driver, 20);

		wait.until(ExpectedConditions.visibilityOf(accept));

	}

	// Reads the Sikuli pictures folder path

	public Pattern fileReaders(String filename) {

		filename = System.getProperty("user.dir") + "\\" + filename;

		Pattern pattern = new Pattern(filename);

		return pattern;
	}

	// Selects required WebElements which are in nonprodportal website
	// Launches the Filemaker
	// Enter the FDID in Preparation tab of CMSNV Test Applciation
	// Navigates to Data Migartion Tab
	// Push the Record
	// Closes the File Maker

	@BeforeSuite(enabled = false)

	public void puhingRecords() throws Exception {

		System.out.println("Started Smkoe Test for CMSNV");

		WebDriver driver = nonProdPortalLaunch();

		ObjectRepository obr = new ObjectRepository(driver);

		obr.WelcomeAccept();

		logStep("Accepted Welcome screen");

		//logger("Accepted Welcome screen");

		obr.detectReceiver();

		logStep("Selected detectReceiver");

		//logger("Selected detectReceiver");

		obr.openCitrixLancher();

		logStep("Closed the Citrix Launcher");

		//logger("Closed the Citrix Launcher");

		obr.alreadyInstalled();

		logStep("Closed the Citrix Launcher");

		//logger("Closed the Citrix Launcher");

		obr.amcCredentials(1, "NonProdLoginCredentials");

		logStep("Entered the AMC Credentials");

		//logger("Entered the AMC Credentials");

		obr.allappsbtn();

		logStep("Selected the all apps button");

		//logger("Selected the all apps button");

		obr.cmsTest();

		logStep("Clicked on the CMS test application");

		//logger("Clicked on the CMS test application");

		obr.cmsFileOpen();

		logStep("Selected the CMS file");

		//logger("Selected the CMS file");

		obr.cmsCredentials();

		logStep("Entered the CMS credntials");

		//logger("Entered the CMS credntials");

		obr.titleVersionSearch();

		logStep("Clicked the title verion button");

		//logger("Clicked the title verion button");

		obr.serialNumberInCMS(0, "FDIDNUMBERS");

		logStep("FDID reading from excel sheet");

		//logger("FDID reading from excel sheet");

		logStep("Entered FDID");

		//logger("Entered FDID");

		obr.accepted();

		logStep("Accepted the title record");

		//logger("Accepted the title record");

		obr.datMigrationTab();

		logStep("Navigated to data migration tab");

		//logger("Navigated to data migration tab");

		obr.pushToMigrate();

		logStep("Pushed the record");

		//logger("Pushed the record");

		obr.migrationRequest();

		logStep("Accepted the migration request");

		//logger("Accepted the migration request");

		try {
			obr.migrationDate();

			logStep("Navigated to migration date");

			//logger("Navigated to migration date");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		} finally {

			obr.mainMenu();

			logStep("Selected the main menu in cms file maker");

			//logger("Selected the main menu in cms file maker");

			obr.quitFileMaker();

			logStep("Quit the file maker");

			//logger("Quit the file maker");

			obr.acknowledgeFileMakerQuit();

			logStep("Acckonwledged the file maker ");

			//logger("Acckonwledged the file maker ");

			obr.closingFilemaker();

			logStep("Closed the file maker");

			//logger("Closed the file maker");

			obr.closingMainWindowFileMaker();

			logStep("Closed the file maker main window");

			//logger("Closed the file maker main window");

			logStep("Waiting some time before fecching ESB events ");

			//logger("Waiting some time before fecching ESB events ");

			Thread.sleep(60000);

			System.out.println("******************************");

			//logger("Waiting Time is completed to fetch the ESB events ");
		}

	}

	// Logout From Nonprodportal Application
	// Closes the Browser

	@AfterSuite(enabled = false)

	public void closingbrowser() throws Exception {

		ObjectRepository obr = new ObjectRepository(driver);

		obr.logoutNonProdPortal();

		logStep("Nonprod portal logged out successfully ");

		//logger("Nonprod portal logged out successfully ");

		browserClose();

		logger("Browser is Closed.. ");
	}

	// Closes the Driver

	public void browserClose() {

		driver.close();
	}

}
