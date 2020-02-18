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
public class AnswerListRowItem 
{
    private String username = "";
    private String advisoryid = "";
    private String answer = "";
    private String correct = "";
    
    public AnswerListRowItem(String username, String advisoryid, String answer, String correct)
    {
        this.username = username;
        this.advisoryid = advisoryid;
        this.answer = answer;
        this.correct = correct;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public String getAdvisoryID()
    {
        return advisoryid;
    }    
    
    public String getAnswer()
    {
        return answer;
    }
    
    public String getCorrect()
    {
        return correct;
    }
}
