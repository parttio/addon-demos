package org.example.views.viritin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.html.VImage;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.layouts.HorizontalFloatLayout;
import org.vaadin.firitin.util.fullscreen.FullScreen;

@Route(layout = DefaultLayout.class)
@MenuItem(icon = VaadinIcon.EXPAND_FULL, parent = ViritinMenuGroup.class)
@Addon("flow-viritin")
public class FullscreenView extends VVerticalLayout {
    public FullscreenView() {
        add(new Markdown("""
                # Fullscreen API
                
                Browsers (pretty much all but not iPhone, but on mobile you should go PWA anyways 🤷‍♂️) 
                support [Fullscreen API](https://developer.mozilla.org/en-US/docs/Web/API/Fullscreen_API) to allow web 
                applications to present content in full screen mode. Viritin provides a simple way to use this API from Java
                and tackles a couple of problems Vaadin apps currently have with fullscreen mode.
                """));

        FullScreen.fullScreenAvailable().thenAccept(available -> {
            if (!available) {
                Notification.show("Fullscreen API is not available in this browser. " +
                        "You are most likely using iPhone. In a real app you should probably hide widgets providing fullscreen functionality.")
                        .setDuration(0);
            }
        });

        add(new HorizontalFloatLayout(
                new Button("Fullscreen the app", event -> {
                    FullScreen.requestFullscreen();
                }),
                new Button("Fullscreen the view", event -> {
                    FullScreen.requestFullscreen(FullscreenView.this);
                }),
                new Button("Exit fullscreen (or use escape)", event -> {
                    FullScreen.exitFullscreen();
                })
        ));
        add(new Paragraph("Click the image below to enter fullscreen mode with the photo only."));
        var photo = new VImage("/view.jpeg", "view").withFullWidth();
        photo.addClickListener(event -> {
            FullScreen.isFullscreen().thenAccept(isFullscreen -> {
                if (isFullscreen) {
                    FullScreen.exitFullscreen();
                } else {
                    FullScreen.requestFullscreen(photo);
                    Notification.show("Click the image again to exit fullscreen mode or press Escape key.", 3000, Notification.Position.MIDDLE);
                }
            });
        });


        add(photo);

    }
}
