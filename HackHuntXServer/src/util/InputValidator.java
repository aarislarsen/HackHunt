/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Dragon
 */
public class InputValidator 
{
    /**
     * default constructor
     */
    public InputValidator()
    {
        //do nothing
    }
    
    /**
     * performs blacklist-based input validation
     * @param disallowedChars a char array with all the characters you don't want
     * @return true if the input string contains any of the disallowed charaters, false if not
     */
    public boolean containsBadChars(char[] disallowedChars, String stringToValidate)
    {
        boolean containsBadChars = false;
        char[] toBeValidated = stringToValidate.toCharArray();
        
        for(int i = 0; i < toBeValidated.length ; i++)
        {
            for(int b = 0; b < disallowedChars.length ; b++)
            {
                if(toBeValidated[i] == disallowedChars[b])
                {
                    containsBadChars = true;
                }
            }
        }
        return containsBadChars;
    }
    
    
    /**
     * performs whitelist-based input validation
     * @param allowedChars a char array with all the characters you allow
     * @return true if the input string contains only allowed characters, false otherwise
     */
    public boolean isValidInput(char[] allowedChars, String stringToValidate)
    {
        boolean isOK = true;
        char[] toBeValidated = stringToValidate.toCharArray();
        
        for(int i = 0; i < toBeValidated.length ; i++)
        {
            boolean charFoundOK = false;
            for(int b = 0; b < allowedChars.length ; b++)
            {
                if(toBeValidated[i] == allowedChars[b])
                {
                    charFoundOK = true;
                    break;
                }
            }
            if(!charFoundOK)
            {
                isOK = false;                
                return isOK;
            }
        }
        return isOK;
    }    
    
    /**
     * for testing it locally
     * @param args 
     */
    public static void main(String args[])
    {
        InputValidator ipv = new InputValidator();
        char[] bad = new char[]{'<','>','/'};
        String input = "abcd<xml>";
        String input2 = "1234/";
        String input3 = "!#¤%&";
        
        char[] good = new char[]{'a','b','c','d','1','2','3','*','%','#'};
        String input4 = "abcd";
        String input5 = "123/";
        String input6 = "aaaa<xml>";
        String input7 = "ab2";       
        String input8 = "C:\\WINDOWS\\system32\\cmd.exe";
        System.out.println(ipv.isValidInput(good, input8));
    }
}
