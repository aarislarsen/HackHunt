/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Dragon
 */
public class TheUsualMethods
{
    private boolean movable;

    public TheUsualMethods()
    {

    }

    /**
     * checks if the field is empty, and if it contains a number that can be used for parsing
     * @param textField the JTextField to check
     * @return true if the field contains a number, that can be used for Long.parseLong(), false if not
     */
    public boolean checkLongInputField(JTextField textField)
    {
        //check if the field is empty
        if(textField.getText().isEmpty())
        {
            return false;
        }
        //check if the field contains a number
        for(int i = 0; i < textField.getText().length(); i++)
        {
            if(!Character.isDigit(textField.getText().charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * checks if the field is empty, and if it contains a number that can be used for parsing
     * @param textField the JTextField to check
     * @return true if the field contains a number, that can be used for Double.parseDouble(), false if not
     */
    public boolean checkDoubleInputField(JTextField textField)
    {
        //check if the field is empty
        if(textField.getText().isEmpty())
        {
            return false;
        }
        //replace all , with .
        textField.setText(textField.getText().replace(",","."));
        //check if the field contains a number
        for(int i = 0; i < textField.getText().length(); i++)
        {
            if(!Character.isDigit(textField.getText().charAt(i)))
            {
                if(textField.getText().charAt(i) == '.')
                {
                    //do nothing, continue the check
                }
                else
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * checks if the string provided is empty, and if it contains a number that can be used for parsing to a double
     * @param text the string to check
     * @return true if the string contains a number that can be used for Double.parseDouble(), false if not
     */
    public boolean checkDoubleString(String text)
    {
        //check if the field is empty
        if(text.isEmpty())
        {
            return false;
        }
        //replace all , with .
        text = text.replace(",",".");
        for(int i = 0; i < text.length(); i++)
        {
            if(!Character.isDigit(text.charAt(i)))
            {
                if(text.charAt(i) == '.')
                {
                    //do nothing, continue the check
                }
                else
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * chekcs if the string provided is empty, and if it contains a number that can be used for parsing to an integer
     * @param text the string to check
     * @return true if the string contains a number that can be used for Intger.parseInt(), false if not
     */
    public boolean checkIntegerString(String text)
    {
        if(text.isEmpty())
        {
            return false;
        }
        for(int i = 0; i < text.length() ; i++)
        {
            if(!Character.isDigit(text.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
    * sets the LookAndFeel to Nimbus
    * does not handle any errors, simply catches and logs them
    */
    public void activateNimbus()
    {
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedLookAndFeelException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Makes the frame movable without being decorated by the native systems frame
     * Needs a global boolean variable with the name "movable" set to false
     * @param frame the JFrame to make movable
     * @param topMenuPanel the panel that replaces the native top border
     */
    public void makeFrameMovable(final JFrame frame, JPanel topMenuPanel)
    {
        final Point point = new Point();
        final int panelsWidth = topMenuPanel.getWidth();
        final int panelsHeight = topMenuPanel.getHeight();

        frame.addMouseListener(new MouseAdapter()
        {
            int xIndryk;
            int yIndryk;

            @Override
            public void mousePressed(MouseEvent e)
            {
                int eventsXPosition = e.getX();
                int eventsYPosition = e.getY();
                if(eventsXPosition > 0 && eventsXPosition < panelsWidth)
                {
                    if(eventsXPosition > 0 && eventsYPosition < panelsHeight)
                    {
                        movable = true;
                        xIndryk = eventsXPosition;
                        yIndryk = eventsYPosition;
                        point.setLocation(xIndryk, yIndryk);
                    }
                }
            }
        });

        frame.addMouseListener(new MouseListener()
        {

            public void mouseClicked(MouseEvent e)
            {
                //do nothing
            }

            public void mousePressed(MouseEvent e)
            {
                //do nothing
            }

            public void mouseReleased(MouseEvent e)
            {
                movable = false;
            }

            public void mouseEntered(MouseEvent e)
            {
                //do nothing
            }

            public void mouseExited(MouseEvent e)
            {
                //do nothing
            }

        });

        frame.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if(!e.isMetaDown() && movable)
                {
                    Point p = frame.getLocation();
                    frame.setLocation(p.x+e.getX()-point.x,p.y +e.getY()-point.y);
                }
            }
        });
    }

    public void centerApplication(JFrame frame)
    {
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode properties = screen.getDisplayMode();
        int width = properties.getWidth();
        int height = properties.getHeight();;
        frame.setLocation((width/2)-(frame.getWidth()/2), (height/2)-(frame.getHeight()/2));
    }
    
    public void centerAboveMiddleApplication(JFrame frame)
    {
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode properties = screen.getDisplayMode();
        int width = properties.getWidth();
        int height = properties.getHeight();;
        frame.setLocation((width/2)-(frame.getWidth()/2), (height/4)-(frame.getHeight()/2));
    }
    
    public void bottomLeftApplication(JFrame frame)
    {
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode properties = screen.getDisplayMode();
        int width = properties.getWidth();
        int height = properties.getHeight();;
        frame.setLocation(0,height-frame.getHeight());
    }
    
    public void bottomRightApplication(JFrame frame)
    {
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode properties = screen.getDisplayMode();
        int width = properties.getWidth();
        int height = properties.getHeight();;
        frame.setLocation(width-frame.getWidth(),height-frame.getHeight());
    }
    
    public void rightBelowMiddleApplication(JFrame frame)
    {
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode properties = screen.getDisplayMode();
        int width = properties.getWidth();
        int height = properties.getHeight();;
        frame.setLocation(width-frame.getWidth(),height/2);
    }

    public void rightMiddleApplication(JFrame frame)
    {
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode properties = screen.getDisplayMode();
        int width = properties.getWidth();
        int height = properties.getHeight();;
        frame.setLocation(width-frame.getWidth(),(height/2)-(frame.getHeight()/2));
    }
    
    public void topRightApplication(JFrame frame)
    {
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode properties = screen.getDisplayMode();
        int width = properties.getWidth();
        int height = properties.getHeight();;
        frame.setLocation(width-frame.getWidth(),0);
    }
    
    public void topLeftApplication(JFrame frame)
    {
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode properties = screen.getDisplayMode();
        int width = properties.getWidth();
        int height = properties.getHeight();;
        frame.setLocation(0,0);
    }
    
    public void TopMiddleApplication(JFrame frame)
    {
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode properties = screen.getDisplayMode();
        int width = properties.getWidth();
        int height = properties.getHeight();;
        frame.setLocation((width/2)-(frame.getWidth()/2),0);
    }
    
    public void leftBelowMiddleApplication(JFrame frame)
    {
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode properties = screen.getDisplayMode();
        int width = properties.getWidth();
        int height = properties.getHeight();;
        frame.setLocation(0,height/2);
    }
    
    public void leftMiddleApplication(JFrame frame)
    {
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode properties = screen.getDisplayMode();
        int width = properties.getWidth();
        int height = properties.getHeight();;
        frame.setLocation(0,(height/2)-(frame.getHeight()/2));
    }

    /**
     * adds an image to the panes top left corner, and fills up the entire panel.
     * @param pane the panel to add the image to
     * @param imageURL the URL of the image you want for a background
     */
    public void addBackgroundImage(JPanel pane, String imageURL)
    {
        final ImageIcon icon = new ImageIcon(imageURL);
        JLabel labelWithBackgroundImage = new JLabel()
        {
            @Override
            public void paintComponent(Graphics g)
            {
                g.drawImage(icon.getImage(), 0, 0, getWidth(),getHeight(),null);
            }
        };
        labelWithBackgroundImage.setBounds(0,0,pane.getWidth(),pane.getHeight());
        pane.add(labelWithBackgroundImage);
    }
    
    /**
     * checks if the supplied String is in the format of an IPv4 address
     * @param IP the String to check
     * @return true if the IP is valid, false if not
     */
    private boolean isIPValid(String IP)
    {            
        String[] octets = IP.split("\\.");
        if(octets.length != 4)
        {
            return false;
        }
        if(Integer.parseInt(octets[0]) == 127)
        {
            return false;
        }
        for(int i = 0; i < octets.length ; i++)
        {
            if(Integer.parseInt(octets[i]) > 255 ||Integer.parseInt(octets[i]) < 0)
            {
                return false;
            }
        }
        return true;        
    }
}
