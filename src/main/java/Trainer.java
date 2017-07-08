import java.io.*;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;


/**
 * Created by shagrath on 7/5/17.
 */

public class Trainer {

    private final static Logger logger = Logger.getLogger(Trainer.class);

    // Split on white spaces in the line to get words
    private TokenizerFactory t = new DefaultTokenizerFactory();
    private String inputCorpusPath;
    private String modelSavePath;
    private int minWordFrequency;
    private int iterations;
    private int layerSize;
    private int windowSize;
    private Double learningRate;





    private Trainer(){

        String configFilePath = "conf/word2vec-default.properties";
        try {
            logger.warn("Reading the input file:" + configFilePath);
            File configFile = new File(configFilePath);
            FileReader reader = new FileReader(configFile);
            InputStream inputStream = new FileInputStream(configFile);
            Properties props = new Properties();
            props.load(inputStream);

            this.inputCorpusPath = props.getProperty("input.corpus.path");
            this.modelSavePath = props.getProperty("output.model.save.path");
            this.minWordFrequency = Integer.parseInt(props.getProperty("min.word.frequency").trim());
            this.iterations = Integer.parseInt(props.getProperty("number.of.iterations").trim());
            this.layerSize = Integer.parseInt(props.getProperty("layer.size").trim());
            this.windowSize = Integer.parseInt(props.getProperty("window.size").trim());
            this.learningRate = Double.valueOf(props.getProperty("learning.rate").trim());

            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("File " + configFilePath + " not found!");
        } catch (IOException ex) {
            logger.error("Error in reading" + configFilePath + "!");
        }

    System.out.println(this.inputCorpusPath);

    }

    private void saveModel(String filePath, Word2Vec model){
        WordVectorSerializer.writeWord2VecModel(model, filePath);


    }

    private Word2Vec loadModel(String filePath){
        return WordVectorSerializer.readWord2VecModel(filePath);
    }


    private SentenceIterator loadCorpus(String filePath) throws FileNotFoundException {

        logger.info("Load & Vectorize Sentences....");
        return new BasicLineIterator(filePath);
    }

    private void train() throws Exception {

        logger.info("Building model....");

        SentenceIterator iter = loadCorpus(this.inputCorpusPath);

        Word2Vec model = new Word2Vec.Builder()
                .minWordFrequency(minWordFrequency)
                .iterations(iterations)
                .layerSize(layerSize)
                .seed(42)
                .windowSize(windowSize)
                .iterate(iter)
                .tokenizerFactory(t)
                .learningRate(learningRate)
                .build();

        logger.info("Fitting Word2Vec model....");
        model.fit();
        logger.info("Training finished, saving the model :" + this.modelSavePath);
        saveModel(this.modelSavePath, model);
        logger.info("All Done!");


    }

    public static void main(String[] args) throws Exception {

        new Trainer().train();

    }



}
