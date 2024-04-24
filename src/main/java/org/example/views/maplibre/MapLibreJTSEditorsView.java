package org.example.views.maplibre;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.vaadin.addons.maplibre.PointField;
import org.vaadin.addons.maplibre.PolygonField;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;

import java.time.LocalDate;

@Route(layout = DefaultLayout.class)
@MenuItem(title = "MapLibre JTS editors", icon = VaadinIcon.MAP_MARKER)
@Addon("maplibregl--add-on")
public class MapLibreJTSEditorsView extends VerticalLayout {

    // Form fields
    PolygonField polygon = new PolygonField("Edit polygon")
            .withAllowCuttingHoles(true);
    PointField point = new PointField("Point");
    DatePicker localDate = new DatePicker("LocalDate");

    Pre preview = new Pre();

    // DTO containen JTS data types for geospatial features
    public static class GeospatialDto {
        LocalDate localDate;
        Point point;
        Polygon polygon;

        public LocalDate getLocalDate() {
            return localDate;
        }

        public void setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
        }

        public Point getPoint() {
            return point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public Polygon getPolygon() {
            return polygon;
        }

        public void setPolygon(Polygon polygon) {
            this.polygon = polygon;
        }
    }

    public MapLibreJTSEditorsView() {

        add(new RichText().withMarkDown("""
                # Editing JTS geometry data types with MapLibre
                
                Most GIS libraries in Java utilize JTS data types in their core. 
                Like e.g. Hibernate's spatial features. The MapLibre add-on contains editor components that you can directly bind to your DTOs with Vaadin's Binder, just like you'd be editing String with TextField or LocalDate with DatePicker. Example here edits DTO with LocalDate, Point & Polygon. There is also editor for LineString.
                """));

        // Normal Vaadin data binding
        var binder = new Binder<>(GeospatialDto.class);
        binder.bindInstanceFields(this);

        add(localDate);
        point.setSizeFull();
        polygon.setSizeFull();
        add(new VHorizontalLayout(point, polygon).withFullWidth().withHeight("300px"));
        add(new Button("Show DTO", e-> {
            GeospatialDto dto = binder.getBean();
            preview.setText(
            """
                datetime: %s
                point: %s
                polygon: %s
            """.formatted(dto.getLocalDate(), dto.getPoint(), dto.getPolygon()));
            preview.scrollIntoView();
        }));
        add(preview);

        // Create DTO with some test data
        var object = new GeospatialDto();
        object.setLocalDate(LocalDate.now());
        var wktReader = new WKTReader();
        try {
            object.setPoint((Point) wktReader.read("POINT(22 60)"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        binder.setBean(object);

    }
}
