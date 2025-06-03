package org.example.views;

import com.fasterxml.jackson.databind.JavaType;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import in.virit.color.Color;
import in.virit.color.HexColor;
import in.virit.color.NamedColor;
import in.virit.color.RgbColor;
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
import org.parttio.SocketGravity;
import org.parttio.SocketType;
import org.vaadin.addons.parttio.colorful.RgbaColorPicker;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.firitin.rad.AutoForm;
import org.vaadin.firitin.rad.AutoFormContext;

import java.util.Arrays;

@Route(layout = DefaultLayout.class)
@MenuItem(title = "LeaderLine", icon = VaadinIcon.LINE_H)
@Addon("LeaderLine")
// TODO for some reason it seams like this is needed,for dev-mode, figure out why.
// Production bundle builds fine even without this (and line appears).
@Uses(LeaderLineFactory.class)
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
                setColor(new RgbColor(255, 0, 255));
                setStartPlug(PlugType.SQUARE);
                setEndPlug(PlugType.ARROW2);
                setDash(new Dash(true, 100.0));
                setEndPlugSize(3.0);
                setEndPlugColor(NamedColor.GREEN);
                setStartSocket(SocketType.BOTTOM);
                setDropShadow(new DropShadow("blue", 2.0, 2.0, 2.5));
                setStartLabel("Start label");
                setMiddleLabel("Middle label");
                setEndLabel("End label");
                setGradient(new Gradient(HexColor.of("#ff0000"), HexColor.of("#00ff00"), 0.0));
                setSize(10.0);
                setOutline(true);
                setOutlineSize(5.0);
                setOutlineColor(NamedColor.RED);
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
            // Use AutoForm to edit the options in the UI, then redraw the line
            AutoFormContext formContext = new AutoFormContext();
            formContext.withPropertyEditor(Color.class, RgbaColorPicker.class);
            formContext.withPropertyEditor(SocketGravity.class, SocketGravityField.class);

            AutoForm<LeaderLineOptions> autoForm = formContext.createForm(leaderLineOptions);
            autoForm.setSaveHandler(options -> {
                redrawLine();
            });
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

    /*
     * Custom field for SocketGravity, which can be int or int,int ...
     */
    public static class SocketGravityField extends CustomField<SocketGravity> {

        private VTextField textField = new VTextField()
                .withPlaceholder("""
                        "int, int" or "int"
                        """)
                ;

        public SocketGravityField() {
            add(textField);
            textField.addValueChangeListener(event -> {
                // Propagate the event to custom field level...
                if (event.isFromClient()) {
                    setModelValue(generateModelValue(), true);
                }
            });
        }

        @Override
        protected SocketGravity generateModelValue() {
            String value = textField.getValue();
            if (value == null || value.isEmpty()) {
                return null;
            }
            String[] values = value.split(",");
            if (values.length == 1) {
                return new SocketGravity(Integer.parseInt(values[0]));
            } else {
                return new SocketGravity(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
            }
        }

        @Override
        protected void setPresentationValue(SocketGravity newPresentationValue) {
            String str = Arrays.toString(newPresentationValue.values())
                    .replaceAll("\\[", "")
                    .replaceAll("]", "");

            textField.setValue(str);
        }
    }
}
