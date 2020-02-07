/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Henrik
 */
public class Server {

    static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    static int clientCount;
    ServerGUI serverGUI;
    int port;
    boolean running;
    
    public void startServer(int port){
        
        ServerSocket serverSocket;
        Socket clientSocket;
        this.port = port;
        running = true;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started");
            
            while (running) {

                clientSocket = serverSocket.accept();
                clientCount++;
                System.out.println("Connection request recieved");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);

                System.out.println("ClientHandler created");

                Thread thread = new Thread(clientHandler);
                thread.start();

            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    static void broadcastData(String data) {

        for (ClientHandler client : clients) {

            client.sendMessage(data);

        }

    }
    
}
