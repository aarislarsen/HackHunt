/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;
import javafx.scene.control.TextArea;

/**
 *
 * @author Dragon
 */
public class AnnouncementsGetter extends HackHuntClass implements Runnable
{
    private TextArea outputTextArea;
    private String token;
    
    public AnnouncementsGetter(String token)
    {
        this.token = token;
        initProperties();
        configureLogLevels();
    }
    
    public void configureForTextArea(TextArea outputTextArea)
    {
        this.outputTextArea = outputTextArea;
    }

    @Override
    public void run() 
    {
        log.log(INFO,"AnnouncementsGetter running");
        String message = "getAnnouncements;token";
        message = message.replace("token", token);
        String response = sendMessage(message);
        log.log(DEBUG,"Message: " + response);
        if(outputTextArea != null)
        {            
            log.log(DEBUG,"TextArea configured");
            if(response.contains("SUCCESS:"))
            {
                if(response.split("SUCCESS: ").length < 1)
                {
                    //do nothing, no announcements
                }
                else
                {                    
                    String announcement = response.split("SUCCESS: ")[1];
                    String[] entries = announcement.split(";");
                    String output = "";
                    for(String s : entries)
                    {
                        String time = s.substring(0, 13);
                        Date d = new Date(Long.parseLong(time));
                        String value = s.substring(s.indexOf(',')+1);
                        output += d.getYear()+1900+"/"+d.getMonth()+"/"+d.getDate()+" " + d.getHours()+ ":"+d.getMinutes()+":"+d.getSeconds() + "\t" + value+"\r\n";
                    }
                    outputTextArea.setText(output);
                }
            }            
        }
        else
        {
            System.out.println(response);
        }        
    }
}
