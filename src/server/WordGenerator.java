/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Henrik
 */
public class WordGenerator {
    
    public String word;
    
    public static String generateWord(){
    
        ArrayList<String> words = new ArrayList<>();
        
        words.add("elefant");
        words.add("lejon");
        words.add("pizza");
        words.add("lampa");
        
        Random r = new Random();
        int index = r.nextInt(4);
        String tag = "WORD-TAG$$";
        
        return tag + words.get(index);
    }
    
}
