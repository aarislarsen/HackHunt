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
public class RankingListRowItem 
{
    private String rank = "";
    private String rank_teamname = "";
    private String rank_prestige = "";
    
    public RankingListRowItem(String rank, String rank_teamname, String rank_prestige)
    {
        this.rank_prestige = rank_prestige;
        this.rank = rank;
        this.rank_teamname = rank_teamname;
    }
    
    public String getRank()
    {
        return rank;
    }
    
    public String getTeamname()
    {
        return rank_teamname;
    }
    
    public String getPrestige()
    {
        return rank_prestige;
    }
    
}
