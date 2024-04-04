package io.vital.app;

import io.vital.app.entities.File;
import io.vital.app.json.JsonParser;
import io.vital.app.statistics.FileStatistics;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        JsonParser jsonParser = new JsonParser();
        String filePath = "src/main/resources/files_json/double_language.json";

        List<File> files = jsonParser.parseFilesFromFile(filePath);

        //jsonParser.prettyPrint(files);
        //jsonParser.prettyPrint(jsonParser.getStatisticsByValue(files, "extension"));

        jsonParser.prettyXmlPrint(FileStatistics.getStatisticsByValue(files, "owner", true));
    }
}