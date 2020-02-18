/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package x;

/**
 *
 * @author Dragon
 */
public class ServicesRowItem 
{
    private String team = "";
    private String crypto = "";
    private String file = "";
    private String storage = "";
    private String communication = "";
    private String web = "";
    
    public ServicesRowItem(String team, String crypto, String file, String storage, String communication, String web)
    {
        this.team = team;
        this.crypto = crypto;
        this.file = file;
        this.storage = storage;
        this.communication = communication;
        this.web = web;
    }
    
    public String getTeam()
    {
        return team;
    }
    
    public String getCrypto()
    {
        return crypto;
    }
    
    public String getFile()
    {
        return file;
    }
    
    public String getStorage()
    {
        return storage;
    }
    
    public String getCommunication()
    {
        return communication;
    }
    
    public String getWeb()
    {
        return web;
    }
}
