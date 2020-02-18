/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

//import view.ServiceViewer;

import x.FXMLDocumentController;


/**
 *
 * @author Dragon
 */
public class ServicesGetter extends HackHuntClass implements Runnable
{
    private String token;
    private FXMLDocumentController parent;
    
    public ServicesGetter(String token, FXMLDocumentController parent)
    {
        this.token = token;
        initProperties();
        configureLogLevels();
        this.parent = parent;
    }        
    
    @Override
    public void run() 
    {
        log.log(INFO,"ServicesGetter running");
        String message = "getServices;token";
        message = message.replace("token", token);
        String response = sendMessage(message);
        log.log(DEBUG,"Message: " + response);
        
            if(response.contains("SUCCESS:"))
            {
                String teams = response.split("SUCCESS: ")[1];
                String[] entries = teams.split(";");
//                String output = "";
//                for(String s : entries)
//                {
//                    output = output + s + "\r\n";
//                }                
                parent.populateServicesTable(entries);                 
            }            
            else
            {
                System.out.println(response);
            }        
    }        
}
