package io.vital.app.concurrent;

import io.vital.app.entities.Statistics;
import io.vital.app.json.CustomJsonParser;

public class FileParseTask implements Runnable{
    private final CustomJsonParser parser;
    private final Statistics statistics;
    private final java.io.File file;
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
