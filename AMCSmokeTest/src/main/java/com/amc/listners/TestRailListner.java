package com.amc.listners;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.cmstestbase.test.CMSTestBase;

public class TestRailListner extends CMSTestBase implements ITestListener {

	HashMap<String, Integer> stringValue = null;
	
	Properties prop;

	public TestRailListner() {
	
		stringValue = storeExcelDataInHashMap();
		
		prop = propHandler.get();
	}

	
	// Invokes the TestRail API's
	
	private APIClient initializeTestRailSetup() {
		
		APIClient client = new APIClient(prop.getProperty("testRailURL"));
		
		client.setUser(prop.getProperty("testRailUserName"));
		
		client.setPassword(prop.getProperty("testRailPassword"));
		
		return client;
	} 

	@Override
	
	public void onTestStart(ITestResult result) {
		
		System.out.println(result.getMethod()+"--Test case started");

	}

	
	// Updates Test Case Status as Pass in TestRail If Test Case is Fail
	
	@SuppressWarnings("unchecked")
	
	@Override
	
	public void onTestSuccess(ITestResult result) {
		
		System.out.println(stringValue);
		
		int testCaseID = stringValue.get(result.getMethod().getMethodName());
		
		int testRunID = Integer.parseInt(prop.getProperty("testRailRunID"));
		
		APIClient client = initializeTestRailSetup();
		
		@SuppressWarnings("rawtypes")
		
		Map data = new HashMap();
		
		data.put("status_id", new Integer(1));
		
		data.put("comment", "This test case is passed!");
		
		try {
		
			client.sendPost("add_result_for_case/" + testRunID + "/" + testCaseID, data);
		
		} catch (Exception e) {
			
			e.printStackTrace(); 
		} 
	}

	// Updates Test Case Status as Fail in TestRail If Test Case is Fail
	
	@SuppressWarnings("unchecked")
	
	@Override
	
	public void onTestFailure(ITestResult result) {
		
		int testCaseID = stringValue.get(result.getMethod().getMethodName());
		
		int testRunID = Integer.parseInt(prop.getProperty("testRailRunID"));
		
		APIClient client = initializeTestRailSetup();
		
		@SuppressWarnings("rawtypes")
		
		Map data = new HashMap();
		
		data.put("status_id", new Integer(5));
		
		data.put("comment", "This test failed due to" + result.getThrowable());
		
		try {
		
			client.sendPost("add_result_for_case/" + testRunID + "/" + testCaseID, data);
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}  
	}

	// Updates Test Case Status as Skipped in TestRail If any dependent Test case if Fails
	
	@SuppressWarnings("unchecked")
	
	@Override
	
	public void onTestSkipped(ITestResult result) {
	
		int testCaseID = stringValue.get(result.getMethod().getMethodName());
		
		int testRunID = Integer.parseInt(prop.getProperty("testRailRunID"));
		
		APIClient client = initializeTestRailSetup();
		
		@SuppressWarnings("rawtypes")
		
		Map data = new HashMap();
		
		data.put("status_id", new Integer(4));
		
		data.put("comment", "This test skipped due to" + result.getThrowable());
		
		try {
		
			client.sendPost("add_result_for_case/" + testRunID + "/" + testCaseID, data);
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}  
	} 

	//  These Methods Are For Future use
	
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		

	}

	@Override
	public void onStart(ITestContext context) {
		

	}

	@Override
	public void onFinish(ITestContext context) {

	}

}
