package org.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
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
import org.vaadin.svgvis.WindRose;

@Route(layout = DefaultLayout.class)
@MenuItem(title = "Windrose", icon = VaadinIcon.PICTURE)
@Addon("svg-vizualizations")
public class WindRoseView extends VerticalLayout {

    public WindRoseView() {
        add("A simple wind rose visualisation built with the new SVG namespace support for Element API (Vaadin 25).");

        Paragraph clickInfo = new Paragraph("Click a sector to see details");
        add(clickInfo);

        WindRose windRose = new WindRose(200) {{
            setTitle("Two series with demo data");
            addSeries("Duration", NamedColor.GREEN, generateSampleWindData(16, 225));
            addSeries("Energy", NamedColor.DARKORANGE, generateSampleWindData(16, 0));
            setSectorClickListener(data -> {
                clickInfo.setText(String.format(
                        "Sector %d (%s, %d°): D=%.1f E=%.1f (%.1f%% %.1f%%)",
                        data.sectorIndex(),
                        data.directionLabel(),
                        data.centerDegrees(),
                        data.seriesValues().get(0),
                        data.seriesValues().get(1),
                        data.seriesPercentages().get(0),
                        data.seriesPercentages().get(1)
                ));
            });
            draw();
        }};
        add(windRose);
    }

    private double[] generateSampleWindData(int sectors, int weigthedAngle) {
        double[] data = new double[sectors];
        double degreesPerSector = 360.0 / sectors;

        for (int i = 0; i < sectors; i++) {
            double angle = i * degreesPerSector;
            double swDistance = Math.abs(angle - weigthedAngle);
            if (swDistance > 180) swDistance = 360 - swDistance;
            data[i] = Math.max(5, 100 - swDistance * 0.4 + Math.random() * 20);
        }
        return data;
    }
}
