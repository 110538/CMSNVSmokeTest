package com.testBase.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.Pattern;
import org.testng.annotations.AfterSuite;
import com.objects.test.ObjectRepository;
import ru.yandex.qatools.allure.annotations.Step;

public class TestBase {
	
	public static WebDriver driver;
	
	// Generates the Allure Reporting

	@Step("{0}")

	public void logStep(String stepName) {

	}

	// Explicit Wait to balance between the Browser and WebElements

	public void timing(WebElement accept) {

		WebDriverWait wait = new WebDriverWait(driver, 20);

		wait.until(ExpectedConditions.visibilityOf(accept));

	}

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



	// Reads the Sikuli pictures folder path

	public Pattern fileReaders(String filename) {

		filename = System.getProperty("user.dir") + "\\" + filename;

		Pattern pattern = new Pattern(filename);

		return pattern;
	}

	// Logout From Nonprodportal Application
	// Closes the Browser

	@AfterSuite(enabled = false)

	public void closingbrowser() throws Exception {

		ObjectRepository obr = new ObjectRepository(driver);

		obr.logoutNonProdPortal();

		logStep("Nonprod portal logged out successfully ");

		System.out.println("Nonprod portal logged out successfully...");

		// logger("Nonprod portal logged out successfully ");

		driverClose();

		logStep("Browser is Closed.. ");

		System.out.println("Browser Is Closed...");
	}

	// Closes the Driver

	public void driverClose() {

		driver.close();
	}

}
