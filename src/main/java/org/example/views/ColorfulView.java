package org.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import in.virit.color.HslColor;
import in.virit.color.NamedColor;
import in.virit.color.RgbColor;
import org.example.Addon;
import org.example.DefaultLayout;
import org.vaadin.addons.parttio.colorful.RgbaColorPicker;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.html.VParagaph;

@Route(layout = DefaultLayout.class)
@MenuItem(title = "Colorful", icon = VaadinIcon.PICTURE)
@Addon("Colorful")
public class ColorfulView extends VerticalLayout {

    public ColorfulView() {
        add("A simple wrapper for React Colorful JS component, which is a color picker.");

        RgbaColorPicker rgbaColorPicker = new RgbaColorPicker();
        rgbaColorPicker.setLabel("RGBA Color Picker");
        add(rgbaColorPicker);

        VParagaph colorDisplay = new VParagaph(){{
            setText("Color display");
            getStyle().setWidth("200px");
            getStyle().setHeight("120px");
            getStyle().set("border", "1px solid black");
            getStyle().setWhiteSpace(Style.WhiteSpace.PRE);
            getStyle().setFontWeight(Style.FontWeight.BOLD);
        }};
        add(colorDisplay);

        rgbaColorPicker.addValueChangeListener(event -> {
            RgbColor color = event.getValue().toRgbColor();
            colorDisplay.getStyle().setBackgroundColor(color);

            colorDisplay.setText("Color:\n" + color.toString() + "\n" + color.toHslColor() + "\n" + color.toHexColor());
            HslColor hslColor = color.toHslColor();
            if(hslColor.l() < 50) {
                colorDisplay.getStyle().setColor(NamedColor.WHITE);
            } else {
                colorDisplay.getStyle().setColor(NamedColor.BLACK);
            }
        });
        rgbaColorPicker.setValue(new RgbColor(255, 0, 0, 0.5));

        add(new Button("Set color to green", e -> {
            rgbaColorPicker.setValue(NamedColor.GREEN.toRgbColor());
        }));

    }
}
