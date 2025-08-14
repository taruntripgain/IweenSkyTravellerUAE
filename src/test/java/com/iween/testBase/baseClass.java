package com.iween.testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import com.iween.utilities.EmailUtils;
import com.iween.utilities.ExtentManager;
import com.iween.utilities.Iween_FutureDates;
import com.iween.utilities.Log;
import com.iween.utilities.ReportUtils;
import com.iween.utilities.ScreenshotUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.lang.reflect.Method;

public class baseClass {

	 public static WebDriver driver;
	    public static Logger logger;
	    public static Properties p;
	    public static ExtentTest test;
	    public Log log;
	    public ScreenshotUtil screenShots;
	    public ExtentReports extent;
	    public Iween_FutureDates futureDates;

	   
	    
    @BeforeMethod
    @Parameters({"os", "browser"})
    public void setup(String os, String browser,Method method) throws Exception {
        // Initialize Logger
        logger = LogManager.getLogger(this.getClass());

        // Load properties
        p = new Properties();
        FileReader file = new FileReader("./src/test/resources/config.properties");
        p.load(file);

        logger.info("Operating System: " + os);
        logger.info("Browser: " + browser);

        // Setup WebDriver
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().window().maximize();
        driver.get(p.getProperty("applicationUrl"));

        logger.info("Browser launched and navigated to URL: " + p.getProperty("applicationUrl"));

        // Setup Extent Reporting properly
        extent = ExtentManager.getExtentReports();
     //   test = extent.createTest(method.getName());
        ExtentManager.setTest(test); // ✅ Now test won't be null
        
        

        // Setup log and screenshot util
        log = new Log(); // Uses ExtentManager.getTest()
        screenShots = new ScreenshotUtil();

      futureDates =new  Iween_FutureDates();
      
       
        
        
    }
    
   


    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            logger.info("Closing browser.");
            driver.quit();
        }
    }
    
    /*
    @AfterSuite //enable this when ever u wnat to send to email
    public void afterSuite() {
        String reportsFolder = "C:/Users/LENOVO/Downloads/iween-main/iween-main/reports";
        String latestReport = ReportUtils.getLatestReportPath(reportsFolder);

        if (latestReport != null) {
            System.out.println("Sending report: " + latestReport);
            EmailUtils.sendEmailWithAttachment(latestReport);
        } else {
            System.out.println("No report file found in folder: " + reportsFolder);
        }
    }
    */
}
