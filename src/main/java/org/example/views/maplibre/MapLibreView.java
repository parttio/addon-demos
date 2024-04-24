package org.example.views.maplibre;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.vaadin.addons.maplibre.MapLibre;
import org.vaadin.addons.maplibre.Marker;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.geolocation.Geolocation;

import java.net.URI;
import java.net.URISyntaxException;

@Route(layout = DefaultLayout.class)
@MenuItem(title = "MapLibreGL JS", icon = VaadinIcon.MAP_MARKER)
@Addon("maplibregl--add-on")
public class MapLibreView extends VerticalLayout {

    private Marker yourPosition;

    public MapLibreView() {
        new VHorizontalLayout()
                .withComponent(new Span("Hello"))
                .space()
                .withComponent(new Span("World"))
        ;


        add(new RichText().withMarkDown("""
                # OpenStreetMap via MapTiler
                
                Many sources provide vector tiles these days. MapLibre also supports old school raster tiles, but why would one use those in 2023 😎 This example contains a simple vector basemap with a marker at Vaadin HQ and a random polygon plotted around it.
                Some action buttons to show basic API of the component.
                
                If you want to use MapTiler base layer in your own project, you need to [sign up for a free account](https://cloud.maptiler.com/).
                Then you can create a new API key and use it in the URI. My API key is restricted to localhost based testing.
                
                """));
        try {
            MapLibre map = new MapLibre(new URI("https://api.maptiler.com/maps/streets/style.json?key=G5n7stvZjomhyaVYP0qU"));
            map.setHeight("400px");
            map.setWidth("100%");
            map.addMarker(22.300, 60.452).withPopup("Hello from Vaadin!");

            Polygon polygon = (Polygon) new WKTReader().read("POLYGON((22.290 60.428, 22.310 60.429, 22.31 60.47, 22.28 60.47, 22.290 60.428))");

//            map.addFillLayer(polygon, "{'fill-color': 'red', 'fill-opacity': 0.2}");

            map.setCenter(22.300, 60.452);
            map.setZoomLevel(13);
            add(map);

            Button b = new Button("Zoom to content");
            b.addClickListener(e -> {
                Geometry g = polygon;
                if(yourPosition != null) {
                    g = g.union(yourPosition.getGeometry());
                }
                map.fitTo(g, 0.01);
            });
            Button seeWorld = new Button("See the world (flyTo(0,0,0)");
            seeWorld.addClickListener(e -> {
                map.flyTo(0,0,0.0);
            });
            Button plotYourself = new Button("Geolocate once");
            plotYourself.addClickListener(e -> {
                Geolocation.getCurrentPosition(position -> {
                    yourPosition = map.addMarker(position.getCoords().getLongitude(), position.getCoords().getLatitude());
                    map.flyTo(yourPosition.getGeometry(), 13);
                }, error -> {
                    System.out.println("Error: ");
                });
            });
            Button track = new Button("Track, follow & rotate (try on iphone & walk)");
            track.addClickListener(e -> {
                new MyPositionMarker(map);
                track.setEnabled(false);
            });
            FlexLayout flexLayout = new FlexLayout();
            flexLayout.setFlexDirection(FlexLayout.FlexDirection.ROW);
            flexLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
            flexLayout.getStyle().set("column-gap", "1em");
            flexLayout.add(b, seeWorld, plotYourself, track);
            add(flexLayout);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
