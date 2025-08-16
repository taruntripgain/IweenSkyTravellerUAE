package com.skytraveller.pageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class ResultPage extends BasePage{

	public ResultPage(WebDriver driver) {
		super(driver);
	}
	public void validateLogoInResultPage(WebDriver driver, String imageXpath, int expectedWidth, int expectedHeight, ExtentTest test) {
	    try {
	        WebElement image = driver.findElement(By.xpath(imageXpath));
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        Long naturalWidth = (Long) js.executeScript("return arguments[0].naturalWidth;", image);
	        Long naturalHeight = (Long) js.executeScript("return arguments[0].naturalHeight;", image);

	        System.out.println("Intrinsic size: " + naturalWidth + " × " + naturalHeight);

	        if (naturalWidth == expectedWidth && naturalHeight == expectedHeight) {
	            System.out.println("✅ Image intrinsic size is valid in Result Page");
	            test.log(Status.PASS, "✅ Image is uploaded and intrinsic size is valid in Result Page: "
	                    + naturalWidth + "×" + naturalHeight);
	        } else {
	            System.out.println("❌ Image intrinsic size is NOT valid In Result Page");
	            test.log(Status.FAIL, "❌ Image is uploaded but intrinsic size is invalid in Result Page. "
	                    + "Expected: " + expectedWidth + "×" + expectedHeight +
	                    ", Found: " + naturalWidth + "×" + naturalHeight);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                    "Intrinsic size mismatch in Result Page", "ImageIntrinsicSizeInvalidInResultPage");
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Exception in validateImageIntrinsicSize: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception during intrinsic image validation in Result Page: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL,
	                "Exception in image validation In ResultPage", "ImageIntrinsicSizeExceptionInResultPage");
	        Assert.fail();
	    }
	}
	
	public void validateResultPage(ExtentTest test) {
	    try {
	        List<WebElement> flightCards = driver.findElements(By.xpath("(//div[@class=' d-flex flex-column mb-2 one-way-new-result-card '])[1]"));
	 
	        if (!flightCards.isEmpty() && flightCards.get(0).isDisplayed()) {
	            System.out.println("✅ Flight card is displayed successfully.");
	            test.log(Status.PASS, "✅ Flight card is displayed successfully.");
	        } else {
	            List<WebElement> noFlightMessages = driver.findElements(By.xpath("//div[text()='No Flights Found']"));
	 
	            if (!noFlightMessages.isEmpty() && noFlightMessages.get(0).isDisplayed()) {
	                System.out.println("⚠️ No flight found for this search.");
	                test.log(Status.INFO, "⚠️ No flight found for this search.");
	            } else {
	                System.out.println("❌ Neither flight cards nor 'No Flights Found' message is present.");
	                test.log(Status.FAIL, "❌ Neither flight cards nor 'No Flights Found' message is present.");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No flight cards or messages", "ResultPageValidationFailure");
	            }
	        }
	 
	    } catch (Exception e) {
	        System.out.println("❌ Error while validating result page: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception while validating result page: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception during result page check", "ResultPageException");
	        Assert.fail();
	    }
	}
	
	public void selectFlightBasedOnIndex(int index, ExtentTest test) {
	    try {
	        // Adjusting index for XPath (1-based indexing)
	        String viewPriceXPath = "(//*[contains(@class,'one-way-new-result-card')]//*[text()='View Price'])[" + index + "]";
	        WebElement viewPriceButton = driver.findElement(By.xpath(viewPriceXPath));

	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewPriceButton);
	        Thread.sleep(500);
	        viewPriceButton.click();
	        test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + index);
             System.out.println("✅ Clicked 'View Price' for flight at index: " + index);
	        // Wait for fare component to be visible
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement fareMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='fare-components-list']")));

	        if (fareMenu.isDisplayed()) {
	            List<WebElement> fares = driver.findElements(By.xpath("//*[contains(@class,'fare-component-watermark')]"));

	            if (!fares.isEmpty()) {
	                WebElement firstFare = fares.get(0);
	                WebElement bookNowBtn = firstFare.findElement(By.xpath(".//*[text()='Book now']")); // dot (.) to search relative to fare element
	                bookNowBtn.click();
	                test.log(Status.PASS, "✅ 'Book now' button clicked for selected flight.");
	            } else {
	                test.log(Status.FAIL, "❌ No fare options available.");
	                ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "No fares found", "FareOptionsMissing");
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("❌ Exception while selecting flight: " + e.getMessage());
	        test.log(Status.FAIL, "❌ Exception while selecting flight: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Selection Failure", "FlightSelectException");
	    }
	}

}
