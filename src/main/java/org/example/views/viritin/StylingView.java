package org.example.views.viritin;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import in.virit.color.NamedColor;
import in.virit.color.RgbColor;
import org.example.Addon;
import org.example.DefaultLayout;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.button.DefaultButton;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.util.VStyle;
import org.vaadin.firitin.util.style.LumoProps;

@Route(layout = DefaultLayout.class)
@MenuItem(icon = VaadinIcon.PICTURE, parent = ViritinMenuGroup.class)
@Addon("flow-viritin")
public class StylingView extends VVerticalLayout {

    public StylingView() {
        add(new H1("Helpers to make small style/visual adjustments"));

        add(new H2("VStyle, Style implementation with extra powers"));

        Grid<String> trivialGridWithSelectedRow = new Grid<>() {{
            addColumn(s -> s).setHeader("String");
            setItems("Foo", "Bar", "Car");
            asSingleSelect().setValue("Bar");
        }};

        add(trivialGridWithSelectedRow);

        var nonBoundStyle = new VStyle() {{
            // VStyle has some of the color methods overridden with properly typed Color objects
            setBackgroundColor(new RgbColor(0, 177, 0)); // Greenish
            setFontWeight(FontWeight.BOLD);
        }};
        // Note, this adds these styles to inline style attribute of the element
        // In this case if you select a different row, the style will not be moved to the new row.
        // If that is desired, use injectWithSelectors(String...) that injects as CSS to the host page
        nonBoundStyle.applyToShadowRoot(trivialGridWithSelectedRow, "td[aria-selected=true]");



        add(new H2("Lumo properties"));

        // Below, within a div element, we re-define primary color to pink,
        // which overrides the default blue color of a primary button.
        add(new Div() {{
            LumoProps.PRIMARY_COLOR.define(this, NamedColor.DARKMAGENTA.name());
            add(new DefaultButton("I'm primary button with PINK derived from LumoProps"));

        }});


    }

}

