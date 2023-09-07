package org.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.example.data.DataService;
import org.example.data.domain.Person;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.components.grid.PagingGrid;

@Route(layout = DefaultLayout.class)
@MenuItem(icon = VaadinIcon.EDIT)
@Addon("flow-viritin")
public class PagingGridView extends VerticalLayout {

    public PagingGridView(DataService service) {

        add(new RichText().withMarkDown("""
        # PagingGrid from Viritin add-on
        
        PagingGrid provides "traditional paging" as an alternative to the
        seamless paging provided by the Vaadin Grid component. Sometimes
        needed for various reasons 🤷‍.
        
        """));

        final PagingGrid<Person> table = new PagingGrid<>(Person.class);
        table.setColumns("id", "firstName", "lastName", "age");

        // Define results with a simpler data provider API, that just gives you page to
        // request
        table.setPagingDataProvider((page, pageSize) -> {
            // This is demo specific line, normally, e.g. with spring data, page number is
            // enough
            int start = (int) (page * table.getPageSize());
            // Optional, sorting
            if (!table.getSortOrder().isEmpty()) {
                GridSortOrder<Person> sortOrder = table.getSortOrder().get(0);
                String propertyId = sortOrder.getSorted().getKey();
                boolean asc = sortOrder.getDirection() == SortDirection.ASCENDING;
                return service.findPersons(start, pageSize, propertyId, asc);
            }
            return service.findPersons(start, pageSize);
        });

        // Optional
        // If you know, or some further long running task can detect the size of
        // results, it can be defined later:
        Button b = new Button("Define size to 1000", e -> {
            table.setTotalResults(1000);
        });
        // Optional
        // If you know, or some further long running task can detect the size of
        // results, it can be defined later:
        Button b2 = new Button("Define size to 90", e -> {
            table.setTotalResults(90);
        });

        Select<PagingGrid.PaginationBarMode> select = new Select<>();
        select.setItems(PagingGrid.PaginationBarMode.values());
        select.setValue(PagingGrid.PaginationBarMode.TOP);
        select.addValueChangeListener(e -> {
            table.setPaginationBarMode(e.getValue());
        });

        add(table, b, b2, select);

    }

}
