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
        int threadNum = Integer.parseInt(args[3]);

        //Dev app testing
//        String folderPath = "src/main/resources/json_files_for_experiment";
//        String value = "language";
//        String xmlPath = "src/main/resources/statistics_xml";
//        int threadNum = 8;

        String statisticsFilePath = Path.of(String.format("%s/statistics_by_%s.xml",xmlPath, value)).toString();

        long start = System.currentTimeMillis();
        Statistics statistics = jsonParser.parseFilesInFolderEfficientConcurrent(folderPath, value, threadNum);
        long end = System.currentTimeMillis();

        statistics.sorted();

        jsonParser.prettyXmlPrintInFile(xmlPath, statistics, value);

        //Pretty result print in console
        System.out.printf("\n%d threads. Parsed folder %s for: %.3f sec\n", threadNum, folderPath, (end-start)/1000.0);
        System.out.println("\n- - - - - - - - - - - - - - - - -\n");
        System.out.printf("File %s has been created.%n", statisticsFilePath);
    }
}