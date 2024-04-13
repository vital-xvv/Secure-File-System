package io.vital.app.json;

import io.vital.app.entities.Statistics;

import java.io.IOException;

public interface CustomJsonParser {
    void parseFileEfficient(Statistics statistics, java.io.File file, String value);
    Statistics parseFilesInFolderEfficient(String folderPath, String value);
    Statistics parseFilesInFolderEfficientConcurrent(String folderPath, String value, int numOfThreads);
    void prettyXmlPrintInFile(String rootPath, Object xml, String value) throws IOException;
}
