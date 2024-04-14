package io.vital.app.json;

import io.vital.app.entities.Statistics;

import java.io.File;
import java.io.IOException;

/**
 * Implementations of this interface serve as parsers of json arrays of serialized {@link io.vital.app.entities.File} objects
 * @author Vitalii Huzii
 */
public interface CustomJsonParser {
    /**
     * Uses Jackson Streaming API to parse a json file with array of file objects
     * @param statistics {@link Statistics} object to be filled
     * @param file java.io.File object with the json file to be parsed
     * @param value Json property of {@link io.vital.app.entities.File} to work with
     */
    void parseFileEfficient(Statistics statistics, java.io.File file, String value);

    /**
     * Uses Jackson Streaming API to parse a folder with json files with an array of file objects
     * Uses {@link CustomJsonParser#parseFileEfficient(Statistics, File, String)}
     * @param folderPath Path to the folder with json files
     * @param value Json property of {@link io.vital.app.entities.File} to work with
     * @return returns a new filled Statistics object
     */
    Statistics parseFilesInFolderEfficient(String folderPath, String value);

    /**
     * Uses Jackson Streaming API to parse a folder with json files with an array of file objects concurrently
     * Uses {@link CustomJsonParser#parseFileEfficient(Statistics, File, String)}
     * Uses {@link java.util.concurrent.ExecutorService}
     * Uses {@link io.vital.app.concurrent.FileParseTask}
     * @param folderPath Path to the folder with json files
     * @param value Json property of {@link io.vital.app.entities.File} to work with
     * @param numOfThreads Number of threads a user wants to use to parse a folder of json files concurrently
     * @return returns a new filled Statistics object
     */
    Statistics parseFilesInFolderEfficientConcurrent(String folderPath, String value, int numOfThreads);

    /**
     * Writes a serialized XML object representation into a specified file location.
     * @param rootPath File location to write XML-serialized Java object
     * @param xml Object to serialize
     * @param value A json property the statistics object was formed by
     * @throws IOException
     */
    void prettyXmlPrintInFile(String rootPath, Object xml, String value) throws IOException;
}
