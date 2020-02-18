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
public class AdvisoriesGetter extends  HackHuntClass implements Runnable
{
    private TableView outputTable;
    private FXMLDocumentController parent;
    
    public AdvisoriesGetter()
    {
        initProperties();
        configureLogLevels();
    }
    
    public AdvisoriesGetter(FXMLDocumentController parent)
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
        log.log(INFO,"AdvisoriesGetter running");
        if(outputTable != null)
        {
            log.log(DEBUG,"Table configured");
            SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
            ArrayList<String[]> advisories = sql.getAllAdvisoriesForOverview();
            parent.populateAdvisoriesTable(advisories);
        }
        else
        {
            log.log(WARNING, "No tableview configured");
        }        
    }
}
