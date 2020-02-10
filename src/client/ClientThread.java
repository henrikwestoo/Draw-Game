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
    boolean myTurn;
    ClientGUI gui;

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

            String formattedGuess;
            if (guess.equals(currentCorrectAnswer)) {

                formattedGuess = "GUESS$-CORRECT$$" + guess;

            } else {

                formattedGuess = "GUESS$-INCORRECT$$" + guess;

            }

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
            //GÖR OM TILL SWITCH
            while (true) {
                String message = in.readLine();

                if (message.startsWith("WORD-TAG")) {

                    System.out.println("correct answer recieved: " + message);
                    String trimmedMessage = message.substring(message.lastIndexOf("$") + 1);
                    currentCorrectAnswer = trimmedMessage;
                    gui.setAnswer(trimmedMessage);
                    System.out.println("correct answer set as: " + trimmedMessage);

                } 
                
                else if (message.equals("METHOD$-CALL$-CORRECTANSWER$")) 
                {

                    gui.setInfoText("A USER HAS GIVEN THE CORRECT ANSWER");
                    System.out.println("a correct answer was given by a user");
                    
                } 
                
                else if(message.equals("TURN$-TRUE$")){
                
                    myTurn = true;
                    gui.setTurn(true);
                }
                
                    else if(message.equals("TURN$-FALSE$")){
                
                    myTurn = false;
                    gui.setTurn(false);
                }
                
                
                else {

                    String[] xy = message.split(",");
                    Point p = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
                    paper.addPoint(p);
                    System.out.println(in.readLine());

                }
            }

        } catch (IOException | NumberFormatException ex) {
            System.out.println("Could not get messages" + ex.getMessage());
        }

    }

}
