/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javax.swing.JTextField;
import x.FXMLDocumentController;

/**
 *
 * @author Dragon
 */
public class ScoreGetter extends HackHuntClass implements Runnable
{
    private JTextField individualScore;
    private JTextField teamScore;
    private Label individualScoreLabel;
    private Label teamScoreLabel;
    private String token;
    private FXMLDocumentController parent;
    
    public ScoreGetter(String token)
    {
        this.token = token;
        initProperties();
        configureLogLevels();
    }
    
    public ScoreGetter(String token, FXMLDocumentController parent)
    {
        this.token = token;
        initProperties();
        configureLogLevels();
        this.parent = parent;
    }
    
    public void configureForTextFields(JTextField individualScore, JTextField teamScore)
    {
        this.individualScore = individualScore;
        this.teamScore = teamScore;
    }
    
    public void configureForLabels(Label individualScoreLabel, Label teamScoreLabel)
    {
        this.individualScoreLabel = individualScoreLabel;
        this.teamScoreLabel = teamScoreLabel;
    }

    @Override
    public void run() 
    {
        log.log(INFO,"ScoreGetter running");
        String message = "getScore;token";
        String message2 = "getTeamPrestige;token;teamname";
        String message3 = "getTeamCredits;token;teamname";
        String message4 = "getTeamname;token";               
        message4 = message4.replace("token", token);
        String teamname = sendMessage(message4).split("SUCCESS: ")[1];
        message = message.replace("token", token).replace("teamname", teamname);
        message2 = message2.replace("token", token).replace("teamname", teamname);
        message3 = message3.replace("token", token).replace("teamname", teamname);
        
        
        String response = sendMessage(message);
        String response2 = sendMessage(message2);
        String response3 = sendMessage(message3);
        log.log(DEBUG,"Message: " + response);
        log.log(DEBUG,"Message: " + response2);
        log.log(DEBUG,"Message: " + response3);
        if(individualScore != null && teamScore != null)
        {
            log.log(DEBUG,"JTextFields configured");
            if(response.contains("SUCCESS:"))
            {
                String score = response.split("SUCCESS: ")[1];
                String[] entries = score.split(",");                
                
                individualScore.setText(entries[1]+"/"+entries[2]);
            }            
            if(response2.contains("SUCCESS") && response3.contains("SUCCESS"))
            {
                String prestige = response2.split("SUCCESS: ")[1];
                String credit = response3.split("SUCCESS: ")[1];
                teamScore.setText(prestige + "/" + credit);
                
            }
        }
        else if(individualScoreLabel != null && teamScoreLabel != null)
        {
            log.log(DEBUG,"Labels configured");
            if(response.contains("SUCCESS:") && response2.contains("SUCCESS") && response3.contains("SUCCESS"))
            {
                String score = response.split("SUCCESS: ")[1];
                String[] entries = score.split(",");                
                String individual_prestige = entries[1];
                String individual_credit = entries[2];
                String prestige = response2.split("SUCCESS: ")[1];
                String credit = response3.split("SUCCESS: ")[1];
                
                Platform.runLater(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        parent.displayScore(Integer.parseInt(individual_credit), Integer.parseInt(individual_prestige), Integer.parseInt(credit), Integer.parseInt(prestige));                
                    }
                });
            }            
        }
        else
        {
            System.out.println(response);
        }        
    }
}
