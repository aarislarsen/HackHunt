/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import control.SQLiteConnectionHandler;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for releasing advisories on a given interval
 * @author Dragon
 */
public class TimedAdvisoryReleaser extends TimerTask
{
    private final String logname = "HackHunt Log";
    private final Logger log = Logger.getLogger(logname);
    private Level INFO = Level.INFO;
    private Level DEBUG = Level.FINE; 
    private Level WARNING = Level.WARNING; 
    private final String propertiesURL = "properties.properties";
    private final Properties properties = new Properties();   
    private boolean sendAdvisories = false;
    private static TimedAdvisoryReleaser uniqueInstance;
    
    /**
     * get the singleton TimedAdvisoryReleaser();
     * @return 
     */
    public static TimedAdvisoryReleaser getInstance()
    {
        if(uniqueInstance == null)
        {
            uniqueInstance = new TimedAdvisoryReleaser();
        }
        return uniqueInstance;
    }
    
    
    /**
     * default constructor
     */
    private TimedAdvisoryReleaser()
    {
        initProperties();
        configureLogLevels();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(this, 0, (Integer.parseInt(properties.getProperty("advisoryReleaseTime")))*1000);
    }
    
    /**
     * sets the bit controlling if advisories should be relased
     */
    public void startSendingAdvisories()
    {
        sendAdvisories = true;
    }
    
    /**
     * sets the bit controlling if advisories should be relased
     */
    public void stopSendingAdvisories()
    {
        sendAdvisories = false;
    }

    /**
     * marks all advisories as un-released
     */
    public void resetAdvisories()
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        sql.unReleaseAllAdvisories();
        sql.unAnswerAllAdvisories();
    }
    
    @Override
    public void run() 
    {
        if(sendAdvisories)
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            ArrayList<Advisory> advisories = sql.getReleasedAdvisories();
            int currentID = 0;
            if(advisories.size() < 1)
            {
                sql.releaseAdvisory(currentID);
                log.log(DEBUG, "First advisory released");
            }
            else
            {
                for(Advisory a : advisories)
                {
                    if(a.getID() > currentID)
                    {
                        currentID = a.getID();
                    }
                }
                log.log(DEBUG,"The latest advisory was: " + currentID);
                currentID += 1;
                log.log(DEBUG, "Releasing " + currentID);
                sql.releaseAdvisory(currentID);
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
    
}
