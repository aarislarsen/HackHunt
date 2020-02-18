/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dragon
 */
public class SQLiteConnection 
{
    private Connection connection;
    private String propertiesURL = "properties.properties";
    private Properties properties = new Properties();
    
    public SQLiteConnection()
    {        
        initProperties();
    }
    
    /**
     * returns the Connection object, where SQLite queries can be executed, or null if no connection could be made to the SQLite file
     * @return 
     */
    public Connection createConnection()
    {
        try
        {
            // load the sqlite-JDBC driver using the current class loader
            Class.forName("org.sqlite.JDBC");

            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:"+properties.getProperty("SQLitePath"));            

            return connection;            
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory", 
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void closeConnection()
    {
        if(connection != null)
        try 
        {                        
            connection.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet execute(Connection connection, String query)
    {        
        try 
        {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery(query);
            return rs;            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * loads the properties from the file
     */
    private void initProperties()
    {
        try
        {
            properties.load(new FileInputStream(propertiesURL));
        }
        catch (IOException ex)
        {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    public static void main(String[] args)
    {
        SQLiteConnection sqliteConnection = new SQLiteConnection();
    }
}
