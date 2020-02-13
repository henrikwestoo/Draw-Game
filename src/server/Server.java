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

/**
 *
 * @author Henrik
 */
public class Server implements Runnable {

    //fixa accessors
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
            //stänger socketen för att förhindra fler anslutningsförfrågningar
            serverSocket.close();
            serverGUI.appendInfoText("Server-socket was closed");

            //stänger socketen för varje klient
            for (ClientHandler client : clients) {

                client.socket.close();
                clients.remove(client);

            }
            
            clientCount = 0;
            
            serverGUI.appendInfoText("Client sockets were closed");

        } catch (IOException ex) {

            serverGUI.appendInfoText(ex.getMessage());
        }
        
        catch(NullPointerException e){
        
            serverGUI.appendInfoText("Could not close serversocket.");
        
        }

    }

   
    @Override
    public void run() {

        try {
            running = true;
            serverSocket = new ServerSocket(port);
            serverGUI.appendInfoText("Server started on port " +port);

             //letar efter anslutningar
            while (running) {

                clientSocket = serverSocket.accept();
                clientCount++;
                serverGUI.appendInfoText("Connection request recieved from: " + clientSocket.getLocalAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientCount, this);

                clients.add(clientHandler);
                
                String clientAlias = "Player " + clientCount;
                serverGUI.appendInfoText("Client " + clientSocket.getLocalAddress() + " ("+clientAlias+") was added to clients");

                //startar run() metoden i clientHandler så att användarens representation på servern
                //lyssnar efter meddelanden som servern skickar ut
                Thread thread = new Thread(clientHandler);
                thread.start();
                
                setNewTurn();

            }

        } catch (IOException ex) {

            serverGUI.setInfoLabel(ex.getMessage());
        }

    }
    
    //genererar ett nytt ord och startar en ny tur
    public void setNewTurn(){
        
        broadcastData("TURN-FALSE$");
       
            //här ser vi till att iterera genom klientlistan så att alla
            //klienter får rita lika mycket
        if(currentTurnClientIndex == clients.size()){
        
            //när vi gått igenom alla klienter återställer vi räknaren
            currentTurnClientIndex = 0;
        }
        
        //väljer en klient och gör det till deras tur
        clients.get(currentTurnClientIndex).setTurn(true);
        currentTurnClientIndex++;
        
        //genererar och skickar ut ett nytt ord
        String word = wg.generateWord();
        broadcastData(word);
        
        String trimmedWord = word.substring(word.lastIndexOf("$") + 1);
        serverGUI.appendInfoText("New word was generated: "+trimmedWord);
    
    }

    //används för att skicka meddelanden till samtliga klienter
    
    public void broadcastData(String data) {

        for (ClientHandler client : clients) {

            client.sendMessage(data);

        }
        System.out.println("broadcasted: " + data);

    }

}
