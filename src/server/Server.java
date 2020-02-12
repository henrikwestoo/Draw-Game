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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Henrik
 */
public class Server implements Runnable {

    static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    static int clientCount;
    static ServerGUI serverGUI;
    int port;
    boolean running = true;
    ServerSocket serverSocket;
    Socket clientSocket;
    WordGenerator wg;
    int currentTurnClientIndex;
    
    public Server(){
    
        wg = new WordGenerator();
        currentTurnClientIndex = 0;
    
    }

    public void stop() {

        running = false;
        try {
            //gör så att inga fler kan connecta
            serverSocket.close();
            serverGUI.appendInfoText("Server-socket was closed");

            //gör så att alla clients kopplas ned
            for (ClientHandler client : clients) {

                client.socket.close();
                clients.remove(client);

            }
            
            clientCount = 0;
            
            serverGUI.appendInfoText("Client sockets were closed");

        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void run() {

        try {
            running = true;
            serverSocket = new ServerSocket(port);
            serverGUI.appendInfoText("Server started on port " +port);

            while (running) {

                clientSocket = serverSocket.accept();
                clientCount++;
                serverGUI.appendInfoText("Connection request recieved from: " + clientSocket.getLocalAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientCount, this);

                clients.add(clientHandler);
                
                String clientAlias = "Player " + clientCount;
                serverGUI.appendInfoText("Client " + clientSocket.getLocalAddress() + " ("+clientAlias+") was added to clients");

                Thread thread = new Thread(clientHandler);
                thread.start();
                
                setNewTurn();

            }

        } catch (IOException ex) {

            System.out.println(ex.getMessage() + "hejhej");
        }

    }
    
    public void setNewTurn(){
        //resetar turnen
        broadcastData("TURN-FALSE$");
        
       //väljer nästa spelare och gör det till deras tur 
        if(currentTurnClientIndex == clients.size()){
        
            currentTurnClientIndex = 0;
        }
        
        clients.get(currentTurnClientIndex).setTurn(true);
        currentTurnClientIndex++;
        
        //genererar och skickar ut ett nytt ord
        String word = wg.generateWord();
        broadcastData(word);
        
        String trimmedWord = word.substring(word.lastIndexOf("$") + 1);
        serverGUI.appendInfoText("New word was generated: "+trimmedWord);
    
    }

    public void broadcastData(String data) {

        for (ClientHandler client : clients) {

            client.sendMessage(data);

        }
        System.out.println("broadcasted: " + data);

    }

}
