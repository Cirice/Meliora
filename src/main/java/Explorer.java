import org.apache.log4j.Logger;
import org.apache.spark.sql.execution.columnar.INT;
import org.bytedeco.javacpp.presets.opencv_core;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.accum.distances.CosineSimilarity;
import org.nd4j.linalg.api.ops.impl.accum.distances.EuclideanDistance;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;
import org.nd4j.linalg.util.ArrayUtil;
import javax.xml.crypto.dsig.Transform;
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
            File gModel = new File(modelSavePath);
            this.model = WordVectorSerializer.readWord2VecModel(gModel);
//            this.model = loadModel(this.modelSavePath);


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


    private Collection <String> intersectCollections(List<Collection<String>> topicList){

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
        }



        Set<String> allTopicSet = new HashSet<String>(all);

        System.out.println(allTopicSet);

        return topics;
    }


    private Collection<String> getTopN(String word, Word2Vec model, int n){
        return model.wordsNearest(word, n);
    }


    private String [] tokenize(String text) throws IOException {
        Cleaner cleaner = new Cleaner();
        return cleaner.splitAtSpaces(cleaner.clean(text));
    }

    private INDArray cosMul(String document, Word2Vec model, int vectorSize) throws IOException {

        String[] tokens = tokenize(document);
        INDArray baseVector = Nd4j.ones(vectorSize);

        for (String token : tokens) {
            if (model.hasWord(token)) {

                baseVector = baseVector.addRowVector(Nd4j.create(model.getWordVector(token)));

            }
        }
    return baseVector;
    }



    private void munchText () throws IOException {

        Cleaner cleaner = new Cleaner();


        while (true) {
            System.out.print("Do you want to play (Y/N) ?\n");
            Scanner ans = new Scanner(System.in);
            String answer = ans.nextLine();
            List<Collection<String>> topicList = new ArrayList<Collection<String>>();
            Collection<String> similarWords = new ArrayList<>();

            if (answer.equalsIgnoreCase("Y")) {
                Scanner input1 = new Scanner(System.in);
                Scanner input2 = new Scanner(System.in);

                System.out.println("Give me the document. \n");
                String document = input1.nextLine().trim();

                System.out.println("Give me the tags. \n");
                String tags = input2.nextLine().trim();
                String [] tagList = cleaner.splitAtSpaces(tags);

                INDArray aggregation = cosMul(document, model, 250);



                //System.out.println(aggregation);

                for (String tag:tagList){
                    if (model.hasWord(tag)) {
                        INDArray vector = Nd4j.create(model.getWordVector(tag));

                        double similarity = Transforms.cosineSim(vector, aggregation);

                        System.out.println(tag + ": " + similarity);

                    }
                }


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
