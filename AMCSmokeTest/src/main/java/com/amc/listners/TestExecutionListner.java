package com.amc.listners;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import com.testBase.test.ExcelUtils;

public class TestExecutionListner extends ExcelUtils implements IMethodInterceptor {
	
	
	ExcelUtils excelutils = new ExcelUtils();
	
	// Loads the Excel sheet from ConfigProperties file
	// Loads the ExecutionModel sheet
	// Executes TestCase Which is Yes in Execute Column in ExecutionModel sheet.
	
	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
	
		Properties prop = propHandler.get();
		
		List<IMethodInstance> result = new ArrayList<IMethodInstance>();
		
		for (IMethodInstance method : methods) {
		
			workbook = excelutils.initializeExcelSheet(prop.getProperty("TestExecutionFileName"));
			
			XSSFSheet sheet;
			
			String sheetName = "ExecutionModel";
			
			sheet = workbook.getSheet(sheetName);
			
			int maxcount = sheet.getLastRowNum();
			
			try {
			
				for (int testcase = 1; testcase <= maxcount; testcase++) {
				
					String testCaseID = excelutils.getDataFromExcel(testcase, 0, sheetName);
					
					if (testCaseID.equalsIgnoreCase(method.getMethod().getMethodName())) {
					
						String row = excelutils.getDataFromExcel(testcase, 1, sheetName);
						
						if (row.equalsIgnoreCase("Yes")) {
						
							result.add(method);
						}
					}
				}
			
			} catch (Exception e) {
			
				System.out.println(e.getStackTrace());
			}
		}
		
		return result;
	}
}
