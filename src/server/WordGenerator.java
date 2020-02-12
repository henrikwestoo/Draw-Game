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
    public ArrayList<String> words;
    public ArrayList<String> usedWords;

    public WordGenerator() {

        words = new ArrayList<>();
        words.add("boll");
        words.add("tomasz");
        words.add("henrik");
        words.add("alexander");
        
        words.add("leonard");
        words.add("samuel");
        words.add("aryan");
        words.add("kai");
        

        usedWords = new ArrayList<>();
        usedWords.add("placeholder");

    }

    public String generateWord() {

        Random r = new Random();
        String tag = "WORD-TAG$";

        word = words.get(r.nextInt(words.size()));

        while (usedWords.contains(word)) {

            if (words.size() == usedWords.size()) {
                usedWords.clear();
                System.out.println("usedwords was emptied!!");
            }

            int index = r.nextInt(words.size());
            word = words.get(index);

            System.out.println("Words: " + words.size() + "  Usedwords: " + usedWords.size());
            System.out.println("new word was generated");
        }

        usedWords.add(word);

        return tag + word;
    }

}
