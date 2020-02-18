/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.scene.control.TableView;
import x.FXMLDocumentController;


/**
 *
 * @author Dragon
 */
public class ActivePlayersGetter extends HackHuntClass implements Runnable
{
    private TableView outputTable;
    private String token;
    private FXMLDocumentController parent;
    
    public ActivePlayersGetter(String token)
    {
        this.token = token;
        initProperties();
        configureLogLevels();
    }
    
    public ActivePlayersGetter(String token, FXMLDocumentController parent)
    {
        this.token = token;
        initProperties();
        configureLogLevels();
        this.parent = parent;
    }
    
    public void configureForTable(TableView outputTable)
    {
        this.outputTable = outputTable;
    }

    @Override
    public void run() 
    {
        log.log(INFO,"ActivePlayersGetter running");
        String message = "getActivePlayers;token";
        message = message.replace("token", token);
        String response = sendMessage(message);
        log.log(DEBUG,"Message: " + response);
        if(outputTable != null)
        {
            log.log(DEBUG,"Table configured");
            if(response.contains("SUCCESS:"))
            {
                String announcement = response.split("SUCCESS: ")[1];
                String[] entries = announcement.split(";");
                String output = "";
                 
                String selection = "";
                //System.out.println(response);
                
                parent.populateActivePlayersTable(entries);                      
            }
        }
        else
        {
            System.out.println(response);
        }        
    }
}
