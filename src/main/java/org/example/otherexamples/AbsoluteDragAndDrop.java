package org.example.otherexamples;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import in.virit.color.NamedColor;
import org.example.ExamplesLayout;
import org.vaadin.firitin.components.html.VDiv;

@Route(layout = ExamplesLayout.class)
public class AbsoluteDragAndDrop extends VDiv implements DropTarget<VDiv> {
    {
        // Configure styles for absolutely positioned canvas
        getStyle().setPosition(Style.Position.RELATIVE);
        getStyle().setBackgroundColor(NamedColor.LIGHTYELLOW.toRgbColor());
        getStyle().setOverflow(Style.Overflow.HIDDEN);
        setHeight("50vh");
        setWidth("50vw");

        // Add a draggable brick, see that separately
        Brick brick = new Brick();
        add(brick);

        // Make the canvas accept drops
        setActive(true);
        addDropListener(event -> {
            // The default Vaadin drop listener, well, pretty useless in this case
            // You could get the componet out of the event
            Component component = event.getDragSourceComponent().get();
            Notification.show("It dropped! " + component.getClass().getSimpleName());
        });

        // overridden DOM event handler that can read the event details
        getElement().addEventListener("drop", e -> {
                    var evt = e.getEventData(EventDetails.class);
                    int dx = evt.clientX() - brick.getStartX();
                    int dy = evt.clientY() - brick.getStartY();
                    brick.moveBy(dx, dy);
                    Notification.show("It dropped here too and now really moved.");
                })
                // TODO figure out how I'm holding automatic DTO mapping wrong...
                .addEventData("event.clientX")
                .addEventData("event.clientY");

    }

    // DragEvent extends MouseEvent...
    // Minimal DTO for MouseEvent details from D&D events
    record EventDetails(@JsonProperty("event.clientX") int clientX, @JsonProperty("event.clientY")int clientY) {
    }

    public class Brick extends VDiv implements DragSource {
        private int startX;
        private int startY;

        private int offsetX = 0;
        private int offsetY = 0;

        {
            getStyle().setPosition(Style.Position.ABSOLUTE);
            getStyle().setPadding("1em");
            getStyle().setBackgroundColor(NamedColor.PINK);
            setDraggable(true);
            setText("DragMe");

            // Basic dragstart in V10+ don't support mouse coordinates so need a custom
            // event handler to save exact start coordinates for perfect drop location
            getElement().addEventListener("dragstart", e -> {
                var evt = e.getEventData(EventDetails.class);
                startX = evt.clientX();
                startY = evt.clientY();
            })
                    .addEventData("event.clientX")
                    .addEventData("event.clientY");
        }

        public int getStartX() {
            return startX;
        }

        public int getStartY() {
            return startY;
        }

        public void moveBy(int dx, int dy) {
            offsetX = offsetX + dx;
            offsetY = offsetY + dy;
            getStyle().setLeft(offsetX + "px");
            getStyle().setTop(offsetY + "px");
        }
    }

}
