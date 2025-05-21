package org.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.parttio.Dash;
import org.parttio.DropShadow;
import org.parttio.Gradient;
import org.parttio.LeaderLine;
import org.parttio.LeaderLineFactory;
import org.parttio.LeaderLineOptions;
import org.parttio.PathType;
import org.parttio.PlugType;
import org.parttio.SocketType;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.rad.AutoForm;
import org.vaadin.firitin.rad.AutoFormContext;

@Route(layout = DefaultLayout.class)
@MenuItem(title = "LeaderLine", icon = VaadinIcon.LINE_H)
@Addon("LeaderLine")
public class LeaderLineView extends VerticalLayout {

    private final AbsoluteButton button2;
    private final AbsoluteButton button1;
    private LeaderLine leaderLine;
    private LeaderLineOptions leaderLineOptions;

    public LeaderLineView() {
        add(new RichText().withMarkDown("""
                # LeaderLine - connecting two components
                """));

        button1 = new AbsoluteButton("Button start");
        button2 = new AbsoluteButton("Button end");

        add(button1, button2);

        leaderLine = LeaderLineFactory.drawLine(button1, button2);

        add(new Button("Random places", event -> {
            button1.randomizePosition();
            button2.randomizePosition();
            leaderLine.position();
        }));

        add(new Button("Weird styling", e -> {
            leaderLineOptions = new LeaderLineOptions() {{
                setColor("#ff00ff");
                setStartPlug(PlugType.SQUARE);
                setEndPlug(PlugType.ARROW2);
                setDash(new Dash(true, 100.0));
                setEndPlugSize(3.0);
                setEndPlugColor("green");
                setStartSocket(SocketType.BOTTOM);
                setDropShadow(new DropShadow("blue", 2.0, 2.0, 2.5));
                setStartLabel("Start label");
                setMiddleLabel("Middle label");
                setEndLabel("End label");
                setGradient(new Gradient("#ff0000", "#00ff00", 0.0));
                setSize(10.0);
                setOutline(true);
                setOutlineSize(5.0);
                setOutlineColor("red");
                setPath(PathType.ARC);
            }};
            redrawLine();
        }));
        add(new Button("Default styling", e -> {
            leaderLineOptions = new LeaderLineOptions();
            redrawLine();
        }));

        add(new Button("Edit options", e -> {
            if (leaderLineOptions == null) {
                leaderLineOptions = new LeaderLineOptions();
            }
            AutoFormContext formContext = new AutoFormContext();
            AutoForm<LeaderLineOptions> autoForm = formContext.createForm(leaderLineOptions);
            autoForm.setSaveHandler(options -> {
                redrawLine();
            });
            // TODO enhance autoform to support configs
            autoForm.openInDialog();
        }));

        getStyle().setPosition(Style.Position.RELATIVE);
    }

    private void redrawLine() {
        if (leaderLine != null) {
            leaderLine.remove();
        }
        leaderLine = LeaderLineFactory.drawLine(button1, button2, leaderLineOptions);
    }

    class AbsoluteButton extends Button {
        public AbsoluteButton(String text) {
            super(text);
            getStyle().setPosition(Style.Position.ABSOLUTE);
            randomizePosition();
        }

        public void randomizePosition() {
            getStyle().setTop((100 + Math.random() * 400) + "px");
            getStyle().setLeft((200 + Math.random() * 400) + "px");
        }
    }
}
