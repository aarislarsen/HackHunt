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
public class ActivePlayersListRowItem 
{
    private String username = "";
    private String teamname = "";
    
    public ActivePlayersListRowItem(String username, String teamname)
    {
        this.username = username;
        this.teamname = teamname;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public String getTeamname()
    {
        return teamname;
    }    
}
