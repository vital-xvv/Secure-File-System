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

//        String folderPath = "src/main/resources/files_json";
//        String value = "size";
//        String xmlPath = "src/main/resources/statistics_xml";

        Statistics statistics = jsonParser.parseFilesInFolderEfficientConcurrent(folderPath, value, 5);
        statistics.sorted();

        jsonParser.prettyXmlPrintInFile(xmlPath, statistics, value);

        String statisticsFilePath = Path.of(String.format("%s/statistics_by_%s.xml",xmlPath, value)).toString();

        System.out.println("\n- - - - - - - - - - - - - - - - -\n");
        System.out.printf("File %s has been created.%n", statisticsFilePath);
        System.out.println("\n- - - - - - - - - - - - - - - - -\n");
    }
}