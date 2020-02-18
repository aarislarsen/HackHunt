/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.scene.control.TableView;
import x.FXMLDocumentController;
//import view.ReleasedAdvisoriesTableModel;


/**
 *
 * @author Dragon
 */
public class AdvisoriesGetter extends HackHuntClass implements Runnable
{
    private TableView outputTable;
    private String token;
    private int run = 0;
    private String currentAdvisories = "";
    private FXMLDocumentController parent;
    
    public AdvisoriesGetter(String token)
    {
        this.token = token;
        initProperties();
        configureLogLevels();
    }
    
    public AdvisoriesGetter(String token, FXMLDocumentController parent)
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

    public String getCurrentAdvisory(int ID)
    {
        String advisory = "";
        String[] advisories = currentAdvisories.split(";");
        for(String s : advisories)
        {
            String[] fields = decode(s).split(";");
            if(Integer.parseInt(decode(fields[0])) == ID)
            {
                for(String f : fields)
                {
                    advisory += decode(f)+"##\r\n";
                }
                return advisory;
            }
        }
        return advisory;
    }
    
    public String getToken()
    {
        return token;
    }
    
    @Override
    public void run() 
    {
        run += 1;
        log.log(DEBUG,"Getting advisories for the "+run+". time");
        log.log(INFO,"AdvisoriesGetter running");
        String message = "getUnansweredAdvisories;token";
        message = message.replace("token", token);
        log.log(DEBUG,"Sending message");
        String response = sendMessage(message);
        log.log(DEBUG,"Response received");
        log.log(DEBUG,"Response: " + response);
        if(outputTable != null)
        {
            log.log(DEBUG,"TableView configured");
            if(response.contains("SUCCESS: "))
            {                
                if(response.split("SUCCESS: ").length > 1)                
                {
                    log.log(DEBUG,"Splitting response"); 
                    String[] allAdvisories = response.split("SUCCESS: ")[1].split(";");
                    log.log(DEBUG,"Splitting done, adding to model");
                    parent.populateAdvisoriesTable(allAdvisories);
                    for(String s : allAdvisories)
                    {                    
                        currentAdvisories += s + ";";
                    }                
                }
            } 
        }
        else
        {
            log.log(DEBUG, "");
        }        
    }
}
