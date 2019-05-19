package com.testBase.test;



import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeSuite;

import com.objects.test.ObjectRepository;

public class CmsApp extends NonProdPortal{
	
	//NonProdPortal nonprod = new NonProdPortal();
	
	ObjectRepository obr;
	
	TestBase cmsTestBase=new TestBase();
	
	//WebDriver driver;
	
	
	
	@BeforeSuite(enabled = true)

	public void puhingRecords() throws Exception {
		
		System.out.println("Started Smkoe Test for CMSNV");

		 nonProdPortalLaunch();
		
		 obr = PageFactory.initElements(driver, ObjectRepository.class);
		 
		 System.out.println(obr);

		//ObjectRepository obr = new ObjectRepository(driver);

		obr.WelcomeAccept();

		cmsTestBase.logStep("Accepted Welcome screen");

		//logger("Accepted Welcome screen");

		obr.detectReceiver();

		cmsTestBase.logStep("Selected detectReceiver");

		//logger("Selected detectReceiver");

		obr.openCitrixLancher();

		cmsTestBase.logStep("Closed the Citrix Launcher");

		//logger("Closed the Citrix Launcher");

		obr.alreadyInstalled();

		cmsTestBase.logStep("Closed the Citrix Launcher");

		//logger("Closed the Citrix Launcher");

		obr.amcCredentials(1, "NonProdLoginCredentials");

		cmsTestBase.logStep("Entered the AMC Credentials");

		//logger("Entered the AMC Credentials");

		obr.allappsbtn();

		cmsTestBase.logStep("Selected the all apps button");

		//logger("Selected the all apps button");

		obr.cmsTest();

		cmsTestBase.logStep("Clicked on the CMS test application");

		//logger("Clicked on the CMS test application");

		obr.cmsFileOpen();

		cmsTestBase.logStep("Selected the CMS file");

		//logger("Selected the CMS file");

		obr.cmsCredentials();

		cmsTestBase.logStep("Entered the CMS credntials");

		//logger("Entered the CMS credntials");

		obr.titleVersionSearch();

		cmsTestBase.logStep("Clicked the title verion button");

		//logger("Clicked the title verion button");

		obr.serialNumberInCMS(0, "FDIDNUMBERS");

		cmsTestBase.logStep("FDID reading from excel sheet");

		//logger("FDID reading from excel sheet");

		cmsTestBase.logStep("Entered FDID");

		//logger("Entered FDID");

		obr.accepted();

		cmsTestBase.logStep("Accepted the title record");

		//logger("Accepted the title record");

		obr.datMigrationTab();

		cmsTestBase.logStep("Navigated to data migration tab");

		//logger("Navigated to data migration tab");

		obr.pushToMigrate();

		cmsTestBase.logStep("Pushed the record");

		//logger("Pushed the record");

		obr.migrationRequest();

		cmsTestBase.logStep("Accepted the migration request");

		//logger("Accepted the migration request");

		try {
			obr.migrationDate();

			cmsTestBase.logStep("Navigated to migration date");

			//logger("Navigated to migration date");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		} finally {

			obr.mainMenu();

			cmsTestBase.logStep("Selected the main menu in cms file maker");

			//logger("Selected the main menu in cms file maker");

			obr.quitFileMaker();

			cmsTestBase.logStep("Quit the file maker");

			//logger("Quit the file maker");

			obr.acknowledgeFileMakerQuit();

			cmsTestBase.logStep("Acckonwledged the file maker ");

			//logger("Acckonwledged the file maker ");

			obr.closingFilemaker();

			cmsTestBase.logStep("Closed the file maker");

			//logger("Closed the file maker");

			obr.closingMainWindowFileMaker();

			cmsTestBase.logStep("Closed the file maker main window");

			//logger("Closed the file maker main window");

			cmsTestBase.logStep("Waiting some time before fecching ESB events ");

			//logger("Waiting some time before fecching ESB events ");

			Thread.sleep(60000);

			System.out.println("******************************");

			//logger("Waiting Time is completed to fetch the ESB events ");
		}

	}

}
