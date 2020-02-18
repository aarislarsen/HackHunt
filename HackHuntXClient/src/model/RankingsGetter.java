/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.scene.control.TableView;
import javax.swing.JTextArea;
import x.FXMLDocumentController;

/**
 *
 * @author Dragon
 */
public class RankingsGetter extends HackHuntClass implements Runnable
{
    private JTextArea outputTextArea;
    private TableView outputTable;
    private FXMLDocumentController parent;
    private String token;
    
    public RankingsGetter(String token)
    {
        this.token = token;
        initProperties();
        configureLogLevels();
    }
    
    public RankingsGetter(String token, FXMLDocumentController parent)
    {
        this.token = token;
        this.parent = parent;
        initProperties();
        configureLogLevels();
    }
    
    public void configureForTextArea(JTextArea outputTextArea)
    {
        this.outputTextArea = outputTextArea;
    }
    
    public void configureForTable(TableView outputTable)
    {
        this.outputTable = outputTable;
    }

    @Override
    public void run() 
    {
        log.log(INFO,"RankingsGetter running");
        String message = "getTeamRankings;token";
        message = message.replace("token", token);
        String response = sendMessage(message);
        log.log(DEBUG,"Message: " + response);
        if(outputTextArea != null)
        {
            log.log(DEBUG,"JTextArea configured");
            if(response.contains("SUCCESS:"))
            {
                String announcement = response.split("SUCCESS: ")[1];
                String[] entries = announcement.split(";");
                String output = "";
                for(int i = 0 ; i < entries.length ; i++)
                {
                    int place = i+1;
                    output = output + place +". " + entries[i].split(",")[1] + "\r\n";
                }
                outputTextArea.setText(output);
            }            
        }
        else if(outputTable != null)
        {
            log.log(DEBUG,"TableView configured");
            if(response.contains("SUCCESS:"))
            {
                //System.out.println(response);
                String rankings = response.split("SUCCESS: ")[1];
                String[] entries = rankings.split(";");
                String output = "";
                for(int i = 0 ; i < entries.length ; i++)
                {
                    int place = i+1;
                    output = output + place +". " + entries[i].split(",")[1] + "\r\n";
                }
                //System.out.println(output);
                parent.populateRankingsTable(entries);
            }            
        }
        else
        {
            System.out.println(response);
        }        
    }
}
