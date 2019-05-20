package com.objects.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import com.testBase.test.TestBase;
import com.testBase.test.ExcelUtils;


public class ObjectRepository extends TestBase {

	ExcelUtils excelUtils =new ExcelUtils();
	
	TestBase cmsBaseTest=new TestBase();
	
	Screen screen = new Screen();
	
	// Created constructor to Initializing the page objects
	
	 public ObjectRepository(WebDriver driver) {

		PageFactory.initElements(driver, this);
	}
   
	// Captured the CMSNV Test Application objects
	
	@FindBy(xpath = "//a[@class='dialog button default']")
	WebElement accept;
	
	@FindBy(xpath = "//*[@class='box-links detection-right-links']/a[contains(text(),'Detect Receiver')]")
	WebElement detectReceiver;
	
	@FindBy(xpath = "(//a[contains(text(),'Already installed')])[1]")
	WebElement alreadyInstalled;
	
	@FindBy(xpath = "//input[@name='username']")
	WebElement usename;
	
	@FindBy(xpath = "//input[@name='password']")
	WebElement password;
	
	@FindBy(xpath = "//a[@id='loginBtn']")
	WebElement login;
	
	@FindBy(xpath = "//a[@id='allAppsBtn']")
	WebElement allAppsBtn;
	
	@FindBy(xpath = "//img[@alt='CMS-Test']")
	WebElement CMStest;
	
	@FindBy(xpath = "//a[@id='userMenuBtn']")
	WebElement userMenuBtn;
	
	@FindBy(xpath = "//a[@id='menuLogOffBtn']")
	WebElement menuLogOffBtn;

	// Accepts the Welcome Screen
	
	   public void WelcomeAccept() throws Exception {
		
		System.out.println(driver);
		
		cmsBaseTest.timing(accept);
		
		accept.click();
		
		Thread.sleep(5000);
	}

	// Selects the DetectReceiver button
	// Used Explicit Wait for Synchronization.
	   
	   public void detectReceiver() {
		
		cmsBaseTest.timing(detectReceiver);
		
		detectReceiver.click();

	}

	// Selects cancel in the openCitrixLancher pop-up
	// Passing images and user directory dynamically.
	   
	    public void openCitrixLancher() throws Exception {
		
		String cancel = "Pictures\\Cancel.PNG";
		
		Pattern pattern =cmsBaseTest. fileReaders(cancel);
		
		screen.wait(pattern,20);
		
		screen.click(pattern);

	}

	// Selects alreadyInstalled button
	
	    public void alreadyInstalled() {
		
		cmsBaseTest.timing(alreadyInstalled);
		
		alreadyInstalled.click();
	}

      // Reading nonprodportal sheet 
	  // Reading nonprodportal credentials based line number.
	     
	    public void amcCredentials(int linenumber,String sheetname) throws Exception {
		
	    	excelUtils.readingExcel(sheetname);	
		
		cmsBaseTest.timing(usename);
		
		usename.sendKeys(excelUtils.excelData[linenumber][0]);
		
		cmsBaseTest.timing(password);
		
		password.sendKeys(excelUtils.excelData[linenumber][1]);
		
		login.click();
	}

	// Selecting the AllApsButton
	   
	   public void allappsbtn() {
		
		cmsBaseTest.timing(allAppsBtn);
		
		allAppsBtn.click();

	}

    // Selecting the CMStest Application button	
	   
	    public void cmsTest() {
		
		cmsBaseTest.timing(CMStest);
		
		CMStest.click();

	}

     //  Opens the CMStest File Reader Application.
	   
	    public void cmsFileOpen() throws Exception {
		
		String CMS = "Pictures\\CMS.PNG";
		
		Pattern pattern = cmsBaseTest.fileReaders(CMS);
		
		screen.wait(pattern, 90);
		
		screen.click(pattern);
		

	}

	 // Giving the CMS File Reader Authorization window Credentials
	 // Selecting the OK button in CMS File Reader Authorization window
	   
	    public void cmsCredentials() throws Exception {
		
	    String pass = "Pictures\\Pass.PNG";
		
	    Pattern pattern = cmsBaseTest.fileReaders(pass);
		
	    screen.wait(pattern, 150);
		
	    screen.type(pattern, "Welcome@123");
		
	    Thread.sleep(50);
		
	    String ok = "Pictures\\Ok.PNG";
		
	    Pattern pattern1 =cmsBaseTest. fileReaders(ok);
		
	    screen.wait(pattern1, 30);
		
	    screen.click(pattern1);

	}
     // Selects the TitleVersion button.
	    
	    public void titleVersionSearch() throws Exception {

		String titleVersion = "Pictures\\TitleVersion .PNG";
		
		Pattern pattern =cmsBaseTest. fileReaders(titleVersion);
		
		screen.wait(pattern, 40);
		
		screen.click(pattern);
	}

	// Reading the FDID's sheetname
	// Reading the FDID's
	// Entering the FDID# 
	// Clicking the Search Continue button.
	    
