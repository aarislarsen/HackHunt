package util;

/*
 * ReadWrite.java
 *
 * Created on September 12, 2007, 2:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;

/**
 *
 * @author Thomas Birn, Bo Hejlemann Olsen Hermansen  og Andreas Aaris-Larsen
 */
public class ReadWrite
{
    private XMLDecoder d;
    
    /** Creates a new instance of ReadWrite */
    public ReadWrite()
    {
        /*nothing*/
    }
    
    /**
     *gemmer registeret til en fil
     *@param register det List-object som skal gemmes
     *@param sti navnet på det register som skal gemmes
     */
    public void saveRegister(List register, String sti)
    {
        try
        {
            XMLEncoder e = new XMLEncoder( new BufferedOutputStream( new FileOutputStream(sti)));
            e.writeObject(register);
            e.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } 
//        catch(IOException e)
//        {
//            e.printStackTrace();
//        }
    }
    
    /**
     *henter registret fra en fil
     *@param sti navnet på det register som skal loades
     *@return returnerer registrets List-object
     */
    public List loadRegister(String sti)
    {
        List register = new ArrayList();
        try
        {
            d = new XMLDecoder(  new BufferedInputStream(new FileInputStream(sti)));
            register = (ArrayList)d.readObject();
        }
        catch(NoSuchElementException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
//        catch(IOException e)
//        {
//            e.printStackTrace();
//        }        
        finally 
        {
            if (d != null)
            {
                d.close();
            }
        }
        return register;  
    }    
    
        /**
     *henter registret fra en fil
     *@param sti navnet på det register som skal loades
     *@return returnerer registrets Map-object (HashMap)
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public Map loadMap(String sti)
    {
        Map register = new HashMap();
        try
        {
            d = new XMLDecoder(  new BufferedInputStream(new FileInputStream(sti)));
            register = (HashMap)d.readObject();
        }
        catch(NoSuchElementException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
//        catch(IOException e)
//        {
//            e.printStackTrace();
//        }        
        finally 
        {
            if (d != null)
            {
                d.close();
            }
        }
        return register;  
    }   
    
    /**
     *gemmer registeret til en fil
     *@param register det Map-object som skal gemmes (HashMap)
     *@param sti navnet på det register som skal gemmes
     */
    @SuppressWarnings({"CallToPrintStackTrace", "ConvertToTryWithResources"})
    public void saveMap(Map register, String sti)
    {
        try
        {
            XMLEncoder e = new XMLEncoder( new BufferedOutputStream( new FileOutputStream(sti)));
            e.writeObject(register);
            e.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } 
//        catch(IOException e)
//        {
//            e.printStackTrace();
//        }
    }    
    
}
