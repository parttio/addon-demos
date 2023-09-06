package org.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.vaadin.firitin.components.RichText;

@Route(value = "", layout = DefaultLayout.class)
@Addon("flow-viritin")
public class AboutView extends VerticalLayout {

    public AboutView() {
        add(new RichText().withMarkDown("""
# Welcome }>  add-on demo project

This is a demo project showcasing various Vaadin add-ons and hosted by "Team Parttio", but add-on examples are not limited to add-ons from Parttio.

For example this welcome screen is using RichText component from [Viritin](https://vaadin.com/directory/component/firitin) add-on. Quick links to the source code of examples can be found on top right corner of the main layout.

If you want to contribute your add-on example, please make a PR to the [GitHub project](https://github.com/parttio/addon-demos).

                """));
        Button button = new Button("Click me",
                event -> add(new Paragraph("Clicked!")));
        add(button);
    }
}
