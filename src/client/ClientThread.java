/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Point;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Henrik
 */
public class ClientThread extends Thread {

    Socket socket;

    public ClientThread(Socket socket) {

        this.socket = socket;

    }

    //send point
    public void sendMessage(Point p) {

        try {

            String message = p.toString();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);

        } catch (IOException ex) {
            System.out.println("Could not send message");
        }
    }
    //recievepoint()

}
