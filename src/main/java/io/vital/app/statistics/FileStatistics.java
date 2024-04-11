package io.vital.app.statistics;

import io.vital.app.entities.Item;
import io.vital.app.entities.Statistics;
import io.vital.app.entities.File;
import java.util.List;

public class FileStatistics {

    public static Statistics getStatisticsByValue(List<File> files, String value) {
        Statistics statistics = new Statistics();
        switch (value) {
            case "size" ->
                    files.forEach(file -> statistics.addItem(new Item(file.getSize().toString())));
            case "extension" ->
                    files.forEach(file -> statistics.addItem(new Item(file.getExtension())));
            case "owner" ->
                    files.forEach(file -> statistics.addItem(new Item(file.getOwner())));
            case "year_published" ->
                    files.forEach(file -> statistics.addItem(new Item(String.valueOf(file.getPublishYear()))));
            default ->
                    files.forEach(f -> f.getLanguages().forEach(l -> statistics.addItem(new Item(l.getPrettyName()))));
        }

        return statistics;
    }

    public static void getStatisticsByValue(Statistics statistics, File file, String value){
        switch (value) {
            case "size" ->
                    statistics.addItem(new Item(file.getSize().toString()));
            case "extension" ->
                    statistics.addItem(new Item(file.getExtension()));
            case "owner" ->
                    statistics.addItem(new Item(file.getOwner()));
            case "year_published" ->
                    statistics.addItem(new Item(String.valueOf(file.getPublishYear())));
            default ->
                    file.getLanguages().forEach(lang -> statistics.addItem(new Item(lang.getPrettyName())));

        }
    }

}
