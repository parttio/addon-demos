package org.example.views;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.CssUtil;
import org.example.DefaultLayout;
import org.vaadin.addons.minicalendar.MiniCalendar;
import org.vaadin.addons.minicalendar.MiniCalendarVariant;
import org.vaadin.firitin.appframework.MenuItem;

import java.time.LocalDate;
import java.util.List;

@Route(layout = DefaultLayout.class)
@MenuItem(title = "MiniCalendar", icon = VaadinIcon.EDIT)
@Addon("minicalendar-add-on-for-vaadin")
public class MinicalendarView extends VerticalLayout {

    public MinicalendarView() {

        injectFunkyCss();

        MiniCalendar miniCalendar = new MiniCalendar();
        miniCalendar.addThemeVariants(MiniCalendarVariant.HOVER_DAYS, MiniCalendarVariant.HIGHLIGHT_WEEKEND);

        miniCalendar.setValue(LocalDate.now());

        var now = LocalDate.now();
        var funkyDays = List.of(
                now.plusDays(2),
                now.plusDays(3),
                now.plusDays(4),
                now.plusDays(7),
                now.plusDays(8),
                now.plusDays(9),
                now.plusDays(10)
        );

        miniCalendar.setDayStyleProvider(day -> {
            if (funkyDays.contains(day)) {
                return List.of("funky", "bounce");
            }
            return null;
        });

        miniCalendar.addValueChangeListener(event -> {
            Notification.show("Value changed to " + event.getValue());
        });

        miniCalendar.addYearMonthChangeListener(event -> {
            Notification.show("Value changed to " + event.getValue());
        });

        add(miniCalendar);

    }

    private void injectFunkyCss() {
        CssUtil.inject("""
            @-webkit-keyframes bounce {
                0%, 20%, 50%, 80%, 100% {-webkit-transform: translateY(0);}
                40% {-webkit-transform: translateY(-30px);}
                60% {-webkit-transform: translateY(-15px);}
            }
            
            @keyframes bounce {
                0%, 20%, 50%, 80%, 100% {transform: translateY(0);}
                40% {transform: translateY(-30px);}
                60% {transform: translateY(-15px);}
            }
            
            .minicalendar .day.funky {
                color: var(--lumo-primary-contrast-color);
                -webkit-animation-duration: 1s;
                animation-duration: 1s;
                -webkit-animation-fill-mode: both;
                animation-fill-mode: both;
                background: linear-gradient(
                        90deg,
                        rgba(255, 0, 0, 1) 0%,
                        rgba(255, 154, 0, 1) 10%,
                        rgba(208, 222, 33, 1) 20%,
                        rgba(79, 220, 74, 1) 30%,
                        rgba(63, 218, 216, 1) 40%,
                        rgba(47, 201, 226, 1) 50%,
                        rgba(28, 127, 238, 1) 60%,
                        rgba(95, 21, 242, 1) 70%,
                        rgba(186, 12, 248, 1) 80%,
                        rgba(251, 7, 217, 1) 90%,
                        rgba(255, 0, 0, 1) 100%
                );
            }
            
            .bounce:hover {
                -webkit-animation-name: bounce;
                animation-name: bounce;
            }
        """);
    }

}
