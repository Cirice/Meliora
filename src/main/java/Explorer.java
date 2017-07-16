import org.apache.log4j.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

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


    private void explore(){


        File gModel = new File(this.modelSavePath);
        Word2Vec model =  WordVectorSerializer.readWord2VecModel(gModel);
        while (true) {
            System.out.print("Do you want to play (Y/N) ?\n");
            Scanner ans = new Scanner(System.in);
            String answer =ans.nextLine();
            if (answer.equalsIgnoreCase("Y")) {
                Scanner input = new Scanner(System.in);
                System.out.println("Input word: \n");
                String word = input.nextLine();
                System.out.println(model.wordsNearest(word, 10));

            } else if (answer.equalsIgnoreCase("N")) {
                System.out.print("Thank you, all done!");
                break;
            } else {
                System.out.print("Try again with (Y/N) only !");
            }

        }

        System.out.println(model.wordsNearest("بهار", 10));
    }


    public static void main(String[] args) throws Exception {


        Explorer explorer = new Explorer();
        explorer.explore();







    }




}
