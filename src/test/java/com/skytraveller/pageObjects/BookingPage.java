package com.skytraveller.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class BookingPage extends BasePage  {
	public BookingPage(WebDriver driver) {
		super(driver);// calls BasePage constructor
	}
	
	
	public void validateLogoInBookingPage(WebDriver driver, String imageXpath, int expectedWidth, int expectedHeight, ExtentTest test) {
	    try {
	        WebElement image = driver.findElement(By.xpath(imageXpath));
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        Long naturalWidth = (Long) js.executeScript("return arguments[0].naturalWidth;", image);
	        Long naturalHeight = (Long) js.executeScript("return arguments[0].naturalHeight;", image);

	        System.out.println("Intrinsic size: " + naturalWidth + " × " + naturalHeight);

	        if (naturalWidth == expectedWidth && naturalHeight == expectedHeight) {
	            System.out.println("✅ Image intrinsic size is valid In Booking Page");
	            test.log(Status.PASS, "✅ Image is uploaded and intrinsic size is valid in Booking Page: "
	                    + naturalWidth + "×" + naturalHeight);
	        } else {
	            System.out.println("❌ Image intrinsic size is NOT valid In Booking Page");
	            test.log(Status.FAIL, "❌ Image is uploaded but intrinsic size is invalid in Booking Page. "
	                    + "Expected: " + expectedWidth + "×" + expectedHeight +
	                    ", Found: " + naturalWidth + "×" + naturalHeight);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                    "Intrinsic size mismatch in Booking Page", "ImageIntrinsicSizeInvalidInBookingPage");
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Exception in validateImageIntrinsicSize: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception during intrinsic image validation in Booking Page: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Exception in image validation In BookingPage", "ImageIntrinsicSizeExceptionInBookingPage");
	        Assert.fail();
	    }
	}
	
	
}
