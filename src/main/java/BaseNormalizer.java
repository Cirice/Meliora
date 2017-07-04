import java.io.IOException;
import java.util.List;

/**
 * Created by kain on 7/3/17.
 */
public interface BaseNormalizer {

    public final String punctuationPath = "stoplists/cleaner/Punctuations.txt";
    public final String conjPath = "stoplists/persian/CONJ.txt";
    public final String detPath = "stoplists/persian/DET.txt";
    public final String pPath = "stoplists/persian/P.txt";
    public final String postpPath = "stoplists/persian/POSTP.txt";
    public final String proPath = "stoplists/persian/PRO.txt";
    public final String stopwordPath = "stoplists/persian/persian.txt";


    List<String> loadRules(String path) throws IOException;
    String normalize(String text);

}
