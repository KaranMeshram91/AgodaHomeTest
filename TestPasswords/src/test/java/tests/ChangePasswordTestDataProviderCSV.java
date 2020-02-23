package tests;

import com.changepassword.service.PasswordChanger;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExtentReports.ExtentTestManager;
import utils.csvReader.CSVDataProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;

public class ChangePasswordTestDataProviderCSV {

    // Extra information:
    // 1) @BeforeClass we declared driver variable.
    // 2) We send driver variable to the page class with below declaration.
    //    Homepage homepage = new HomePage(driver);
    // 3) super () method in page class transfer the driver variable value to the BasePage class.

   /* @BeforeTest
    public void doBeforeTest() {
        PasswordChanger.setOldPasswordSystemCheck(true);
    }

    @DataProvider(name = "testData")
    public Iterator<Object[]> testData() throws IOException {
        CSVDataProvider csvDataProvider = new CSVDataProvider();
        String workingDir = System.getProperty("user.dir");
        String testDataFilePath = workingDir + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "Passwords.csv";
        return csvDataProvider.parseCsvData(testDataFilePath);
    }

    @Test(priority = 0, description = "null sent as new password", dataProvider = "testData")
    public void nullAsNewPassword(Method method, String oldPassword, String newPassword) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName() + ": Old Password : "+ oldPassword + ": New Password : "+newPassword, "null sent as new password" + ": Old Password : "+ oldPassword + ": New Password : "+newPassword);
        Assert.assertEquals(PasswordChanger.changePassword(oldPassword, newPassword), false);
    }*/


}