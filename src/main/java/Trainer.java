import org.apache.log4j.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.deeplearning4j.ui.UiServer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by shagrath on 7/5/17.
 */

public class Trainer {

    final static Logger logger = Logger.getLogger(Trainer.class);

    // Split on white spaces in the line to get words
    private TokenizerFactory t = new DefaultTokenizerFactory();



    public SentenceIterator loadCorpus(String filePath) throws FileNotFoundException {

        logger.info("Load & Vectorize Sentences....");
        return new BasicLineIterator(filePath);
    }

    public void train() throws Exception {

        logger.info("Building model....");

        SentenceIterator iter = loadCorpus("/media/shagrath/Hoji/sample-output.txt");

        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(2)
                .iterations(1)
                .layerSize(100)
                .seed(100)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        logger.info("Fitting Word2Vec model....");
        vec.fit();

        WordVectorSerializer.writeWord2VecModel(vec, "pathToWriteto.txt");

        logger.info("Closest Words:");
        Collection<String> lst = vec.wordsNearest("اشتباه", 10);
        System.out.println(vec.vocab().words());
        System.out.println(lst);


    }

    public static void main(String[] args) throws Exception {

        new Trainer().train();

    }



}
