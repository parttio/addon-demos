package org.example.views;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.CssUtil;
import org.example.DefaultLayout;
import org.vaadin.addons.minicalendar.MiniCalendar;
import org.vaadin.addons.minicalendar.MiniCalendarVariant;
import org.vaadin.firitin.appframework.MenuItem;
import software.xdev.vaadin.daterange_picker.business.DateRangeModel;
import software.xdev.vaadin.daterange_picker.business.SimpleDateRange;
import software.xdev.vaadin.daterange_picker.business.SimpleDateRanges;
import software.xdev.vaadin.daterange_picker.ui.DateRangePicker;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Route(layout = DefaultLayout.class)
@MenuItem(title = "DateRange", icon = VaadinIcon.DATE_INPUT)
@Addon("DateRangePicker for Vaadin")
public class DateRangePickerView extends VerticalLayout {

    protected static final List<SimpleDateRange> DATERANGE_VALUES = Arrays.asList(SimpleDateRanges.allValues());

    protected final DateRangePicker<SimpleDateRange> dateRangePicker =
            new DateRangePicker<>(
                    () -> new DateRangeModel<>(LocalDate.now(), LocalDate.now(), SimpleDateRanges.WEEK));

    public DateRangePickerView() {
        add(dateRangePicker);
    }

}
