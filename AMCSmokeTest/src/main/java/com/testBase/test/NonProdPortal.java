package com.testBase.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class NonProdPortal {
	
	public static WebDriver driver;
	
	
	    // Browser Launching
		// Hits the nonprodportal.amcnetworks.com

		public WebDriver nonProdPortalLaunch() {

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\Drivers\\chromedriver.exe");

			driver = new ChromeDriver();

			driver.manage().window().maximize();

			driver.get("https://nonprodportal.amcnetworks.com/Citrix/AppsWeb/");

			System.out.println(driver);

			return driver;

		}
	
	
	

}
