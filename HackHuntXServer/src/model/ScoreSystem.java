/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import control.SQLiteConnectionHandler;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.InputValidator;

/**
 *
 * @author Dragon
 */
public class ScoreSystem 
{
    private final InputValidator inputValidator = new InputValidator();
    private final String logname = "HackHunt Log";
    private final Logger log = Logger.getLogger(logname);
    private Level INFO = Level.INFO;
    private Level DEBUG = Level.FINE;
    private Level WARNING = Level.WARNING;
    private final String propertiesURL = "properties.properties";
    private Properties properties = new Properties();
    
    /**
     * default constructor
     */
    public ScoreSystem()
    {
        initProperties();
        configureLogLevels();
    }
    
    public synchronized void addPrestige(String difficulty, String username)
    {        
        int prestige = 0;
        switch (difficulty)
        {
            case "Easy": prestige =  Integer.parseInt(properties.getProperty("easyPrestigePoints"));
                            break;
            case "Medium": prestige =  Integer.parseInt(properties.getProperty("mediumPrestigePoints"));
                            break;
            case "Hard": prestige =  Integer.parseInt(properties.getProperty("hardPrestigePoints"));
                            break;
            case "Improbable": prestige =  Integer.parseInt(properties.getProperty("improbablePrestigePoints"));
                            break;
            case "Impossible": prestige =  Integer.parseInt(properties.getProperty("impossiblePrestigePoints"));
                            break;                            
        }
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();        
        int currentPrestige = sql.getPrestige(username);
        boolean result = sql.setPrestige(prestige+currentPrestige, username);
    }
    
    public synchronized void addCredit(String username)
    {
        int credit = 0;
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        int currentCredit = sql.getCredit(username);
        boolean result = sql.setCredit(Integer.parseInt(properties.getProperty("creditPoints"))+currentCredit, username);
    }
    
    public synchronized void setCredit(int credit, String username)
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        sql.setCredit(credit, username);
    }
    
    public synchronized void setPrestige(int prestige, String username)
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        sql.setPrestige(prestige, username);
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
}
