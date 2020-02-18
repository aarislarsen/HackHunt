/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Class with all the default methods that is being used everywhere
 * @author Dragon
 */
public class HackHuntClass 
{
    private String logname = "HackHunt Log";
    public Logger log = Logger.getLogger(logname);
    public Level INFO = Level.INFO;
    public Level DEBUG = Level.FINE; 
    public Level WARNING = Level.WARNING; 
    private String propertiesURL = "properties.properties";
    public Properties properties = new Properties();
    private int socketBufferSize = 1024;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean useSSL;    
    private Socket socket;
    public InetSocketAddress serverAddress;
    
    /**
     * loads the properties from the file
     */
    public void initProperties()
    {
        try
        {
            properties.load(new FileInputStream(propertiesURL));
        }
        catch (IOException ex)
        {
            log.log(DEBUG,""+ex);
        }
    }
    
    /**
     * configures the LogLevel based on the properties file
     */
    public void configureLogLevels()
    {        
        String level = properties.getProperty("LogLevel");
        
        switch(level)
        {
            case "INFO":    INFO = Level.INFO; 
                            DEBUG = Level.FINE; 
                            WARNING = Level.FINE;
                            break;
            case "DEBUG":   INFO = Level.INFO;
                            DEBUG = Level.INFO; 
                            WARNING = Level.INFO;
                            break;
            case "WARNING": INFO = Level.FINE; 
                            DEBUG = Level.FINE; 
                            WARNING = Level.INFO;    
                            break;
            default:        INFO = Level.INFO; 
                            DEBUG = Level.FINE; 
                            WARNING = Level.WARNING;
                            break;
        }
    }
    
    /**
     * sets up the client, and indicates whether or not it's ready
     * @return true if everything is ready, false if not
     */
    public String sendMessage(String message)
    {           
        serverAddress = new InetSocketAddress(properties.getProperty("ServerIP"),Integer.parseInt(properties.getProperty("ServerPort")));
        
        useSSL = Boolean.parseBoolean(properties.getProperty("UseSSL"));
        
        if(!useSSL)
        {
            log.log(INFO, "Client configured without SSL");
            socket = new Socket(); 
            try 
            {                 
                socket.setSendBufferSize(socketBufferSize);
                socket.setReceiveBufferSize(socketBufferSize);
                socket.setTcpNoDelay(true);
                socket.setKeepAlive(false);
                socket.setReuseAddress(true);
                socket.connect(serverAddress);
                out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream())); 
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            } 
            catch (SocketException ex) 
            {
                log.log(DEBUG,""+ex);            
            } 
            catch (IOException ex) 
            {
                log.log(DEBUG,""+ex);
            }            
        }
        else
        {
            try 
            {
                log.log(INFO,"Client configured with SSL");
                System.setProperty("javax.net.ssl.trustStore", properties.getProperty("TruststorePath"));
                System.setProperty("javax.net.ssl.trustStorePassword",properties.getProperty("TruststorePassword"));
                socket = SSLSocketFactory.getDefault().createSocket(serverAddress.getAddress(),1337);                
                ((SSLSocket)socket).startHandshake();                
                out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream())); 
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            } 
            catch (IOException ex) 
            {
                log.log(DEBUG,""+ex);    
            }
        }    
        
        try 
        {
            log.log(DEBUG, "Sending message");
            message += "\r\n\r\n";
            out.writeBytes(message);
            out.flush();
            String returnString = in.readLine();
            out.close();
            log.log(DEBUG, "Done sending message");
            return returnString;
        } 
        catch (IOException ex) 
        {
            log.log(DEBUG, ""+ex);
            return null;
        }
    }    
    
    /**
     * helper method that removes the trailing blank line from the client-server implementation
     * @param request the request
     * @return the request without the trailing blank line
     */
    private String removeTrailingBlankLine(String request) 
    {
        String[] lines = request.split("\r\n");       
        String returnValue  = "";
        for(int i = 0 ; i < lines.length ; i++)
        {
            if(i == lines.length-1)
            {
                returnValue += lines[i];
            }
            else
            {
                returnValue += lines[i] + "\r\n";
            }
        }    
        return returnValue;
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
            log.log(DEBUG,""+ex);
        }
        return null;
    }
    
    /**
     * Base64 decodes the input message (Unicode UFT-8)
     * @param message the message to decode
     * @return the decoded message as a string
     */
    public String decode(String message)
    {        
        if(message != null)
        {
            try 
            {
                byte[] messageBytes = message.getBytes("UTF-8");
                Decoder decoder = Base64.getDecoder();
                byte[] decodedBytes = decoder.decode(messageBytes);
                String decoded = new String(decodedBytes);
                //String decoded = new String(Base64.getDecoder().decode(message.getBytes("UTF-8")));
                return decoded;
            } 
            catch (UnsupportedEncodingException ex) 
            {
                log.log(DEBUG,""+ex);
            }
        }
        else
        {
            return null;
        }
        return null;
    }

}
