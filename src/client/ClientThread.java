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

    private Socket socket;
    public Paper paper;
    private String currentCorrectAnswer;
    public boolean myTurn;
    public ClientGUI gui;
    private PrintWriter out;

    public ClientThread(Socket socket) {

        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    //skicka en punkt från paper
    public void sendPoint(Point p) {
        String pointString = "" + p.x + "," + p.y + "";
        out.println(pointString);

    }

    //skickar en gissning
    public void sendGuess(String guess) {

        String formattedGuess;
        if (guess.equals(currentCorrectAnswer)) {

            formattedGuess = "GUESS-CORRECT$" + guess;
        } else {

            formattedGuess = "GUESS-INCORRECT$" + guess;
            System.out.println(formattedGuess + "WAS SENT");
        }
        out.println(formattedGuess);
    }

    //kallar till slut på resetCanvas() på samtliga anslutna klienter
    public void sendResetMessage() {
        out.println("RESET$");
    }

    //lyssnar efter meddelanden från servern
    @Override
    public void run() {

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int counter = 0;

            while (true) {
                String message = in.readLine();
                System.out.println(counter + ":message recieved was: "+message);

                //hämta taggen från meddelandet
                String[] sa = message.split("\\$");
                String tag = sa[0];

                switch (tag) {

                    //ett nytt rätt svar har anlänt
                    case "WORD-TAG":

                        paper.resetCanvas();

                        String trimmedMessage = message.substring(message.lastIndexOf("$") + 1);
                        currentCorrectAnswer = trimmedMessage;
                        gui.setAnswer(trimmedMessage);
                        System.out.println("correct answer set as: " + trimmedMessage);
                        break;

                        //en användare har gissat rätt
                    case "METHOD-CALL-CORRECTANSWER":
                        gui.setInfoText("The answer was: " + currentCorrectAnswer);
                        System.out.println("a correct answer was given by a user");
                        break;
                        
                        //det är denna klientens tur att rita
                    case "TURN-TRUE":
                        myTurn = true;
                        gui.setTurn(true);
                        break;

                        //det är inte denna klientens tur att rita
                    case "TURN-FALSE":
                        myTurn = false;
                        gui.setTurn(false);
                        break;

                        //en användare har rensat sin canvas
                    case "RESET":
                        paper.resetCanvas();
                        break;

                        
                        //en användare har gissat fel eller ville skicka ett chattmeddelande
                    case "CHAT-MESSAGE":
                        String replaced = message.replace("CHAT-MESSAGE$", "");
                        String replaced2 = replaced.replace("$", "-");
                        String[] msg = replaced2.split("-");

                        String alias = msg[0];
                        String chatMessage = msg[1];
                        gui.appendToTextArea(alias, chatMessage);
                        break;
                        
                    case "SERVER-STOPPED":
                        System.exit(0);
                        

                        //en användare har ritat
                    default:
                        counter++;
                        String[] xy = message.split(",");
                        Point p = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
                        paper.addPoint(p);

                }

//                if (message.startsWith("WORD-TAG")) {
//
//                    System.out.println("correct answer recieved: " + message);
//
//                    paper.resetCanvas();
//
//                    String trimmedMessage = message.substring(message.lastIndexOf("$") + 1);
//                    currentCorrectAnswer = trimmedMessage;
//                    gui.setAnswer(trimmedMessage);
//                    System.out.println("correct answer set as: " + trimmedMessage);
//
//                } else if (message.equals("METHOD$-CALL$-CORRECTANSWER$")) {
//
//                    gui.setInfoText("The answer was: " + currentCorrectAnswer);
//                    System.out.println("a correct answer was given by a user");
//
//                } else if (message.equals("TURN$-TRUE$")) {
//
//                    myTurn = true;
//                    gui.setTurn(true);
//                } else if (message.equals("TURN$-FALSE$")) {
//
//                    myTurn = false;
//                    gui.setTurn(false);
//                    //
//                } else if (message.equals("RESET$")) {
//
//                    paper.resetCanvas();
//
//                } else if (message.startsWith("CHAT$-MESSAGE$")) {
//                    String replaced = message.replace("CHAT$-MESSAGE$", "");
//                    String replaced2 = replaced.replace("$", "-");
//                    String[] msg = replaced2.split("-");
//
//                    String alias = msg[0];
//                    String chatMessage = msg[1];
//                    gui.appendToTextArea(alias, chatMessage);
//
//                } else {
//
//                    String[] xy = message.split(",");
//                    Point p = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
//                    paper.addPoint(p);
//                    System.out.println(in.readLine());
//
//                }
            }

        } catch (IOException | NumberFormatException ex) {
            System.out.println("Could not get messages" + ex.getMessage());
        }

    }

}
