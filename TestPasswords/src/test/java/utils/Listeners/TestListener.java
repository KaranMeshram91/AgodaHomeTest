package utils.Listeners;

import com.changepassword.service.PasswordChanger;
import com.relevantcodes.extentreports.LogStatus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import utils.ExtentReports.ExtentManager;
import utils.ExtentReports.ExtentTestManager;

public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("onStart method " + iTestContext.getName());
        logger.info("------------------------------------------------------------------------------------------------");
        logger.info("onStart method " + iTestContext.getName());
        //iTestContext.setAttribute("WebDriver", this.driver);
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("onFinish method " + iTestContext.getName());
        logger.info("onFinish method " + iTestContext.getName());
        //Do tier down operations for extentreports reporting!
        ExtentTestManager.endTest();
        ExtentManager.getReporter().flush();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("onTestStart method " + getTestMethodName(iTestResult) + " start");
        logger.info("onTestStart method " + getTestMethodName(iTestResult) + " start");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("onTestSuccess method " + getTestMethodName(iTestResult) + " succeed");
        logger.info("onTestSuccess method " + getTestMethodName(iTestResult) + " succeed");
        logger.info("------------------------------------------------------------------------------------------------\n");
        //ExtentReports log operation for passed tests.
        ExtentTestManager.getTest().log(LogStatus.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("onTestFailure method " + getTestMethodName(iTestResult) + " failed");
        logger.info("onTestFailure method " + getTestMethodName(iTestResult) + " failed");

        //Get driver from BaseTest and assign to local webDriver variable.
        Object testClass = iTestResult.getInstance();
        //WebDriver webDriver = ((BaseTest) testClass).getDriver();

        //Take base64Screenshot screenshot.
        //String base64Screenshot = "data:image/png;base64," + ((TakesScreenshot) webDriver).
            //getScreenshotAs(OutputType.BASE64);

        //ExtentReports log and screenshot operations for failed tests.
        ExtentTestManager.getTest().log(LogStatus.FAIL, "Test Failed");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("onTestSkipped method " + getTestMethodName(iTestResult) + " skipped");
        logger.info("onTestSkipped method " + getTestMethodName(iTestResult) + " skipped");
        //ExtentReports log operation for skipped tests.
        ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
        logger.info("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
    }

}
