/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Henrik
 */
public class Main {
    
    public static void main(String[] args) {
    
        Server server = new Server();
        ServerGUI serverGUI = new ServerGUI(server);
        serverGUI.setVisible(true);
        
    
    
    }
    
}
