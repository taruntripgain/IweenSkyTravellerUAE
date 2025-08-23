package com.skytraveller.pageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class ResultPageRoundTrip extends BasePage{

	public ResultPageRoundTrip(WebDriver driver) {
		super(driver);
	}

	
	
	public String selectFlightBasedOnIndexAndValidateAmenities(int index, ExtentTest test) {
	    try {
	        // Build XPath for the desired "View Price" button using the index
	        String viewPriceXPath = "(//*[contains(@class,'one-way-new-result-card')]//*[text()='View Price'])[" + index + "]";
	        WebElement viewPriceButton = driver.findElement(By.xpath(viewPriceXPath));

	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewPriceButton);
	        Thread.sleep(500);
	        viewPriceButton.click();
	        test.log(Status.PASS, "✅ Clicked 'View Price' for flight at index: " + index);
	        System.out.println("✅ Clicked 'View Price' for flight at index: " + index);

	        // Wait for the fare components section to be visible
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement fareMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='fare-components-list']")));

	        if (fareMenu.isDisplayed()) {
	           // List<WebElement> fares = driver.findElements(By.xpath("//*[contains(@class,'fare-component-watermark')]"));
	            List<WebElement> fares = driver.findElements(By.xpath("//*[contains(@class,'fare-components-list')]/div"));

	            if (!fares.isEmpty()) {
	                WebElement firstFare = fares.get(0);

	                // Look for the Book now button within the selected fare block
	                WebElement bookNowBtn = firstFare.findElement(By.xpath(".//*[text()='Book now']"));

	                // Find Amenities link (relative to the firstFare, not globally)
	                List<WebElement> amenities = firstFare.findElements(By.xpath(".//a[text()='Amenities']"));
	                if (!amenities.isEmpty()) {
	                	Thread.sleep(2000);
	                    WebElement firstAmenities = amenities.get(0);
	                    firstAmenities.click();

	                    // Wait for the amenities popup to be visible
	                    WebElement amenitiesPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                        By.xpath("//div[contains(@class,'modal-content') and contains(.,'Amenities')]")
	                    ));

	                    if (amenitiesPopup.isDisplayed()) {
	                        test.log(Status.PASS, "✅ Amenities popup is displayed for flight index: " + index);

	                        WebElement popupTable = amenitiesPopup.findElement(By.xpath(".//table[contains(@class,'table-bordered')]"));

	                        if (popupTable.isDisplayed()) {
	                            test.log(Status.PASS, "✅ Amenities table is displayed for flight index: " + index);
	                            amenitiesPopup.findElement(By.xpath("//button[@class='btn-close']")).click();
	                            Thread.sleep(2000);
	                            String totalPrice=firstFare.findElement(By.xpath("//*[contains(@class,'fare-totalfq')]")).getText();
	                            
	                            bookNowBtn.click();
	                            return totalPrice;
	                        } else {
	                            test.log(Status.FAIL, "❌ Amenities table not displayed.");
	                            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Amenities Table Missing", "AmenitiesTableFail");
	                        }
	                    } else {
	                        test.log(Status.FAIL, "❌ Amenities popup not displayed.");
	                        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Amenities Popup Missing", "AmenitiesPopupFail");
	                    }
	                } else {
	                    test.log(Status.INFO, "ℹ️ No 'Amenities' link found for this fare.");
	                }

	                test.log(Status.PASS, "✅ 'Book now' button found for selected flight.");
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
		return null;
	}
}
