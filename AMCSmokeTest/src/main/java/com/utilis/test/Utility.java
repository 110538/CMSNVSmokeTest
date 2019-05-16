package com.utilis.test;

import java.util.ArrayList;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import com.cmstestbase.test.CMSTestBase;

public class Utility extends CMSTestBase {

	public String errorText;

	public ArrayList<String> rowData = new ArrayList<String>();

	// Calling the DB Connection and Query Execution functionality from CMSTestBase Class
	
	public void connectToEsbDatabase() throws Throwable {

		CMSTestBase cmstest = new CMSTestBase();

		cmstest.ConnectDBandExecuteESBQuery(1);

	}

	// Validating the Expected Events with Actual Events of ESB Database

	public void hierarchyValidation(String ExpectedEvents) {

		for (int i = 0; i <= CMSTestBase.ActualEvents.size() - 1; i++) {

			String rowdata = CMSTestBase.ActualEvents.get(i);

			// Condition Executes if Expected Events Should Match With Actual Events && Shouldn't have Exception && Shouldn't have .ACK Extension
			
			if (rowdata.contains(ExpectedEvents) && !rowdata.contains("Exception") && !rowdata.contains(".ACK")) {

				rowdata = rowdata.replace("[", "").replace("]", " ").replace(",", " ");

				String[] words = rowdata.split(" ");

				for (int j = 0; j <= words.length - 1; j++) {

					if (ExpectedEvents.equalsIgnoreCase(words[j])) {

						logStep("Expected :" + ExpectedEvents);

						logStep("Actual :" + words[j]);

						System.out.println("Expected :" + ExpectedEvents + "---->" + "Actual :" + words[j]);

						break;
					}
				}

		     // Condition Executes if Expected Events Should Match With Actual Events && Shouldn't have Exception && Should have .ACK Extension		
				
			} else if (rowdata.contains(ExpectedEvents) && !rowdata.contains("Exception") && rowdata.contains(".ACK")) {

				String errormsg = "Expected :" + ExpectedEvents + "---->" + "Actual -- But found error inthe DB";

				System.out.println("Error msg found : - " + errormsg);

				rowdata = rowdata.replace("[", "").replace("]", " ").replace(",", " ");

				String[] words = rowdata.split(" ");

				for (int k = 0; k < words.length - 1; k++) {

					if (ExpectedEvents.equalsIgnoreCase(words[k])) {

						logStep("Expected :" + ExpectedEvents + "---->" + "Actual :" + words[k]);

						System.out.println("Expected :" + ExpectedEvents + "---->" + "Actual :" + words[k]);

						break;

					}

				}

			}

		}

	}

	// Executes the WOP API According to the API POST/GET Calls
	// Executes the MP API Acoording to the API POST/GET Calls
	
	public void APIResponse(String sheetname, String RLID) throws Exception {

		readingexcelFiles(sheetname);

		String[][] dataBook = getDataFromExcel(workbook, sheetname);

		String ApiType = null;

		String GetValue = null;

		XSSFSheet sheet = workbook.getSheet(sheetname);

		int rowCount = sheet.getPhysicalNumberOfRows();

		finalApiType = rowCount;

		for (int exData = 1; exData <= rowCount; exData++) {

			finalApiCount = exData + 1;

			ApiType = dataBook[exData][1].toString();

			GetValue = dataBook[exData][5].toString();

			System.out.println("ApiType is : " + ApiType);

			System.out.println("GetValue is : " + GetValue);

			// Calls the API POST Method From CMSTestBase Class When API Type is Post and GetValue Not Equals Resutl
			
			if (ApiType.equalsIgnoreCase("Post") && !(GetValue.equalsIgnoreCase("Result"))) {

				testhttpclientforPost(exData, sheetname, RLID);

				readJson(exData, GetValue);

		     // Calls the API GET Method From CMSTestBase Class When API Type is Get and GetValue is Result
			
			} else if (ApiType.equalsIgnoreCase("GET") && GetValue.equalsIgnoreCase("Result")) {

				getapiExecute(String.valueOf(exData), sheetname, RLID);

				break;
            
			// Calls the API POST Method From CMSTestBase Class When API Type is Post and GetValue is Result
				
			} else if (ApiType.equalsIgnoreCase("Post") && GetValue.equalsIgnoreCase("Result")) {

				testhttpclientforPost(exData, sheetname, RLID);

				break;
			}
		}

	}

}
