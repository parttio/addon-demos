package org.example.views.maplibre;

import org.springframework.stereotype.Component;
import org.vaadin.addons.maplibre.BaseMapConfigurer;
import org.vaadin.addons.maplibre.MapLibre;

/**
 * Providing a bean of type MapLibreBaseMapProvider configures base map
 * for those maps that don't have it explicitly defined.
 */
@Component
public class BaseMapProvider implements BaseMapConfigurer {

    @Override
    public void configure(MapLibre mapLibre) {
        mapLibre.initStyle("https://api.maptiler.com/maps/streets/style.json?key=G5n7stvZjomhyaVYP0qU");

    }
}
