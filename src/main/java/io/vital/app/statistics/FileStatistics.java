package io.vital.app.statistics;

import io.vital.app.entities.Item;
import io.vital.app.entities.Statistics;
import io.vital.app.entities.File;
import java.util.List;

public class FileStatistics {

    public static void getStatisticsByValue(Statistics statistics, File file, String value){
        switch (value) {
            case "size" ->
                    statistics.addItem(file.getSize() == null ? new Item() : new Item(String.valueOf(file.getSize())));
            case "extension" ->
                    statistics.addItem(new Item(file.getExtension()));
            case "owner" ->
                    statistics.addItem(new Item(file.getOwner()));
            case "year_published" ->
                    statistics.addItem(file.getPublishYear() == null ? new Item() :new Item(String.valueOf(file.getPublishYear())));
            default ->
                    file.getLanguages().forEach(lang -> statistics.addItem(new Item(lang.getPrettyName())));

        }
    }

    public static Statistics getStatisticsByValue(List<File> files, String value) {
        Statistics statistics = Statistics.empty();
        files.forEach(file -> getStatisticsByValue(statistics, file, value));
        return statistics;
    }
}
