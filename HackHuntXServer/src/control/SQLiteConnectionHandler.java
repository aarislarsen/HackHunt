/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Advisory;
import model.SQLiteConnection;

/**
 *
 * @author Dragon
 */
public class SQLiteConnectionHandler 
{
    SQLiteConnection sqliteConnection;
    
    public SQLiteConnectionHandler()
    {
        sqliteConnection = new SQLiteConnection();        
    }
    
    /**
     * Selects the player from the database
     * @param username username of the player to get
     * @return an arraylist with an arraylist for each row of the returned resultset
     */
    public synchronized ArrayList<ArrayList> getUser(String username)
    {
        ArrayList returnValue = new ArrayList();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT * FROM users WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            result = selectPrepared.executeQuery();
            connection.commit();            
            while(result.next())
            {
                int numberOfColumns = result.getMetaData().getColumnCount();
                ArrayList player = new ArrayList();
                for(int i = 1 ; i <= numberOfColumns ; i++)
                {
                    player.add(result.getString(i));                    
                }
                returnValue.add(player);
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}                
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnValue;
    }
    
    /**
     * adds a new user to the database
     * @param username
     * @param password
     * @param teamname 
     * @return true if successful, false if an error occurs
     */
    public synchronized boolean insertIntoUsers(String username, String password, String teamname)
    {
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        try 
        {
            String insertString = "INSERT INTO users (username,password,teamname) VALUES (?,?,?)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(insertString);
            selectPrepared.setString(1, username);
            selectPrepared.setString(2, password);
            selectPrepared.setString(3, teamname);
            selectPrepared.executeUpdate();            
            connection.commit();
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * 
     * @param username the username of the user to be removed
     * @param password the password of the user to be removed
     * @return true if the statement completed, false if it failed
     */
    public synchronized boolean removeFromUsers(String username, String password)
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM users WHERE username = ? AND PASSWORD = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.setString(1, username);
            deletePrepared.setString(2, password);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Selects the player token from the active players database
     * @param token the token of the active player
     * @return an arraylist with an arraylist for each row of the returned resultset
     */
    public synchronized ArrayList<ArrayList> getActivePlayerByToken(String token)
    {
        ArrayList returnValue = new ArrayList();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT * FROM activeplayers WHERE token = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, token);
            result = selectPrepared.executeQuery();
            connection.commit();            
            while(result.next())
            {
                int numberOfColumns = result.getMetaData().getColumnCount();
                ArrayList player = new ArrayList();
                for(int i = 1 ; i <= numberOfColumns ; i++)
                {
                    player.add(result.getString(i));                    
                }
                returnValue.add(player);
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnValue;
    }    
    
    /**
     * Selects the player by name from the active players database
     * @param username
     * @return an arraylist with an arraylist for each row of the returned resultset
     */
    public synchronized ArrayList<ArrayList> getActivePlayerByUsername(String username)
    {
        ArrayList returnValue = new ArrayList();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT * FROM activeplayers WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            result = selectPrepared.executeQuery();
            connection.commit();            
            while(result.next())
            {
                int numberOfColumns = result.getMetaData().getColumnCount();
                ArrayList player = new ArrayList();
                for(int i = 1 ; i <= numberOfColumns ; i++)
                {
                    player.add(result.getString(i));                    
                }
                returnValue.add(player);
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnValue;
    }  
    
    /**
     * adds a user to the active players database
     * @param username
     * @param token
     * @param timestamp
     * @return true if successful, false if anything goes wrong
     */
    public synchronized boolean insertIntoActivePlayers(String username, String token, String timestamp)
    {
        Connection connection = null;
        PreparedStatement selectPrepared = null;        
        try 
        {
            String insertString = "INSERT INTO activeplayers (username,token,timestamp) VALUES (?,?,?)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(insertString);
            selectPrepared.setString(1, username);
            selectPrepared.setString(2, token);
            selectPrepared.setString(3, timestamp);
            selectPrepared.executeUpdate();
            connection.commit();
            selectPrepared.close();
            connection.close();            
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
    
    /**
     * removes a user from the active players database
     * @param token the token of the player to remove
     * @return true if the user was removed, false if not
     */
    public synchronized boolean removeFromActivePlayersByToken(String token)
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        
        try 
        {
            String deleteString = "DELETE FROM activeplayers WHERE token = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.setString(1, token);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * removes a user from the active players database
     * @param username the username of the player to remove
     * @return true if the user was removed, false if not
     */
    public synchronized boolean removeFromActivePlayersByUsername(String username)
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM activeplayers WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.setString(1, username);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * gets all currently active players
     * @return a HashMap of all active players by handle and teamname, or null if it fails
     */
    public synchronized HashMap<String,String> getActivePlayers()
    {        
        HashMap<String, String> returnValue = new HashMap();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT username, teamname FROM users WHERE username IN (SELECT username FROM activeplayers)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                returnValue.put(result.getString("username"), result.getString("teamname"));
            }
            
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
            return returnValue;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * gets all players and their current status
     * @return an arraylist with a 4-field String array with username, teamname, password and activity status. 
     */
    public synchronized ArrayList<String[]> getAllPlayers()
    {
        ArrayList<String[]> returnValue = new ArrayList<String[]>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT * FROM users";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();
            HashMap<String,String> activePlayers = getActivePlayers();
            while(result.next())
            {
                String[] player = new String[4];
                player[0] = result.getString("username");
                player[1] = result.getString("teamname");
                player[2] = result.getString("password");                                
                if(activePlayers.keySet().contains(result.getString("username")))
                {
                    player[3] = "Active";
                }
                else
                {
                    player[3] = "Inactive";
                }
                returnValue.add(player);
            }
            
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
            return returnValue;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();};
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * gets and advisory with the specified ID
     * @param id
     * @return 
     */
    public synchronized Advisory getAdvisory(int id)
    {
        Advisory returnValue = null;
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT * FROM advisories WHERE id = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setInt(1, id);
            result = selectPrepared.executeQuery();
            connection.commit();
            returnValue = new Advisory(result.getString("title"), result.getString("affectedservice"), result.getString("description"), result.getString("challenge"), result.getString("hint"), result.getString("secondhint"), result.getString("answer"), result.getString("difficulty"), result.getString("challengeuri"), result.getString("partofvulnerability"), Integer.parseInt(result.getString("id")));
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnValue;
    }    
    
    /**
     * gets the score for the user who's active with the provided token
     * @param token
     * @return a hashmap with the colums from the database as strings
     */
    public synchronized HashMap<String,String> getScoreByToken(String token)
    {
        HashMap<String,String> returnValue = new HashMap();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT * FROM score WHERE username IN (SELECT username FROM activeplayers WHERE token = ?)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, token);
            result = selectPrepared.executeQuery();
            connection.commit();            
            int numberOfColumns = result.getMetaData().getColumnCount();
            while(result.next())
            {
                for(int i = 1 ; i <= numberOfColumns ; i++)
                {
                    returnValue.put(result.getMetaData().getColumnName(i), result.getString(i));
                }                
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();                        
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnValue;
    }
    
    /**
     * gets the credit sum of the team
     * @param teamname the name of the team to get the score for
     * @return an integer of the sum of the teams scores. 
     */
    public synchronized int getTeamCredit(String teamname)
    {
        int credits = 0;
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {            
            String selectString = "SELECT SUM(numbers) AS credit FROM (SELECT credit AS numbers FROM users,score WHERE teamname = ? AND users.username == score.username)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, teamname);
            result = selectPrepared.executeQuery();
            connection.commit();                  
            while(result.next())
            {
                int numberOfColumns = result.getMetaData().getColumnCount();
                for(int i = 1 ; i <= numberOfColumns ; i++)
                {
                    credits = result.getInt(i);
                }                
            }            
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();                    
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return credits;
    }
        
    /**
     * gets the prestige sum of the team
     * @param teamname the name of the team to get the score for
     * @return an integer of the sum of the teams scores
     */
    public synchronized int getTeamPrestige(String teamname)
    {
        int prestige = 0;
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {            
            String selectString = "SELECT SUM(numbers) AS prestige FROM (SELECT prestige AS numbers FROM users,score WHERE teamname = ? AND users.username == score.username)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, teamname);
            result = selectPrepared.executeQuery();
            connection.commit();                  
            while(result.next())
            {
                int numberOfColumns = result.getMetaData().getColumnCount();
                for(int i = 1 ; i <= numberOfColumns ; i++)
                {
                    prestige = result.getInt(i);
                }                
            }            
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();                    
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return prestige;
    }
    
    /**
     * Gets the status of all services for all teams
     * @return an arraylist of hashmaps, where each entry is the teamname, and status of their services
     */
    public synchronized ArrayList<HashMap<String,String>> getServicesForAll()
    {
        ArrayList services = new ArrayList<HashMap<String,String>>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT * FROM services";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();            
            while(result.next())
            {
                HashMap teamEntry = new HashMap<String,String>();
                int numberOfColumns = result.getMetaData().getColumnCount();
                for(int i = 1 ; i <= numberOfColumns; i++)
                {
                    teamEntry.put(result.getMetaData().getColumnName(i), result.getString(i));
                }
                services.add(teamEntry);
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();               
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return services;
    }

    /**
     * Gets the status of all services for the team with this user in it
     * @param username the user whose teams service you want to have
     * @return an arraylist of hashmaps, where each entry is the teamname, and status of their services
     */
    public synchronized ArrayList<HashMap<String,String>> getServicesForTeamWithPlayer(String username)
    {
        ArrayList services = new ArrayList<HashMap<String,String>>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT * FROM services WHERE teamname IN (SELECT teamname FROM users WHERE username = ?)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            result = selectPrepared.executeQuery();
            connection.commit();            
            while(result.next())
            {
                HashMap teamEntry = new HashMap<String,String>();
                int numberOfColumns = result.getMetaData().getColumnCount();
                for(int i = 1 ; i <= numberOfColumns; i++)
                {
                    teamEntry.put(result.getMetaData().getColumnName(i), result.getString(i));
                }
                services.add(teamEntry);
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();               
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return services;
    }
    
    /**
     * gets all announcements, sorted by timestamp as a list of hashmaps
     * @return an arraylist of hashmaps that contain announcement entries
     */
    public synchronized ArrayList<HashMap<String,String>> getAnnouncements()
    {
        ArrayList announcements = new ArrayList<HashMap<String,String>>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT * FROM announcements ORDER BY timestamp DESC";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                int numberOfColumns = result.getMetaData().getColumnCount();
                HashMap announcement = new HashMap<String,String>();
                for(int i = 1; i <= numberOfColumns ; i++)
                {
                    announcement.put(result.getMetaData().getColumnName(i), result.getString(i));
                }
                announcements.add(announcement);
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return announcements;
    }
    
    /**
     * gets all released advisories sorted by timestamp as an arraylist of advisory objects
     * @return an arraylist with advisory objects
     */
    public synchronized ArrayList<Advisory> getReleasedAdvisories()
    {
        ArrayList releasedAdvisories = new ArrayList<Advisory>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT * FROM advisories WHERE released = 1 ORDER BY releasetime ASC";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                Advisory releasedAdvisory;
                releasedAdvisory = new Advisory(result.getString("title"), result.getString("affectedservice"), result.getString("description"), result.getString("challenge"), result.getString("hint"), result.getString("secondhint"), result.getString("answer"), result.getString("difficulty"), result.getString("challengeuri"), Integer.parseInt(result.getString("id")));
                releasedAdvisories.add(releasedAdvisory);
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return releasedAdvisories;
    }
    
    /**
     * gets all answered advisories sorted by timestamp as an arraylist with hashmaps of id and timestamps
     * @return an arraylist of hashmaps with advisory objects
     */
    public synchronized ArrayList<HashMap<Integer,String>> getAnsweredAdvisories()
    {
        ArrayList releasedAdvisories = new ArrayList<HashMap<Integer,String>>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT * FROM advisories WHERE released = 1 AND answered = 1 ORDER BY answeredtime ASC";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                HashMap answered = new HashMap<Integer,String>();
                answered.put(Integer.parseInt(result.getString("id")), result.getString("answeredtime"));
                releasedAdvisories.add(answered);
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return releasedAdvisories;
    }
    
    /**
     * gets the answer to a given advisory from the database
     * @param advisoryID the ID of the advisory
     * @return the answer to the advisory as a string, or an empty string if no answer can be found
     */
    public synchronized String getAdvisoryAnswer(int advisoryID)
    {
        String advisoryAnswer = "";
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT answer FROM advisories WHERE released = 1 AND id = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setInt(1, advisoryID);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                advisoryAnswer = result.getString("answer");                
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return advisoryAnswer;
    }
    
    /**
     * marks the given advisory as answered and updates the timestamp
     * WARNING: Does not check if the advisory has already been answered
     * @param advisoryID
     * @return 
     */
    public synchronized boolean markAdvisoryAsAnswered(int advisoryID)
    {
        boolean advisoryAnswered = false;
        Connection connection = null;
        PreparedStatement updatePrepared = null;
        try 
        {
            String updateString =   "UPDATE advisories SET answered = 1, answeredtime = (SELECT strftime('%s','now')) WHERE id=?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            updatePrepared = connection.prepareStatement(updateString);
            updatePrepared.setInt(1, advisoryID);
            updatePrepared.execute();
            connection.commit();
            advisoryAnswered = true; //TODO consider doing a check to see if it actually updated it
            updatePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!updatePrepared.isClosed()){updatePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return advisoryAnswered;
    }
    
    /**
     * checks if an advisory has been answered
     * @return true if already answered, false if not
     */
    public synchronized boolean hasAdvisoryBeenAnswered(int advisoryID)
    {
        boolean answered = false;
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT * FROM advisories WHERE released = 1 AND answered = 1 AND id = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setInt(1, advisoryID);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                answered = true;
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return answered;
    }
    
    /**
     * adds any given answer attempt to the register
     * @param username the user answering
     * @param advisoryID the advisory being answered
     * @param answer the answer given
     * @param timestamp the time the answer is given
     * @param correct 1 if correct, 0 if wrong answer is given
     * @return true if the answer is added, false if it fails
     */
    public synchronized boolean addAnswer(String username, int advisoryID, String answer, String timestamp, int correct)
    {
        boolean answerAdded;
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        try
        {
            String insertString = "INSERT INTO answers (username, advisoryid, answer, timestamp, correct) values (?,?,?,?,?)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(insertString);
            selectPrepared.setString(1, username);
            selectPrepared.setInt(2, advisoryID);
            selectPrepared.setString(3, answer);
            selectPrepared.setString(4, timestamp);
            selectPrepared.setInt(5, correct);
            selectPrepared.executeUpdate();
            connection.commit();
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            answerAdded = true;
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            answerAdded = false;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return answerAdded;
        
    }
    
    /**
     * checks if the user already answered this advisory correctly
     * @param username the user answering
     * @param advisoryID the advisory being answered
     * @return true if they already answered it, false if not
     */
    public synchronized boolean didUserAlreadyAnswerThisAdvisoryCorrectly(String username, int advisoryID)
    {
        boolean hasBeenAnswered = false;
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT * FROM answers WHERE username = ? AND advisoryid = ? and correct = 1";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            selectPrepared.setInt(2, advisoryID);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                hasBeenAnswered = true;
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return hasBeenAnswered;
    }
    
    /**
     * gets prestige of a given user
     * @param username the user whom the prestige should be gotten
     * @return the prestige score of the user, or 0 of something fails
     */
    public synchronized int getPrestige(String username)
    {
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT * FROM score WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            result = selectPrepared.executeQuery();
            connection.commit();
            int currentPrestige = 0;
            while(result.next())
            {
                currentPrestige = Integer.parseInt(result.getString("prestige"));
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
            return currentPrestige;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * sets the prestige of a given user
     * @param prestige the new amount of prestige
     * @param username the user to give the prestige
     * @return true if prestige is successfully set, false if it fails
     */
    public synchronized boolean setPrestige(int prestige, String username)
    {
        Connection connection = null;
        PreparedStatement updatePrepared = null;
        try 
        {
            String updateString = "UPDATE score SET prestige = ? WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);            
            updatePrepared = connection.prepareStatement(updateString);
            updatePrepared.setInt(1, prestige);
            updatePrepared.setString(2, username);
            updatePrepared.execute();
            connection.commit();
            updatePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!updatePrepared.isClosed()){updatePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * adds the user to the scoreboard
     * @param username the user to add
     * @return true if successfully added, false otherwise
     */
    public synchronized boolean insertIntoScore(String username)
    { 
        boolean userExists = false;
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        PreparedStatement insertPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT * FROM score WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            result = selectPrepared.executeQuery();
            connection.commit();                
            while(result.next())
            {
                userExists = true;
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(userExists)
        {
            //do nothing, player already exists)
            return false;
        }
        else
        {
            try
            {
                String insertString = "INSERT INTO score (username,prestige,credit) VALUES (?,0,0)";
                connection = sqliteConnection.createConnection();
                connection.setAutoCommit(false);
                insertPrepared = connection.prepareStatement(insertString);
                insertPrepared.setString(1, username);
                insertPrepared.executeUpdate();            
                connection.commit();
                insertPrepared.close();
                connection.close();
                sqliteConnection.closeConnection();
                return true;
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            finally
            {
                try 
                {
                    if(!insertPrepared.isClosed()){insertPrepared.close();}
                    if(!connection.isClosed()){connection.close();}
                    sqliteConnection.closeConnection();
                } 
                catch (SQLException ex) 
                {
                    Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }         
    }
    
    /**
     * removes the user from the score table
     * @param username the user to remove
     * @return true if it completes, false if not
     */
    public synchronized boolean removeFromScore(String username)
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM score WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.setString(1, username);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * removes the user from the scores table
     * @param username the user to remove
     * @return true if it completes, false if not
     */
    public synchronized boolean removeFromAnswers(String username)
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM answers WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.setString(1, username);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * gets the current status for all exploits available to the player
     * @param username the name of the player to query for
     * @return an arraylist with strings of the players exploit status
     */
    public synchronized ArrayList<String> getExploitStatus(String username)
    {
        ArrayList<String> exploitStatus = new ArrayList();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {            
            String selectString =   "SELECT \n" +
                                    "  partofvulnerability, \n" +
                                    "  COUNT (id) AS advisoryCount, \n" +
                                    "  (SELECT COUNT (a.advisoryid) FROM answers a INNER JOIN advisories b ON a.advisoryid = b.id WHERE username = ? AND correct = 1 AND partofvulnerability = ass.partofvulnerability) AS CorrectCount\n" +
                                    "FROM \n" +
                                    " advisories ass \n" +
                                    "GROUP BY \n" +
                                    " partofvulnerability";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            result = selectPrepared.executeQuery();
            connection.commit();            
            while(result.next())
            {
                exploitStatus.add(result.getString("partofvulnerability")+","+result.getString("advisoryCount")+","+result.getString("CorrectCount"));
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
            return exploitStatus;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }  
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * checks if the user already generated this exploit
     * @param username the user generating
     * @param vulnerability the vulnerability letter of the vuln
     * @return true if they already generated it, false if not
     */
    public synchronized boolean didUserAlreadyGenerateThisExploit(String username, String vulnerability)
    {
        boolean hasBeenGenerated = false;
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT * FROM vulnerabilities WHERE username = ? AND vulnerability = ? AND exploit = 1";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            selectPrepared.setString(2, vulnerability);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                hasBeenGenerated = true;
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return hasBeenGenerated;
    }
    
    /**
     * checks if the user already generated this exploit
     * @param username the user generating
     * @param vulnerability the vulnerability letter of the vuln
     * @return true if they already generated it, false if not
     */
    public synchronized boolean didUserAlreadyGenerateThisPatch(String username, String vulnerability)
    {
        boolean hasBeenGenerated = false;
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT * FROM vulnerabilities WHERE username = ? AND vulnerability = ? AND patch = 1";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            selectPrepared.setString(2, vulnerability);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                hasBeenGenerated = true;
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return hasBeenGenerated;
    }
        
    /**
     * generates an exploit if the user has all the needed answered advisories
     * @param username 
     * @param vulnerability
     * @return 
     */
    public synchronized boolean generateExploit(String username, String vulnerability)
    {
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString =   "SELECT * FROM (SELECT \n" +
                                    "  partofvulnerability, \n" +
                                    "  COUNT (id) AS advisoryCount, \n" +
                                    "  (SELECT COUNT (a.advisoryid) FROM answers a INNER JOIN advisories b ON a.advisoryid = b.id WHERE username = ? and correct = 1 AND partofvulnerability = ass.partofvulnerability) AS CorrectCount\n" +
                                    "FROM \n" +
                                    " advisories ass\n" +
                                    "GROUP BY\n" +
                                    " partofvulnerability) WHERE advisoryCount = CorrectCount AND partofvulnerability = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            selectPrepared.setString(2, vulnerability);
            result = selectPrepared.executeQuery();
            connection.commit();            
            boolean allAdvisoriesAnswered = false;
            while(result.next())
            {
                allAdvisoriesAnswered = true;
            }
            selectPrepared.close();            
            if(allAdvisoriesAnswered)
            {
                String insertString = "INSERT INTO vulnerabilities (username,vulnerability,exploit) VALUES(?,?,?)";
                PreparedStatement insertPrepared = connection.prepareStatement(insertString);
                insertPrepared.setString(1, username);
                insertPrepared.setString(2, vulnerability);
                insertPrepared.setInt(3, 1);
                insertPrepared.executeUpdate();
                connection.commit();
                connection.close();
                sqliteConnection.closeConnection();
                result.close();
                return true;
            }
            else
            {
                connection.close();
                sqliteConnection.closeConnection();
                result.close();
                return false;
            }            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }  
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * generates a patch if the user has all the needed answered advisories
     * @param username 
     * @param vulnerability
     * @return 
     */
    public synchronized boolean generatePatch(String username, String vulnerability)
    {
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        PreparedStatement insertPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString =   "SELECT * FROM (SELECT \n" +
                                    "  partofvulnerability, \n" +
                                    "  COUNT (id) AS advisoryCount, \n" +
                                    "  (SELECT COUNT (a.advisoryid) FROM answers a INNER JOIN advisories b ON a.advisoryid = b.id WHERE username = ? and correct = 1 AND partofvulnerability = ass.partofvulnerability) AS CorrectCount\n" +
                                    "FROM \n" +
                                    " advisories ass\n" +
                                    "GROUP BY\n" +
                                    " partofvulnerability) WHERE advisoryCount = CorrectCount AND partofvulnerability = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            selectPrepared.setString(2, vulnerability);
            result = selectPrepared.executeQuery();
            connection.commit();            
            boolean allAdvisoriesAnswered = false;
            while(result.next())
            {
                allAdvisoriesAnswered = true;
            }
            selectPrepared.close();            
            if(allAdvisoriesAnswered)
            {
                String insertString = "INSERT INTO vulnerabilities (username,vulnerability,patch) VALUES(?,?,?)";
                insertPrepared = connection.prepareStatement(insertString);
                insertPrepared.setString(1, username);
                insertPrepared.setString(2, vulnerability);
                insertPrepared.setInt(3, 1);
                insertPrepared.executeUpdate();
                connection.commit();
                connection.close();
                sqliteConnection.closeConnection();
                result.close();
                return true;
            }
            else
            {
                connection.close();
                sqliteConnection.closeConnection();
                result.close();
                return false;
            }            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
        finally
        {
            try 
            {                
                if(!insertPrepared.isClosed()){insertPrepared.close();}
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}                
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * removes the user from the vulnerabilities table
     * @param username the user to remove
     * @return true if it completes, false if not
     */
    public synchronized boolean removeFromVulnerabilities(String username)
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM vulnerabilities WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.setString(1, username);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * gets a list of users who have not applied patches for the given vulneraiblity
     * @param vulnerability the vulnerability
     * @return an array list of usernames
     */
    public synchronized ArrayList<String> getVulnerableTargets(String vulnerability)
    {
        ArrayList vulnerableTargets = new ArrayList<String>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {            
            String selectString =   "SELECT username FROM users WHERE username NOT IN (SELECT username FROM vulnerabilities WHERE vulnerability = ? AND patch = 1)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, vulnerability);
            result = selectPrepared.executeQuery();
            connection.commit();            
            while(result.next())
            {
                vulnerableTargets.add(result.getString("username"));
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
            return vulnerableTargets;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return vulnerableTargets;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * gets a list of users in the team who have not applied patches for the given vulneraiblity
     * @param vulnerability the vulnerability
     * @param teamname
     * @return an array list of usernames
     */
    public synchronized ArrayList<String> getVulnerableTargetsInTeam(String vulnerability,String teamname)
    {
        ArrayList vulnerableTargets = new ArrayList<String>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {            
            String selectString =   "SELECT username FROM users WHERE username IN (SELECT username FROM users WHERE username NOT IN (SELECT username FROM vulnerabilities WHERE vulnerability = ? AND patch = 1)) and teamname LIKE ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, vulnerability);
            selectPrepared.setString(2, teamname);
            result = selectPrepared.executeQuery();
            connection.commit();            
            while(result.next())
            {
                vulnerableTargets.add(result.getString("username"));
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
            return vulnerableTargets;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return vulnerableTargets;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * gets the service that is affected by a given vulnerability
     * @param vulnerability
     * @return the service name of the service affected by the vulnerability
     */
    public synchronized String getServiceAffectedByVulnerability(String vulnerability)
    {
        String affectedService = "";
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT affectedservice FROM advisories WHERE partofvulnerability = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, vulnerability);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                affectedService = result.getString("affectedservice");                
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return affectedService;
    }
    
    /**
     * disables a service by setting it to 0 in the database. Servicename is vulnerable to SQLi, so make sure you validate this or only call it when userinput is not used
     * @param teamname
     * @param servicename
     * @return true if the service was successfully disabled, false if not
     */
    public synchronized boolean disableTeamsService(String teamname, String servicename)
    {
        boolean serviceDisabled = false;
        Connection connection = null;
        PreparedStatement updatePrepared = null;
        try 
        {
            String updateString =   "UPDATE services SET "+servicename+" = 0 WHERE teamname = ?"; // TODO the columnname cannot be made dynamically with prepared statements.
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            updatePrepared = connection.prepareStatement(updateString);
            updatePrepared.setString(1, teamname);
            updatePrepared.execute();
            connection.commit();
            if(registerTeamsServiceAsDowned(teamname, servicename))
            {
                serviceDisabled = true; //TODO consider doing a check to see if it actually updated it
            }            
            updatePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!updatePrepared.isClosed()){updatePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return serviceDisabled;
    }
    
    /**
     * enables a service by setting it to 1 in the database. Servicename is vulnerable to SQLi, so make sure you validate this or only call it when userinput is not used
     * @param teamname
     * @param servicename
     * @return true if the service was successfully enabled, false if not
     */
    public synchronized boolean enableTeamsService(String teamname, String servicename)
    {
        boolean serviceEnabled = false;
        Connection connection = null;
        PreparedStatement updatePrepared = null;
        try 
        {
            String updateString =   "UPDATE services SET "+servicename+" = 1 WHERE teamname = ?";// TODO the columnname cannot be made dynamically with prepared statements.
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            updatePrepared = connection.prepareStatement(updateString); 
            updatePrepared.setString(1, teamname);
            updatePrepared.execute();
            connection.commit();
            if(unregisterTeamServiceAsDowned(teamname, servicename))
            {
                serviceEnabled = true; //TODO consider doing a check to see if it actually updated it
            }
            updatePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!updatePrepared.isClosed()){updatePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return serviceEnabled;
    }
    
    /**
     * gets the teamname that includes the specified player
     * @param username
     * @return the teamname that has this player as a member
     */
    public synchronized String getTeamWithThisMember(String username)
    {
        String teamname = "";
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT teamname FROM users WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                teamname = result.getString("teamname");                
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return teamname;
    }    

    /**
     * gets a list of teamnames
     * @return an ArrayList with unique teamnames
     */
    public synchronized ArrayList<String> getTeamNamesFromServicesBoard()
    {
        ArrayList teamnames = new ArrayList<String>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT DISTINCT teamname FROM services";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                teamnames.add(result.getString("teamname"));                
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return teamnames;
    }
    
    /**
     * adds the team to the service board
     * @param teamname the team to add
     * @return true if successfully added, false otherwise
     */
    public synchronized boolean insertIntoServices(String teamname)
    {
        boolean teamExists = false;
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        PreparedStatement insertPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT * FROM services WHERE teamname = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, teamname);
            result = selectPrepared.executeQuery();
            connection.commit();                
            while(result.next())
            {
                teamExists = true;
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(teamExists)
        {
            //do nothing, team already exists)
            return false;
        }
        else
        {
            try
            {
                String insertString = "INSERT INTO services (teamname,fileservice,communicationservice,cryptoservice,webservice,storageservice) VALUES (?,1,1,1,1,1)";
                connection = sqliteConnection.createConnection();
                connection.setAutoCommit(false);
                insertPrepared = connection.prepareStatement(insertString);
                insertPrepared.setString(1, teamname);
                insertPrepared.executeUpdate();            
                connection.commit();
                insertPrepared.close();
                connection.close();
                sqliteConnection.closeConnection();
                return true;
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            finally
            {
                try 
                {
                    if(!insertPrepared.isClosed()){insertPrepared.close();}
                    if(!connection.isClosed()){connection.close();}
                    sqliteConnection.closeConnection();
                } 
                catch (SQLException ex) 
                {
                    Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } 
    }
    
    /**
     * marks an advisory as released by setting the released value to 1
     * @param advisoryID the advisory ID of the released advisory
     * @return true if successful, false if not
     */
    public synchronized boolean releaseAdvisory(int advisoryID)
    {
        boolean released = false;
        Connection connection = null;
        PreparedStatement updatePrepared = null;
        try 
        {
            String updateString =   "UPDATE advisories SET released = 1, releasetime = ? WHERE id = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            updatePrepared = connection.prepareStatement(updateString); 
            updatePrepared.setLong(1, System.currentTimeMillis());
            updatePrepared.setInt(2, advisoryID);
            updatePrepared.execute();
            connection.commit();
            released = true; //TODO consider doing a check to see if it actually updated it
            updatePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!updatePrepared.isClosed()){updatePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return released;
    }
    
    /**
     * marks all advisories as not released, effectively restarting the game
     * @return true if successful, false if not
     */
    public synchronized boolean unReleaseAllAdvisories()
    {
        boolean unreleased = false;
        Connection connection = null;
        PreparedStatement updatePrepared = null;
        try 
        {
            String updateString =   "UPDATE advisories SET released = 0, releasetime = NULL";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            updatePrepared = connection.prepareStatement(updateString); 
            updatePrepared.execute();
            connection.commit();
            unreleased = true; //TODO consider doing a check to see if it actually updated it
            updatePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!updatePrepared.isClosed()){updatePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return unreleased;
    }
    
    /**
     * marks all advisories as unanswered, effectively restarting the game
     * @return true if successful, false if not
     */
    public synchronized boolean unAnswerAllAdvisories()
    {
        boolean unAnswered = false;
        Connection connection = null;
        PreparedStatement updatePrepared = null;
        try 
        {
            String updateString =   "UPDATE advisories SET answered = 0";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            updatePrepared = connection.prepareStatement(updateString); 
            updatePrepared.execute();
            connection.commit();
            unAnswered = true; //TODO consider doing a check to see if it actually updated it
            updatePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!updatePrepared.isClosed()){updatePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return unAnswered;
    }
    
    /**
     * marks the service as down and sets the timestamp of the downing, so that the servicemonitor can determine when it should be re-enabled
     * @param teamname the team whose service should be downed
     * @param service the service to be downed
     * @return true if successful, false if not
     */
    private synchronized boolean registerTeamsServiceAsDowned(String teamname, String service)
    {
        boolean registered = false;
        Connection connection = null;
        PreparedStatement insertPrepared = null;
        try 
        {
            String insertString = "INSERT INTO servicecooldowns (teamname,service,timeofcompromise) VALUES (?,?,?)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            insertPrepared = connection.prepareStatement(insertString);
            insertPrepared.setString(1, teamname);
            insertPrepared.setString(2, service);
            insertPrepared.setLong(3, System.currentTimeMillis());
            insertPrepared.executeUpdate();
            connection.commit();
            registered = true;
            insertPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!insertPrepared.isClosed()){insertPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return registered;
    }
    
    /**
     * removes the entry in the servicecooldowns with the stated teamname and service
     * @param teamname the team whose service is no longer on cooldown
     * @param service the service which is no longer on cooldown
     * @return true if successful, false if not
     */
    private synchronized boolean unregisterTeamServiceAsDowned(String teamname, String service)
    {
        boolean deleted = false;
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        ResultSet result = null;
        try 
        {
            String deleteString = "DELETE FROM servicecooldowns WHERE teamname = ? AND service = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.setString(1, teamname);
            deletePrepared.setString(2, service);
            deletePrepared.executeUpdate();
            connection.commit();
            deleted = true;
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return deleted;
    }
    
    /**
     * gets the service cooldowns for all currently disabled services
     * @return an arraylist with an arraylist for each entry, containing teamname, service and timeofcompromise
     */
    public synchronized ArrayList<ArrayList> getServiceCooldowns()
    {
        ArrayList<ArrayList> returnValue = new ArrayList();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT * FROM servicecooldowns";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                ArrayList line = new ArrayList();
                line.add(result.getString("teamname"));
                line.add(result.getString("service"));
                line.add(result.getString("timeofcompromise"));
                returnValue.add(line);                
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnValue;
    }
    
    /**
     * reset all services so that the monitor can re-activate them
     * @return true if successfull, false if not
     */
    public synchronized boolean resetAllServiceCooldowns()
    {
        boolean deleted = false;
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM servicecooldowns";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.executeUpdate();
            connection.commit();
            deleted = true;
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return deleted;
    }
    
    /**
     * checks to see if anyone has created the exploit (to see if patches should be offered)
     * @param vulnerability
     * @return true if exploit has been created, false if not.
     */
    public synchronized boolean hasExploitBeenCreated(String vulnerability)
    {
        boolean returnValue = false;
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT * FROM vulnerabilities WHERE vulnerability = ? AND exploit = 1";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1,vulnerability);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                returnValue = true;
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            returnValue = false;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnValue;
    }
    
    /**
     * gets a list of users that haven't patched for the vulnerability. This is almost the same as getVulnerableTargets, but this leave out those players who chose to develop the patch instead of the exploit
     * @param vulnerability
     * @return an arraylist with strings of usernames
     */
    public synchronized ArrayList<String> getUsersThatArentPatchedAgainst(String vulnerability)
    {
        ArrayList<String> returnValue = new ArrayList<String>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT username FROM users WHERE username NOT IN (SELECT username FROM vulnerabilities WHERE vulnerability = ?)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1,vulnerability);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                returnValue.add(result.getString(1));
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnValue;
    }
    
    /**
     * marks the user as having patched the vulnerability
     * @param username
     * @param vulnerability
     * @return true if successful, false if it fails
     */
    public synchronized boolean offerPatch(String username, String vulnerability)
    {
        boolean returnValue = false;
        Connection connection = null;
        PreparedStatement insertPrepared = null;
        try 
        {            
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            String insertString = "INSERT INTO vulnerabilities (username,vulnerability,patch) VALUES(?,?,?)";
            insertPrepared = connection.prepareStatement(insertString);
            insertPrepared.setString(1, username);
            insertPrepared.setString(2, vulnerability);
            insertPrepared.setInt(3, 1);
            insertPrepared.executeUpdate();
            returnValue = true;
            connection.commit();
            connection.close();
            sqliteConnection.closeConnection();            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            returnValue = false;
        }
        finally
        {
            try 
            {
                if(!insertPrepared.isClosed()){insertPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnValue;
    }
                
    /**
     * adds an announcement
     * @param announcement the message to add
     * @return true if the message is added, false if it fails
     */
    public synchronized boolean addAnnouncement(String announcement)
    {
        boolean announcementAdded;
        Connection connection = null;
        PreparedStatement insertPrepared = null;
        try
        {
            String insertString = "INSERT INTO announcements (announcement, timestamp) values (?,?)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            insertPrepared = connection.prepareStatement(insertString);
            insertPrepared.setString(1, announcement);
            insertPrepared.setLong(2, System.currentTimeMillis());            
            insertPrepared.executeUpdate();
            connection.commit();
            insertPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            announcementAdded = true;
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            announcementAdded = false;
        }
        finally
        {
            try 
            {
                if(!insertPrepared.isClosed()){insertPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return announcementAdded;
        
    }
    
    /**
     * sets the credit of a given user
     * @param credit the new amount of credit
     * @param username the user to give the credit
     * @return true if credit is successfully set, false if it fails
     */
    public synchronized boolean setCredit(int credit, String username)
    {
        Connection connection = null;
        PreparedStatement updatePrepared = null;
        try 
        {
            String updateString = "UPDATE score SET credit = ? WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);            
            updatePrepared = connection.prepareStatement(updateString);
            updatePrepared.setInt(1, credit);
            updatePrepared.setString(2, username);
            updatePrepared.execute();
            connection.commit();
            updatePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!updatePrepared.isClosed()){updatePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * gets credit of a given user
     * @param username the user whom the credit should be gotten
     * @return the credit score of the user, or 0 of something fails
     */
    public synchronized int getCredit(String username)
    {
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT * FROM score WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            result = selectPrepared.executeQuery();
            connection.commit();
            int currentCredit = 0;
            while(result.next())
            {
                currentCredit = Integer.parseInt(result.getString("credit"));
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
            return currentCredit;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * gets the released advisories that the user hasn't answered yet
     * @param username the username of the player
     * @return an arraylist of advisory objects
     */
    public synchronized ArrayList<Advisory> getUnansweredAdvisories(String username)
    {
        ArrayList returnValue = new ArrayList<Advisory>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT * FROM advisories WHERE released = 1 AND id NOT IN (SELECT advisoryid FROM answers WHERE username = ? AND correct = 1)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, username);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                Advisory releasedAdvisory;
                releasedAdvisory = new Advisory(result.getString("title"), result.getString("affectedservice"), result.getString("description"), result.getString("challenge"), result.getString("hint"), result.getString("secondhint"), result.getString("answer"), result.getString("difficulty"), result.getString("challengeuri"), Integer.parseInt(result.getString("id")));
                returnValue.add(releasedAdvisory);
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
            return returnValue;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return returnValue;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * gets the unique teams
     * @return an arraylist with team names
     */
    public synchronized ArrayList<String> getTeams()
    {
        ArrayList returnValue = new ArrayList<String>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT DISTINCT teamname FROM users";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {                
                returnValue.add(result.getString("teamname"));
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
            return returnValue;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return returnValue;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * gets the second hint for the given advisory
     * @return a string with the second hint
     */
    public synchronized String getSecondHint(int advisoryID)
    {
        String returnValue = "";
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT secondhint FROM advisories WHERE ID = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setInt(1, advisoryID);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {                
                returnValue += result.getString("secondhint");
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
            return returnValue;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return returnValue;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Removes all active players' session
     * @return true if successful, false if not
     */
    public synchronized boolean terminateAllSessions()
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM activeplayers";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Removes all attempted scores
     * @return true if successful, false if not
     */
    public synchronized boolean removeAllAnswers()
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM answers";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Removes all users
     * Does not remove their entries from other tables
     * @return true if successful, false if not
     */
    public synchronized boolean deleteAllPlayers()
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM users";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Removes all scores
     * @return true if successful, false if not
     */
    public synchronized boolean deleteAllScores()
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM score";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Reset all scores, set credit and prestige to 0
     * @return true if successful, false if not
     */
    public synchronized boolean resetAllScores()
    {
        Connection connection = null;
        PreparedStatement updatePrepared = null;
        try 
        {
            String updateString = "UPDATE score SET prestige = 0, credit = 0";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            updatePrepared = connection.prepareStatement(updateString);
            updatePrepared.executeUpdate();
            connection.commit();
            updatePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!updatePrepared.isClosed()){updatePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Removes all announcements
     * @return true if successful, false if not
     */
    public synchronized boolean removeAllAnnouncements()
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM announcements";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Reset all services, enable all of them
     * @return true if successful, false if not
     */
    public synchronized boolean resetAllServices()
    {
        Connection connection = null;
        PreparedStatement updatePrepared = null;
        try 
        {
            String updateString = "UPDATE services SET fileservice = 1, communicationservice = 1, cryptoservice = 1, webservice = 1, storageservice = 1";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            updatePrepared = connection.prepareStatement(updateString);
            updatePrepared.executeUpdate();
            connection.commit();
            updatePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!updatePrepared.isClosed()){updatePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Removes all services, deletes them
     * @return true if successful, false if not
     */
    public synchronized boolean removeAllServices()
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM services";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Removes all vulnerabilities, deletes them
     * @return true if successful, false if not
     */
    public synchronized boolean removeAllVulnerabilities()
    {
        Connection connection = null;
        PreparedStatement deletePrepared = null;
        try 
        {
            String deleteString = "DELETE FROM vulnerabilities";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            deletePrepared = connection.prepareStatement(deleteString);
            deletePrepared.executeUpdate();
            connection.commit();
            deletePrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!deletePrepared.isClosed()){deletePrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * get the vulnerabilities affecting any user in this team
     * @return a comma-separated string with the exploit IDs
     */
    public synchronized String getUnpatchedVulns(String teamName)
    {
        String returnValue = "";
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT DISTINCT partofvulnerability FROM advisories WHERE partofvulnerability NOT IN (SELECT DISTINCT vulnerability FROM vulnerabilities WHERE username IN (SELECT username FROM users WHERE teamname LIKE ?)) ORDER BY partofvulnerability";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, teamName);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {                
                returnValue += result.getString("partofvulnerability")+",";
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
            return returnValue;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return returnValue;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * get the vulnerabilities affecting the given service of any user in this team
     * @return a comma-separated string with the exploit IDs
     */
    public synchronized String getUnpatchedVulnsForService(String teamName, String servicename)
    {
        String returnValue = "";
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT DISTINCT partofvulnerability FROM advisories WHERE partofvulnerability NOT IN (SELECT DISTINCT vulnerability FROM vulnerabilities WHERE username IN (SELECT username FROM users WHERE teamname LIKE ?) AND patch = 1) AND affectedservice LIKE ? ORDER BY partofvulnerability";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, teamName);
            selectPrepared.setString(2, servicename);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {                
                returnValue += result.getString("partofvulnerability")+",";
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
            
            return returnValue;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return returnValue;
        } 
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    /**
     * updates the user details of the given user
     * @param old_username
     * @param new_username
     * @param teamname
     * @param password
     * @return 
     */
    public synchronized boolean updateUser(String old_username, String new_username, String teamname, String password)
    {
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        try
        {
            String selectString = "UPDATE users SET username = ?, teamname = ?, password = ? WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, new_username);
            selectPrepared.setString(2, teamname);
            selectPrepared.setString(3, password);
            selectPrepared.setString(4, old_username);
            selectPrepared.executeUpdate();
            connection.commit();                       
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            
            selectString = "UPDATE activeplayers SET username = ? WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, new_username);
            selectPrepared.setString(2, old_username);
            selectPrepared.executeUpdate();
            connection.commit();                       
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            
            selectString = "UPDATE answers SET username = ? WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, new_username);
            selectPrepared.setString(2, old_username);
            selectPrepared.executeUpdate();
            connection.commit();                       
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            
            selectString = "UPDATE score SET username = ? WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, new_username);
            selectPrepared.setString(2, old_username);
            selectPrepared.executeUpdate();
            connection.commit();                       
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            
            selectString = "UPDATE vulnerabilities SET username = ? WHERE username = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setString(1, new_username);
            selectPrepared.setString(2, old_username);
            selectPrepared.executeUpdate();
            connection.commit();                       
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public synchronized String executeRawQuery(String statement)
    {
        String returnValue = "";
        SQLiteConnection sql = null;
        try 
        {            
            sql = new SQLiteConnection();
            ResultSet result = sql.execute(sql.createConnection(), statement);
            while(result.next())                
            {
                for (int i = 1 ; i <= result.getMetaData().getColumnCount() ; i++)
                {
                    returnValue += result.getString(i)+"\t";
                }
                returnValue += "\r\n";
                
            }
            sql.closeConnection();
        } 
        catch (SQLException ex) 
        {
            returnValue = "Command failed";
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {            
            sql.closeConnection();
        }
        return returnValue;
    }
    
    /**
     * Gets the ID, title, released and answered fields for all advisories
     * @return an arraylist with string arrays (4 size) with id, title, released, answered, in that order
     */
    public synchronized ArrayList<String[]> getAllAdvisoriesForOverview()
    {
        ArrayList<String[]> returnValue = new ArrayList<String[]>();        
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try 
        {
            String selectString = "SELECT id,title,released,answered FROM advisories";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();            
            while(result.next())
            {
                String[] advisory = new String[4];
                advisory[0] = result.getString("id");
                advisory[1] = result.getString("title");
                advisory[2] = result.getString("released");       
                advisory[3] = result.getString("answered"); 
                returnValue.add(advisory);
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnValue;
    }
    
    /**
     * updates the advisory
     * @param id
     * @param title
     * @param difficulty
     * @param service
     * @param description
     * @param challenge
     * @param challengeURI
     * @param hint
     * @param secondHint
     * @param answer
     * @param partOfVulnerability
     * @return 
     */
    public synchronized boolean updateAdvisory(String id, String title, String difficulty, String service, String description, String challenge, String challengeURI, String hint, String secondHint, String answer, String partOfVulnerability)
    {
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        try
        {
            String selectString = "UPDATE advisories SET id = ?, title = ?, affectedservice = ?, difficulty = ?, description = ?, challenge = ?, hint = ?, secondhint = ?, answer = ?, challengeuri = ?, partofvulnerability = ? WHERE id = ?";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            selectPrepared.setInt(1, Integer.parseInt(id));
            selectPrepared.setString(2, title);
            selectPrepared.setString(3, service);
            selectPrepared.setString(4, difficulty);
            selectPrepared.setString(5, description);
            selectPrepared.setString(6, challenge);
            selectPrepared.setString(7, hint);
            selectPrepared.setString(8, secondHint);
            selectPrepared.setString(9, answer);
            selectPrepared.setString(10, challengeURI);
            selectPrepared.setString(11, partOfVulnerability);
            selectPrepared.setInt(12, Integer.parseInt(id));
            selectPrepared.executeUpdate();
            connection.commit();                       
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            
            //TODO update in the vulnerabilities table, to reflect if the cve is changed during the game
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * adds a new advisory
     * @param title
     * @param difficulty
     * @param service
     * @param description
     * @param challenge
     * @param challengeURI
     * @param hint
     * @param secondHint
     * @param answer
     * @param partOfVulnerability
     * @return true if successfully added, false if not
     */
    public synchronized boolean addAdvisory(String title, String difficulty, String service, String description, String challenge, String challengeURI, String hint, String secondHint, String answer, String partOfVulnerability)
    {
        int nextAdvisoryID = 0;
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        PreparedStatement insertPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "select MAX(id) from advisories;";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {   
                nextAdvisoryID = Integer.parseInt(result.getString(1));
            }
            selectPrepared.close();            
            connection.close();
            sqliteConnection.closeConnection();
            result.close();   
            nextAdvisoryID += 1;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try
        {
            String insertString = "INSERT INTO advisories (id, title,affectedservice,difficulty,description,challenge,hint,secondhint,answer,challengeuri,released,partofvulnerability,answered) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            insertPrepared = connection.prepareStatement(insertString);
            
            insertPrepared.setInt(1, nextAdvisoryID);
            insertPrepared.setString(2, title);
            insertPrepared.setString(3, service);
            insertPrepared.setString(4, difficulty);
            insertPrepared.setString(5, description);
            insertPrepared.setString(6, challenge);
            insertPrepared.setString(7, hint);
            insertPrepared.setString(8, secondHint);
            insertPrepared.setString(9, answer);
            insertPrepared.setString(10, challengeURI);
            insertPrepared.setInt(11, 0);
            insertPrepared.setString(12, partOfVulnerability);
            insertPrepared.setInt(13, 0);
            insertPrepared.executeUpdate();
            connection.commit();
            insertPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            return true;
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        finally
        {
            try 
            {
                if(!insertPrepared.isClosed()){insertPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * gets all the answers that have been submitted, along with whether or not the answer was correct. Sorted by newest first
     * @return arraylist of string[]
     */
    public synchronized ArrayList<String[]> getAllAnswers()
    {
        ArrayList answers = new ArrayList<String[]>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT * FROM answers ORDER BY timestamp DESC;";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                String[] a = new String[4]; 
                a[0] = result.getString("username");
                a[1] = result.getString("advisoryid");
                a[2] = result.getString("answer");
                a[3] = result.getString("correct");
                answers.add(a);
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return answers;
    }
    
    /**
     * gets all the scores
     * @return arraylist of string[]
     */
    public synchronized ArrayList<String[]> getAllScores()
    {
        ArrayList scores = new ArrayList<String[]>();
        Connection connection = null;
        PreparedStatement selectPrepared = null;
        ResultSet result = null;
        try
        {
            String selectString = "SELECT * FROM score ORDER BY prestige DESC;";
            connection = sqliteConnection.createConnection();
            connection.setAutoCommit(false);
            selectPrepared = connection.prepareStatement(selectString);
            result = selectPrepared.executeQuery();
            connection.commit();
            while(result.next())
            {
                String[] a = new String[4]; 
                a[0] = result.getString("username");
                a[1] = result.getString("prestige");
                a[2] = result.getString("credit");
                scores.add(a);
            }
            selectPrepared.close();
            connection.close();
            sqliteConnection.closeConnection();
            result.close();  
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try 
            {
                if(!selectPrepared.isClosed()){selectPrepared.close();}
                if(!connection.isClosed()){connection.close();}
                sqliteConnection.closeConnection();
                result.close();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(SQLiteConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return scores;
    }
}
