package org.example.views;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.vaadin.addons.pulltorefresh.PullToRefreshScroller;
import org.vaadin.firitin.appframework.MenuItem;

import java.time.LocalTime;

@Route(layout = DefaultLayout.class)
@Addon("pulltorefresh--add-on")
@MenuItem(title = "Pull to refresh", icon = VaadinIcon.TOUCH)
public class PulltoRefreshView extends VerticalLayout {

    public PulltoRefreshView() {

        add(new Paragraph("Try with touch device and do a swipe down gesture on the scroller"));

        VerticalLayout layout = new VerticalLayout();
        for (int i = 0; i < 100; i++) {
            layout.add(new Paragraph("This is a paragraph " + i + " in scroller."));
        }
        Scroller scroller = new PullToRefreshScroller(() -> {
            layout.addComponentAsFirst(new Paragraph("Refreshed at " + LocalTime.now()));
        });
        scroller.setContent(layout);

        scroller.setHeight("50vh");
        add(scroller);


    }
}
