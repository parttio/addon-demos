package org.example.views.viritin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.example.data.DataService;
import org.example.data.domain.Person;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.grid.PagingGrid;
import org.vaadin.firitin.layouts.HorizontalFloatLayout;
import org.vaadin.firitin.util.PageVisibility;
import org.vaadin.firitin.util.webnotification.NotificationOptions;
import org.vaadin.firitin.util.webnotification.WebNotification;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Route(layout = DefaultLayout.class)
@MenuItem(icon = VaadinIcon.ALARM, parent = ViritinMenuGroup.class)
@Addon("flow-viritin")
public class WebNotificationsView extends VerticalLayout {

    private final WebNotification webNotification;
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private PageVisibility.Visibility visibility;

    public WebNotificationsView() {

        add(new RichText().withMarkDown("""
        # Web Notifications
        
        Web Notifications API allows you to show notifications via operating system's notification system.
        In certain cases, this gives better experience than standard Vaadin Notification component, e.g.
        when the application is not in focus.
        """));
        webNotification = WebNotification.get(); // Get the WebNotification instance, which bound to
        // the current UI. You can also use `WebNotification.get(yourUIInstance)` in more complex scenarios.

        // Check the permission status of Web Notifications, if not granted, request it,
        // else show the demo buttons to trigger notifications.
        webNotification.checkPermission().thenAccept(permission -> {
            if (permission == WebNotification.Permission.GRANTED) {
                Notification.show("Web Notification permission is already granted.");
                addDemoButtons();
                return;
            }

            if (permission == WebNotification.Permission.DENIED) {
                // Using Vaadin Notification to show Web Notifications permission denied :-)
                Notification.show("Web Notification permission is denied. You can change it in browser settings. Web" +
                        "app alone can't anymore request it.");
                add("Permission denied by the user. You can change it in browser settings. Web app alone can't request it anymore.");
            } else {
                Notification.show("Web Notification permission is default. You can request it to enable the demo.");
            }
            add(new VButton("requestPermission", e -> {
                webNotification.requestPermission(() -> {
                    Notification.show("Permissions granted. You can now try the demo buttons.");
                    addDemoButtons();
                    e.getSource().removeFromParent();
                }, () -> {
                    Notification.show("User denied permission.");
                });
            }));

        });
    }

    private void addDemoButtons() {
        add(new VButton("showNotification", e -> {
            webNotification.showNotification("Hello from Vaadin!");
        }));

        add(new VButton("showNotificationWithDelay (5 secs)", e -> {
            executorService.schedule(() -> {
                /*
                 * Note that this is happening in non UI thread, so modifying the UI directly
                 * is not allowed. Thus, we use `showNotificationAsync` method instead, which
                 * uses the `UI.access()` method to safely modify the UI. In case you are a big
                 * fan of spoiling your UI code with UI handling and UI.access() calls, you can
                 * also use the `webNotification.showNotification` method with the usual dance.
                 */
                webNotification.showNotificationAsync("Delayed notification from Vaadin!");
            }, 5, java.util.concurrent.TimeUnit.SECONDS);
        }));

        add(new VButton("notifications with 2 sec delay and custom settings", e -> {
            executorService.schedule(() -> {
                var options = new NotificationOptions();
                options.setBody("This is a custom notification body.");
                record StructuredData(String url, String status) {
                }
                // Note, MDN claims "data" is supported by all browsers, but I can't make it work in any 🤔
                options.setData(new StructuredData("https://vaadin.com", "success"));
                options.setSilent(false); // default varies by browser
                // There are many some options available, but many has browser differences, see the NotificationOptions class.
                webNotification.showNotificationAsync("Custom notification from Vaadin!", options);
            }, 2, java.util.concurrent.TimeUnit.SECONDS);
        }));

        add(new VButton("Web notification if page is visible and focused, else Vaadin Notification", e -> {
            UI ui = UI.getCurrent();
            executorService.schedule(() -> {
                switch (visibility) {
                    // The page is visible and focused, so we use the regular Vaadin notification, we can expect it
                    // is actively used by the user.
                    case VISIBLE -> ui.access(() -> Notification.show("\"Normal\" Vaadin Notification, page: " + visibility));
                    // Else we use the Web Notification, which is shown even if the browser tab is not focused or hidden.
                    case VISIBLE_NON_FOCUSED, HIDDEN -> webNotification.showNotificationAsync("Web, page: " + visibility);
                }
            }, 2, java.util.concurrent.TimeUnit.SECONDS);
        }));

        // Maintain the visibility state of the page, so we can use it in the button above.
        PageVisibility.get().addVisibilityChangeListener(v -> {
            visibility = v;
        });

    }
}
