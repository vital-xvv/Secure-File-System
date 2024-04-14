package io.vital.app;

import io.vital.app.entities.Statistics;
import io.vital.app.json.CustomJsonParser;
import io.vital.app.json.implementation.CustomJsonParserImpl;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        CustomJsonParser jsonParser = new CustomJsonParserImpl();

        String folderPath = args[0];
        String value = args[1];
        String xmlPath = args[2];
        int threadNum = 2;

        //Params validation
        if(args.length == 4 && args[3] != null) {
            try {
                threadNum = Integer.parseInt(args[3]);
            }catch (NumberFormatException e) {
                System.err.printf("\n\nIllegal whole number literal. Default number of threads - %d.\n", threadNum);
            }
        }

        if(threadNum <= 0){
            System.err.println("\nIllegal whole number literal (args[3]). threadNum should be greater than 0.\n");
            return;
        }

        if(!new java.io.File(folderPath).exists() || !new java.io.File(folderPath).isDirectory()){
            System.err.println("\nIllegal argument for folderPath (args[0]): the folder does not exist or it's a file.\n");
            return;
        }

        if(!new java.io.File(xmlPath).exists() || !new java.io.File(xmlPath).isDirectory()){
            System.err.println("\nIllegal argument for xmlPath (args[2]): the folder does not exist or it's a file\n");
            return;
        }

        //Constructing full xml folder path
        String statisticsFilePath = Path.of(String.format("%s/statistics_by_%s.xml",xmlPath, value)).toString();

        //Parsing and calculating exec time
        long start = System.currentTimeMillis();
        Statistics statistics = jsonParser.parseFilesInFolderEfficientConcurrent(folderPath, value, threadNum);
        long end = System.currentTimeMillis();

        statistics.sorted();

        //Writing serialized xml statistics into file
        jsonParser.prettyXmlPrintInFile(xmlPath, statistics, value);

        //Pretty result print in console
        System.out.printf("\n%d threads. Parsed folder %s for: %.3f sec\n", threadNum, folderPath, (end-start)/1000.0);
        System.out.println("\n- - - - - - - - - - - - - - - - -\n");
        System.out.printf("File %s has been created.%n\n", statisticsFilePath);
    }
}