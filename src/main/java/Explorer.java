import org.apache.log4j.Logger;
import org.apache.spark.sql.execution.columnar.INT;
import org.bytedeco.javacpp.presets.opencv_core;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import java.io.*;
import java.util.*;

/**
 * Created by shagrath on 7/16/17.
 */
public class Explorer {
    private final static Logger logger = Logger.getLogger(Explorer.class);

    private String modelSavePath;
    private Word2Vec model;

    private Explorer(){

        String configFilePath = "conf/word2vec-default.properties";
        try {
            logger.warn("Reading the input file:" + configFilePath);
            File configFile = new File(configFilePath);
            FileReader reader = new FileReader(configFile);
            InputStream inputStream = new FileInputStream(configFile);
            Properties props = new Properties();
            props.load(inputStream);
            this.modelSavePath = props.getProperty("output.model.save.path");
            this.model = loadModel(this.modelSavePath);


            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("File " + configFilePath + " not found!");
        } catch (IOException ex) {
            logger.error("Error in reading" + configFilePath + "!");
        }

        System.out.println(this.modelSavePath);

    }

    private Word2Vec loadModel(String modelSavePath){
        File gModel = new File(modelSavePath);
        return WordVectorSerializer.readWord2VecModel(gModel);

    }


    private void explore() throws IOException {

        while (true) {
            System.out.print("Do you want to play (Y/N) ?\n");
            Scanner ans = new Scanner(System.in);
            String answer = ans.nextLine();
            if (answer.equalsIgnoreCase("Y")) {
                Scanner input = new Scanner(System.in);
                System.out.println("Input word: \n");
                String word = input.nextLine();
                System.out.println(model.wordsNearest(word, 10));
                System.out.println(model.getWordVector(word)[0]);

            } else if (answer.equalsIgnoreCase("N")) {
                System.out.print("Thank you, all done!");
                break;
            } else {
                System.out.print("Try again with (Y/N) only !");
            }

        }
    }

    Collection <String> intersectCollections(List<Collection<String>> topicList){

        Collection<String> topics = new ArrayList<>();
        Collection<String> all = new ArrayList<>();
        for (Collection <String> coll:topicList){
            for(String word:coll){


          }
        }

        Set<String> allTopicSet = new HashSet<String>(all);

        System.out.println(allTopicSet);

        return topics;
    }

    Collection <String> unionCollections(List<Collection<String>> topicList){

        Collection<String> topics = new ArrayList<>();

        Collection<String> all = new ArrayList<>();
        for (Collection <String> coll:topicList){
            all.addAll(coll);
//          for(String word:coll){
//
//
//          }
        }



        Set<String> allTopicSet = new HashSet<String>(all);

        System.out.println(allTopicSet);

        return topics;
    }


    private Collection<String> getTopN(String word, Word2Vec model, int n){
        return model.wordsNearest(word, n);
    }

    private void munchText () throws IOException {

        Cleaner cleaner = new Cleaner();
        String cleanText;



        while (true) {
            System.out.print("Do you want to play (Y/N) ?\n");
            Scanner ans = new Scanner(System.in);
            String answer = ans.nextLine();
            List<Collection<String>> topicList = new ArrayList<Collection<String>>();
            Collection<String> similarWords = new ArrayList<>();

            if (answer.equalsIgnoreCase("Y")) {
                Scanner input = new Scanner(System.in);
                System.out.println("Input word: \n");
                String words = input.nextLine().trim();

                cleanText = cleaner.clean(words);
                String [] tokens = cleaner.splitAtSpaces(cleanText);


                for (String token:tokens){
                    similarWords = model.wordsNearest(token, 10);
                    System.out.println(similarWords);
                    topicList.add(similarWords);

                }

             unionCollections(topicList);


            } else if (answer.equalsIgnoreCase("N")) {
                System.out.print("Thank you, all done!");
                break;
            } else {
                System.out.print("Try again with (Y/N) only !");
            }

        }
    }


    public static void main(String[] args) throws Exception {

        Explorer explorer = new Explorer();
        explorer.munchText();
    }
}
