package org.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.router.RouterLayout;
import org.vaadin.firitin.appframework.MainLayout;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;

@RoutePrefix("examples")
public class ExamplesLayout extends VerticalLayout implements RouterLayout {

    Anchor viewSource = new Anchor("", "View source");

    private static final String baseSourceUrl = "https://github.com/parttio/addon-demos/blob/main/src/main/java/%s.java";

    @Override
    public void showRouterLayoutContent(HasElement content) {
        String name = content.getClass().getName();
        String simpleName = content.getClass().getSimpleName();
        viewSource.setHref(baseSourceUrl.formatted(name.replace(".", "/")));
        add(new VHorizontalLayout().withExpanded(new H1(simpleName)).withComponent(viewSource).withFullWidth());
        RouterLayout.super.showRouterLayoutContent(content);
    }
}
