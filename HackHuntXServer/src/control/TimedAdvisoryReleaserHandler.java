/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import model.TimedAdvisoryReleaser;

/**
 *
 * @author Dragon
 */
public class TimedAdvisoryReleaserHandler 
{
    private TimedAdvisoryReleaser tar;
    
    /**
     * default constructor
     */
    public TimedAdvisoryReleaserHandler()
    {
        tar = TimedAdvisoryReleaser.getInstance();
    }
    
    public void startSendingAdvisories()
    {
        tar.startSendingAdvisories();
    }
    
    public void stopSendingAdvisories()
    {
        tar.stopSendingAdvisories();
    }
    
    public void resetAdvisories()
    {
        tar.resetAdvisories();
    }
    
}
