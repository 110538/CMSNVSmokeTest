package com.runner.test;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.cmstestbase.test.CMSTestBase;
import com.objects.test.ObjectRepository;
import com.utilis.test.ConnectingESBDatabase;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;

public class TestEvents extends CMSTestBase {

	ConnectingESBDatabase ceb = new ConnectingESBDatabase();

	ObjectRepository obr = new ObjectRepository(driver);

	@BeforeTest()

	public void connectESBDatabaseTest() throws Throwable {

		ceb.connectESBDatabase();

	}

	@SuppressWarnings("unused")
	@Features("CMSNV SMOKE")
	@Stories("ESB Hierachry")
	@Test(priority = 1)
	@Title("WOPHierarchyCreationTest")
	public void WOPHierarchyCreationTest() throws Exception {

		test: try {

			ceb.hierarchyCreation("WOP.updateProductVersion");

			if (ceb.eventType.equalsIgnoreCase("event not found")) {

				ceb.hierarchyCreation("WOP.createProductXRefMedia");

				logger("=======WOP Create Hierachry is available in ESB==========");

			} else {

				logger("=========WOP Update Hierachry is available in ESB============");
			}

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

	}

	@Features("CMSNV SMOKE")
	@Stories("ESB Hierachry")
	@Test(priority = 2)
	@Title("MPHierarchyCreationTest")
	public void MPHierarchyCreationTest() throws Exception {

		ceb.hierarchyCreation("AMCN.EVENT.WOP.MP.PRODUCT.DATA");

		logger("=========MP Hierachry is available in ESB===========");

	}

	@Features("CMSNV SMOKE")
	@Stories("ESB Hierachry")
	@Test(priority = 3)
	@Title("MediatorHierarchyCreationTest")

	public void MediatorHierarchyCreationTest() throws Exception {

		ceb.hierarchyCreation("AMCN.EVERTZ.MEDIATOR.REGISTRATION.TO.ADAPTER");

		logger("=========Mediator Hierachry is available in ESB===========");

		
	}

}
