/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import control.SQLiteConnectionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import main.FXMLDocumentController;


/**
 *
 * @author Dragon
 */
public class AnnouncementsGetter extends  HackHuntClass implements Runnable
{
    private TextArea outputTable;
    private FXMLDocumentController parent;
    
    public AnnouncementsGetter()
    {
        initProperties();
        configureLogLevels();
    }
    
    public AnnouncementsGetter(FXMLDocumentController parent)
    {
        this.parent = parent;
        initProperties();
        configureLogLevels();
    }
    
    public void configureForTable(TextArea outputTable)
    {
        this.outputTable = outputTable;
    }

    @Override
    public void run() 
    {
        log.log(INFO,"AdvisoriesGetter running");
        if(outputTable != null)
        {
            log.log(DEBUG,"TextArea configured");
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            ArrayList<HashMap<String, String>> announcements = sql.getAnnouncements();
            String populateString = "";
            for(HashMap<String,String> h : announcements)
            {
                populateString += h.get("timestamp")+" - "+h.get("announcement")+"\r\n";
            }
            parent.populateAnnouncements(populateString);
        }
        else
        {
            log.log(WARNING, "No tableview configured");
        }        
    }
}
