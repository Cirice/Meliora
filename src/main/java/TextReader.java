import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author      Hossein Abedi, abedi.hossein@protonmail.ch
 * @version     1.0
 * @since       1.0
 */

public class TextReader {
    /* A class for reading data gathered from online news agencies.
     */

    /**
     * Declaring some variables here.
     */
    private String csvData = "data/deploy-sample.txt";
    private BufferedReader br = null;
    private String line = "";
    private String cvsSplitBy = "\\*\\*\\*";
    private static final int [] columns = {0, 3, 5};
    private static final int clumnSize = 16;


    public void printData() throws IOException {

        br = new BufferedReader(new FileReader(csvData));
        while ((line = br.readLine()) != null) {

            String[] record = line.split(cvsSplitBy);
            if (record.length == clumnSize)
            for (int i:columns) {
                System.out.println(record[i]);

            }
        }
    };

    public static void main(String[] args) throws IOException {

        new TextReader().printData();

    }


}
