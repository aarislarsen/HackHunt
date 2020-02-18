/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Dragon
 */
public class Advisory 
{
    private int ID = 0;
    private String title = "";
    private String affectedService = "";
    private String difficulty = "";
    private String description = "";
    private String challenge = "";    
    private String hint = "";
    private String secondHint = "";        
    private String answer = "";
    private String challengeURI = "";
    private String partOfVuln = "";
    
    
    /**
     * default constructor
     */
    public Advisory()
    {
        //do nothing
    }
    
    /**
     * constructor
     * @param affectedService the client service affected by this vuln
     * @param description a technical description of the issue, i.e. buffer overflow in username, sqli in select, etc
     * @param challenge the challenge the users needs to solve to be able to patch/exploit this
     * @param hint tutorial text on how to find the vuln
     * @param secondHint additional tutorial text on how to solve the challenge
     * @param correctAnswer the exact string of the correct answer
     * @param difficulty how hard the advisory is to answer. Possible values are "Easy","Medium","Hard","Improbable","Impossible"
     * @param challengeURI the URI to the file associated with the challenge, can be empty
     * @param ID ID of the advisory
     */
    public Advisory(String title, String affectedService, String description, String challenge, String hint, String secondHint, String correctAnswer, String difficulty, String challengeURI, String partofVuln, int ID)
    {
        this.title = title;
        this.affectedService = affectedService;
        this.description = description;
        this.challenge = challenge;
        this.hint = hint;
        this.secondHint = secondHint;
        this.answer = correctAnswer;
        this.ID = ID;
        this.challengeURI = challengeURI;
        this.difficulty = difficulty;
        this.partOfVuln = partofVuln;
    }
    
    /**
     * duplicate for backwards compatability
     * @param title
     * @param affectedService
     * @param description
     * @param challenge
     * @param hint
     * @param secondHint
     * @param correctAnswer
     * @param difficulty
     * @param challengeURI
     * @param ID 
     */
    public Advisory(String title, String affectedService, String description, String challenge, String hint, String secondHint, String correctAnswer, String difficulty, String challengeURI, int ID)
    {
        this.title = title;
        this.affectedService = affectedService;
        this.description = description;
        this.challenge = challenge;
        this.hint = hint;
        this.secondHint = secondHint;
        this.answer = correctAnswer;
        this.ID = ID;
        this.challengeURI = challengeURI;
        this.difficulty = difficulty;
    }
    
    /**
     * gets the affected service
     * @return the affected service
     */
    public String getAffectedService()
    {
        return affectedService;
    }
    
    /**
     * sets the affected service
     * @param affectedService the service to set
     */
    public void setAffectedService(String affectedService)
    {
        this.affectedService = affectedService;
    }
    
    /**
     * gets the description
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * sets the description of the vuln
     * @param description the description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    /**
     * gets the challenge
     * @return the challenge
     */
    public String getChallenge()
    {
        return challenge;
    }
    
    /**
     * sets the challenge
     * @param challenge the challenge to set
     */
    public void setChallenge(String challenge)
    {
        this.challenge = challenge;
    }
    
    /**
     * gets the hint
     * @return the hint
     */
    public String getHint()
    {
        return hint;
    }
    
    /**
     * sets the hint
     * @param hint the hint to set
     */
    public void setHint(String hint)
    {
        this.hint = hint;
    }
    
    /**
     * gets the hint
     * @return the hint
     */
    public String getSecondHint()
    {
        return secondHint;
    }
    
    /**
     * sets the hint
     * @param secondHint the hint to set
     */
    public void setSecondHint(String secondHint)
    {
        this.secondHint = secondHint;
    }
    
    
    
    /**
     * gets the ID
     * @return the ID
     */
    public int getID()
    {
        return ID;
    }
    
    /**
     * sets the ID of the advisory
     * @param ID the ID to set
     */
    public void setID(int ID)
    {
        this.ID = ID;
    }
    
    @Override
    /**
     * toString
     * @return a string representation of the object
     */
    public String toString()
    {
        return title + ";" + difficulty + ";"+ affectedService + ";" + description + ";" + challenge + ";" + hint + ";" + ID;
    }
    
    /**
     * gets the title of the advisory
     * @return advisory title
     */
    public String getTitle()
    {
        return title;
    }
    
    /**
     * sets the title of the advisory
     * @param title the new title of the advisory
     */
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    /**
     * gets the exact string value of the correct answer
     * @return the correct answer string value, or an empty string if no string answer is set (correctLine being used instead)
     */
    public String getCorrectAnswer()
    {
        return answer;
    }
    
    /**
     * sets the correct answer
     * @param correctAnswer the string value that is correct
     */
    public void setCorrectAnswer(String correctAnswer)
    {
        this.answer = correctAnswer;
    }
    
    /**
     * returns the challengeURI
     * @return the challengeURI string
     */
    public String getChallengeURI()
    {
        return challengeURI;
    }
    
    /**
     * sets the challengeURI
     * @param challengeURI the new challengeURI string
     */
    public void setChallengeURI(String challengeURI)
    {
        this.challengeURI = challengeURI;
    }
    
    /**
     * gets the difficulty of an advisory
     * @return the difficulty. Possible values are "Easy","Medium","Hard","Improbable","Impossible"
     */
    public String getDifficulty()
    {
        return difficulty;
    }
    
    /**
     * sets the difficulty of an advisory
     * @param difficulty the difficulty to set it to. Possible values are "Easy","Medium","Hard","Improbable","Impossible"
     */
    public void setDifficulty(String difficulty)
    {
        this.difficulty = difficulty;
    }    
    
    /**
     * gets the part of vuln
     * @return part of vuln
     */
    public String getPartOfVuln()
    {
        return partOfVuln;
    }
    
    /**
     * sets the partofvuln
     * @param partOfVuln 
     */
    public void setPartOfVuln(String partOfVuln)
    {
        this.partOfVuln = partOfVuln;
    }
}
