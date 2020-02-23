package com.changepassword.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PasswordChanger {

    private static final Logger logger = LogManager.getLogger(PasswordChanger.class);
    /*below regex checks for
    *(?!\d+$) - upfront check that not all characters are digit
    * [a-zA-Z0-9!@#$&*] should start with atleast lower case or upper case or a digit or allowed special characters !@#$&*
    * [a-zA-Z0-9!@#$&*]* Any number of repeatition of lower case or upper case or a digit or allowed special characters !@#$& */
    private static Pattern allowedCharPattern = Pattern.compile("^(?!\\d+$)([a-zA-Z0-9!@#$&*][a-zA-Z0-9!@#$&*]*)$");
    /*Below regex checks that
    * (?=.*[a-z]) should contain atleast 1 lower case
    * (?=.*[A-Z]) should contain atleast 1 upper case
    * (?=.*\d) should contain atleast 1 digit
    * (?=.*([^\w])) should contain atleast 1 special symbol
    * +$ Match upto the end of string*/
    private static Pattern allowedLenghtPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*([^\\w])).+$");

    //created all the constants for various checks that can be changed in the future easily
    private static final int PASSWORD_MIN_LENGTH = 18;
    private static final int CHAR_REPEAT_LENGTH = 4;
    private static final int DIGIT_ALLOWED_PERCENT = 50;
    private static final int ALLOWED_MATCH_PERCENT = 80;
    private static final int SPECIAL_CHAR_REPEAT_LENGTH = 4;

    //Mock boolean variable to check if old password matches with the system true/false
    private static boolean oldPasswordSystemCheck;

    /*Main function to check if change password is allowed
    * Checks 3 conditions and returns true if all the 3 of them are satisfied
    * 1) verifyOldPassword - checks old password with system
    * 2) validateNewPassword - validated if the new password is as per the rules defined
    * 3) withinMatchPercentLimit - if the new password and old password are within the match % limit defined */
    public static boolean changePassword(String oldPassword, String newPassword) {
        logger.info("In Change password for Old Password : {} and New Password : {}",oldPassword,newPassword);
        System.out.println("In Change password for Old Password :" +oldPassword + " and New Password :" + newPassword);
        boolean allowPasswordChange = false;
        if (verifyOldPassword(oldPassword) && validateNewPassword(newPassword) && withinMatchPercentLimit(oldPassword, newPassword)) {
            allowPasswordChange = true;
        }
        System.out.println("Allow password change : "+allowPasswordChange);
        logger.info("Allow password change : {}",allowPasswordChange);
        return allowPasswordChange;
    }


    private static boolean verifyOldPassword(String oldPassword) {
        if (null != oldPassword && !("".equals(oldPassword))) {
            return mockOldPasswordSystemCheck();
        } else {
            System.out.println("null or empty old password");
            logger.error("null or empty old password");
            return false;
        }

    }

    /**
     *
     * @param newPassword - is the password to be validated as per defined rules
     * @return true if all the rules satisfied false otherwise
     */
    private static boolean validateNewPassword(String newPassword) {
        boolean isValid = false;
        //check new password is not null and non empty
        if (null != newPassword && !("".equals(newPassword))) {
            int newPasswordLength = newPassword.length();
            /*check the new password satisfies
            * 1) The minimum length requirement
            * 2) Check for conditions - At least 18 alphanumeric characters and list of special chars !@#$& and At least 1 Upper case, 1 lower case ,least 1 numeric, 1 special character*/
            if (newPasswordLength >= PASSWORD_MIN_LENGTH && isAlphaNumeric(newPassword) ) {
                //Map to store the counts of each char in the new password
                Map<Character, Integer> passwordCharCount = new HashMap<Character, Integer>();
                int allowedSpecialCharCount = 0;
                int allowedDigitCount = 0;
                float digitPercentage = 0;
                //boolean to check if the loop breaks if any one of the allowed new password rule breaks
                boolean continueLoop = true;
                for (int i = 0; i < newPasswordLength && continueLoop; i++) {
                    char thisChar = newPassword.charAt(i);
                    Integer charCount = passwordCharCount.get(thisChar);
                    // if a char is encountered for the first time
                    if (null == charCount) {
                        passwordCharCount.put(thisChar, 1);
                    } else {
                        charCount++;
                        //if char count is > that the allowed char repeat length break
                        if (charCount > CHAR_REPEAT_LENGTH) {
                            continueLoop = false;
                            System.out.println(thisChar + " Repeate length exceeded : " + CHAR_REPEAT_LENGTH);
                            logger.error("{} Repeate length exceeded : {}",thisChar, CHAR_REPEAT_LENGTH);

                        } else {
                            passwordCharCount.put(thisChar, charCount);
                        }
                    }
                    // if the char is digit track its count to check the Digit allowed % is not breached
                    if (Character.isDigit(thisChar)) {
                        allowedDigitCount += 1;
                        digitPercentage = (float) (allowedDigitCount * 100) / newPasswordLength;
                        /*System.out.println("Digit % : "+ digitPercentage + " : " + allowedDigitCount+"/"+newPasswordLength);
                        logger.debug("Digit % : "+ digitPercentage + " : " + allowedDigitCount+"/"+newPasswordLength);*/
                        if (digitPercentage >= DIGIT_ALLOWED_PERCENT) {
                            continueLoop = false;
                            System.out.println(thisChar + " Digit percent equal or exceeded : " + DIGIT_ALLOWED_PERCENT);
                            logger.error( "{} Digit percent equal or exceeded : {} ",thisChar,DIGIT_ALLOWED_PERCENT);
                        }
                    }
                    // special characters count to be tracked to see if they do not exceed special character allowed repeat length
                    else if (!(thisChar >= 'A' && thisChar <= 'Z') && !(thisChar >= 'a' && thisChar <= 'z')) {
                        allowedSpecialCharCount++;
                        /*System.out.println("Allowed Special character count : "+ allowedSpecialCharCount);
                        logger.debug("Allowed Special character count : {}",allowedSpecialCharCount);*/
                        if (allowedSpecialCharCount > SPECIAL_CHAR_REPEAT_LENGTH) {
                            continueLoop = false;
                            System.out.println(thisChar + " Allowed special character count exceeded : " + SPECIAL_CHAR_REPEAT_LENGTH);
                            logger.error("{} Allowed special character count exceeded : {}",thisChar,SPECIAL_CHAR_REPEAT_LENGTH);
                        }
                    }
                }
                //some loggers and console output for debugging
                System.out.println("Digit % : "+ digitPercentage + " : " + allowedDigitCount+"/"+newPasswordLength);
                logger.debug("Digit % : "+ digitPercentage + " : " + allowedDigitCount+"/"+newPasswordLength);
                System.out.println("Allowed Special character count : "+ allowedSpecialCharCount);
                logger.debug("Allowed Special character count : {}",allowedSpecialCharCount);
                System.out.println(passwordCharCount.toString());
                logger.debug(passwordCharCount.toString());
                //if the loop never breaks then all checks on new password has passed
                if (continueLoop) {
                    isValid = true;
                }
            }
            else
            {
                System.out.println("condition 1 or 2 or length check failed");
                logger.error("condition 1 or 2 or length check failed");
            }
        }
        else
        {
            System.out.println("null or empty new password");
            logger.error("null or empty new password");
        }
        logger.info("New password valid status : {}", isValid);
        System.out.println("New password Valid status : " + isValid);
        return isValid;
    }

    /**
     *
     * @param oldPassword - old password to be changed
     * @param newPassword - new password sent by calling function
     * @return - true if the match % as per the Edit distance is within allowed limit else false
     */
    private static boolean withinMatchPercentLimit(String oldPassword, String newPassword) {
        boolean isAllowedMatchPercent = false;
        int editDistance = EditDistance.editDistDP(oldPassword, newPassword, oldPassword.length(), newPassword.length());
        //finding the larger of the old and new password for % match calculation
        int largerStringLength = 0;
        if (oldPassword.length() >= newPassword.length()) {
            largerStringLength = oldPassword.length();
        } else {
            largerStringLength = newPassword.length();
        }
        float percentMatch = (float) ((largerStringLength - editDistance) * 100) / largerStringLength;
        if (percentMatch < ALLOWED_MATCH_PERCENT) {
            isAllowedMatchPercent = true;
            System.out.println("Percent match is < 80%");
            logger.error("Percent match is < 80%");
        }
        System.out.println("% match : " + percentMatch + " : Edit distance : " + editDistance + ": larger string length : " + largerStringLength);
        logger.debug("% match : " + percentMatch + " : Edit distance : " + editDistance + ": larger string length : " + largerStringLength);
        return isAllowedMatchPercent;
    }

    /**
     * Helper for mock function
     * @return true if system old password matched the given old password
     */
    public static boolean oldPasswordSystemCheck() {
        return oldPasswordSystemCheck;
    }

    // to set the old password mock check to true or false for old password test conditions
    public static void setOldPasswordSystemCheck(boolean oldPasswordSystemCheck) {
        PasswordChanger.oldPasswordSystemCheck = oldPasswordSystemCheck;
    }

    /**
     * Mock function for old password system check
     * @return true if old password matches with the System else false
     */
    private static boolean mockOldPasswordSystemCheck() {
        return oldPasswordSystemCheck();
    }

    /**
     * Function to check 2 conditions
     * 1. At least 18 alphanumeric characters and list of special chars !@#$&*
     * 2. At least 1 Upper case, 1 lower case ,least 1 numeric, 1 special character
     * @param password takes the new password
     * @return true if both the above conditions 1 and 2 satisfy by regex matching
     */
    public static boolean isAlphaNumeric(String password) {
        System.out.println("in is alpha numeric and allowed special character check");
        logger.info("in is alpha numeric and allowed special character check");
        boolean retVal = allowedCharPattern.matcher(password).find() && allowedLenghtPattern.matcher(password).find();
        if(!retVal)
        {
            logger.info("alpha numeric and allowed special character check failed");
            System.out.println("alpha numeric and allowed special character check failed");
        }
        return retVal;
    }

}
