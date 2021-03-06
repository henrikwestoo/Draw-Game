/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 *
 * @author Henrik
 */
public class Paper extends JPanel {

    public HashSet hs = new HashSet();
    ClientThread clientThread;
    
    private int brushSizeX;
    private int brushSizeY;

    public Paper(ClientThread clientThread) {
        this.clientThread = clientThread;
        setBackground(Color.white);
        this.setSize(750, 600);
        addMouseListener(new L1());
        addMouseMotionListener(new L2());
        
        brushSizeX = 10;
        brushSizeY = 10;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        Iterator i = hs.iterator();

        try {
            while (i.hasNext()) {
                Point p = (Point) i.next();
                g.fillOval(p.x, p.y, brushSizeX, brushSizeY);
            }
        } catch (Exception e) {
        }

    }
    
    //används när klienten ritar
    public void addAndSendPoint(Point p) {
        
        if(clientThread.myTurn == true){
        hs.add(p);
        repaint();
        clientThread.sendPoint(p);
        }
    }

    //används när klienten endast tar emot
    public void addPoint(Point p) {
        hs.add(p);
        repaint();
    }
    
    //rensar hashmapen som innehåller alla punkter
    public void resetCanvas(){
    
        hs.clear();
        repaint();
    
    }

    class L1 extends MouseAdapter {

        public void mousePressed(MouseEvent me) {
            addAndSendPoint(me.getPoint());
            
        }
    }

    class L2 extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent me) {
            addAndSendPoint(me.getPoint());
           
        }
    }

}
