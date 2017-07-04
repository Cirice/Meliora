/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Hossein Abedi
 */

public class TextCleaner implements BaseNormalizer {

    private final String punctuationPath = "stoplists/cleaner/Punctuations.txt";
    private final String conjPath = "stoplists/persian/CONJ.txt";
    private final String detPath = "stoplists/persian/DET.txt";
    private final String pPath = "stoplists/persian/P.txt";
    private final String postpPath = "stoplists/persian/POSTP.txt";
    private final String proPath = "stoplists/persian/PRO.txt";
    private final String stopwordPath = "stoplists/persian/persian.txt";

    private List<String> omissionRules;

    public static final Pattern RTL_CHARACTERS = Pattern.compile("[\u0600-\u06FF\u0750-\u077F\u0590-\u05FF\uFE70-\uFEFF]");

    private List<String> joinLists(List<String> list1, List<String> list2) {

        for (String elem : list2) {
            list1.add(elem);
        }
        return list1;
    }

    public TextCleaner() throws IOException {
        omissionRules = loadRules(punctuationPath);
        omissionRules = joinLists(omissionRules, loadRules(conjPath));
        omissionRules = joinLists(omissionRules, loadRules(detPath));
        omissionRules = joinLists(omissionRules, loadRules(pPath));
        omissionRules = joinLists(omissionRules, loadRules(postpPath));
        omissionRules = joinLists(omissionRules, loadRules(proPath));
        omissionRules = joinLists(omissionRules, loadRules(stopwordPath));
    }

    public String normalize(String text) {
        String finalText = "";
        StringTokenizer tokenizer = new StringTokenizer(text.trim().replace("\r\n", " ").
                replace("\n", " ").replace("\r\n", " ").replace("\n", " "));
        String word;
        while (tokenizer.hasMoreTokens()) {
            word = tokenizer.nextToken();
            Matcher matcher = RTL_CHARACTERS.matcher(word);

            if(contains(omissionRules, word))
                 continue;
                if (matcher.find()) {
                    finalText = finalText + " " + word.trim();
                }
        }
        return finalText;
    }

    //
    public List<String> loadRules(String path) throws IOException {
        List<String> rules = new ArrayList<>();
        File file = new File(path);
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
        String line = in.readLine();
        while (line != null) {
            rules.add(line);
            line = in.readLine();
        }
        return rules;
    }

    //
//    private String removeUnwantedTokens(String text, List<String> list){
//        StringTokenizer tokenizer = new StringTokenizer(text);
//        String result = "";
//        while(tokenizer.hasMoreTokens()){
//            String token = tokenizer.nextToken();
//            if(!contains(list, token)){
//                result += " " + token;
//            }
//        }
//        return result.trim();
//    }
//
//
//


    private boolean contains(List<String> list, String word) {

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }
//
//    private String removePunctuation(String text){
//        StringTokenizer tokenizer = new StringTokenizer(text);
//        String result = "";
//        while(tokenizer.hasMoreTokens()){
//            String token = tokenizer.nextToken();
//            token = containsPunctuation(token);
//            if(!StringUtils.isEmpty(token)){
//                result += " " + token;
//            }
//        }
//        return result;
//    }
//
//    public String containsPunctuation(String word){
//        for(int i=0; i <omissionRules.size(); i++){
//            if(word.contains(omissionRules.get(i))){
//                word = word.replace(omissionRules.get(i), "");
//            }
//        }
//        return word.trim();
//    }

    public static void main(String[] args) throws IOException {

        new TextCleaner();

    }


}
