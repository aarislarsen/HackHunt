/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Dragon
 */
public class AdvisoryListRowItem 
{
    private String id = "";
    private String title = "";
    private String released = "";    
    private String answered = "";
    
    public AdvisoryListRowItem(String id, String title, String released, String answered)
    {
        this.answered = answered;
        this.id = id;
        this.title = title;
        this.released = released;
    }
    
    public String getID()
    {
        return id;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public String getAnswered()
    {
        return answered;
    }
    
    public String getReleased()
    {
        return released;
    }
    
}
