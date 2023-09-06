package org.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.dom.Style;
import org.vaadin.firitin.appframework.MainLayout;

public class DefaultLayout extends MainLayout {

    Anchor viewSource = new Anchor("", "View source...");

    @Override
    protected String getDrawerHeader() {
        return "}> add-on demos";
    }


    private static final String baseSourceUrl = "https://github.com/parttio/addon-demos/blob/main/src/main/java/%s.java";

    @Override
    public void setContent(Component content) {
        super.setContent(content);
        String name = content.getClass().getName();
        viewSource.setHref(baseSourceUrl.formatted(name.replace(".", "/")));
        if(!viewSource.isAttached()) {
            viewSource.getStyle().setPosition(Style.Position.ABSOLUTE);
            viewSource.getStyle().setRight("1em");
            viewSource.getStyle().setTop("1em");
            addToNavbar(true, viewSource);
        }
    }
}
