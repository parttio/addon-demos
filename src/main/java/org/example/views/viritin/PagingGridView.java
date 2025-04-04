package org.example.views.viritin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.example.data.DataService;
import org.example.data.domain.Person;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.components.grid.PagingGrid;
import org.vaadin.firitin.layouts.HorizontalFloatLayout;

@Route(layout = DefaultLayout.class)
@MenuItem(icon = VaadinIcon.GRID, parent = ViritinMenuGroup.class)
@Addon("flow-viritin")
public class PagingGridView extends VerticalLayout {

    private final PersonPagingGrid table = new PersonPagingGrid();
    private final DataService dao;

    public PagingGridView(DataService service) {
        this.dao = service;

        add(new RichText().withMarkDown("""
        # PagingGrid from Viritin add-on
        
        PagingGrid provides "traditional paging" as an alternative to the
        seamless paging provided by the Vaadin Grid component. Sometimes
        needed for various reasons 🤷‍.
        
        """));

        add(new FirstNameFilter(), table);

        table.list(""); // Initial load with no filter


        // options/democonfigurations
        add(new HorizontalFloatLayout(){{
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
            select.setLabel("Pagination bar mode");
            select.setItems(PagingGrid.PaginationBarMode.values());
            select.setValue(PagingGrid.PaginationBarMode.TOP);
            select.addValueChangeListener(e -> {
                table.setPaginationBarMode(e.getValue());
            });

            add(new FirstNameFilter(), table, new HorizontalFloatLayout(b, b2, select));
        }});


    }


    private class PersonPagingGrid extends PagingGrid<Person> {

        public PersonPagingGrid() {
            super(Person.class);
            setColumns("id", "firstName", "lastName", "age");
        }

        public void list(String filter) {
            table.setPagingDataProvider((page, pageSize) -> {
                // This is demo specific line, normally, e.g. with spring data, page number is
                // enough
                int start = (int) (page * table.getPageSize());
                String sortProperty;
                boolean asc = true;
                // Optional, sorting
                if (!table.getSortOrder().isEmpty()) {
                    GridSortOrder<Person> sortOrder = table.getSortOrder().get(0);
                    sortProperty = sortOrder.getSorted().getKey();
                    asc = sortOrder.getDirection() == SortDirection.ASCENDING;
                } else {
                    // sort by id for stability if nothing else defined
                    sortProperty = "id";
                }
                return dao.findPersons(filter, start, pageSize, "id", true);
            });
        }

    }

    private class FirstNameFilter extends TextField {
        {
            setValueChangeMode(ValueChangeMode.LAZY);
            setClearButtonVisible(true);
            setPlaceholder("Filter by first name");

            addValueChangeListener(e -> {
                String filter = e.getValue();
                // Define results with a simpler data provider API, that just gives you page to
                // request
                table.list(filter);
            });

        }
    }
}
