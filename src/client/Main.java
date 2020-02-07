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

        //skapa socket
        String ip = "127.0.0.1";
        int port = 2000;
        try {
            Socket socket = new Socket(ip, port);

            //skapa klienttråd
            ClientThread clientThread = new ClientThread(socket);
            //skapa gui
            Paper paper = new Paper(clientThread);
            clientThread.paper = paper;
            ClientGUI gui = new ClientGUI(paper);
            
            clientThread.start();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
