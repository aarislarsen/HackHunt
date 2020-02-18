/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * Class for generic TCP server services using NAT.
 * Please note that this implementation IS VULNERABLE to depletion attacks. It is up to the application logic to monitor if clients are idling too long, and disconnect them if necessary
 * The socket uses \r\n.\r\n as a terminator
 * @author Dragon
 */
public class Server implements Runnable
{
    private ServerSocket serverSocket;
    private int port;
    private InetSocketAddress serverAddress;
    private String serverIP;
    private Socket clientSocket;
    private int socketBufferSize = 1024;
    private boolean shutdownServer = false;
    private boolean startServer = false;
    private boolean useSSL = false;
    private ProtocolInterface protocol;
    private String logname = "HackHunt Log";
    private Logger log = Logger.getLogger(logname);
    private Level INFO = Level.INFO;
    private Level DEBUG = Level.FINE;
    private Level WARNING = Level.WARNING; 
    private static Server uniktServer;
    private String propertiesURL = "properties.properties";
    private Properties properties = new Properties();
    
    /**
     * gets the singleton Server object
     * @param protocol
     * @param port
     * @param serverIP
     * @return 
     */
    public static Server getInstance(ProtocolInterface protocol, int port, String serverIP)
    {
        if(uniktServer == null)
        {
            uniktServer = new Server(protocol,port,serverIP);
        }
        return uniktServer;
    }
    
    
    /**
     * constructor
     * @param protocol an implementation of the ProtocolInterface, which dictates how the requests/responses are handled
     * @param port the port to run the server on
     * @param serverIP the IP of the local interface you want to bind the server to
     */
    private Server(ProtocolInterface protocol, int port, String serverIP)
    {
        initProperties();
        configureLogLevels();
        useSSL = Boolean.parseBoolean(properties.getProperty("UseSSL"));
        this.serverIP = serverIP;
        this.port = port;
        this.protocol = protocol;
        try 
        {
            if(!useSSL)
            {
                log.log(INFO,"Server configured with plaintext");
                serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
                serverAddress = new InetSocketAddress(InetAddress.getByName(serverIP),port);
                serverSocket.bind(serverAddress,0);            
            }
            else
            {
                log.log(INFO,"Server configured with SSL");
                System.setProperty("javax.net.ssl.keyStore", properties.getProperty("KeystorePath"));
                System.setProperty("javax.net.ssl.keyStorePassword", properties.getProperty("KeystorePassword"));                                
                serverSocket = SSLServerSocketFactory.getDefault().createServerSocket(port);
                serverSocket.setReuseAddress(true);                
            }            
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Listen for connections, sets up the socket to the client and hands off communication to the inner class "ServerCommunication"
     */    
    @Override
    public void run() 
    {
        ExecutorService executor = Executors.newCachedThreadPool();
        while(true)
        {
            try 
            {
                if(shutdownServer)
                {
                    serverSocket.close();
                }
                else if(startServer)
                {
                    clientSocket = serverSocket.accept();
                    clientSocket.setKeepAlive(false);
                    clientSocket.setTcpNoDelay(true);
                    clientSocket.setReuseAddress(true);
                    clientSocket.setSendBufferSize(socketBufferSize);
                    clientSocket.setReceiveBufferSize(socketBufferSize);
                    log.log(DEBUG,"Client connected: " + clientSocket.getRemoteSocketAddress());
                    //Now pass it on to a thread that handles the communication, listens for input and gives output
                    ServerCommunication serverCom = new ServerCommunication(clientSocket);
                    executor.execute(serverCom);
                    //Thread comThread = new Thread(serverCom);
                    //comThread.start();
                }
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * shuts down the server
     */
    public void serverShutdown()
    {
        log.log(INFO,"Shutting down server");
        shutdownServer = true;    
    }
       
    /**
     * starts the server
     */
    public void startServer()
    {
        log.log(INFO,"Starting server");
        startServer = true;
    }
    
    /**
     * Private inner class that handles the communication between the server and client sockets. How it's actually being handled is determined by the Protocol used by the parent class
     */
    private class ServerCommunication implements Runnable
    {
        private Socket clientSocket;
        
        public ServerCommunication(Socket clientSocket)
        {
            this.clientSocket = clientSocket;
        }        

        @Override
        public void run() 
        {
            String client = clientSocket.getRemoteSocketAddress().toString(); 
            String input = "";
            String line = "";
            while(true)
            {
                try 
                {                    
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));                    
                    PrintStream out = new PrintStream(clientSocket.getOutputStream());
                    while((line = in.readLine()) != null && line.length() > 0)
                    {
                        input = input + line+"\r\n";
                    }
                    input = removeTrailingBlankLine(input);
                    if(input.length() < 1)
                    {
                        out.close();
                        in.close();
                        log.log(DEBUG,"Socket has been closed");
                        break;
                    }                                        
                    String response = protocol.handleRequest(input); //TODO decode input from base64, encode respone with base64
                    if(response != null)
                    {
                        out.print(response);
                        out.flush();
                        out.close();
                        in.close();
                    }                        

                    log.log(DEBUG,"Request/response received from client ("+client + "): \t: " + input);
                    log.log(DEBUG,"Server protocol response: " + response);
                    input = "";
                    line = "";
                } 
                catch (IOException ex) 
                {
                    log.log(DEBUG,"Disconnected " + client + " from server");
                    break;
                }
            }
        }
        
    }
       
    /**
     * loads the properties from the file
     */
    private void initProperties()
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
    private void configureLogLevels()
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
     * helper method that removes the trailing blank line from the client-server implementation
     * @param request the request
     * @return the request without the trailing blank line
     */
    public String removeTrailingBlankLine(String request) 
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
    
    public static void main(String[] args)
    {
        Server server = new Server(null,1337,"192.168.0.20");
        Thread serverThread = new Thread(server);
        server.startServer();
        serverThread.start();        
    }
}
