package com.skytraveller.pageObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.iween.utilities.ScreenshotUtil;

public class FlightDetails extends BasePage  {
	public FlightDetails(WebDriver driver) {
		super(driver);// calls BasePage constructor
	}
	
	public void validateFlightDetailsBasedOnFlightCard(String[] userEnteredData,ExtentTest test) {
	    try {
	        String onWardLocation = userEnteredData[0];          // e.g., BLR
	        String returnLocation = userEnteredData[1];          // e.g., HAM
	        String userEnteredDate = userEnteredData[2];         // e.g., 20 Aug, 2025
	        String travelClass = userEnteredData[userEnteredData.length - 1]; // Last item assumed to be travel class

	        
	        // Extract airport codes from sector span
	        String sectorText = driver.findElement(By.xpath("//*[@class='sector-span']")).getText(); // e.g., Bengaluru(BLR) -> Hamburg(HAM)
	        String[] airportCodes = sectorText.split("\\(");
	        String fromCode = airportCodes[1].split("\\)")[0].trim();
	        String toCode = airportCodes[2].split("\\)")[0].trim();

	        // Extract and format travel date
	        String dateText = driver.findElement(By.xpath("//*[@class='date-span']")).getText(); // e.g., One Way Wed, 20 Aug, 2025
	        String[] parts = dateText.split(",");
	        String formattedDate = parts[1].trim() + ", " + parts[2].trim(); // e.g., 20 Aug, 2025

	        // Extract travel class from flight card
	        String flightTravelClass = driver.findElement(By.xpath("(//*[@class='d-flex gap-sm-2 flex-sm-row flex-column flex-wrap gap-1']/span)[1]")).getText().trim();

	        // Validation logs n
	        if (fromCode.equalsIgnoreCase(onWardLocation) &&
	            toCode.equalsIgnoreCase(returnLocation) &&
	            formattedDate.equals(userEnteredDate) &&
	            flightTravelClass.equalsIgnoreCase(travelClass)) {
//Note travell class id need to add so we can use loop for that
	            test.log(Status.PASS, "Flight details matched successfully.");
	            test.log(Status.INFO, "From: " + fromCode + ", To: " + toCode + ", Date: " + formattedDate + ", Class: " + flightTravelClass);
	          
	        } else {
	            test.log(Status.FAIL, "Flight details mismatch.");
	            test.log(Status.INFO, "Expected -> From: " + onWardLocation + ", To: " + returnLocation + ", Date: " + userEnteredDate + ", Class: " + travelClass);
	            test.log(Status.INFO, "Actual   -> From: " + fromCode + ", To: " + toCode + ", Date: " + formattedDate + ", Class: " + flightTravelClass);
	            ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Detail Mismatch", "Mismatch in flight card details");
	            Assert.fail();
	            
	        }

	    } catch (Exception e) {
	        test.log(Status.FAIL, "Exception while validating flight details: " + e.getMessage());
	        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Exception", "Error during validation");
	        Assert.fail();
	        
	    }
	}


