package com.biit.gui.tester;

import com.biit.abcd.logger.IntegrationTestLogging;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {
    private static final String SCREENSHOT_TYPE = ".png";

    @Override
    public void onTestStart(ITestResult result) {
        IntegrationTestLogging.info(this.getClass().getName(), "Test '" + result.getMethod().getMethodName() + "' from '" + result.getTestClass().getName() + "' has started.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        IntegrationTestLogging.info(this.getClass().getName(), "Test '" + result.getMethod().getMethodName() + "' from '" + result.getTestClass().getName() + "' has finished.");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // here we are accessing the driver object that we added in Test class
        WebDriver driver = (WebDriver) result.getTestContext().getAttribute("driver");
        IntegrationTestLogging.errorMessage(this.getClass().getName(), "Test '" + result.getMethod().getMethodName() + "' from '" + result.getTestClass().getName() + "' has failed!");
        takeScreenshot(driver, result.getTestClass().getName() + "_" + result.getMethod().getMethodName() + ".png");
        IntegrationTestLogging.debug(this.getClass().getName(), "Screenshot '" + result.getTestClass().getName() + "_" + result.getMethod().getMethodName() + ".png' stored.");
    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        IntegrationTestLogging.info(this.getClass().getName(), "Starting tests from '" + context.getName() + "'.");
    }

    @Override
    public void onFinish(ITestContext context) {
        IntegrationTestLogging.info(this.getClass().getName(), "Tests finished from '" + context.getName() + "'.");
    }

    /**
     * Takes a screenshot of the webpage and stores it in the Temp folder
     *
     * @param screenshotName
     */
    public void takeScreenshot(WebDriver driver, String screenshotName) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            FileUtils.copyFile(scrFile,
                    new File(System.getProperty("java.io.tmpdir") + File.separator + screenshotName + "_" + dtf.format(LocalDateTime.now()) + SCREENSHOT_TYPE),
                    true);
        } catch (IOException e) {
        }
    }
}
