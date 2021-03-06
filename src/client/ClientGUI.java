/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 *
 * @author Henrik
 */
public class ClientGUI extends javax.swing.JFrame {

    private ClientThread clientThread;
    private Paper paper;

    public ClientGUI(Paper paper, ClientThread clientThread) {
        this.clientThread = clientThread;
        setVisible(true);
        initComponents();
        this.setVisible(true);
        setLocation(400,100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(paper, BorderLayout.CENTER);
        this.paper = paper;

        hideTurnBasedElements();

    }

    //ska visas eller inte beroende på om det är din tur
    public void hideTurnBasedElements() {

        sendMessageBtn.setVisible(false);
        resetCanvasBtn.setVisible(false);
        messageTxt.setVisible(false);

    }

    public void revealMsgButton() {

        sendMessageBtn.setVisible(true);
        messageTxt.setVisible(true);

    }

    public void revealResetButton() {

        resetCanvasBtn.setVisible(true);

    }

    public void setAnswer(String answer) {

        if (!clientThread.myTurn) {
            
            String hint = "_";
            
            for(int i = 2; i <= answer.length(); i++){
            
            hint += " _";
            
            }
            
            correctAnswerHintLbl.setText(hint);

        } else {

            correctAnswerHintLbl.setText(answer);
        }
    }

    public void setInfoText(String text) {

        infoLbl.setText(text);

    }
    
    //fyller meddelanderutan
    public void appendToTextArea(String alias, String message){
    
        messagesTxt.append(alias+": "+message +"\n");
    
    }

    public void setTurn(boolean myTurn) {

        hideTurnBasedElements();

        if (myTurn) {
            turnLbl.setText("It is your turn");
            revealResetButton();
        } else {
            turnLbl.setText("It is someone elses turn");
            revealMsgButton();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        correctAnswerHintLbl = new javax.swing.JLabel();
        messageTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        messagesTxt = new javax.swing.JTextArea();
        sendMessageBtn = new javax.swing.JButton();
        infoLbl = new javax.swing.JLabel();
        turnLbl = new javax.swing.JLabel();
        resetCanvasBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        correctAnswerHintLbl.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        correctAnswerHintLbl.setText("_________");

        messagesTxt.setEditable(false);
        messagesTxt.setColumns(20);
        messagesTxt.setRows(5);
        jScrollPane1.setViewportView(messagesTxt);

        sendMessageBtn.setText("SEND");
        sendMessageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessageBtnActionPerformed(evt);
            }
        });

        resetCanvasBtn.setText("RESET CANVAS");
        resetCanvasBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetCanvasBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(855, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(correctAnswerHintLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(messageTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sendMessageBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetCanvasBtn))
                    .addComponent(infoLbl)
                    .addComponent(turnLbl))
                .addGap(43, 43, 43))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(correctAnswerHintLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 214, Short.MAX_VALUE)
                .addComponent(turnLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(infoLbl)
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(messageTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendMessageBtn)
                    .addComponent(resetCanvasBtn))
                .addGap(56, 56, 56))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendMessageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendMessageBtnActionPerformed

        String message = messageTxt.getText();
        
        //eftersom $ används som en tag för socket-meddelande-tolkning vill vi inte att de ska kunna komma in via user input
        if(!message.contains("$") && !message.equals("")){
        clientThread.sendGuess(message);
        messageTxt.setText("");
        }
        
        else{
            setInfoText("Empty message or illegal character in message");
            };
    }//GEN-LAST:event_sendMessageBtnActionPerformed

    private void resetCanvasBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetCanvasBtnActionPerformed
        
        paper.resetCanvas();
        clientThread.sendResetMessage();
    }//GEN-LAST:event_resetCanvasBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel correctAnswerHintLbl;
    private javax.swing.JLabel infoLbl;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField messageTxt;
    private javax.swing.JTextArea messagesTxt;
    private javax.swing.JButton resetCanvasBtn;
    private javax.swing.JButton sendMessageBtn;
    private javax.swing.JLabel turnLbl;
    // End of variables declaration//GEN-END:variables
}
