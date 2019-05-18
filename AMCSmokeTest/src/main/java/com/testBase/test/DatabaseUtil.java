package com.testBase.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.json.JSONObject;

public class DatabaseUtil extends TestBase {
	
	public String databaseURL;	
	public String passWord;
	public ArrayList<String> ActualEvents = new ArrayList<String>();
	public static ArrayList<String> SourceEvents = new ArrayList<String>();
	public static ArrayList<String> sourceList = new ArrayList<String>();
	public static ArrayList<String> listEvents = new ArrayList<String>();
	public static ArrayList<String> errors = new ArrayList<String>();
	public static ArrayList<String> armeta = new ArrayList<String>();
	public String[][] FDIDNumbers;
	public String FDID;
	public String user;
	public static String[][] dataBook;
	public static JSONObject sourceJsonObj;
	
	
	ApiUtil apiUtil = new ApiUtil();
	ExcelUtils excelUtils = new ExcelUtils();
	
	// Reading FDID Numbers From Excel Sheet and Passing Into BuildQuery Method
		// Giving finalQuery With FDID Numbers into ConnectToESBDatabase Method

		public void ConnectDBandExecuteESBQuery(int appNumber) throws Exception {

			excelUtils.readDatabaseCredentials(1);

			String[][] FDidList = excelUtils.readingExcel("FDIDNUMBERS");

			int lastRowexcel = excelUtils.lastRow;

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

			String queryString = excelUtils.sqlQueries(appNumber);

			String[] querySplits = queryString.split("IDNUMBER");

			String finalQuery = querySplits[0].toString() + FDID + querySplits[1].toString();

			System.out.println("");

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

				String URL = excelUtils.databaseURL;

				String Username =excelUtils.user;

				String Password = excelUtils.passWord;

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
									
									excelUtils.readingexcelFiles("source");
           
									System.out.println(" ");				
									
									dataBook = excelUtils.getDataFromExcel(ExcelUtils.workbook, "source");

									XSSFSheet sheet = ExcelUtils.workbook.getSheet("source");

									int rowCount = sheet.getPhysicalNumberOfRows();

									for (int i = 1; i <= rowCount - 1; i++) {

										// Calling the GetValue Method to Read the CMSNV payload fields From Excel sheet

										apiUtil.getValue("source", dataBook[i][0].toString());

									}
								}
								loop = loop + 1;

								line = 0;
							}else {
								sourceList.clear();
							}
						}

					}

				}

			con.close();

		}

		
		
		
		
}
