package com.testBase.test;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.Pattern;
import org.testng.annotations.AfterSuite;
import com.objects.test.ObjectRepository;
import ru.yandex.qatools.allure.annotations.Step;

public class TestBase extends NonProdPortal {

	// Generates the Allure Reporting

	@Step("{0}")

	public void logStep(String stepName) {

	}

	// Explicit Wait to balance between the Browser and WebElements

	public void timing(WebElement accept) {

		WebDriverWait wait = new WebDriverWait(driver, 20);

		wait.until(ExpectedConditions.visibilityOf(accept));

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

		browserClose();

		logStep("Browser is Closed.. ");

		System.out.println("Browser Is Closed...");
	}

	// Closes the Driver

	public void browserClose() {

		driver.close();
	}

}
