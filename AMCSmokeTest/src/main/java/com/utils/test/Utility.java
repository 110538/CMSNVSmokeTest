package com.utils.test;

import java.util.ArrayList;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.testBase.test.ApiUtil;

import com.testBase.test.DatabaseUtil;
import com.testBase.test.ExcelUtils;

public class Utility extends ApiUtil {

	ApiUtil apiUtil = new ApiUtil();
	ExcelUtils excelUtils = new ExcelUtils();
	DatabaseUtil databaseUtil = new DatabaseUtil();
	public String errorText;

	public ArrayList<String> rowData = new ArrayList<String>();

	// Calling the DB Connection and Query Execution functionality from CMSTestBase Class
	
	public void connectToEsbDatabase() throws Throwable {

		try {databaseUtil.ConnectDBandExecuteESBQuery(1);
		}
		
		catch(Exception e) {
		
			System.out.println(e.getMessage());
		}

	}

	// Validating the Expected Events with Actual Events of ESB Database

	public void hierarchyValidation(String ExpectedEvents) {

		for (int i = 0; i <= databaseUtil.ActualEvents.size() - 1; i++) {

			String rowdata = databaseUtil.ActualEvents.get(i);

			// Condition Executes if Expected Events Should Match With Actual Events && Shouldn't have Exception && Shouldn't have .ACK Extension
			
			if (rowdata.contains(ExpectedEvents) && !rowdata.contains("Exception") && !rowdata.contains(".ACK")) {

				rowdata = rowdata.replace("[", "").replace("]", " ");

				String[] words = rowdata.split(" ");

				for (int j = 0; j <= words.length - 1; j++) {

					if (ExpectedEvents.equalsIgnoreCase(words[j].trim())) {

						logStep("Expected :" + ExpectedEvents);

						logStep("Actual :" + words[j]);
						
						System.out.println("-----------------------------------------------------------------");

						System.out.println("Expected :" + ExpectedEvents + "---->" + "Actual :" + words[j]);
						
						System.out.println("-----------------------------------------------------------------");

						break;
					}
				}

		     // Condition Executes if Expected Events Should Match With Actual Events && Shouldn't have Exception && Should have .ACK Extension		
				
			} else if (rowdata.contains(ExpectedEvents) && !rowdata.contains("Exception") && rowdata.contains(".ACK")) {

				rowdata = rowdata.replace("[", "").replace("]", " ").replace(",", " ");

				String[] words = rowdata.split(" ");

				for (int k = 0; k < words.length - 1; k++) {

					if (ExpectedEvents.equalsIgnoreCase(words[k].trim())) {

						logStep("Expected :" + ExpectedEvents + "---->" + "Actual :" + words[k]);
						
						System.out.println("-----------------------------------------------------------------");

						System.out.println("Expected :" + ExpectedEvents + "---->" + "Actual :" + words[k]);
						
						System.out.println("-----------------------------------------------------------------");

						break;

					}

				}

			}

		}
         
	}

	// Executes the WOP API According to the API POST/GET Calls
	// Executes the MP API Acoording to the API POST/GET Calls
	
	public void APIResponse(String sheetname, String RLID) throws Exception {

		excelUtils.readingexcelFiles(sheetname);

		String[][] dataBook = excelUtils.getDataFromExcel(ExcelUtils.workbook, sheetname);

		String ApiType = null;

		String GetValue = null;

		XSSFSheet sheet = ExcelUtils.workbook.getSheet(sheetname);

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

				apiUtil.testhttpclientforPost(exData, sheetname, RLID);

				apiUtil.readJson(exData, GetValue);

		     // Calls the API GET Method From CMSTestBase Class When API Type is Get and GetValue is Result
			
			} else if (ApiType.equalsIgnoreCase("GET") && GetValue.equalsIgnoreCase("Result")) {

				apiUtil.getapiExecute(String.valueOf(exData), sheetname, RLID);

				break;
            
			// Calls the API POST Method From CMSTestBase Class When API Type is Post and GetValue is Result
				
			} else if (ApiType.equalsIgnoreCase("Post") && GetValue.equalsIgnoreCase("Result")) {

				apiUtil.testhttpclientforPost(exData, sheetname, RLID);

				break;
			}
		}

	}

}
