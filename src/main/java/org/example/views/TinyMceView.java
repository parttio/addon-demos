package org.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.jsoup.safety.Safelist;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.tinymce.TinyMce;

@Route(layout = DefaultLayout.class)
@MenuItem(title = "TinyMce", icon = VaadinIcon.EDIT)
@Addon("tinymce-for-flow")
public class TinyMceView extends VerticalLayout {

    public TinyMceView() {
        add(new RichText().withMarkDown("""
        # TinyMCE editor
        
        TinyMCE is a platform independent web based Javascript HTML WYSIWYG editor control released as Open Source under LGPL.
        
        See more configuration options from [add-on tests](https://github.com/parttio/tinymce-for-flow/tree/master/src/test/java/org/vaadin/tinymce) or [TinyMCE documentation](https://www.tiny.cloud/docs/configure/).
        """));

        TinyMce editor = new TinyMce();
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