	public void closeNavBar(ExtentTest test) {
	    try {
	        WebElement closeButton = driver.findElement(By.xpath("//button[@class='btn-close']"));

	        if (closeButton.isDisplayed()) {
	            closeButton.click();
	            System.out.println("✅ Navigation bar closed successfully.");
	            test.log(Status.INFO, "✅ Navigation bar closed successfully.");
	        } else {
	        	System.out.println("⚠️ Close button is not visible.");
	            test.log(Status.WARNING, "⚠️ Close button is not visible.");
	            Assert.fail();
	        }
	    } catch (NoSuchElementException e) {
	        test.log(Status.FAIL, "❌ Close button not found.");
	    } catch (Exception e) {
	        test.log(Status.FAIL, "❌ Failed to close navigation bar. Error: " + e.getMessage());
	    }
	}

	
	public String[] getFlightDetails() throws InterruptedException {
		Thread.sleep(2000);
	    WebElement sector = driver.findElement(By.xpath("//span[@class='sector-span']"));
	    String[] sectors = sector.getText().split("->");
 
	    String[] sector1Parts = sectors[0].trim().split("\\(");
	    String city1 = sector1Parts[0].trim();
	    String code1 = sector1Parts[1].replace(")", "").trim();
 
	    String[] sector2Parts = sectors[1].trim().split("\\(");
	    String city2 = sector2Parts[0].trim();
	    String code2 = sector2Parts[1].replace(")", "").trim();
 
	    Thread.sleep(2000);
	    WebElement departDate = driver.findElement(By.xpath("//span[@class='date-span']"));
	    String[] departDate1 = departDate.getText().split(",");
	    String departDate2 = departDate1[1].trim();
	    String departDate3 = departDate1[2].trim();
	    String departDateConcat = departDate2 + ", " + departDate3;
 
	    List<WebElement> Airlines = driver.findElements(By.xpath("//span[@class='primary-color flight-company']"));
	    ArrayList<String> airlines = new ArrayList<>();
	    for (WebElement Airline : Airlines) {
	        airlines.add(Airline.getText().trim().toLowerCase());
	    }
 
	    List<WebElement> flightNumber = driver.findElements(By.xpath("//span[@class='flight-number']"));
	    ArrayList<String> flightNumbers = new ArrayList<>();
	    for (WebElement flightNumber1 : flightNumber) {
	        flightNumbers.add(flightNumber1.getText());
	    }
 
	    List<WebElement> FlightEquipment = driver.findElements(By.xpath("//span[@class='flight-equipment']"));
	    ArrayList<String> FlightEquipments = new ArrayList<>();
	    for (WebElement FlightEquipment1 : FlightEquipment) {
	        String text = FlightEquipment1.getText().trim().replace("AIRCRAFT TYPE:", "").trim();
	        FlightEquipments.add(text);
	    }
 
	    List<WebElement> OperatorName = driver.findElements(By.xpath("//span[@class='flight-operated-by']"));
	    ArrayList<String> OperatorNames = new ArrayList<>();
	    for (WebElement OperatorName1 : OperatorName) {
	        String text = OperatorName1.getText().trim().replace("OPERATED BY:", "").trim().toLowerCase();
	        OperatorNames.add(text);
	    }
 
	    List<WebElement> departTimeGet = driver.findElements(By.xpath("//span[@class='flight-deptime']"));
	    String DepartTimeText = departTimeGet.get(0).getText();
 
	    List<WebElement> arrivalTimeGet = driver.findElements(By.xpath("//span[@class='flight-arrtime']"));
	    String ArrivalTimeTextt = arrivalTimeGet.get(arrivalTimeGet.size() - 1).getText();
 
	    
	    
	 WebElement firstLegDepartDate = driver.findElement(By.xpath("//div[@class='modal-dialog slide-from-right-animation modal-fullscreen']//span[@class='flight-depdate']"));
	 String[] Date = firstLegDepartDate.getText().split(",");
	 String Date1 = Date[1].trim();
	 String year = Date[2].trim();
	 String dateYear = Date1 +" "+year;
	 String dateYear1 =  dateYear.replace(")", "");
	 System.out.println(dateYear1);
	
	 List<WebElement> firstLegDepartLocation = driver.findElements(By.xpath("//span[@class='flight-origin_name']"));
	 WebElement firstLegDepartLocationFirst = firstLegDepartLocation.getFirst();
	 String[] firstLegDepartLocation1 = firstLegDepartLocationFirst.getText().split("-");
	 String firstLegDepartLocation2 = firstLegDepartLocation1[0].trim();
	 System.out.println(firstLegDepartLocation2);
	
	
	  List<WebElement> firstLegArrivalLocation = driver.findElements(By.xpath("//span[@class='flight-origin_name']"));
	  WebElement firstLegArrivalLocation1 = firstLegArrivalLocation.getLast();
      String[] firstLegArrivalLocation2 = firstLegArrivalLocation1.getText().split("-");
	  String firstLegArrivalLocation3 = firstLegArrivalLocation2[0].trim();
	  System.out.println(firstLegArrivalLocation3);
	
	
	  List<WebElement> fareTypes = driver.findElements(By.xpath("//div[@class='flight-fare-type']"));
	  ArrayList<String> Fare = new ArrayList<>();
	 for(WebElement fareType:fareTypes)
	 {
		 String fareTypeText = fareType.getText().trim();
		 Fare.add(fareTypeText);
		 
	 }
	 
	 System.out.println(Fare);
 
	
	  List<WebElement> bookingClass = driver.findElements(By.xpath("//span[@class='flight-booking-class']"));
	  ArrayList<String> bookingcls = new ArrayList<>();
 
	 for(WebElement bookingClas : bookingClass)
	 {
		 String bookingClas1 = bookingClas.getText().replace("BOOKING CLASS:", "").trim();
		 bookingcls.add(bookingClas1);
 
		 System.out.println(bookingClas1);
	 }
	 System.out.println(bookingcls);
 
	 /*
	 List<WebElement> baggage = driver.findElements(By.xpath("//span[@class='flight-baggage']"));
	 ArrayList<String> checkInbaggage = new ArrayList<>();

	 for (WebElement baggages1 : baggage) {
	     String baggages = baggages1.getText().trim();
	     checkInbaggage.add(baggages);

	     System.out.println(baggages);
	 }

	 // Optional: print the whole list
	 System.out.println("Final baggage list: " + checkInbaggage);

	 Thread.sleep(2000);
	 List<WebElement> cabin = driver.findElements(By.xpath("//span[@class='flight-baggage']/following-sibling::span"));
	 ArrayList<String> cabinBaggage = new ArrayList<>();

	 for (WebElement cabin1 : cabin) {
	     String cabinbaggages = cabin1.getText().trim();
	     checkInbaggage.add(cabinbaggages);

	     System.out.println(cabinbaggages);
	 }

	 // Optional: print the whole list
	 System.out.println("Final baggage list: " + cabinBaggage);
	 */
	 List<WebElement> baggage = driver.findElements(By.xpath("//span[@class='flight-baggage']"));
	 ArrayList<String> checkInbaggage = new ArrayList<>();

	 for (WebElement baggages1 : baggage) {
	     String baggages = baggages1.getText().trim();
	     checkInbaggage.add(baggages);

	     System.out.println(baggages);
	 }

	 System.out.println("Final Check-in baggage list: " + checkInbaggage);

	 Thread.sleep(2000);

	 List<WebElement> cabin = driver.findElements(By.xpath("//span[@class='flight-baggage']/following-sibling::span"));
	 ArrayList<String> cabinBaggage = new ArrayList<>();

	 for (WebElement cabin1 : cabin) {
	     String cabinbaggages = cabin1.getText().trim();
	     cabinBaggage.add(cabinbaggages);  // ✅ fixed here

	     System.out.println(cabinbaggages);
	 }

	 System.out.println("Final Cabin baggage list: " + cabinBaggage);  // ✅ fixed here

	 WebElement dateElement = driver.findElement(By.xpath("(//span[@class='flight-arrdate'])[last()]"));
	 String fullDate = dateElement.getText();
	 String arrivalDate = fullDate.replaceAll("^\\(\\w+,\\s*", "").replaceAll("\\)$", "");

	 System.out.println(arrivalDate);  // Output: 24 Aug, 2025

	
	
	    // Convert lists to strings
	    String airlinesStr = String.join(",", airlines);
	    String flightNumbersStr = String.join(",", flightNumbers);
	    String flightEquipmentsStr = String.join(",", FlightEquipments);
	    String operatorNamesStr = String.join(",", OperatorNames);
	    String FareStr = String.join(",", Fare);
	    String bookingclsStr = String.join(",", bookingcls);
	    String checkInbaggage1 = String.join(",", checkInbaggage);
	    String cabinBaggage1 = String.join(",", cabinBaggage);
 
	    
	    
	    return new String[]{
	        city1, code1, city2, code2, departDateConcat,
	        airlinesStr, flightNumbersStr, flightEquipmentsStr, operatorNamesStr,
	        DepartTimeText, ArrivalTimeTextt ,FareStr,bookingclsStr,checkInbaggage1,cabinBaggage1,arrivalDate
	    };
	}
 
