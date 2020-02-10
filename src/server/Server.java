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

    public void stop() {

        running = false;
        try {
            //gör så att inga fler kan connecta
            serverSocket.close();
            serverGUI.appendInfoText("Server-socket was closed");

            //gör så att alla clients kopplas ned
            for (ClientHandler client : clients) {

                client.socket.close();

            }
            serverGUI.appendInfoText("Client sockets were closed");

        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(port);
            serverGUI.appendInfoText("Server started");

            while (running) {

                clientSocket = serverSocket.accept();
                clientCount++;
                serverGUI.appendInfoText("Connection request recieved from: " + clientSocket.getLocalAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);

                clients.add(clientHandler);

                serverGUI.appendInfoText("Client " + clientSocket.getLocalAddress() + "was added to clients");

                Thread thread = new Thread(clientHandler);
                thread.start();
                
                setNewTurn();
                broadcastNewWord();

            }

        } catch (IOException ex) {

            System.out.println(ex.getMessage() + "hejhej");
        }

    }
    
    static void setNewTurn(){
        //resetar turnen
        broadcastData("TURN$-FALSE$");
        
        //väljer en slumpmässig spelare och gör det till deras tur
        Random r = new Random();
        clients.get(r.nextInt(clients.size())).setTurn(true);
        
    
    }

    static void broadcastNewWord() {

        broadcastData(WordGenerator.generateWord());

    }

    static void broadcastData(String data) {

        for (ClientHandler client : clients) {

            client.sendMessage(data);

        }
        System.out.println("broadcasted: " + data);

    }

}
