/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import javax.swing.JList;
import model.TimedActivePlayers;
import model.TimedActiveTeams;

/**
 *
 * @author Dragon
 */
public class TimedActivePlayersHandler 
{
    private TimedActivePlayers tar;
    
    /**
     * default constructor
     */
    public TimedActivePlayersHandler()
    {
        tar = TimedActivePlayers.getInstance();
    }
    
    public void configureForTable(JList playersList)
    {
        tar.configureForList(playersList);
    }
    
    public void start()
    {
        tar.start();
    }
    
}
