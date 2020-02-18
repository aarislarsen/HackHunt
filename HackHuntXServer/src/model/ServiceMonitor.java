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
 *
 * @author Dragon
 */
public class ServiceMonitor extends TimerTask
{
    private final String logname = "HackHunt Log";
    private final Logger log = Logger.getLogger(logname);
    private Level INFO = Level.INFO;
    private Level DEBUG = Level.FINE; 
    private Level WARNING = Level.WARNING; 
    private final String propertiesURL = "properties.properties";
    private final Properties properties = new Properties();   
    private static ServiceMonitor uniqueInstance;
    
    /**
     * gets the singleton ServiceMonitor
     * @return 
     */
    public static ServiceMonitor getInstance()
    {
        if(uniqueInstance == null)
        {
            uniqueInstance = new ServiceMonitor();
        }
        return uniqueInstance;
    }
    
    /**
     * private constructor
     */
    private ServiceMonitor()
    {
        initProperties();
        configureLogLevels();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(this, 0, (Integer.parseInt(properties.getProperty("serviceMonitorCheckInterval")))*1000);
    }
    
    @Override
    public void run() 
    {
        SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
        for(ArrayList a : sql.getServiceCooldowns())
        {            
            if(System.currentTimeMillis()-Long.parseLong(a.get(2).toString())>=Long.parseLong(properties.getProperty("ServiceCooldown"))*1000)
            {
                log.log(DEBUG, a.get(1) + " for " + a.get(0) + " is being re-enabled");
                sql.enableTeamsService(a.get(0).toString(), a.get(1).toString());
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
