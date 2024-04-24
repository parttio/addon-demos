package org.example.views.maplibre;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Component;
import org.vaadin.addons.maplibre.LineLayer;
import org.vaadin.addons.maplibre.LinePaint;
import org.vaadin.addons.maplibre.MapLibre;
import org.vaadin.addons.maplibre.Marker;
import org.vaadin.firitin.geolocation.Geolocation;
import org.vaadin.firitin.geolocation.GeolocationCoordinates;
import org.vaadin.firitin.geolocation.GeolocationErrorEvent;
import org.vaadin.firitin.geolocation.GeolocationEvent;
import org.vaadin.firitin.geolocation.GeolocationOptions;

import java.util.ArrayList;
import java.util.List;

public class MyPositionMarker implements Geolocation.UpdateListener, Geolocation.ErrorListener {
    static GeometryFactory gf = new GeometryFactory();

    private GeolocationCoordinates coords;
    private MapLibre map;
    private Marker marker;
    private Geolocation geolocation;
    private List<Coordinate> tailpoints = new ArrayList<>();
    private LineLayer tail;

    public MyPositionMarker(MapLibre map) {
        var options = new GeolocationOptions();
        options.setEnableHighAccuracy(true);
        geolocation = Geolocation.watchPosition(this, this, options);
        this.map = map;
    }

    @Override
    public void geolocationUpdate(GeolocationEvent geolocationEvent) {
        coords = geolocationEvent.getCoords();
        Coordinate coordinate = new Coordinate(coords.getLongitude(), coords.getLatitude(), 0);

        if (marker == null) {
            marker = map.addMarker(coords.getLongitude(), coords.getLatitude());
            marker.setColor("#009900");
            map.flyTo(marker.getGeometry());
        } else {
            // update position
            var p = gf.createPoint(coordinate);
            marker.setPoint(p);

            Double heading = geolocationEvent.getCoords().getHeading();
            if(heading != null) {
                map.setBearing(heading);
            }
            map.getViewPort().thenAccept(viewPort -> {
                // adjust if marker has moved out of viewport
                if(!viewPort.getBounds().contains(p)) {
                    map.flyTo(p);
                }
            });
        }
        tailpoints.add(coordinate);
        if (tailpoints.size() > 100) {
            tailpoints.remove(0);
        }
        if (tailpoints.size() == 2) {
            var ls = gf.createLineString(tailpoints.toArray(new Coordinate[0]));
            // create and start showing the tail
            tail = map.addLineLayer(ls, new LinePaint("#00ff00", 2.0));
        } else if (tailpoints.size() > 2) {
            // update tail
            tail.addCoordinates(tailpoints.size() == 100 ? 1 : 0, tailpoints.get(tailpoints.size() - 1));
        }
    }

    @Override
    public void geolocationError(GeolocationErrorEvent e) {
        Notification.show("Geolocation error: " + e);
    }

}
