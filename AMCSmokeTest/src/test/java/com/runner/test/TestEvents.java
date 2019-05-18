package com.runner.test;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.testBase.test.ApiUtil;

import com.testBase.test.CmsApp;
import com.testBase.test.NonProdPortal;
import com.utils.test.Utility;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;

public class TestEvents extends NonProdPortal {
	
	
	CmsApp cmsapp= new CmsApp();
	
	Utility utility = new Utility();
	
	//ObjectRepository obr = new ObjectRepository(driver);

	@BeforeTest()

	public void connectESBDatabaseTest() throws Throwable {
		
		//cmsapp.puhingRecords();
		
		utility.connectToEsbDatabase();

	}

	@Features("CMSNV SMOKE")

	@Stories("WOP ESB EVENTS")

	@Test(priority = 1)

	@Title("WOPHierarchyValidationTest")

	public void WOPHierarchyValidationTest() throws Exception {

		utility.hierarchyValidation("AMCN.EVENT.WOP.CREATE.VERSIONS");

		//logger("WOP Hierachry is available in ESB");

	}

	@Features("CMSNV SMOKE")

	@Stories("MP ESB EVENTS")

	@Test(priority = 2)

	@Title("MPHierarchyValidationTest")

	public void MPHierarchyValidationTest() throws Exception {

		utility.hierarchyValidation("AMCN.EVENT.WOP.MP.PRODUCT.DATA");

		//logger("=========MP Hierachry is available in ESB===========");

	}

	@Features("CMSNV SMOKE")

	@Stories("MEDIATOR ESB EVENTS")

	@Test(priority = 3)

	@Title("MediatorHierarchyValidationTest")

	public void MediatorHierarchyValidationTest() throws Exception {

		utility.hierarchyValidation("AMCN.EVERTZ.MEDIATOR.REGISTRATION.TO.ADAPTER");

		//logger("=========Mediator Hierachry is available in ESB===========");

	}

	@Features("CMSNV SMOKE")

	@Stories("CMSNV Field Validations With WOP")

	@Test(priority = 4)

	@Title("CMSNV Field Validations With WOP Test")

	public void CMSNVFieldValidationsWithWOPTest() throws Exception {

		utility.APIResponse("WOPAPI", ApiUtil.RLID);

	}

	@Features("CMSNV SMOKE")

	@Stories("CMSNV Field Validations With MP")

	@Test(priority = 5)

	@Title("CMSNV Field Validations With MP Test")

	public void CMSNVFieldValidationsWithMPTest() throws Exception {

		utility.APIResponse("APIMP", ApiUtil.RLID);

	}


}
