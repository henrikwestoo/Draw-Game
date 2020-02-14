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

    //ordet som klassen kommit fram till
    public String word;
    //biblioteket med möjliga ord
    public ArrayList<String> words;
    //lista med ord som redan använts
    public ArrayList<String> usedWords;

    public WordGenerator() {

        //biblioteket skapas
        words = new ArrayList<>();
        words.add("boll");
        words.add("lejon");
        words.add("tiger");
        words.add("cecilia");
        words.add("lovisa");
        words.add("bengt");
        words.add("zebra");
        words.add("haj");

        usedWords = new ArrayList<>();
        usedWords.add("placeholder");

    }

    public String generateWord() {

        Random r = new Random();
        String tag = "WORD-TAG$";

        //hämtar ett slumpmässigt ord ur biblioteket
        word = words.get(r.nextInt(words.size()));

        //kontrollerar ordet mot de ord som redan använts
        while (usedWords.contains(word)) {

            //om biblioteket är lika stort som de använda orden är det dags för en ny cykel
            if (words.size() == usedWords.size()) {
                usedWords.clear();
            }

            int index = r.nextInt(words.size());
            word = words.get(index);
        }

        usedWords.add(word);

        return tag + word;
    }

}
