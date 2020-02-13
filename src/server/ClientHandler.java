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

/**
 *
 * @author Henrik
 */
public class ClientHandler implements Runnable {

    
    //lägg till accessors
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
    
    //gör det till denna klients tur att rita
    public void setTurn(boolean myTurn){
    
    this.myTurn = myTurn;
    server.serverGUI.appendInfoText("It is now "+ alias +"s turn");
    sendMessage("TURN-TRUE$");
    
    }

    @Override
    public void run() {

        //lyssnar efter meddelanden från klienten
        while (running) {

            try {
                in = new BufferedReader(new InputStreamReader(dis));
                //det inkomna meddelandet
                String message = in.readLine();
                
                //här hämtar vi ut taggen för att tolka meddelandet
                String[] sa = message.split("\\$");
                String tag = sa[0];
                System.out.println("TAG WAS: " + tag);
                
                switch(tag){
                
                    //ifall gissningen matchade det nuvarande ordet
                    case "GUESS-CORRECT":
                        server.broadcastData("METHOD-CALL-CORRECTANSWER$");
                        System.out.println("A CORRECT GUESS WAS MADE!!!");
                        server.setNewTurn();
                        break;
                        
                        //ifall gissningen var fel (behandlas alltså som ett chattmeddelande på klientsidan)
                    case "GUESS-INCORRECT":
                        String formattedMessage = "CHAT-MESSAGE$"+alias+"$"+message.substring(message.lastIndexOf("$") + 1);
                        server.broadcastData(formattedMessage);
                        System.out.println("User gave an incorrectasd guess: " + formattedMessage);
                        break;
                        
                        //ifall klienten vill återställa sin canvas
                    case "RESET":
                        server.broadcastData("RESET$");
                     
                        //ifall det är en point som skickas(den enda typen av meddelande utan en tag)
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

    //skickar ett meddelande till just denna klient
    //används i broadcastMessage() i serverklassen
    public void sendMessage(String message) {

        out.println(message);
        out.flush();

    }
}
