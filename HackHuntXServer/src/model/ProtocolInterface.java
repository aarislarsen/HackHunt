/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * This interface dictates the protocol logic
 * If the response to a request should not be send to the client/server, return null
 * @author Dragon
 */
public interface ProtocolInterface 
{
    /**
     * Handle the request
     * @param request
     * @return 
     */
    String handleRequest(String request);    
    
    /**
     * Remove the trailing blank line added by the client/server implementation
     * @param request
     * @return 
     */
    String removeTrailingBlankLine(String request);
}
