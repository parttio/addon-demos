package org.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.jsoup.safety.Safelist;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.tinymce.TinyMce;

@Route(layout = DefaultLayout.class)
@MenuItem(title = "TinyMce", icon = VaadinIcon.EDIT)
@Addon("tinymce-for-flow")
public class TinyMceView extends VerticalLayout {

    public TinyMceView() {
        TinyMce editor = new TinyMce();
        String originalValue = "This is a <b>bold</b> text with <i>italic</i> text.";
        editor.setValue(originalValue);

        Div rawHtml = new Div();

        Button showValue = new Button("show value", event -> {
            rawHtml.getElement().setProperty("innerHTML", editor.getValue());
        });

        add(editor, showValue, new H5("Value:"), rawHtml);
    }
}
