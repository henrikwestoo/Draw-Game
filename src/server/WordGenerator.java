/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Henrik
 */
public class WordGenerator {
    
    public String word;
    
    public WordGenerator(){
    
       String simpleWord = "elefant";
       String tag = "WORD-TAG$$";
       
       word = tag + simpleWord;
    
    }
    
}
