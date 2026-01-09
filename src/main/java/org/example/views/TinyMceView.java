package org.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.hugerte.HugeRte;

@Route(layout = DefaultLayout.class)
@MenuItem(title = "HugeRte", icon = VaadinIcon.EDIT)
@Addon("hugerte-for-flow")
public class TinyMceView extends VerticalLayout {

    public TinyMceView() {
        add(new RichText().withMarkDown("""
        # HugeRTE editor
        
        HugeRTE is a platform independent web based Javascript HTML WYSIWYG editor. It is a fork of more commonly known
        TinyMCE editor, which now has less liberal license.
        
        See more configuration options from [add-on tests](https://github.com/parttio/hugerte-for-flow/tree/master/src/test/java/org/vaadin/hugerte).
        """));

        HugeRte editor = new HugeRte();
        String originalValue = "This is a <b>bold</b> text with <i>italic</i> text.";
        editor.setValue(originalValue);
        editor.setWidth("100%");

        Div rawHtml = new Div();
        rawHtml.setWidth("100%");

        Button showValue = new Button("show value", event -> {
            rawHtml.getElement().setProperty("innerHTML", editor.getValue());
        });

        add(new VHorizontalLayout(editor, new VerticalLayout(
                        showValue,
                        new H5("Value:"),
                        rawHtml
                )
        ).withFullWidth());
    }
}
