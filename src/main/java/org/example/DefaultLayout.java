package org.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import org.vaadin.firitin.appframework.MainLayout;

public class DefaultLayout extends MainLayout {

    Anchor viewSource = new Anchor("", "View source");
    Anchor viewAddon = new Anchor("", "Install");

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
        String addonId = content.getClass().getAnnotation(Addon.class).value();
        viewAddon.setHref("https://vaadin.com/directory/component/%s".formatted(addonId));
        if(!viewSource.isAttached()) {
            HorizontalLayout links = new HorizontalLayout(viewSource, viewAddon);
            links.getStyle().setPosition(Style.Position.ABSOLUTE);
            links.getStyle().setRight("1em");
            links.getStyle().setTop("1em");
            addToNavbar(true, links);
        }
    }
}
