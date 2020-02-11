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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    PrintWriter out;

    public ClientThread(Socket socket) {

        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    //send point
    public void sendPoint(Point p) {
        String pointString = "" + p.x + "," + p.y + "";
        out.println(pointString);

    }

    //send guess
    public void sendGuess(String guess) {
        
        String formattedGuess;
        if (guess.equals(currentCorrectAnswer)) {

            formattedGuess = "GUESS$-CORRECT$$" + guess;
        } else {

            formattedGuess = "GUESS$-INCORRECT$$" + guess;
        }
        out.println(formattedGuess);
    }

    public void sendResetMessage() {

        out.println("RESET$");
        
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

                    paper.resetCanvas();

                    String trimmedMessage = message.substring(message.lastIndexOf("$") + 1);
                    currentCorrectAnswer = trimmedMessage;
                    gui.setAnswer(trimmedMessage);
                    System.out.println("correct answer set as: " + trimmedMessage);

                } else if (message.equals("METHOD$-CALL$-CORRECTANSWER$")) {

                    gui.setInfoText("A USER HAS GIVEN THE CORRECT ANSWER");
                    System.out.println("a correct answer was given by a user");

                } else if (message.equals("TURN$-TRUE$")) {

                    myTurn = true;
                    gui.setTurn(true);
                } else if (message.equals("TURN$-FALSE$")) {

                    myTurn = false;
                    gui.setTurn(false);
                    //
                } 
                
                else if(message.equals("RESET$"))
                {
                
                    paper.resetCanvas();
                
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
