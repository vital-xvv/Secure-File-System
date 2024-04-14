package io.vital.app.concurrent;

import io.vital.app.entities.Statistics;
import io.vital.app.json.CustomJsonParser;

/**
 * This class represents a task of a json file parsing to be executed concurrently
 * @author Vitalii Huzii
 */
public class FileParseTask implements Runnable{
    private final CustomJsonParser parser;
    private final Statistics statistics;
    private final java.io.File file;
    /**
     * String value of a json property to parse for
     */
    private final String value;

    public FileParseTask(CustomJsonParser parser, Statistics statistics, java.io.File file, String value){
        this.parser = parser;
        this.statistics = statistics;
        this.file = file;
        this.value = value;
    }

    @Override
    public void run() {
        parser.parseFileEfficient(statistics, file, value);
    }
}
