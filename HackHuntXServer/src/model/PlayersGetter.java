/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import control.SQLiteConnectionHandler;
import java.util.ArrayList;
import javafx.scene.control.TableView;
import main.FXMLDocumentController;


/**
 *
 * @author Dragon
 */
public class PlayersGetter extends  HackHuntClass implements Runnable
{
    private TableView outputTable;
    private FXMLDocumentController parent;
    
    public PlayersGetter()
    {
        initProperties();
        configureLogLevels();
    }
    
    public PlayersGetter(FXMLDocumentController parent)
    {
        this.parent = parent;
        initProperties();
        configureLogLevels();
    }
    
    public void configureForTable(TableView outputTable)
    {
        this.outputTable = outputTable;
    }

    @Override
    public void run() 
    {
        log.log(INFO,"PlayersGetter running");
        if(outputTable != null)
        {
            log.log(DEBUG,"Table configured");
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            ArrayList<String[]> players = sql.getAllPlayers();            
            parent.populatePlayersTable(players);
        }
        else
        {
            log.log(WARNING, "No tableview configured");
        }        
    }
}
