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
public class PlayersListRowItem 
{
    private String username = "";
    private String teamname = "";
    private String password = "";
    private String active = "";
    
    public PlayersListRowItem(String username, String teamname, String password, String active)
    {
        this.username = username;
        this.teamname = teamname;
        this.password = password;
        this.active = active;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public String getTeamname()
    {
        return teamname;
    }    
    
    public String getPassword()
    {
        return password;
    }
    
    public String getActive()
    {
        return active;
    }
}
