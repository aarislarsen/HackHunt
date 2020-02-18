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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTable;

/**
 * Class for releasing advisories on a given interval
 * @author Dragon
 */
public class TimedActiveTeams extends TimerTask
{
    private final String logname = "HackHunt Log";
    private final Logger log = Logger.getLogger(logname);
    private Level INFO = Level.INFO;
    private Level DEBUG = Level.FINE; 
    private Level WARNING = Level.WARNING; 
    private final String propertiesURL = "properties.properties";
    private final Properties properties = new Properties();   
    private static TimedActiveTeams uniqueInstance;
    private JList playersList;
    private boolean start = false;
    
    /**
     * get the singleton TimedAdvisoryReleaser();
     * @return 
     */
    public static TimedActiveTeams getInstance()
    {
        if(uniqueInstance == null)
        {
            uniqueInstance = new TimedActiveTeams();
        }
        return uniqueInstance;
    }
    
    
    /**
     * default constructor
     */
    private TimedActiveTeams()
    {
        initProperties();
        configureLogLevels();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(this, 0, (Integer.parseInt(properties.getProperty("getActivePlayersInterval")))*1000);
    }
    
    public void configureForList(JList playersList)
    {
        this.playersList = playersList;
    }
    
    public void start()
    {
        start = true;
    }
    
    @Override
    public void run() 
    {        
        if(start)
        {
            String value = "";
            if(playersList.getSelectedIndex() != -1)
            {
                value = (String) playersList.getModel().getElementAt(playersList.getSelectedIndex());
            }
            DefaultListModel<String> model = new DefaultListModel<>();
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            HashMap<String,String> players = sql.getActivePlayers();
            for(String key : players.keySet())
            {
                model.addElement(players.get(key));                
            }  
            playersList.setModel(model);
            for(int i = 0 ; i < playersList.getModel().getSize() ; i++)
            {
                if (((String) playersList.getModel().getElementAt(i)).equals(value))
                {
                    playersList.setSelectedIndex(i);
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
    
}
