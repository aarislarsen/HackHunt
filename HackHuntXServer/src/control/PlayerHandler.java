/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

//TODO add encoding to each field of the responses returned

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constants;
import model.SessionGenerator;
import util.InputValidator;

/**
 *
 * @author Dragon
 */
public class PlayerHandler 
{
    private final InputValidator inputValidator = new InputValidator();
    private final String logname = "HackHunt Log";
    private final Logger log = Logger.getLogger(logname);
    private Level INFO = Level.INFO;
    private Level DEBUG = Level.FINE;
    private Level WARNING = Level.WARNING;
    private Timer timer;
    private final String propertiesURL = "properties.properties";
    private Properties properties = new Properties();
    private SessionGenerator sessionGenerator = new SessionGenerator();

    public PlayerHandler()
    {
        initProperties();
        configureLogLevels();
    }
    
    /**
     * adds a player to the register
     * @param username username
     * @param password password
     * @param teamName name of your team
     * @return SUCCES if the user was successfully added, ERROR if a user with the same handle already exists, or input was wrongly formatted
     */
    public synchronized String addUser(String username, String password, String teamName)
    {
        //input validation
        if(!inputValidator.isValidInput(Constants.ALLOWEDINUSERNAME, username))
        {
            log.log(DEBUG,"Invalid character in username");
            return "ERROR: Invalid characters in username, please try again";
        }
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTEAMNAME, teamName))
        {
            log.log(DEBUG,"Invalid character in teamname");
            return "ERROR: Invalid characters in teamname, please try again";
        }        
        if(!inputValidator.isValidInput(Constants.ALLOWEDINPASSWORD, password))
        {
            log.log(DEBUG,"Invalid character in password");
            return "ERROR: Invalid characters in password, please try again";
        }        

        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        ArrayList foundPlayers = sql.getUser(username);
        
        if(foundPlayers.size() > 0)
        {
            log.log(DEBUG,"User already exists");
            return "ERROR: A user with that name already exists";
        }
        else
        {
            if(sql.insertIntoUsers(username, password, teamName))
            {
                if(sql.insertIntoScore(username))
                {
                    if(sql.getTeamNamesFromServicesBoard().contains(teamName))
                    {
                        log.log(DEBUG,"User " + username + " added to existing team");
                        return "SUCCESS: User " + username + " added to existing team " + teamName;
                    }
                    else
                    {
                        if(sql.insertIntoServices(teamName))
                        {
                            log.log(DEBUG,"User " + username + " added");
                            return "SUCCESS: User " + username + " added";
                        }
                        else
                        {
                            log.log(DEBUG,"User " + username + " was added and added to scoreboard, but not to the services board");
                            return "ERROR: User " + username + " was added and added to scoreboard, but not to the services board";
                        }
                        
                    }
                }
                else
                {
                    log.log(DEBUG,"User " + username + " was added, but was not added to the scoreboard");
                    return "ERROR: User " + username + " was added, but was not added to the scoreboard. Please contact the administrator.";
                }                
            }
            else
            {
                log.log(DEBUG,"User " + username + " was not added");
                return "ERROR: User " + username + " was not added";
            }
        }
    }   
    
    /**
     * deletes a user
     * @param username the username of the user to be deleted
     * @param password the password of the user to be deleted
     * @return SUCCESS if the player was deleted, ERROR if it failed
     */
    public synchronized String deleteUser(String username, String password)
    {
        log.log(DEBUG,"Deleting user");
        //input validation
        if(!inputValidator.isValidInput(Constants.ALLOWEDINUSERNAME, username))
        {
            log.log(DEBUG,"Invalid character in username");
            return "ERROR: Invalid characters in username, please try again";
        }     
        if(!inputValidator.isValidInput(Constants.ALLOWEDINPASSWORD, password))
        {
            log.log(DEBUG,"Invalid character in password");
            return "ERROR: Invalid characters in password, please try again";
        } 
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        boolean completed = sql.removeFromUsers(username,password);
        if(completed)
        {
            if(!sql.removeFromScore(username))
            {
                return "ERROR: User could not be deleted from scoreboard";
            }
            if(sql.getUser(username).size() > 0)
            {
                
                return "ERROR: User could not be deleted";
            }
            if(!sql.removeFromAnswers(username))
            {
                return "ERROR: User could not be deleted from answers";
            }
            if(!sql.removeFromVulnerabilities(username))
            {
                return "ERROR: User could not be deleted from vulnerabilities";
            }
        }
        sql.removeFromActivePlayersByUsername(username);
        return "SUCCESS: User " + username + " was deleted";
    }
    
    public synchronized String updateUser(String old_username, String new_username, String password, String teamname)
    {
        log.log(DEBUG,"Deleting user");
        //input validation
        if(!inputValidator.isValidInput(Constants.ALLOWEDINUSERNAME, new_username))
        {
            log.log(DEBUG,"Invalid character in new username");
            return "ERROR: Invalid characters in new username, please try again";
        }     
        if(!inputValidator.isValidInput(Constants.ALLOWEDINPASSWORD, password))
        {
            log.log(DEBUG,"Invalid character in password");
            return "ERROR: Invalid characters in password, please try again";
        } 
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTEAMNAME, teamname))
        {
            log.log(DEBUG,"Invalid character in new teamname");
            return "ERROR: Invalid characters in teamname, please try again";
        } 
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        boolean completed = sql.updateUser(old_username, new_username, teamname, password);
        if(completed)
        {
            return "SUCCESS: User " + old_username + " was updated";
        }
        return "ERROR: User could not be updated";
    }
    
    /**
     * Adds a user to the Active Players database and gets an authentication token
     * @param username
     * @param password
     * @return SUCCESS if the user was logged in, ERROR if not
     */
    public synchronized String loginUser(String username, String password)
    {
        log.log(DEBUG,"Logging in user");
        //input validation
        if(!inputValidator.isValidInput(Constants.ALLOWEDINUSERNAME, username))
        {
            log.log(DEBUG,"Invalid character in username");
            return "ERROR: Invalid characters in username, please try again";
        }        
        if(!inputValidator.isValidInput(Constants.ALLOWEDINPASSWORD, password))
        {
            log.log(DEBUG,"Invalid character in password");
            return "ERROR: Invalid characters in password, please try again";
        }        
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        String token = sessionGenerator.getToken();
        ArrayList<ArrayList> foundTokens = sql.getActivePlayerByToken(token);
        boolean tokenInUse = false;
        for(ArrayList a : foundTokens)
        {
            if(a.get(1).toString().trim().equalsIgnoreCase(token))
            {                    
                log.log(DEBUG,"Token collision detected");
                tokenInUse = true;
                break;
            }
            tokenInUse = false;
        }
        if(tokenInUse)
        {
            log.log(DEBUG,"Token collision, restarting login process");
            loginUser(username,password);
        }
        ArrayList<ArrayList> foundPlayers = sql.getActivePlayerByUsername(username);
        boolean alreadyLoggedIn = false;
        for(ArrayList a : foundPlayers)
        {
            sql.removeFromActivePlayersByUsername(username);
            alreadyLoggedIn = true;
        }
        if(alreadyLoggedIn)
        {
            log.log(DEBUG,"User already logged in, resetting session and restarting login process");
            loginUser(username, password);
        }
        ArrayList<ArrayList> user = sql.getUser(username);
        for(ArrayList a : user)
        {
            if(a.get(0).toString().trim().equalsIgnoreCase(username) && a.get(1).toString().trim().equalsIgnoreCase(password))
            {
                sql.insertIntoActivePlayers(username, token, ""+System.currentTimeMillis());
                return "SUCCESS: " + token + ";" + "User " + username + " logged in";        
            }
        }
        return "ERROR: User not found";
    }
    
    /**
     * removes a user from the active players database
     * @param token
     * @return SUCCES if user is removed, ERROR otherwise
     */
    public synchronized String logoutUser(String token)
    {
        log.log(DEBUG,"Logging out user");
        //input validation
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        } 
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            if(sql.removeFromActivePlayersByToken(token))
            {
                log.log(DEBUG,"User was logged out");
                return "SUCCESS: User was logged out";
            }
            else
            {
                log.log(DEBUG,"User was not logged out");
                return "ERROR: User was not logged out";
            }
        }
        else
        {
            return "ERROR: User is not logged in";
        }
    }
    
    /**
     * gets the active players from the database
     * @param token the users authentication token
     * @return a string representation of all players and their teamname
     */
    public synchronized String getActivePlayers(String token)
    {
        log.log(DEBUG,"Retrieving active players");
        //input validation
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        }  
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            HashMap<String, String> players = sql.getActivePlayers();
            String returnString = "";
            if(players != null)
            {
                for(Object k : players.keySet())
                {
                    returnString += (String)k +","+players.get(k)+";";
                }
                return "SUCCESS: " + returnString;
            }
            else
            {
                return "ERROR: Active players could not be retrieved";
            }
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
    }
    
    public synchronized String getScore(String token)
    {
        log.log(DEBUG,"Retrieving score");
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        } 
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();            
            HashMap<String, String> score = sql.getScoreByToken(token);
            String returnString = "";
            if(score != null)
            {
                returnString = score.get("username")+","+score.get("prestige")+","+score.get("credit");                
                return "SUCCESS: " + returnString;
            }
            else
            {
                return "ERROR: Score could not be retrieved";
            }
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
        
    }
    
    /**
     * gets the teams sum credits
     * @param token
     * @param teamname
     * @param creditsOrPrestige if true, will ask for credits. If false, will ask for prestige
     * @return the teams sum credits
     */
    public synchronized String getTeamScore(String token, String teamname, boolean creditsOrPrestige)
    {
        log.log(DEBUG,"Retrieving score");
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        } 
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTEAMNAME, teamname))
        {
            log.log(DEBUG,"Invalid character in teamname");
            return "ERROR: Invalid characters in teamname, please try again";
        }        
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();            
            //check if player is on the team
            String username = sql.getActivePlayerByToken(token).get(0).get(0).toString();
            String actualTeam = sql.getTeamWithThisMember(username);
            if(teamname.equals(actualTeam))
            {
                int score = 0;
                if(creditsOrPrestige)
                {                
                    score = sql.getTeamCredit(teamname);
                }     
                else
                {
                    score = sql.getTeamPrestige(teamname);
                }
                return "SUCCESS: " + score;    
            }
            else
            {
                return "ERROR: You're not part of that team, and therefore can't get their score";
            }
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
        
    }

    /**
     * Gets the status of all services for all teams
     * @param token
     * @return a string representation of all teams services, seperated by ;
     */
    public synchronized String getServicesForAll(String token)
    {
        String returnString = "SUCCESS: ";
        log.log(DEBUG,"Retrieving services");
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        }
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            ArrayList<HashMap<String,String>> result = sql.getServicesForAll();
            for(HashMap<String,String> h : result)
            {
                returnString += h.get("teamname")+","+h.get("fileservice")+","+h.get("communicationservice")+","+h.get("cryptoservice")+","+h.get("webservice")+","+h.get("storageservice")+";";
            }
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
        return returnString;
    }
    
    /**
     * gets all the announcements, sorted by timestamp
     * @param token
     * @return a string representation of all announcements
     */
    public synchronized String getAnnouncements(String token)
    {
        String returnString = "SUCCESS: ";
        log.log(DEBUG,"Retrieving announcements");
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        }
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            ArrayList<HashMap<String,String>> result = sql.getAnnouncements();
            for(HashMap<String,String> h : result)
            {
                returnString += h.get("timestamp")+","+h.get("announcement")+";";
            }
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
        return returnString;
    }
    
    /**
     * checks to see if at least one token is present in the active players database
     * @param token
     * @return true if the token was found, false otherwise
     */
    private synchronized boolean isPlayerAuthenticated(String token)
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        ArrayList<ArrayList> foundTokens = sql.getActivePlayerByToken(token);
        boolean tokenInUse = false;
        for(ArrayList a : foundTokens)
        {
            if(a.get(1).toString().trim().equalsIgnoreCase(token))
            {                    
                log.log(DEBUG,"Users token found");
                tokenInUse = true;
                break;
            }
        }
        return tokenInUse;
    }
    
    /**
     * Gets the team rankings
     * @param token
     * @return comma separated list of scores
     */
    public synchronized String getTeamRankings(String token)
    {
        String returnString = "SUCCESS: ";
        log.log(DEBUG,"Retrieving team rankings");
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        }
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            ArrayList<String> teams = sql.getTeams();
            ArrayList<String> ranking = new ArrayList();
            for(String s : teams)
            {
                ranking.add(sql.getTeamPrestige(s)+","+s);
            }
            ranking.sort(new Comparator() 
            {
                @Override
                public int compare(Object o1, Object o2) 
                {
                    if(Integer.parseInt(o1.toString().split(",")[0]) < Integer.parseInt(o2.toString().split(",")[0]))
                    {
                        return -1;
                    }
                    if(Integer.parseInt(o1.toString().split(",")[0]) > Integer.parseInt(o2.toString().split(",")[0]))
                    {
                        return 1;
                    }
                    return 0;
                }
            });
            Collections.reverse(ranking);
            for(String s : ranking)
            {
                returnString += s+";";
            }
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
        return returnString;
    }
        
    /**
     * terminates all current active sessions (removes the tokens)
     * @return true of successful, false if not
     */
    public synchronized boolean terminateAllSessions()
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        return sql.terminateAllSessions();
    }
    
    /**
     * removes all answers (for restarting the game)
     * @return true of successful, false if not
     */
    public synchronized boolean removeAllAnswers()
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        return sql.removeAllAnswers();
    }
    
    /**
     * deletes all players (for restarting the game)
     * @return true of successful, false if not
     */
    public synchronized boolean deleteAllPlayers()
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        boolean returnValue = false;
        returnValue = sql.deleteAllScores();
        returnValue = sql.deleteAllPlayers();
        return returnValue;
        
    }
    
    /**
     * resets all scores <8credit and prestige set to 0)
     * @return true of successful, false if not
     */
    public synchronized boolean resetAllScores()
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        return sql.resetAllScores();
    }
    
    /**
     * Deletes all scores (all entries are removed)
     * @return true of successful, false if not
     */
    public synchronized boolean deleteAllFromScores()
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        return sql.deleteAllScores();
    }
    
    /**
     * removes all announcements (for restarting the game)
     * @return true of successful, false if not
     */
    public synchronized boolean removeAllAnnouncements()
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        return sql.removeAllAnnouncements();
    }
    
    /**
     * gets the team of the user with the given token
     * @param token
     * @return the name of the users team
     */
    public synchronized String getTeamWithThisMember(String token)
    {
        String returnValue = "SUCCESS: ";
        log.log(DEBUG,"Retrieving teamname");
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        }
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            returnValue += sql.getTeamWithThisMember((String) sql.getActivePlayerByToken(token).get(0).get(0));
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
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
     * removes all services (for restarting the game)
     * @return true of successful, false if not
     */
    public synchronized boolean removeAllServices()
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        return sql.removeAllServices();
    }
    
    /**
     * removes all vulnerabilities (for restarting the game)
     * @return true of successful, false if not
     */
    public synchronized boolean removeAllVulnerabilities()
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        return sql.removeAllVulnerabilities();
    }
    
    /**
     * Re-enables all services (for restarting the game)
     * @return true of successful, false if not
     */
    public synchronized boolean resetAllServices()
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        return sql.resetAllServices();
    }
    
    /**
     * Gets the vulnerabilities affecting any player in the team
     * @param token
     * @return comma separated list of exploitIDs affecting the team
     */
    public synchronized String getUnpatchedVulns(String token, String teamname)
    {
        String returnString = "SUCCESS: ";
        log.log(DEBUG,"Retrieving exploits affecting this team. If one user is unpatched, the whole team is vulnerable");
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        }
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTEAMNAME, token))
        {
            log.log(DEBUG,"Invalid character in teamname");
            return "ERROR: Invalid characters in teamname, please try again";
        }
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            returnString += sql.getUnpatchedVulns(teamname);            
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
        return returnString;
    }    
    
    /**
     * Gets the vulnerabilities affecting any player in the team for the specified service
     * @param token
     * £param service
     * @return comma separated list of exploits for that service
     */
    public synchronized String getUnpatchedVulnsForService(String token, String teamname, String service)
    {
        String returnString = "SUCCESS: ";
        log.log(DEBUG,"Retrieving exploits affecting this teams "+service+". If one user is unpatched, the whole team is vulnerable");
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTOKEN, token))
        {
            log.log(DEBUG,"Invalid character in token");
            return "ERROR: Invalid characters in token, please try again";
        }
        if(!inputValidator.isValidInput(Constants.ALLOWEDINTEAMNAME, teamname))
        {
            log.log(DEBUG,"Invalid character in teamname");
            return "ERROR: Invalid characters in teamname, please try again";
        }
        if(!inputValidator.isValidInput(Constants.ALLOWEDINSTRINGS, service))
        {
            log.log(DEBUG,"Invalid character in servicename");
            return "ERROR: Invalid characters in servicename, please try again";
        }
        if(isPlayerAuthenticated(token))
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            returnString += sql.getUnpatchedVulnsForService(teamname,service);            
        }
        else
        {
            return "ERROR: You must be logged in to perform this action";
        }
        return returnString;
    }    
    
    /**
     * unauthenticates the given user and disconnects them from the game. This command is only accessible from the server and is not processed when coming from clients
     * @param username the user to disconnect.
     * @return SUCCESS if it works, ERROR if not
     */
    public synchronized String terminateUsersSession(String username)
    {
        String returnString = "SUCCESS: ";
        log.log(DEBUG,"Terminating " + username +"'s session");
        if(!inputValidator.isValidInput(Constants.ALLOWEDINUSERNAME, username))
        {
            log.log(DEBUG,"Invalid character in username");
            return "ERROR: Invalid characters in username, please try again";
        }
        else
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            if(sql.removeFromActivePlayersByUsername(username))
            {
                return "SUCCESS: "+username+" was removed";
            }
            else
            {
                return "ERROR: User was not removed";
            }
            
        }
    }
}
