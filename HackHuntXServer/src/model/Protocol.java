/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


import control.AdvisoryHandler;
import control.ExploitHandler;
import control.PlayerHandler;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for interpreting the input received from the clients, and generating and returning the responses
 * @author Dragon
 */
public class Protocol implements ProtocolInterface
{
    private final String logname = "HackHunt Log";
    private final Logger log = Logger.getLogger(logname);
    private Level INFO = Level.INFO;
    private Level DEBUG = Level.FINE; 
    private Level WARNING = Level.WARNING; 
    private final String propertiesURL = "properties.properties";
    private final Properties properties = new Properties();    
    private PlayerHandler playerHandler = new PlayerHandler();
    private AdvisoryHandler advisoryHandler = new AdvisoryHandler();
    private ExploitHandler exploitHandler = new ExploitHandler();
    
    /**
     * default constructor
     */
    public Protocol()
    {
        initProperties();
        configureLogLevels();
    }
    
    /**
     * @param request the string received from the client
     * @return String protocol responses, or null if something goes terribly wrong
     */
    @Override
    public String handleRequest(String request) 
    {
        request = removeTrailingBlankLine(request);
        String[] requestParameters = request.split(";");
        if( requestParameters.length < 2)
        {            
            return "ProtocolError";            
        }
        else
        {
            String command = requestParameters[0].trim(); //TODO add decode to each field
            String token = requestParameters[1].trim(); //TODO add decode, and add to all commands that use more than these parameters            

            switch(command)
            {   
                //cases that can be handled with and without authentication
                case "addUser":     
                    // <editor-fold defaultstate="collapsed" desc="addUser">
                    if(requestParameters.length != 5)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String username = requestParameters[2].trim();
                        String password = requestParameters[3].trim();
                        String teamname = requestParameters[4].trim();
                        return playerHandler.addUser(username, password, teamname); //TODO add encode
                    }
                    // </editor-fold>
                case "login":       
                    // <editor-fold defaultstate="collapsed" desc="login">
                    if(requestParameters.length != 4)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String username = requestParameters[2].trim();
                        String password = requestParameters[3].trim();
                        return playerHandler.loginUser(username, password); 
                    }
                    // </editor-fold>
                //cases that require authentication
                case "deleteUser":  
                    // <editor-fold defaultstate="collapsed" desc="deleteUser">
                    if(requestParameters.length != 4)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String username = requestParameters[2].trim();
                        String password = requestParameters[3].trim();
                        return playerHandler.deleteUser(username, password); 
                    }
                    // </editor-fold>
                case "logout":      
                    // <editor-fold defaultstate="collapsed" desc="logout">
                    if(requestParameters.length != 2)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        return playerHandler.logoutUser(token);
                    }
                    // </editor-fold>
                case "getActivePlayers": 
                    // <editor-fold defaultstate="collapsed" desc="getActivePlayers">
                    if(requestParameters.length != 2)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        return removeTrailingBlankLine(playerHandler.getActivePlayers(token));
                    }
                    // </editor-fold>
                case "getScore": 
                    // <editor-fold defaultstate="collapsed" desc="getScore">
                    if(requestParameters.length != 2)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        return removeTrailingBlankLine(playerHandler.getScore(token));
                    }
                    // </editor-fold>
                case "getTeamCredits": 
                    // <editor-fold defaultstate="collapsed" desc="getTeamCredits">
                    if(requestParameters.length != 3)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String teamname = requestParameters[2].trim();
                        return removeTrailingBlankLine(playerHandler.getTeamScore(token, teamname, true));
                    }
                    // </editor-fold>
                case "getTeamPrestige": 
                    // <editor-fold defaultstate="collapsed" desc="getTeamPrestige">
                    if(requestParameters.length != 3)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String teamname = requestParameters[2].trim();
                        return removeTrailingBlankLine(playerHandler.getTeamScore(token, teamname, false));
                    }
                    // </editor-fold>
                case "getServices":        
                    // <editor-fold defaultstate="collapsed" desc="getServices">
                    if(requestParameters.length != 2)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        return removeTrailingBlankLine(playerHandler.getServicesForAll(token));
                    }
                    // </editor-fold>
                case "getAnnouncements":    
                    // <editor-fold defaultstate="collapsed" desc="getAnnouncements">
                    if(requestParameters.length != 2)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        return removeTrailingBlankLine(playerHandler.getAnnouncements(token));
                    }
                    // </editor-fold>
                case "getReleasedAdvisories": 
                    // <editor-fold defaultstate="collapsed" desc="getReleasedAdvisories">
                    if(requestParameters.length != 2)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        return removeTrailingBlankLine(advisoryHandler.getReleasedAdvisories(token));
                    }
                    // </editor-fold>
                case "getAnsweredAdvisories":
                    // <editor-fold defaultstate="collapsed" desc="getAnsweredAdvisories">
                    if(requestParameters.length != 2)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        return removeTrailingBlankLine(advisoryHandler.getAnsweredAdvisories(token));
                    }
                // </editor-fold>
                case "answerAdvisory":        
                    // <editor-fold defaultstate="collapsed" desc="answerAdvisory">
                    if(requestParameters.length != 4)
                    {
                        if(requestParameters[2].trim() == null)
                        {
                            return "ERROR: invalid request, advisory ID must be a number";
                        }
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String advisoryID = requestParameters[2].trim();
                        String answer = requestParameters[3].trim();
                        return removeTrailingBlankLine(advisoryHandler.answerAdvisory(token,advisoryID,answer));
                    }
                    // </editor-fold>
                case "getExploitStatus":        
                    // <editor-fold defaultstate="collapsed" desc="getExploitStatus">
                    if(requestParameters.length != 2)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        return removeTrailingBlankLine(exploitHandler.getExploitStatus(token));
                    }
                    // </editor-fold>
                case "generateExploit":        
                    // <editor-fold defaultstate="collapsed" desc="generateExploit">                    
                    if(requestParameters.length != 3)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String vulnerability = requestParameters[2].trim();
                        return removeTrailingBlankLine(exploitHandler.generateExploit(token,vulnerability));
                    }
                    // </editor-fold>    
                case "generatePatch":        
                    // <editor-fold defaultstate="collapsed" desc="generatePatch">                    
                    if(requestParameters.length != 3)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String vulnerability = requestParameters[2].trim();
                        return removeTrailingBlankLine(exploitHandler.generatePatch(token,vulnerability));
                    }
                    // </editor-fold>        
                case "getVulnerableTargets":        
                    // <editor-fold defaultstate="collapsed" desc="getVulnerableTargets">                    
                    if(requestParameters.length != 3)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String vulnerability = requestParameters[2].trim();
                        return removeTrailingBlankLine(exploitHandler.getVulnerableTargets(token,vulnerability));
                    }
                    // </editor-fold>   
                case "getVulnerableTargetsInTeam":        
                    // <editor-fold defaultstate="collapsed" desc="getVulnerableTargetsInTeam">                    
                    if(requestParameters.length != 4)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String vulnerability = requestParameters[2].trim();
                        String teamname = requestParameters[3].trim();
                        return removeTrailingBlankLine(exploitHandler.getVulnerableTargetsInTeam(token,vulnerability,teamname));
                    }
                    // </editor-fold>    
                case "attackTarget":        
                    // <editor-fold defaultstate="collapsed" desc="attackTarget">                    
                    if(requestParameters.length != 4)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String vulnerability = requestParameters[2].trim();
                        String username = requestParameters[3].trim();
                        return removeTrailingBlankLine(exploitHandler.attackTarget(token, vulnerability, username));
                    }
                    // </editor-fold>  
                case "getUnansweredAdvisories":        
                    // <editor-fold defaultstate="collapsed" desc="getUnansweredAdvisories">                    
                    if(requestParameters.length != 2)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        return removeTrailingBlankLine(advisoryHandler.getUnansweredAdvisories(token));
                    }
                    // </editor-fold> 
                case "getTeamRankings":        
                    // <editor-fold defaultstate="collapsed" desc="getTeamRankings">                    
                    if(requestParameters.length != 2)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        return removeTrailingBlankLine(playerHandler.getTeamRankings(token));
                    }
                    // </editor-fold> 
                case "getSecondHint":        
                    // <editor-fold defaultstate="collapsed" desc="getSecondHint">                    
                    if(requestParameters.length != 3)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String advisoryID = requestParameters[2];
                        return removeTrailingBlankLine(advisoryHandler.getSecondHint(token,advisoryID));
                    }
                    // </editor-fold> 
                case "getTeamname":        
                    // <editor-fold defaultstate="collapsed" desc="getTeamname">                    
                    if(requestParameters.length != 2)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        return removeTrailingBlankLine(playerHandler.getTeamWithThisMember(token));
                    }
                    // </editor-fold> 
                case "getUnpatchedVulns":        
                    // <editor-fold defaultstate="collapsed" desc="getUnpatchedVulns">                    
                    if(requestParameters.length != 3)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String teamname = requestParameters[2];
                        return removeTrailingBlankLine(playerHandler.getUnpatchedVulns(token,teamname));
                    }
                    // </editor-fold>     
                case "getUnpatchedVulnsForService":        
                    // <editor-fold defaultstate="collapsed" desc="getUnpatchedVulnsForService">                    
                    if(requestParameters.length != 4)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String teamname = requestParameters[2];
                        String service = requestParameters[3];
                        return removeTrailingBlankLine(playerHandler.getUnpatchedVulnsForService(token,teamname,service));
                    }
                    // </editor-fold> 
                case "isExploitAvailable":        
                    // <editor-fold defaultstate="collapsed" desc="isExploitAvailable">                    
                    if(requestParameters.length != 4)
                    {
                        return "ERROR: invalid request";
                    }
                    else
                    {
                        String username = requestParameters[2];
                        String vulnerability = requestParameters[3];
                        return removeTrailingBlankLine(exploitHandler.isExploitAvailable(token, username,vulnerability));
                    }
                    // </editor-fold> 
                case "exit":
                    // <editor-fold defaultstate="collapsed" desc="exit">  

                // </editor-fold>                
                default:            
                    return "unknown command";
            }            
        }
    }

    @Override
    public String removeTrailingBlankLine(String request) 
    {
        String returnValue  = "";
        if(request != null)
        {
            String[] lines = request.split("\r\n");       
            
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
        }
        return returnValue;
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
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex +"\r\n" + message);
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
        if(message != null && message.getBytes().length > 2)
        {
            try 
            {
                String decoded = new String(Base64.getDecoder().decode(message.getBytes("UTF-8")));
                return decoded;
            } 
            catch (UnsupportedEncodingException | IllegalArgumentException ex)
            {                                
                return null;
            }
            catch (Exception ex) //In case it's invalid base64
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
}
