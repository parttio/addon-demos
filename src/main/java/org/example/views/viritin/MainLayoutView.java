package org.example.views.viritin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.fields.CommaSeparatedStringField;
import org.vaadin.firitin.fields.DurationField;
import org.vaadin.firitin.fields.ElementCollectionField;
import org.vaadin.firitin.fields.EnumSelect;
import org.vaadin.firitin.form.BeanValidationForm;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Route(layout = DefaultLayout.class)
@MenuItem(icon = VaadinIcon.LAYOUT, parent = ViritinMenuGroup.class)
@Addon("flow-viritin")
public class MainLayoutView extends VerticalLayout {

    public MainLayoutView() {
        add(new RichText().withMarkDown("""
            # AppLayout with an automatic menu suitable for small to medium size applications
            
            Viritin provides a simple way to create a main layout and a top level navigation for your application.
            Nowadays the implementation is based on the AppLayout and SideNav components from the Vaadin core.
            
            This example uses this MainLayout component of which the DefaultLayout is a subclass.
            The menu is built automatically for views that define the DefaultLayout as their layout.
            *Add a view to your app -> it automatically appears in the menu.*
            Only views for which the user has access (role based check) are shown in the menu and
            dynamically registered routes are supported.
            
            To customize the menu (icons, order, custom title etc), you can use the *@MenuItem annotation*.
            
            In Vaadin 24.5 and later Vaadin ships a similar in nature @Menu annotation. Latest Viritin
            versions support both annotations, but to define hierarcy and some other more advanced features
            you need to use the Viritin specific @MenuItem annotation.
            """));
    }

}

