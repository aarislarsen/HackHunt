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
public class AdvisoryListRowItem 
{
    private String id = "";
    private String title = "";
    private String difficulty = "";
    
    public AdvisoryListRowItem(String id, String title, String difficulty)
    {
        this.difficulty = difficulty;
        this.id = id;
        this.title = title;
    }
    
    public String getID()
    {
        return id;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public String getDifficulty()
    {
        return difficulty;
    }
    
}
