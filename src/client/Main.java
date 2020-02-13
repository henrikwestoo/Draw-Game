/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Henrik
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        StartGUI startGUI = new StartGUI();
        startGUI.setVisible(true);

    }

}
