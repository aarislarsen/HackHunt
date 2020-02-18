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
public class ScoreListRowItem 
{
    private String username = "";
    private String prestige = "";
    private String credit = "";
    
    public ScoreListRowItem(String username, String prestige, String credit)
    {
        this.username = username;
        this.prestige = prestige;
        this.credit = credit;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public String getPrestige()
    {
        return prestige;
    }    
    
    public String getCredit()
    {
        return credit;
    }        
}
