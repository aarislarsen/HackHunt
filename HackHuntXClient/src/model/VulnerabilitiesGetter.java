/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.swing.JTextField;

/**
 *
 * @author Dragon
 */
public class VulnerabilitiesGetter extends HackHuntClass
{
    private String token;
    private String teamName;
    private String servicename;
    
    public VulnerabilitiesGetter(String token, String teamName, String servicename)
    {
        this.token = token;
        this.teamName = teamName;
        this.servicename = servicename;
        initProperties();
        configureLogLevels();
    }
    
    public VulnerabilitiesGetter(String token, String teamName)
    {
        this.token = token;
        this.teamName = teamName;
        initProperties();
        configureLogLevels();
    }
    
    public String getVulnerabilities()
    {
        log.log(INFO,"VulnerabilitiesGetter running");
        String message = "getUnpatchedVulnsForService;token;teamname;servicename";      
        message = message.replace("token", token).replace("teamname", teamName).replace("servicename",servicename);        
        String response = sendMessage(message);
        log.log(DEBUG,"Message: " + response);
        
        if(response.contains("SUCCESS:"))
        {
            return response.split("SUCCESS: ")[1];
        }          
        else
        {
            return "";
        }
    }
    
    public String getVulnerableTeamMembers(String vulnerability)
    {
        log.log(INFO,"VulnerabilitiesGetter running");
        String message = "getVulnerableTargetsInTeam;token;vulnerability;teamname";
        message = message.replace("token", token).replace("vulnerability", vulnerability).replace("teamname", teamName);
        String response = sendMessage(message);
        log.log(DEBUG,"Message: " + response);
        
        if(response.contains("SUCCESS:"))
        {
            return response.split("SUCCESS: ")[1];
        }          
        else
        {
            return "";
        }
    }
}
