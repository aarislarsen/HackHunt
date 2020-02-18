/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import javax.swing.JList;
import javax.swing.JTable;
import model.TimedActiveTeams;
import model.TimedAdvisoryReleaser;

/**
 *
 * @author Dragon
 */
public class TimedActiveTeamsHandler 
{
    private TimedActiveTeams tar;
    
    /**
     * default constructor
     */
    public TimedActiveTeamsHandler()
    {
        tar = TimedActiveTeams.getInstance();
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
