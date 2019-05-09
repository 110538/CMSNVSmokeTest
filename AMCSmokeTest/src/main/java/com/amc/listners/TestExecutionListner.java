package com.amc.listners;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import com.cmstestbase.test.CMSTestBase;

public class TestExecutionListner extends CMSTestBase implements IMethodInterceptor {
	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		Properties prop = propHandler.get();
		List<IMethodInstance> result = new ArrayList<IMethodInstance>();
		for (IMethodInstance method : methods) {
			initializeExcelSheet(prop.getProperty("TestExecutionFileName"));
			XSSFSheet sheet;
			XSSFWorkbook workBook;
			String sheetName = "ExecutionModel";
			workBook = excelWorkBook.get();
			sheet = workBook.getSheet(sheetName);
			int maxcount = sheet.getLastRowNum();
			try {
				for (int testcase = 1; testcase <= maxcount; testcase++) {
					String testCaseID = getDataFromExcel(testcase, 0, sheetName);
					if (testCaseID.equalsIgnoreCase(method.getMethod().getMethodName())) {
						String row = getDataFromExcel(testcase, 1, sheetName);
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
