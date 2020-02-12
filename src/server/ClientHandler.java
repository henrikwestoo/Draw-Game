/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Henrik
 */
public class ClientHandler implements Runnable {

    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String ip;
    boolean running = true;
    boolean myTurn;
    String alias;
    Server server;

    public ClientHandler(Socket socket, int alias, Server server) {

        this.socket = socket;
        this.alias = "Player " + alias;
        this.server = server;

        try {

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            this.out = new PrintWriter(dos);
            ip = socket.getInetAddress().getHostName();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
    
    public void setTurn(boolean myTurn){
    
    this.myTurn = myTurn;
    server.serverGUI.appendInfoText("It is now "+ alias +"s turn");
    sendMessage("TURN-TRUE$");
    
    }

    @Override
    public void run() {

        while (running) {

            try {
                in = new BufferedReader(new InputStreamReader(dis));
                //det inkomna meddelandet
                String message = in.readLine();

                // if startswith tag
                //check if correct
                //if correct, send specific message
                //gör om till switch
                
                String[] sa = message.split("\\$");
                String tag = sa[0];
                System.out.println("TAG WAS: " + tag);
                
                switch(tag){
                
                    case "GUESS-CORRECT":
                        server.broadcastData("METHOD-CALL-CORRECTANSWER$");
                        System.out.println("A CORRECT GUESS WAS MADE!!!");
                        server.setNewTurn();
                        break;
                        
                    case "GUESS-INCORRECT":
                        String formattedMessage = "CHAT-MESSAGE$"+alias+"$"+message.substring(message.lastIndexOf("$") + 1);
                        server.broadcastData(formattedMessage);
                        System.out.println(formattedMessage +" was sentADGSGDJ");
                        System.out.println("User gave an incorrectasd guess: " + formattedMessage);
                        break;
                    case "RESET":
                        server.broadcastData("RESET$");
                     
                    default:
                        server.broadcastData(message);
                }
                
                
                
                
                
                
//                if (message.startsWith("GUESS")) {
//
//                    System.out.println("guess detected");
//
//                    if (message.startsWith("GUESS-CORRECT$")) {
//
//                        //broadcast infomessage
//                        server.broadcastData("METHOD-CALL-CORRECTANSWER$");
//                        System.out.println("A CORRECT GUESS WAS MADE!!!");
//                        server.setNewTurn();
//
//                    } else if (message.startsWith("GUESS-INCORRECT$")) {
//
//                        //append to chat
//                        String formattedMessage = "CHAT-MESSAGE$"+alias+"$"+message.substring(message.lastIndexOf("$") + 1);
//                        server.broadcastData(formattedMessage);
//                        System.out.println(formattedMessage +" was sentADGSGDJ");
//                        System.out.println("User gave an incorrectasd guess: " + formattedMessage);
//
//                    }
//
//                }
//                
//                else if(message.startsWith("RESET$")){
//                
//                    server.broadcastData("RESET$");
//                
//                }
//                
//                //det var en point som skickades
//                else {
//                    server.broadcastData(message);
//                }
            } catch (IOException ex) {

                running = false;
                server.serverGUI.appendInfoText(alias +" "+socket.getLocalAddress()+" disconnected");
                server.setNewTurn();
                server.clients.remove(this);
                
            }

        }

    }

    public void sendMessage(String message) {

        out.println(message);
        out.flush();

    }
}
