package tests;

import com.changepassword.service.PasswordChanger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExtentReports.ExtentTestManager;
import utils.csvReader.CSVDataProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Iterator;

public class ChangePasswordTest {


    @BeforeTest
    public void doBeforeTest() {
        PasswordChanger.setOldPasswordSystemCheck(true);
    }

    @Test(priority = 0, description = "null sent as new password")
    public void nullAsNewPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "null sent as new password");
        Assert.assertEquals(PasswordChanger.changePassword("karan12K#@", null),false);
    }

    @Test(priority = 0, description = "blank new password")
    public void blankNewPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "blank new password");
        Assert.assertEquals(PasswordChanger.changePassword("karan12K#@", ""),false);
    }

    @Test(priority = 0, description = "only spaces in new password")
    public void onlySpacesInNewPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "only spaces in new password");
        Assert.assertEquals(PasswordChanger.changePassword("karan12K#@", "                      "),false);
    }

    @Test(priority = 0, description = "Leading spaces in new password")
    public void leadingSpacesInNewPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "Leading spaces in new password");
        Assert.assertEquals(PasswordChanger.changePassword("karan12K#@", "              kK12@"),false);
    }

    @Test(priority = 0, description = "Trailing spaces in new password")
    public void trailingSpacesInNewPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "Trailing spaces in new password");
        Assert.assertEquals(PasswordChanger.changePassword("karan12K#@", "kK12@             "),false);
    }

    @Test(priority = 0, description = "Spaces in between new password")
    public void spacesInBetweenNewPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "Spaces in between new password");
        Assert.assertEquals(PasswordChanger.changePassword("karan12K#@", "k K 1 2 @ 4 h s r t u i e"),false);
    }

    @Test(priority = 0, description = "Escape sequence in between new password")
    public void escapeSequenceInBetweenNewPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "Escape sequence in between new password");
        Assert.assertEquals(PasswordChanger.changePassword("karan12K#@", "\n\tkK12@poiu8mytrepo"),false);
    }

    @Test(priority = 0, description = "Valid new password starts with a lower case character")
    public void validNewPasswordStartsWithLowerCase(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "Valid new password starts with a upper case character");
        Assert.assertEquals(PasswordChanger.changePassword("karan12K#@", "kKjhkhjhkbdxd12@#$&"),true);
    }

    @Test(priority = 0, description = "Valid new password starts with a upper case character")
    public void validNewPasswordStartsWithUpperCase(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "Valid new password starts with a upper case character");
        Assert.assertEquals(PasswordChanger.changePassword("karan12K#@", "RkKkhjhkbdxd12@#$&"),true);
    }

    @Test(priority = 0, description = "Valid new password starts with a digit")
    public void validNewPasswordStartsWithDigit(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "Valid new password starts with a digit");
        Assert.assertEquals(PasswordChanger.changePassword("karan12K#@", "1RkK2hkbdxcvxd12@#$&"),true);
    }

    @Test(priority = 0, description = "Valid new password starts with an allowed special character")
    public void validNewPasswordStartsWithAllowedSpecialCharacter(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "Valid new password starts with an allowed special character");
        Assert.assertEquals(PasswordChanger.changePassword("karan12K#@", "#1RkK22hkbdxd1#$&o"),true);
    }

    @Test(priority = 0, description = "Valid new password which is Reverse of correct Old password")
    public void validNewPasswordReverseOfCorrectOldPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "Valid new password which is Reverse of correct Old password");
        Assert.assertEquals(PasswordChanger.changePassword("#1RkK22hkbdxd1#$&o", "o&$#1dxdbkh22KkR1#"),true);
    }

    @Test(priority = 0, description = "New password same as Old password but all upper change to lower change and vice versa")
    public void newPasswordSameAsOldPasswordWithUpperChangeToLowerAndSimilar(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password same as Old password but all upper change to lower change and vice versa");
        Assert.assertEquals(PasswordChanger.changePassword("o&$#1dxdbkh22KkR1#", "O&$#1DXDBKH22kKr1#"),true);
    }

    @Test(priority = 0, description = "New password with only un allowed special characters")
    public void newPasswordWithOnlyUnallowedSpecialCharacters(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only un allowed special characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "%^()_+=:,.?;||~.?;||~"),false);
    }

    //new password <18
    @Test(priority = 0, description = "New password with <18 characters")
    public void newPasswordLessThan18(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with <18 characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "O&$#1DXDB2kK"),false);
    }
    //new password >18
    @Test(priority = 0, description = "New password with >18 characters")
    public void newPasswordGreaterThan18(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with >18 characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "O&$#1DXDBKH22kKr1#p"),true);
    }

    //new password = 18
    @Test(priority = 0, description = "New password with =18 characters")
    public void newPasswordEqualTo18(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with =18 characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "O&$#1DXDBKH22kKr1#"),true);
    }


    @Test(priority = 0, description = "New password with only allowed special characters")
    public void newPasswordWithOnlyAllowedSpecialCharacters(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only allowed special characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "!@#$&*!@#$&*!@#$&*!@#$&*"),false);
    }

    @Test(priority = 0, description = "New password with only digit and allowed special characters")
    public void newPasswordWithOnlyDigitAndAllowedSpecialCharacters(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only digit and allowed special characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "42343243437878!@#$&*!@#$&*"),false);
    }

    @Test(priority = 0, description = "New password with only digit")
    public void newPasswordWithOnlyDigit(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only digit");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "42343243437454543878"),false);
    }

    @Test(priority = 0, description = "New password with only lower case characters")
    public void newPasswordWithOnlyLowerCaseCharacters(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only lower case characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "ajshskndkasndksndskajn"),false);
    }

    @Test(priority = 0, description = "New password with only lower case characters and Allowed special characters")
    public void newPasswordWithOnlyLowerCaseCharactersAndAllowedSpecialCharacters(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only lower case characters and Allowed special characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "ajshskndkasndksndskajn!@#$&*!@#$&*"),false);
    }

    @Test(priority = 0, description = "New password with only lower case characters and digits")
    public void newPasswordWithOnlyLowerCaseCharactersAndDigits(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only lower case characters and digits");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "ajshskndkasndksndskajn2312321"),false);
    }

    @Test(priority = 0, description = "New password with only lower case characters, digits and allowed special characters")
    public void newPasswordWithOnlyLowerCaseCharactersDigitsAndAllowedSpecialCharacters(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only lower case characters, digits and allowed special characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "ajshskndkasndksndskajn2312321!@#$&*!@#$&*"),false);
    }

    @Test(priority = 0, description = "New password with only upper case characters")
    public void newPasswordWithOnlyUpperCaseCharacters(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only upper case characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "JGJHGJHAGSHJCASGCHJS"),false);
    }

    @Test(priority = 0, description = "New password with only upper case characters and Allowed special characters")
    public void newPasswordWithOnlyUpperCaseCharactersAndAllowedSpecialCharacters(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only upper case characters and Allowed special characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "JGJHGJHAGSHJCASGCHJS!@#$&*!@#$&*"),false);
    }

    @Test(priority = 0, description = "New password with only upper case characters and digits")
    public void newPasswordWithOnlyUpperCaseCharactersAndDigits(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only upper case characters and digits");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "JGJHGJHAGSHJCASGCHJS7676823"),false);
    }

    @Test(priority = 0, description = "New password with only upper case characters, digits and allowed special characters")
    public void newPasswordWithOnlyUpperCaseCharactersDigitsAndAllowedSpecialCharacters(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only upper case characters, digits and allowed special characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "JGJHGJHAGSHJCASGCHJS7676823!@#$&*!@#$&*"),false);
    }

    @Test(priority = 0, description = "New password with only upper case characters and lower case characters")
    public void newPasswordWithOnlyUpperAndLowerCaseCharacters(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only upper case characters and lower case characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "JGJHGJHAGSHJCASGCHJSkjhdashdksahds"),false);
    }

    @Test(priority = 0, description = "New password with only upper case characters, lower case characters and Digits")
    public void newPasswordWithOnlyUpperCaseLowerCaseCharactersAndDigits(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with only upper case characters, lower case characters and Digits");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "JGJHGJHAGSHJCASGCHJSkjhdashdksahds87343249374"),false);
    }

    @Test(priority = 0, description = "New password with upper case characters, lower case characters, Digits and allowed special characters")
    public void newPasswordWithUpperCaseLowerCaseCharactersDigitsAndAllowedSpecialCharacters(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with upper case characters, lower case characters, Digits and allowed special characters");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "KJHKJhghgh876758!@#$"),true);
    }

    @Test(priority = 0, description = "New password with upper case characters Repeated <4 times")
    public void newPasswordWithUpperCaseRepeatedLessThanFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with upper case characters Repeated <4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "KKKabcd9868966!@#$"),true);
    }

    @Test(priority = 0, description = "New password with upper case characters Repeated =4 times")
    public void newPasswordWithUpperCaseRepeatedEqualToFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with upper case characters Repeated =4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "KKKKabcd9868966!@#$"),true);
    }

    @Test(priority = 0, description = "New password with upper case characters Repeated >4 times")
    public void newPasswordWithUpperCaseRepeatedGreaterThanFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with upper case characters Repeated >4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "KKKKKabcd9868966!@#$"),false);
    }

    @Test(priority = 0, description = "New password with lower case characters Repeated <4 times")
    public void newPasswordWithLowerCaseRepeatedLessThanFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with lower case characters Repeated <4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "1KKKaaabcd9868966!@#$"),true);
    }

    @Test(priority = 0, description = "New password with lower case characters Repeated =4 times")
    public void newPasswordWithLowerCaseRepeatedEqualToFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with lower case characters Repeated =4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "KKKaaaabcd9868966!@#$"),true);
    }

    @Test(priority = 0, description = "New password with lower case characters Repeated >4 times")
    public void newPasswordWithLowerCaseRepeatedGreaterThanFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with lower case characters Repeated >4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "KKKaaaaabcd9868966!@#$"),false);
    }

    @Test(priority = 0, description = "New password with digit Repeated <4 times")
    public void newPasswordWithDigitRepeatedLessThanFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with digit Repeated <4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "gKKaaabcd986866!@#$"),true);
    }

    @Test(priority = 0, description = "New password with digit Repeated =4 times")
    public void newPasswordWithDigitRepeatedEqualToFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with digit Repeated =4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "hKKaaabcd9868666!@#$"),true);
    }

    @Test(priority = 0, description = "New password with digit Repeated >4 times")
    public void newPasswordWithDigitRepeatedGreaterThanFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with digit Repeated >4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "hKKaaabcd98686666!@#$"),false);
    }

    @Test(priority = 0, description = "New password with allowed special character Repeated <4 times")
    public void newPasswordWithAllowedSpecialCharacterRepeatedLessThanFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with allowed special character Repeated <4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "gKKaaabcd986866!!!@"),true);
    }

    @Test(priority = 0, description = "New password with allowed special character Repeated =4 times")
    public void newPasswordWithAllowedSpecialCharacterRepeatedEqualToFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with allowed special character Repeated =4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "gKKaaabcd986866!!!!"),true);
    }

    @Test(priority = 0, description = "New password with allowed special character Repeated >4 times")
    public void newPasswordWithAllowedSpecialCharacterRepeatedGreaterThanFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with allowed special character Repeated >4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "gKKaaabcd986866!!!!!"),false);
    }

    @Test(priority = 0, description = "New password with allowed special characters <4 times")
    public void newPasswordWithAllowedSpecialCharactersLessThanFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with allowed special characters <4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "hKKaaabcd9868666!@#"),true);
    }

    @Test(priority = 0, description = "New password with allowed special characters =4 times")
    public void newPasswordWithAllowedSpecialCharactersEqualToFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with allowed special characters =4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "hKKaaabcd9868666!@#*"),true);
    }

    @Test(priority = 0, description = "New password with allowed special characters >4 times")
    public void newPasswordWithAllowedSpecialCharactersGreaterThanFourTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with allowed special characters >4 times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "hKKaaabcd9868666!@#*&"),false);
    }

    @Test(priority = 0, description = "New password with digits <50% times")
    public void newPasswordWithDigitsLessThanFiftyPercentTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with digits <50% times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "hKKaaabcd9868666!@#*1"),true);
    }

    @Test(priority = 0, description = "New password with digits =50% times")
    public void newPasswordWithDigitsEqualToFiftyPercentTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with digits =50% times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "hKKaab9868666!@#*123"),false);
    }

    @Test(priority = 0, description = "New password with digits >50% times")
    public void newPasswordWithDigitsGreaterThanFiftyPercentTimes(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password with digits >50% times");
        Assert.assertEquals(PasswordChanger.changePassword("Kk12@", "hKKaab9868666!@#*1235"),false);
    }

    @Test(priority = 0, description = "New password and old password 100% match")
    public void newPassword100PercentMatchWithOldPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password and old password 100% match");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#*12we", "hKKaab9868666!@#*12we"),false);
    }

    @Test(priority = 0, description = "New password and old password 95% match")
    public void newPassword95PercentMatchWithOldPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password and old password 95% match");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#*12w", "hKKaab9868666!@#*12we"),false);
    }

    @Test(priority = 0, description = "New password and old password 90% match")
    public void newPassword90PercentMatchWithOldPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password and old password 90% match");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#*12", "hKKaab9868666!@#*12we"),false);
    }

    @Test(priority = 0, description = "New password and old password 85% match")
    public void newPassword85PercentMatchWithOldPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password and old password 85% match");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#*1", "hKKaab9868666!@#*12we"),false);
    }

    @Test(priority = 0, description = "New password and old password 80% match")
    public void newPassword80PercentMatchWithOldPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password and old password 80% match");
        Assert.assertEquals(PasswordChanger.changePassword("KKaab9868666!@#*", "KKaab9868666!@#*12we"),false);
    }

    @Test(priority = 0, description = "New password and old password <80% match")
    public void newPasswordLessThan80PercentMatchWithOldPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password and old password <80% match");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#", "hKKaab9868666!@#*12we"),true);
    }

    @Test(priority = 0, description = "New password palindrome of old password")
    public void newPasswordLessPalindromeOfOldPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password palindrome of old password");
        Assert.assertEquals(PasswordChanger.changePassword("ew21*#@!6668689baaKKh", "hKKaab9868666!@#*12we"),true);
    }

    @Test(priority = 0, description = "New password has Regex (The value must be between 0.10 and 0.90)")
    public void newPasswordHasRegex1(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password has Regex (The value must be between 0.10 and 0.90)");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#", "/^0[,.]([1-8]\\d|90)$/"),false);
    }

    @Test(priority = 0, description = "New password has Regex (the value must be in 3 and 4, taking into account decimals.)")
    public void newPasswordHasRegex2(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password has Regex (the value must be in 3 and 4, taking into account decimals.)");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#", "/^(3([.,]\\d{1,2})?|4([.,]0{1,2})?)$/"),false);
    }

    @Test(priority = 0, description = "New password has Regex of our test condition 1")
    public void newPasswordHasRegexOfTestCondition1(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password has Regex of our test condition 1");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#", "^(?!\\d+$)([a-zA-Z0-9!@#$&*][a-zA-Z0-9!@#$&*]*)$"),false);
    }

    @Test(priority = 0, description = "New password has unusual character ♔")
    public void newPasswordHasUnusualCharacter(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password has unusual character ♔");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#", "♔♔♔♔♔♔♔♔♔♔♔♔♔♔♔♔♔♔♔♔♔♔"),false);
    }

    @Test(priority = 0, description = "New password has unicode characters")
    public void newPasswordHasUnicodeCharacter(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password has unicode characters");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#", "¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶"),false);
    }

    @Test(priority = 0, description = "Old Password doesn't match with System")
    public void oldPasswordDoesntMatchWithSystem(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "Old Password doesn't match with System");
        PasswordChanger.setOldPasswordSystemCheck(false);
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@", "ew21*#@!6668689baaKKh"),false);
        PasswordChanger.setOldPasswordSystemCheck(true);
    }

    @Test(priority = 0, description = "Old Password null")
    public void oldPasswordnull(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "Old Password null");
        Assert.assertEquals(PasswordChanger.changePassword(null, "ew21*#@!6668689baaKKh"),false);
    }

    @Test(priority = 0, description = "Old Password empty")
    public void oldPasswordEmpty(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "Old Password empty");
        Assert.assertEquals(PasswordChanger.changePassword("", "ew21*#@!6668689baaKKh"),false);
    }

    //ℹ️⌛️⚠️✒️❤️🀄️🈚️ℹ️⌛️⚠️✒️❤️🀄️🈚️ℹ️⌛️⚠️✒️❤️🀄️🈚️ℹ️⌛️⚠️✒️❤️🀄️🈚️
    @Test(priority = 0, description = "New password has emoji characters")
    public void newPasswordHasEmojiCharacter(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName()+ " : New Password : ℹ️⌛️⚠️✒️❤️\uD83C\uDC04️\uD83C\uDE1A️ℹ️⌛️⚠️✒️❤️\uD83C\uDC04️\uD83C\uDE1A️ℹ️⌛️⚠️✒️❤️\uD83C\uDC04️\uD83C\uDE1A️ℹ️⌛️⚠️✒️❤️\uD83C\uDC04️\uD83C\uDE1A️", "New password has emoji characters");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#", "ℹ️⌛️⚠️✒️❤️\uD83C\uDC04️\uD83C\uDE1A️ℹ️⌛️⚠️✒️❤️\uD83C\uDC04️\uD83C\uDE1A️ℹ️⌛️⚠️✒️❤️\uD83C\uDC04️\uD83C\uDE1A️ℹ️⌛️⚠️✒️❤️\uD83C\uDC04️\uD83C\uDE1A️️"),false);
    }

    @Test(priority = 0, description = "New password contains base64 encoded string of a valid password")
    public void Base64EncodedStringAsNewPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "New password has unicode characters");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#", "aEtLYWFiOTg2ODY2NiFAIw=="),false);
    }

    //eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
    @Test(priority = 0, description = "JWT token sent as new password")
    public void JWTTokenAsNewPassword(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "JWT token sent as new password");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"),false);
    }

    @DataProvider(name = "sqlInjectionData")
    public Iterator<Object> sqlInjectionData() throws IOException {
        CSVDataProvider csvDataProvider = new CSVDataProvider();
        String workingDir = System.getProperty("user.dir");
        String testDataFilePath = workingDir + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "Sql_Injection_String.csv";
        return csvDataProvider.parseCsvData(testDataFilePath);
    }

    @Test(priority = 0, description = "SQL injection Commands as new password", dataProvider = "sqlInjectionData")
    public void SQLInjectionAsNewPassword(Method method,String newPassword) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName() + " : New Password : "+newPassword, "SQL injection Commands as new password");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#", newPassword), false);
    }

    @DataProvider(name = "DangerousLinuxCommands")
    public Iterator<Object> DangerousLinuxCommands() throws IOException {
        CSVDataProvider csvDataProvider = new CSVDataProvider();
        String workingDir = System.getProperty("user.dir");
        String testDataFilePath = workingDir + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "Dangerous_Linux_Commands.csv";
        return csvDataProvider.parseCsvData(testDataFilePath);
    }

    @Test(priority = 0, description = "New password contains dangerous Linux commands", dataProvider = "DangerousLinuxCommands")
    public void DangerousLinuxCommandsAsNewPassword(Method method,String newPassword) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName() + " : New Password : "+newPassword, "New password contains dangerous Linux commands");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#", newPassword), false);
    }

    @DataProvider(name = "CrossSiteScripting")
    public Iterator<Object> CrossSiteScripting() throws IOException {
        CSVDataProvider csvDataProvider = new CSVDataProvider();
        String workingDir = System.getProperty("user.dir");
        String testDataFilePath = workingDir + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "Cross_Site_Scripting.csv";
        return csvDataProvider.parseCsvData(testDataFilePath);
    }

    @Test(priority = 0, description = "New password contains cross site scripting commands", dataProvider = "DangerousLinuxCommands")
    public void CrossSiteScriptingAsNewPassword(Method method,String newPassword) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName() + " : New Password : "+newPassword, "New password contains cross site scripting commands");
        Assert.assertEquals(PasswordChanger.changePassword("hKKaab9868666!@#", newPassword), false);
    }




}