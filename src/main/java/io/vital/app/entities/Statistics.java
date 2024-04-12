package io.vital.app.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "statistics")
public class Statistics {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    private final List<Item> statistics = new ArrayList<>();

    public static Statistics empty(){
        return new Statistics();
    }

    public void addItem(Item item) {
        if(item.getValue() == null) return;
        if (statistics.stream().noneMatch(t -> t.getValue().equals(item.getValue())))
            statistics.add(item);
        else statistics
                .stream()
                .filter(t -> t.getValue().equals(item.getValue()))
                .findFirst().get().incrementCount();
    }

    public void sorted() {
        statistics.sort(Item::compareTo);
    }

    public int size(){
        return statistics.size();
    }
}