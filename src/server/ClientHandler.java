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

    public ClientHandler(Socket socket) {

        this.socket = socket;

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
    Server.serverGUI.appendInfoText("It is now "+ ip +"s turn");
    sendMessage("TURN$-TRUE$");
    
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
                if (message.startsWith("GUESS$")) {

                    System.out.println("guess detected");

                    if (message.startsWith("GUESS$-CORRECT$$")) {

                        //broadcast infomessage
                        Server.broadcastData("METHOD$-CALL$-CORRECTANSWER$");
                        System.out.println("A CORRECT GUESS WAS MADE!!!");
                        
                        
                        Server.setNewTurn();

                    } else if (message.startsWith("GUESS$-INCORRECT$$")) {

                        //do nothing
                        System.out.println("User gave an incorrect guess: " + message);

                    }

                }
                
                else if(message.startsWith("RESET$")){
                
                    Server.broadcastData("RESET$");
                
                }
                
                //det var en point som skickades
                else {
                    Server.broadcastData(message);
                }
            } catch (IOException ex) {

                running = false;
                Server.serverGUI.appendInfoText("Client "+socket.getLocalAddress()+" disconnected");
                
            }

        }

    }

    public void sendMessage(String message) {

        out.println(message);
        out.flush();

    }
}
