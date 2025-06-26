package org.example.views.viritin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import org.example.Addon;
import org.example.DefaultLayout;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.util.PageVisibility;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Route(layout = DefaultLayout.class)
@MenuItem(icon = VaadinIcon.ALARM, parent = ViritinMenuGroup.class)
@Addon("flow-viritin")
public class PageVisibilityView extends VerticalLayout {

    private final ScheduledFuture<?> future;
    private final Registration registration;
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private PageVisibility.Visibility visibility;

    public PageVisibilityView() {

        add(new RichText().withMarkDown("""
        # Page Visibility
        
        The Page Visibility API allows you to determine the visibility state of the page, i.e. whether it is currently
        hidden behind another tab. The Viritin Java API for PageVisibility extends the 
        [Page Visibility API](https://developer.mozilla.org/en-US/docs/Web/API/Page_Visibility_API) to provide also some
        hints if a visible page is focused or not (e.g. on top-most tab, but another application is active).
        
        These features can be used to improve the user experience, e.g. by showing notifications with different settings
        for hidden page or to optimise some heavy computation if page is hidden.
        
        The demo below disables an action related to background task if the page is not focused or hidden. Also
        it configures the notification to stay on the screen until the user closes it, if the page is not focused or hidden.
        Try switching to another tab or using another app and then returning to this view.
        """));

        Log log = new Log();

        // In some cases detecting the state once is enough
        PageVisibility.get().isVisible().thenAccept(v -> {
            visibility = v;
        });

        // Maintain the visibility state and reacting to changes via listener
        registration = PageVisibility.get().addVisibilityChangeListener(v -> {
            visibility = v;
            log.log("Page visibility changed: " + v);
            Notification notification = Notification.show("Page visibility changed: " + v);
            if (v == PageVisibility.Visibility.VISIBLE) {
                // By default, the notification is shown at bottom left and will disappear soonish
            } else {
                // If the page is not focused or is totally hidden, our notification would be lost
                // Configure it so that it stays on the screen until the user closes it
                notification.setDuration(-1);
                notification.add(new H5("Page was not focused or hidden"));
                notification.add(new Paragraph("This notification is configured to stay on the screen until you close it. " +
                        "Otherwise you would not see it if you stay in other app for more than couple of secs."));
                notification.add(new Button("Got it!", e -> notification.close()));
                // Also position it in the middle of the screen
                notification.setPosition(Notification.Position.MIDDLE);
                // Optionally you could make it dissapear automatically after a delay after
                // the page becomes visible again...
            }
        });

        UI ui = UI.getCurrent();
        future = executorService.scheduleWithFixedDelay(() -> {
            ui.access(() -> {
                if(visibility == PageVisibility.Visibility.VISIBLE) {
                    // Only do this if the page is visible and focused
                    log.log("periodic task, visibility: " + visibility);
                } else {
                    // You could do something else still, e.g. based on last request timestamp
                    Instant lastRequest = Instant.ofEpochMilli(ui.getSession().getLastRequestTimestamp());
                    System.out.println("periodic task, visibility: " + visibility + ", last request: " + lastRequest);
                }
            });

        }, 1000, 1000, java.util.concurrent.TimeUnit.MILLISECONDS);

        add(log);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        registration.remove();
        future.cancel(true);
    }


    static class Log extends Composite<VerticalLayout> {
        public Log() {
            getStyle().setPadding("1em");
            getStyle().setBorder("1px solid #ccc");
            getContent().setSpacing(false);
        }

        public void log(String message) {
            getContent().add(new Pre(LocalDateTime.now() + ": " +message){{
                getStyle().setMargin("0");
            }});
            // Keep the log size reasonable
            if(getContent().getComponentCount() > 10) {
                getContent().getChildren().findFirst().ifPresent(Component::removeFromParent);
            }
        }
    }

}
