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
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author Henrik
 */
public class ClientThread extends Thread {

    Socket socket;
    Paper paper;
    String currentCorrectAnswer;

    public ClientThread(Socket socket) {

        this.socket = socket;

    }

    //send point
    public void sendPoint(Point p) {

        try {

            String pointString = "" + p.x + "," + p.y + "";
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(pointString);

        } catch (IOException ex) {
            System.out.println("Could not send message");
        }
    }

    //send guess
    public void sendGuess(String guess) {

        try {

            String formattedGuess = "THETAG" + guess; 
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(formattedGuess);

        } catch (IOException ex) {
            System.out.println("Could not send message");
        }
    }

    //recievepoint()
    @Override
    public void run() {

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Läser in alla meddelanden som skickas
            while (true) {
                String message = in.readLine();
                
                
                if(message.startsWith("WORD-TAG")){
                
                    System.out.println("correct answer recieved: " + message);
                
                }
                
                else{
                
                
                String[] xy = message.split(",");
                Point p = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
                paper.addPoint(p);
                System.out.println(in.readLine());
                
                }
            }

        } catch (Exception ex) {
            System.out.println("Could not get messages");
        }

    }

}
