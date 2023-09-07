package org.example.otherexamples;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Route;
import org.example.ExamplesLayout;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Route(layout = ExamplesLayout.class)
public class SortByRomanNumbers extends VerticalLayout {
    public SortByRomanNumbers() {
        Grid<String> grid = new Grid<>();
        grid.addColumn(s -> s).setHeader("roman")
                .setSortProperty("roman"); // makes column sortable & gives it a property name
        grid.addColumn(s -> romanToInt(s)).setHeader("int value");
        grid.addColumn(s -> s.length()).setHeader("Str length");

        List<String> values = Arrays.asList("IIC", "CXI", "XI", "IV", "V", "VI", "I", "II", "III", "X");
        // Don't do this! Basic setItems fails sorting naturally
        // grid.setItems(values);

        // Do this instead, using the "lazy loading API", you do the sorting!
        grid.setItems(q -> {
            Stream<String> stream = values.stream();
            // the sorting part
            if(!q.getSortOrders().isEmpty()) {
                // here we sort on one column only, in real life probably bit
                // more complex logic
                QuerySortOrder so = q.getSortOrders().get(0);
                stream = stream.sorted((a, b) ->
                        (so.getDirection() == SortDirection.ASCENDING) ?
                                romanToInt(a) - romanToInt(b) :
                                romanToInt(b) - romanToInt(a)
                );
            }
            // the lazy loading part, skip & limit
            return stream.skip(q.getOffset()).limit(q.getLimit());
        });


        add(grid);

    }


    int numValue(char rom) {
        if (rom == 'I')
            return 1;
        if (rom == 'V')
            return 5;
        if (rom == 'X')
            return 10;
        if (rom == 'L')
            return 50;
        if (rom == 'C')
            return 100;
        if (rom == 'D')
            return 500;
        if (rom == 'M')
            return 1000;
        return -1;
    }
    int romanToInt(String str) {
        int sum = 0;
        for (int i=0; i<str.length(); i++) {
            int s1 = numValue(str.charAt(i));
            if (i+1 <str.length()) {
                int s2 = numValue(str.charAt(i+1));
                if (s1 >= s2) {
                    sum = sum + s1;
                }
                else{
                    sum = sum - s1;
                }
            }
            else {
                sum = sum + s1;
            }
        }
        return sum;
    }
}
