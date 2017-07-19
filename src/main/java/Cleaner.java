/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author behnam
 */
public class Cleaner {

    private final String punctuationPath = "stoplists/Cleaner/Punctuations.txt";
    private final String conjPath = "stoplists/Persian/CONJ.txt";
    private final String detPath = "stoplists/Persian/DET.txt";
    private final String pPath = "stoplists/Persian/P.txt";
    private final String postpPath = "stoplists/Persian/POSTP.txt";
    private final String proPath = "stoplists/Persian/PRO.txt";
    private final String stopwordPath = "stoplists/Persian/persian.txt";

    private List<String> punctuations;
    private List<String> conj;
    private List<String> det;
    private List<String> p;
    private List<String> postp;
    private List<String> pro;
    private List<String> stopword;

    public static final Pattern RTL_CHARACTERS = Pattern.compile("[\u0600-\u06FF\u0750-\u077F\u0590-\u05FF\uFE70-\uFEFF]");

    public Cleaner() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        punctuations = initialize(punctuationPath);
//        conj = initialize(conjPath);
//        det = initialize(detPath);
//        p = initialize(pPath);
//        postp = initialize(postpPath);
//        pro = initialize(proPath);
        stopword = initialize(stopwordPath);
    }

    private List<String> initialize(String path) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        List<String> result = new ArrayList<>();
        File file = new File(path);
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
        String line = in.readLine();
        while(line != null){
            result.add(line);
            line = in.readLine();
        }
        return result;
    }

    private String removeUnwantedTokens(String text, List<String> list){
        StringTokenizer tokenizer = new StringTokenizer(text);
        String result = "";
        while(tokenizer.hasMoreTokens()){
            String token = tokenizer.nextToken();
            if(!contains(list, token)){
                result += " " + token;
            }
        }
        return result.trim();
    }

    private boolean contains(List<String> list, String word){
        for(int i=0;i<list.size();i++){
            if(list.get(i).equalsIgnoreCase(word)){
                return true;
            }
        }
        return false;
    }

    private String removePunctuation(String text){
        StringTokenizer tokenizer = new StringTokenizer(text);
        String result = "";
        while(tokenizer.hasMoreTokens()){
            String token = tokenizer.nextToken();
            token = containsPunctuation(token);
            if(!token.isEmpty()){
                result += " " + token;
            }
        }
        return result;
    }

    public String containsPunctuation(String word){
        for(int i=0;i<punctuations.size();i++){
            if(word.contains(punctuations.get(i))){
                word = word.replace(punctuations.get(i), " ");
            }
        }
        return word.trim();
    }

    public String [] splitAtSpaces(String text){
        return text.split("\\s+");
    }

    public String clean(String text){
        String finalText = "";
        text = text.trim();
        text = text.replace("\r\n", " ").replace("\n", " ");
        text = text.replaceAll("[^\\u0600-\\u065F\\u066A-\\u06EF\\u06FA-\\u06FF-\\s]","");
        StringTokenizer tokenizer = new StringTokenizer(text);
        String word;
        while(tokenizer.hasMoreTokens()){
            word = tokenizer.nextToken();
            Matcher matcher = RTL_CHARACTERS.matcher(word);
            if(matcher.find()){
                finalText = finalText + " " + word.trim();
            }
        }
        finalText = removePunctuation(finalText);
//        finalText = removeUnwantedTokens(finalText, conj);
//        finalText = removeUnwantedTokens(finalText, det);
//        finalText = removeUnwantedTokens(finalText, p);
//        finalText = removeUnwantedTokens(finalText, postp);
//        finalText = removeUnwantedTokens(finalText, pro);
        finalText = removeUnwantedTokens(finalText, stopword);
        return finalText;
    }


    public static void main(String[] args) throws Exception {

        Cleaner cleaner = new Cleaner();
        String cleanText = cleaner.clean("به گزارش خبرگزاری فارس، پندار خمارلو در گفتگو با سایت رسمی باشگاه پرسپولیس اظهار داشت: برای برگزاری اولین دربی برون مرزی ، مسایل گوناگونی مورد توجه و بررسی طرف\u200Cهای دست اندر کار بود که در نهایت با جمع بندی های صورت گرفته ، برگزاری این بازی منتفی شد.\n" +
                "\n" +
                "وی ادامه داد: براین اساس در بازگشت تیم از اردوی اوکراین که صبح فردا خواهد بود، جلسه\u200Cای بین آقای طاهری و برانکو ایوانکوویچ برگزار و برنامه های تیم برای بازی در سوپر جام ایران بررسی خواهد شد و تیم فوتبال پرسپولیس خود را آماده حضور پر قدرت در این بازی خواهد کرد.");

        String [] tokens = cleaner.splitAtSpaces(cleanText);

        System.out.println(tokens[0]);
    }


}
