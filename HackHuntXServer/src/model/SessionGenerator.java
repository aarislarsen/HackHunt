/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dragon
 */
public class SessionGenerator 
{
    private final SecureRandom random = new SecureRandom();
    /**
     * default constructor
     */
    public SessionGenerator()
    {
        //do nothing
    }
    
    /**
     * gets a random token value
     * @return the token value as base64
     */
    public String getToken()
    {
        BigInteger bi = new BigInteger(256, random);
        String token = bi.toString(32);        
        return encode(token);
    }
    
    /**
     * Base64 encodes the input message (Unicode UTF-8)
     * @param message the message to encode
     * @return the encoded message as a string
     */
    public String encode(String message)
    {
        try 
        {
            String encoded = new String(Base64.getEncoder().encode(message.getBytes("UTF-8")));
            return encoded;
        } 
        catch (UnsupportedEncodingException ex) 
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void main(String[] args)
    {
        SessionGenerator sg = new SessionGenerator();
    }
}
