import org.apache.log4j.Logger;
import org.apache.spark.sql.execution.columnar.INT;
import org.apache.spark.sql.execution.columnar.STRING;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
    final static Logger logger = Logger.getLogger(TextReader.class);
    String configFilePath;
    private String csvData;
    private String cvsSplitBy;
    private int clumnSize;
    List <Integer> columnList = new ArrayList<Integer>();


    public TextReader(String configFilePath){

        this.configFilePath = configFilePath;
        File configFile = new File(configFilePath);

        try {
            logger.warn("Reading the input file:" + this.configFilePath);
            FileReader reader = new FileReader(configFile);
            InputStream inputStream = new FileInputStream(configFile);
            Properties props = new Properties();
            props.load(inputStream);
            this.csvData = props.getProperty("input.data.file");
            this.cvsSplitBy = props.getProperty("column.delimiter");
            this.clumnSize = Integer.parseInt(props.getProperty("columns.size"));

            String columns = props.getProperty("selected.cloumns.indices");
            for(String elem:columns.split(",")){
                this.columnList.add(Integer.parseInt(elem.trim()));
            }

            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("File " + this.configFilePath + " not found!");
        } catch (IOException ex) {
            logger.error("Error in reading" + this.configFilePath + "!");
        }
    }


    public void printData() throws IOException {

        String line = "";
        BufferedReader br = new BufferedReader(new FileReader(this.csvData));
        while ((line = br.readLine()) != null) {

            String[] record = line.split(this.cvsSplitBy);
            if (record.length == clumnSize)
            for (int i:this.columnList) {
                //System.out.println(new TextCleaner().normalize(record[i]));
                new TextCleaner().normalize(record[i]);

            }
        }
    };

    public static void main(String[] args) throws IOException {

        new TextReader("conf/parser-conf.properties").printData();

    }


}
