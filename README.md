# AgodaHomeTest

1) This project is created using IntelliJ IDE in Java programming language

2) There are two main classes for changePassword functionlity under com.changepassword.service package where the core business logic is written

-EditDistance - to find the match in between old and new password
-PasswordChanger - This has the core logic for password validations as per the requirement

3) I have used log4j for logging purpose
The log4j xml is provided in the resources folder - log4j2.xml

In the log4j2.xml , fileName should be updated by user as per system <path to the logger>/change-password.log
This is the location where the framework log file will be generated apart from console logging

4) I have kept console logging for simple debugging which can be removed as we centralized logger as metioned in #4.

5) I have used TestNG for the automated testing of the test cases 
a. TestNG.xml file is in the TestPasswords folder which is the project parent directory
b. All the automated test cases are in ChangePasswordTest class which is in test package

6) I have integrated the Extent report to see the test results as an html file in browser post automated test execution.
This html report gets generated in the ExtentReports folder upon running the automted tests from TestNG.xml file as mentioned in #5
The Extent report is generated by the name : ExtentReportResults.html

To support Extent reporting we have added some util classes under utils.ExtentReports
a. ExtentManager class - here you can define the location where the report html should be generaterd
b. ExtentTestManager class 

7) Testlistner class under Listners Package is used by TestNG to print the start,End and other activites of test cases whenever an automated test runs

8) Data driven test cases are also supported using data providers in TestNG
This will help to read list of new passwords from CSV file 
All the passwords read from CSV file will be executed by the test case till the end of CSV file

To support that we have created under utils.csvReader package CSVDataProvider class that reads all the test data from test cases.
We have used the same in test cases like (SQL injection, Cross site scripting and Dangerous linux commands) being passed as new password

9) Test.Resources folder has the .csv files for 
SQL injection string
Cross site scripting strings
Dangerous linux commands

This is used by data providers

10) We have used maven build tools for fetching external dependencies under pom.xml file

----------------------------------------------------------------------------------------------------------------------------------------------------------------------
#Steps to run from IDE :
1) git clone the project'
2) Open the TestPasswords project in IntelliJ IDE
3) Open the TestNG.xml file
4) Right click and run the TestNG.xml file

This will execute all the test cases automated in ChangePasswordTest class under package tests.ChangePasswordTest

#To run individual test cases
1) go to ChangePasswordTest class under package tests.ChangePasswordTest
2) right click on the test method you wish to execute that has @Test annotation
3) Click run for that test method name

----------------------------------------------------------------------------------------------------------------------------------------------------------------------
#Steps to add a new Automated test case
1) go to ChangePasswordTest class under package tests.ChangePasswordTest
2) At the end of the file
add

@Test(priority = 0, description = <give a suitable test description>")
    public void testCaseMethodName(Method method) {
        //ExtentReports Description
        ExtentTestManager.startTest(method.getName(), "<give a suitable test description>");
        Assert.assertEquals(PasswordChanger.changePassword("<give old password>", "<give new password>"),<give boolen aasertion : true/false>);
    }

----------------------------------------------------------------------------------------------------------------------------------------------------------------------
#Following 65 Test cases have been Automated

1. null sent as new password - Negative
2. blank new password - Negative
3. only spaces in new password   
4. Leading spaces in new password
5. Trailing spaces in new password
6. Spaces in between new password"
7. Escape sequence in between new password 
8. Valid new password starts with a lower case character
9. Valid new password starts with a upper case character   
10. Valid new password starts with a digit
11. Valid new password starts with an allowed special character 
12. Valid new password which is Reverse of correct Old password
13. New password same as Old password but all upper change to lower change and vice versa   
14. New password with only un allowed special characters
15. New password with <18 characters
16. New password with >18 characters 
17. New password with =18 characters
18. New password with only allowed special characters
19. New password with only digit and allowed special characters
20. New password with only digit
21. New password with only lower case characters
22. New password with only lower case characters and Allowed special characters
23. New password with only lower case characters and digits  
24. New password with only lower case characters, digits and allowed special characters
25. New password with only upper case characters
26. New password with only upper case characters and Allowed special characters
27. New password with only upper case characters and digits
28. New password with only upper case characters, digits and allowed special characters
29. New password with only upper case characters and lower case characters 
30. New password with only upper case characters, lower case characters and Digits
31. New password with upper case characters, lower case characters, Digits and allowed special characters
32. New password with upper case characters Repeated <4 times
33. New password with upper case characters Repeated =4 times
34. New password with upper case characters Repeated >4 times
35. New password with lower case characters Repeated <4 times
36. New password with lower case characters Repeated =4 times
37. New password with lower case characters Repeated >4 times
38. New password with digit Repeated <4 times
39. New password with digit Repeated =4 times
40. New password with digit Repeated >4 times
41. New password with allowed special character Repeated <4 times
42. New password with allowed special character Repeated =4 times
43. New password with allowed special character Repeated >4 times 
44. New password with digits <50% times
45. New password with digits =50% times   
46. New password with digits >50% times
47. New password and old password 100% match 
48. New password and old password 95% match
49. New password and old password 90% match
50. New password and old password 85% match
51. New password and old password 80% match
52. New password and old password <80% match
53. New password palindrome of old password
54. New password has Regex (The value must be between 0.10 and 0.90) /^0[,.]([1-8]\\d|90)$/
55. New password has Regex (the value must be in 3 and 4, taking into account decimals.) /^(3([.,]\\d{1,2})?|4([.,]0{1,2})?)$/
56. New password has Regex of our test condition 1 ^(?!\\d+$)([a-zA-Z0-9!@#$&*][a-zA-Z0-9!@#$&*]*)$
57. New password has unusual character ♔
58. New password has unicode characters ¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶
59. Old Password doesn't match with System
60. Old Password null
61. Old Password empty
62. New password has emoji characters - ℹ️⌛️⚠️✒️❤️🀄️🈚️ℹ️⌛️⚠️✒️❤️🀄️🈚️ℹ️⌛️⚠️✒️❤️🀄️🈚️ℹ️⌛️⚠️✒️❤️🀄️🈚️
63. SQL injection Commands as new password - Data provider used
64. New password contains dangerous Linux commands - Data provider used
65. New password contains cross site scripting commands - Data provider used

