package io.vital.app.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@JacksonXmlRootElement(localName = "statistics")
public class Statistics {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    private final List<Item> statistics = new ArrayList<>();

    public void addItem(Item item) {
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


}