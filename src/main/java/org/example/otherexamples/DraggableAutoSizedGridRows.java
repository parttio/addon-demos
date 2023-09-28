package org.example.otherexamples;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDropEvent;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Route;
import org.example.ExamplesLayout;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Route(layout = ExamplesLayout.class)
public class DraggableAutoSizedGridRows extends VerticalLayout {
    public DraggableAutoSizedGridRows() {
        Grid<String> grid = getStringGrid("G1");
        Grid<String> grid2 = getStringGrid("G2");
        grid.addDropListener(event -> {
            handleDrop(event, grid2);
        });

        grid2.addDropListener(event -> {
            handleDrop(event, grid);
        });

        add(new VHorizontalLayout().withExpanded(grid,grid2));

    }

    private static void handleDrop(GridDropEvent<String> event, Grid<String> source) {
        Grid<String> targetGrid = event.getSource();
        String item = event.getDataTransferData("item").get();
        event.getDropTargetItem().ifPresentOrElse(target -> {
            if(event.getDropLocation() == GridDropLocation.ABOVE) {
                targetGrid.getListDataView().addItemBefore(item, target);
            } else {
                targetGrid.getListDataView().addItemAfter(item, target);
            }
        }, () ->{
            targetGrid.getListDataView().addItem(item);
        });
        source.getListDataView().removeItem(item);
    }

    private static Grid<String> getStringGrid(String g) {
        Grid<String> grid = new Grid<>();
        grid.addColumn(item -> item).setHeader("Str");
        grid.addColumn(item -> item.length()).setHeader("Length");
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            items.add(g + " Item " + i);
        }
        grid.setItems(items);
        grid.setAllRowsVisible(true);
        grid.setDragDataGenerator("item", item -> item);
        grid.setRowsDraggable(true);
        grid.setDropMode(GridDropMode.ON_TOP_OR_BETWEEN);
        return grid;
    }

}
