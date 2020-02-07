/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Henrik
 */
public class Server {

    public static void main(String[] args) {

        ServerSocket serverSocket;
        Socket clientSocket;
        int port = 2000;
        boolean running = true;
        int clientCount = 0;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started");
            while (running) {

                clientSocket = serverSocket.accept();
                clientCount++;
                System.out.println("Connection request recieved");
                
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                
                System.out.println("ClientHandler created");
                
                Thread thread = new Thread(clientHandler);
                thread.start();
                

            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    //ha en samling med clients
    //ha en serversocket
    //lyssna efter client sockets på serversocketen
    //ta emot en client-socket
    //skapa en serverthread med den socketen
}