	/*
	public void validateFlightCardAndFlightDetails(Map<String, String> GetFlightCardDetailsReturned,String[] getFlightDetailsReturned,List<String> fareTypeDetails) throws InterruptedException
	{
	String	checkin= fareTypeDetails.get(0);
	String cabin	= fareTypeDetails.get(1);
		String fareTypeName= fareTypeDetails.get(2);
		 String fareClass=fareTypeDetails.get(3);
		
		
		String flightNoStr = GetFlightCardDetailsReturned.get("flightNumbers");
		String airLineName = GetFlightCardDetailsReturned.get("airlineName");
		String flightEqStr = GetFlightCardDetailsReturned.get("flightEquipment");
		String cleanAirlinesStr = GetFlightCardDetailsReturned.get("operatedBy");
		String flightDeptTime = GetFlightCardDetailsReturned.get("departureTime");
		String flightArrivalTime = GetFlightCardDetailsReturned.get("arrivalTime");
		String flightDeptLocation2 = GetFlightCardDetailsReturned.get("departureLocation");
		String flightArrivalLocation2 = GetFlightCardDetailsReturned.get("arrivalLocation");
		String flightDeptDate = GetFlightCardDetailsReturned.get("departureDate");
		String flightArrivalDate = GetFlightCardDetailsReturned.get("arrivalDate");
		String flightPrice = GetFlightCardDetailsReturned.get("price");
		String totalDuration = GetFlightCardDetailsReturned.get("duration");
		String stop2 = GetFlightCardDetailsReturned.get("stops");

	//------------------------------------------------------------------------------------------
		
		String city1 = getFlightDetailsReturned[0];
		System.out.println(city1);
		String code1 = getFlightDetailsReturned[1];
		System.out.println(code1);
		String city2 = getFlightDetailsReturned[2];
		System.out.println(city2);
		String code2 = getFlightDetailsReturned[3];
		System.out.println(code2);
		String departDateConcat = getFlightDetailsReturned[4];
		System.out.println(departDateConcat);
		String airlinesStr = getFlightDetailsReturned[5];
		System.out.println(airlinesStr);
		String flightNumbersStr = getFlightDetailsReturned[6];
		System.out.println(flightNumbersStr);
		String flightEquipmentsStr = getFlightDetailsReturned[7];
		System.out.println(flightEquipmentsStr);
		String operatorNamesStr = getFlightDetailsReturned[8];
		System.out.println(operatorNamesStr);
		String DepartTimeText = getFlightDetailsReturned[9];
		System.out.println(DepartTimeText);
		String ArrivalTimeTextt = getFlightDetailsReturned[10];
		System.out.println(ArrivalTimeTextt);
		
		
		
	 
		System.out.println(airLineName);
		System.out.println(airlinesStr);
		System.out.println(flightEquipmentsStr);
		System.out.println(flightEqStr);
		System.out.println(operatorNamesStr);
		System.out.println(cleanAirlinesStr);
		System.out.println(flightNoStr);
		System.out.println(flightNumbersStr);
		//-------------------------------------------------
		if(airlinesStr.contains(airLineName)&& flightEquipmentsStr.contains(flightEqStr) && operatorNamesStr.contains(cleanAirlinesStr) && flightNoStr.contains(flightNumbersStr))
		{
			//&& flightNoStr.contains(flightNumbersStr)
			System.out.println("Pass");
		}
		//-------------------------------------------------------
	 
		if(flightDeptTime.equals(DepartTimeText) && flightArrivalTime.equals(ArrivalTimeTextt) && flightDeptLocation2.equals(code1) && flightArrivalLocation2.equals(code2) && flightDeptDate.equals(departDateConcat))
		{
			System.out.println("Pass");
		}
		
		if()
	 
	}
 */
	/*
	public void validateFlightCardAndFlightDetails(Map<String, String> GetFlightCardDetailsReturned, String[] getFlightDetailsReturned, List<String> fareTypeDetails) throws InterruptedException {
	    
	    // Extract Fare Type Details
	    String checkin = fareTypeDetails.get(0);
	    String cabin = fareTypeDetails.get(1);
	    String fareTypeName = fareTypeDetails.get(2);
	    String fareClass = fareTypeDetails.get(3);
	    
	    // Extract Flight Card Details
	    String flightNoStr = GetFlightCardDetailsReturned.get("flightNumbers");
	    String airLineName = GetFlightCardDetailsReturned.get("airlineName");
	    String flightEqStr = GetFlightCardDetailsReturned.get("flightEquipment");
	    String cleanAirlinesStr = GetFlightCardDetailsReturned.get("operatedBy");
	    String flightDeptTime = GetFlightCardDetailsReturned.get("departureTime");
	    String flightArrivalTime = GetFlightCardDetailsReturned.get("arrivalTime");
	    String flightDeptLocation2 = GetFlightCardDetailsReturned.get("departureLocation");
	    String flightArrivalLocation2 = GetFlightCardDetailsReturned.get("arrivalLocation");
	    String flightDeptDate = GetFlightCardDetailsReturned.get("departureDate");
	    String flightArrivalDate = GetFlightCardDetailsReturned.get("arrivalDate");
	    String flightPrice = GetFlightCardDetailsReturned.get("price");
	    String totalDuration = GetFlightCardDetailsReturned.get("duration");
	    String stop2 = GetFlightCardDetailsReturned.get("stops");
	    
	    // Extract Modal Flight Details
	    String city1 = getFlightDetailsReturned[0];
	    String code1 = getFlightDetailsReturned[1];
	    String city2 = getFlightDetailsReturned[2];
	    String code2 = getFlightDetailsReturned[3];
	    String departDateConcat = getFlightDetailsReturned[4];
	    String airlinesStr = getFlightDetailsReturned[5];
	    String flightNumbersStr = getFlightDetailsReturned[6];
	    String flightEquipmentsStr = getFlightDetailsReturned[7];
	    String operatorNamesStr = getFlightDetailsReturned[8];
	    String DepartTimeText = getFlightDetailsReturned[9];
	    String ArrivalTimeTextt = getFlightDetailsReturned[10];
	    
	    System.out.println("============= FLIGHT CARD VS MODAL VALIDATION START =============");
	    
	    System.out.println("Airline Name: " + airLineName + " | Expected in: " + airlinesStr);
	    if (airlinesStr.contains(airLineName)) {
	        System.out.println("✅ Airline matched.");
	    } else {
	        System.out.println("❌ Airline mismatch.");
	    }

	    System.out.println("Flight Equipment: " + flightEqStr + " | Expected in: " + flightEquipmentsStr);
	    if (flightEquipmentsStr.contains(flightEqStr)) {
	        System.out.println("✅ Flight Equipment matched.");
	    } else {
	        System.out.println("❌ Flight Equipment mismatch.");
	    }

	    System.out.println("Operated By: " + cleanAirlinesStr + " | Expected in: " + operatorNamesStr);
	    if (operatorNamesStr.contains(cleanAirlinesStr)) {
	        System.out.println("✅ Operated By matched.");
	    } else {
	        System.out.println("❌ Operated By mismatch.");
	    }

	    System.out.println("Flight Number: " + flightNoStr + " | Expected in: " + flightNumbersStr);
	    if (flightNumbersStr.contains(flightNoStr)) {
	        System.out.println("✅ Flight Number matched.");
	    } else {
	        System.out.println("❌ Flight Number mismatch.");
	    }

	    System.out.println("Departure Time: " + flightDeptTime + " | Modal: " + DepartTimeText);
	    System.out.println("Arrival Time: " + flightArrivalTime + " | Modal: " + ArrivalTimeTextt);
	    System.out.println("Departure Location Code: " + flightDeptLocation2 + " | Modal: " + code1);
	    System.out.println("Arrival Location Code: " + flightArrivalLocation2 + " | Modal: " + code2);
	    System.out.println("Departure Date: " + flightDeptDate + " | Modal: " + departDateConcat);

	    if (flightDeptTime.equals(DepartTimeText)) {
	        System.out.println("✅ Departure Time matched.");
	    } else {
	        System.out.println("❌ Departure Time mismatch.");
	    }

	    if (flightArrivalTime.equals(ArrivalTimeTextt)) {
	        System.out.println("✅ Arrival Time matched.");
	    } else {
	        System.out.println("❌ Arrival Time mismatch.");
	    }

	    if (flightDeptLocation2.equals(code1)) {
	        System.out.println("✅ Departure Location matched.");
	    } else {
	        System.out.println("❌ Departure Location mismatch.");
	    }

	    if (flightArrivalLocation2.equals(code2)) {
	        System.out.println("✅ Arrival Location matched.");
	    } else {
	        System.out.println("❌ Arrival Location mismatch.");
	    }

	    if (flightDeptDate.equals(departDateConcat)) {
	        System.out.println("✅ Departure Date matched.");
	    } else {
	        System.out.println("❌ Departure Date mismatch.");
	    }

	    System.out.println("Fare Type: " + fareTypeName);
	    System.out.println("Cabin: " + cabin);
	    System.out.println("Check-in: " + checkin);
	    System.out.println("Booking Class: " + fareClass);

	    System.out.println("============= FLIGHT CARD VS MODAL VALIDATION END =============");
	}
*/
	public void validateFlightCardAndFlightDetails(
		    Map<String, String> GetFlightCardDetailsReturned,
		    String[] getFlightDetailsReturned,
		    List<String> fareTypeDetails,
		    ExtentTest test,
		    WebDriver driver) throws InterruptedException {

		    boolean hasMismatch = false;
		    StringBuilder detailedComparison = new StringBuilder();

		    // Fare Details
		    String checkin = fareTypeDetails.get(0);
		    System.out.println(checkin);
		    String cabin = fareTypeDetails.get(1);
		    System.out.println(cabin);
		    String fareTypeName = fareTypeDetails.get(2);
		    String fareClass = fareTypeDetails.get(3);

		    
		    // Map values
		    String flightNoStr = GetFlightCardDetailsReturned.get("flightNumbers");
		    String airLineName = GetFlightCardDetailsReturned.get("airlineName");
		    String flightEqStr = GetFlightCardDetailsReturned.get("flightEquipment");
		    String cleanAirlinesStr = GetFlightCardDetailsReturned.get("operatedBy");
		    String flightDeptTime = GetFlightCardDetailsReturned.get("departureTime");
		    String flightArrivalTime = GetFlightCardDetailsReturned.get("arrivalTime");
		    String flightDeptLocation2 = GetFlightCardDetailsReturned.get("departureLocation");
		    String flightArrivalLocation2 = GetFlightCardDetailsReturned.get("arrivalLocation");
		    String flightDeptDate = GetFlightCardDetailsReturned.get("departureDate");
		    String flightArrivalDate = GetFlightCardDetailsReturned.get("arrivalDate");
		    String flightPrice = GetFlightCardDetailsReturned.get("price");
		    String totalDuration = GetFlightCardDetailsReturned.get("duration");
		    String stop2 = GetFlightCardDetailsReturned.get("stops");

		    // Modal values
		    String city1 = getFlightDetailsReturned[0];
		    String code1 = getFlightDetailsReturned[1];
		    String city2 = getFlightDetailsReturned[2];
		    String code2 = getFlightDetailsReturned[3];
		    String departDateConcat = getFlightDetailsReturned[4];
		    String airlinesStr = getFlightDetailsReturned[5];
		    String flightNumbersStr = getFlightDetailsReturned[6];
		    String flightEquipmentsStr = getFlightDetailsReturned[7];
		    String operatorNamesStr = getFlightDetailsReturned[8];
		    String DepartTimeText = getFlightDetailsReturned[9];
		    String ArrivalTimeTextt = getFlightDetailsReturned[10];
		    String fareType = getFlightDetailsReturned[11];
		    String classType = getFlightDetailsReturned[12];
		    String checkIn = getFlightDetailsReturned[13];
		    String cabinbaggage = getFlightDetailsReturned[14];
		    String arrivalDate = getFlightDetailsReturned[15];
		   System.out.println(checkIn);
		   System.out.println(cabinbaggage);
		   
		    

		    detailedComparison.append("<table border='1' style='border-collapse: collapse;'>");
		    detailedComparison.append("<tr><th>Field</th><th>Card Value</th><th>Detail Value</th><th>Status</th></tr>");

		    hasMismatch |= logCompare("Airline", airLineName, airlinesStr, detailedComparison);
		    hasMismatch |= logCompare("Flight Equipment", flightEqStr, flightEquipmentsStr, detailedComparison);
		    hasMismatch |= logCompare("Operated By", cleanAirlinesStr, operatorNamesStr, detailedComparison);
		    hasMismatch |= logCompare("Flight Numbers", flightNoStr, flightNumbersStr, detailedComparison);
		    hasMismatch |= logCompare("Departure Time", flightDeptTime, DepartTimeText, detailedComparison);
		    hasMismatch |= logCompare("Arrival Time", flightArrivalTime, ArrivalTimeTextt, detailedComparison);
		    hasMismatch |= logCompare("Departure Location", flightDeptLocation2, code1, detailedComparison);
		    hasMismatch |= logCompare("Arrival Location", flightArrivalLocation2, code2, detailedComparison);
		    hasMismatch |= logCompare("Departure Date", flightDeptDate, departDateConcat, detailedComparison);
		    hasMismatch |= logCompare("Arrival Date", flightArrivalDate, arrivalDate, detailedComparison);
		    hasMismatch |= logCompare("Fare Type", fareTypeName, fareType, detailedComparison); 
		    hasMismatch |= logCompare("Class Type", fareClass, classType, detailedComparison);
		    hasMismatch |= logCompare("Check-In", checkin, checkIn, detailedComparison);
		    hasMismatch |= logCompare("Cabbin", cabin, cabinbaggage, detailedComparison);
		    // Arrival Date not checked here, but you can add if needed

		    detailedComparison.append("</table><br>");

		    // Log fare details
		    detailedComparison.append("<b>Fare Details:</b><br>");
		    detailedComparison.append("Fare Type: ").append(fareTypeName)
		            .append(", Cabin: ").append(cabin)
		            .append(", Check-in: ").append(checkin)
		            .append(", Booking Class: ").append(fareClass)
		            .append("<br>");

		    if (hasMismatch) {
		        test.log(Status.FAIL, "❌ Flight detail comparison failed:<br>" + detailedComparison.toString());
		        ScreenshotUtil.captureAndAttachScreenshot1(driver, test, Status.FAIL, "Flight Detail Mismatch", "Mismatch in flight card and detail modal");
		        Assert.fail("Flight details mismatch. See ExtentReport for breakdown.");
		    } else {
		        test.log(Status.PASS, "✅ All flight details matched successfully:<br>" + detailedComparison.toString());
		    }
		}
	/*
	private boolean logCompare(String label, String expected, String actual, StringBuilder log) {
	    boolean match = expected.trim().equalsIgnoreCase(actual.trim());
	    log.append("<tr>")
	       .append("<td>").append(label).append("</td>")
	       .append("<td>").append(expected).append("</td>")
	       .append("<td>").append(actual).append("</td>")
	       .append("<td style='color:").append(match ? "green" : "red").append(";'>")
	       .append(match ? "PASS" : "FAIL")
	       .append("</td></tr>");
	    return !match;
	}
*/
	/*
	private boolean logCompare(String label, String expected, String actual, StringBuilder log) {
	    boolean match;

	    switch (label.toLowerCase()) {
	        case "airline":
	        case "check-in":
	        case "cabbin":
	            // Case-insensitive containment check
	            match = actual.toLowerCase().contains(expected.toLowerCase());
	            break;

	        case "fare type":
	            // All values in actual should match the expected
	            String[] fareTypes = actual.split(",");
	            match = Arrays.stream(fareTypes)
	                          .allMatch(f -> f.trim().equalsIgnoreCase(expected.trim()));
	            break;

	        default:
	            // Default full string comparison
	            match = expected.trim().equalsIgnoreCase(actual.trim());
	            break;
	    }

	    log.append("<tr>")
	       .append("<td>").append(label).append("</td>")
	       .append("<td>").append(expected).append("</td>")
	       .append("<td>").append(actual).append("</td>")
	       .append("<td style='color:").append(match ? "green" : "red").append(";'>")
	       .append(match ? "PASS" : "FAIL")
	       .append("</td></tr>");

	    return !match;
	}

	*/
	private boolean logCompare(String label, String expected, String actual, StringBuilder log) {
	    boolean match;

	    switch (label.toLowerCase()) {
	        case "airline":
	            // Check if expected airline is in actual airline list
	            match = actual.toLowerCase().contains(expected.toLowerCase());
	            break;

	        case "check-in":
	        case "cabbin":
	            // Extract numeric weights from actual value
	            List<String> actualWeights = Arrays.stream(actual.split(","))
	                .map(val -> val.replaceAll("[^0-9]", "").trim())
	                .filter(val -> !val.isEmpty())
	                .collect(Collectors.toList());

	            // Extract numeric value from expected
	            String expectedWeight = expected.replaceAll("[^0-9]", "").trim();

	            // Check if all actual weights match expected
	            match = actualWeights.stream()
	                .allMatch(w -> w.equals(expectedWeight));
	            break;

	        case "fare type":
	            // All actual fare types should match expected
	            String[] fareTypes = actual.split(",");
	            match = Arrays.stream(fareTypes)
	                .allMatch(f -> f.trim().equalsIgnoreCase(expected.trim()));
	            break;

	        default:
	            // Default full string comparison
	            match = expected.trim().equalsIgnoreCase(actual.trim());
	            break;
	    }

	    log.append("<tr>")
	       .append("<td>").append(label).append("</td>")
	       .append("<td>").append(expected).append("</td>")
	       .append("<td>").append(actual).append("</td>")
	       .append("<td style='color:").append(match ? "green" : "red").append(";'>")
	       .append(match ? "PASS" : "FAIL")
	       .append("</td></tr>");

	    return !match;
	}

	
}
