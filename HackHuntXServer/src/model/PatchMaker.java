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
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constants;
import util.InputValidator;

/**
 *
 * @author Dragon
 */
public class PatchMaker extends TimerTask
{
    private final String logname = "HackHunt Log";
    private final Logger log = Logger.getLogger(logname);
    private Level INFO = Level.INFO;
    private Level DEBUG = Level.FINE; 
    private Level WARNING = Level.WARNING; 
    private final String propertiesURL = "properties.properties";
    private final Properties properties = new Properties(); 
    private String vulnerability;
    private Timer timer;
    private final InputValidator inputValidator = new InputValidator();
    
    /**
     * default constructor
     */
    public PatchMaker()
    {
        initProperties();
        configureLogLevels();
    }
    
    /**
     * makes an new instance and set it with the timer
     * @param vulnerability 
     */
    public void createPatchMaker(String vulnerability)
    {
        PatchMaker instans = new PatchMaker();
        instans.setID(vulnerability);
        timer = new Timer(vulnerability+" patch timer");
        timer.schedule(instans, (Integer.parseInt(properties.getProperty("PatchDelay")))*1000);
        //timer.schedule(instans, (Integer.parseInt(properties.getProperty("PatchDelay")))*1000,1); //for if it needs to be run on a re-orrucring basis
    }
    
    /**
     * sets the ID of the advisory for which a patch should be offered
     * @param vulnerability 
     */
    public void setID(String vulnerability)
    {
        this.vulnerability = vulnerability;
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

    @Override
    public void run() 
    {
        log.log(DEBUG, "Gonna offer a patch for " + vulnerability + " in " + (Integer.parseInt(properties.getProperty("PatchDelay")))*1000 + " seconds");
        if(!inputValidator.isValidInput(Constants.ALLOWEDINSTRINGS, vulnerability))
        {
            log.log(DEBUG,"Invalid character in vulnerability");
        }
        else
        {
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            for (String s: sql.getUsersThatArentPatchedAgainst(vulnerability))
            {
                if(sql.didUserAlreadyGenerateThisPatch(s, vulnerability))
                {
                    //do nothing
                }
                else
                {
                    sql.offerPatch(s, vulnerability);
                    
                }
            }
            sql.addAnnouncement("A public patch has been made available for the " + vulnerability + " vulnerability");
        }      
    }
    
    public static void main(String args[])
    {
        PatchMaker m = new PatchMaker();
        m.createPatchMaker("F");
        m.createPatchMaker("B");
        m.createPatchMaker("C");
    }
}