	   public void serialNumberInCMS(int FDIdLine, String sheetname) throws Exception {
		
		   excelUtils.readingExcel(sheetname);
		
		String serialNumber = "Pictures\\SerialNumber.PNG";
		
		Pattern pattern = cmsBaseTest.fileReaders(serialNumber);		
		
		screen.wait(pattern, 50);
		
		screen.wait((double) 3.0);
		
		screen.type(pattern,excelUtils.excelData[FDIdLine][0]);
		
		String serach ="Pictures\\Search.PNG";
		
		Pattern pattern1 = cmsBaseTest.fileReaders(serach);
		
		screen.wait(pattern1, 50);
		
		screen.wait((double) 3.0);
		
		screen.click(pattern1);
	}

	
	// Clicking the Accepted button	
	// After Selecting the Accepted button displayed the corresponding FDID data.
	   
	   public void accepted() throws Exception {

		String accepted ="Pictures\\Accepted.PNG";
		
		Pattern pattern = cmsBaseTest.fileReaders(accepted);
		
		screen.wait(pattern, 50);
		
		screen.click(pattern);
	}

	// Navigating to the DataMigration tab
	   
	   public void datMigrationTab() throws Exception {

		String dataMigrationTab ="Pictures\\DataMigrationTab.PNG";
		
		Pattern pattern =cmsBaseTest.fileReaders(dataMigrationTab);	
		
		screen.click(pattern, 50);
		
		screen.wait((double) 3.0);
		
		screen.click(pattern);
	}
     // Clicking the pushtoMigrate button
	 
	    public void pushToMigrate() throws Exception {
		
		String pushToMigrate ="Pictures\\PushToMigrate.PNG";
		
		Pattern pattern = cmsBaseTest.fileReaders(pushToMigrate);
		
		screen.click(pattern, 40);
		
		screen.click(pattern);

	}

	 // Navigating to migrationRequest pop-up and Clicking OK button.
	
	   public void migrationRequest() throws Exception {

		String migrationRequest ="Pictures\\MigrationRequest.PNG";
		
		Pattern pattern =cmsBaseTest.fileReaders(migrationRequest);
		
		screen.wait(pattern, 40);
		
		screen.click(pattern);
		
	}
	   
	   // Reading the Migration Date Field to confirm migration success/Failure.
	   
	    public String migrationDate() throws Exception {

	    	String migrationDate = "Pictures\\MigrationDate.PNG";
			
	    	Pattern pattern = cmsBaseTest.fileReaders(migrationDate);
			
	    	screen.wait((double) 50.0);
			
			screen.wait(pattern,80).offset(50, 0).grow(25,0).click();

	        String value = screen.wait(pattern,80).offset(50, 0).grow(25,0).text();
	       
	        String migrationDateValue= value.replaceAll("[^0-9]", "");
	        
	        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
	        
	        Date dateandTimeStamp=formatter.parse(migrationDateValue);
	        
	        System.out.println("MigrationDateValue from CMSNV :" + dateandTimeStamp );
	        
	        System.out.println("=======CMSNV Record Pushed Successfully=========");
	        
	        return migrationDateValue;
	    }

	       // Clicking the CMSNV File Reader MainMenu
	     
	         public void mainMenu() throws Exception{
	    	 
	    	 String mainMenu = "Pictures/MainMenu.PNG";	 

	    	 Pattern pattern = cmsBaseTest.fileReaders(mainMenu); 
	    	 
	    	 screen.wait(pattern, 40);
	    	 
	    	 screen.click(pattern);
	    	 	    	 
	     }
	    
	    // Quit the CMSNV File Maker 
	        
	    public void quitFileMaker() throws Exception {
	    	
	    	 String mainMenu = "Pictures/quit.PNG";	 

	    	 Pattern pattern =cmsBaseTest. fileReaders(mainMenu); 
	    	 
	    	 screen.wait(pattern, 40);
	    	 
	    	 screen.click(pattern);
	    }
	        
	    // Acknowledge the Yes button
	        
	    public void acknowledgeFileMakerQuit() throws Exception {
	    	 
	    	String mainMenuYes = "Pictures/Yes.PNG";	 
	    	
	    	Pattern pattern = cmsBaseTest.fileReaders(mainMenuYes); 
	    	
	    	screen.wait(pattern, 50);
	    	
	    	screen.click(pattern);
	    	
	    }
	    // Acknowledge the close button
	    
	    public void closingFilemaker() throws Exception {
 	
	    	 String close = "Pictures/Close.PNG";	 

	    	 Pattern pattern =cmsBaseTest. fileReaders(close); 
	    	 
	    	 screen.wait(pattern, 50);
	    	 
	    	 screen.click(pattern);
	    }
	        
	    // Close the CMSNV FileMaker Main Window.   
	     
	       public void closingMainWindowFileMaker() throws Exception {
 	
	    	 String closeMainWindow = "Pictures/CloseMainWindow.PNG";	 

	    	 Pattern pattern = cmsBaseTest.fileReaders(closeMainWindow); 
	    	 
	    	 screen.wait(pattern, 50);
	    	 screen.wait((double) 3.0);
	    	 screen.click(pattern);
	    }
	        
	   // Logging out from the nonprodportal 	   
	   
	   public void logoutNonProdPortal() throws InterruptedException {
	   
	    cmsBaseTest.timing(userMenuBtn);
	   
	    userMenuBtn.click();
	   
	    cmsBaseTest.timing(menuLogOffBtn);
	   
	    menuLogOffBtn.click();
	    
	    Thread.sleep(6);
	}

}
