package io.vital.app.statistics;

import io.vital.app.entities.Item;
import io.vital.app.entities.Statistics;
import io.vital.app.entities.File;
import java.util.List;

public class FileStatistics {

    public static Statistics getStatisticsByValue(List<File> files, String value, boolean sorted) {
        Statistics statistics = new Statistics();
        switch (value){
            case "size": {
                files.forEach(file -> statistics.addItem(new Item(file.getSize().toString())));
            } break;
            case "extension": {
                files.forEach(file -> statistics.addItem(new Item(file.getExtension())));
            } break;
            case "owner": {
                files.forEach(file -> statistics.addItem(new Item(file.getOwner())));
            } break;
            case "year_published": {
                files.forEach(file -> statistics.addItem(new Item(String.valueOf(file.getPublishYear()))));
            }
            default: {
                files.forEach(f -> f.getLanguages().forEach(l -> statistics.addItem(new Item(l.getPrettyName()))));
            }
        }

        if(sorted)
            statistics.sorted();

        return statistics;
    }

}
