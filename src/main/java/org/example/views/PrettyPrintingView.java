package org.example.views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.example.data.domain.Address;
import org.example.data.domain.ComplexPerson;
import org.example.data.domain.Person;
import org.jetbrains.annotations.NotNull;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.layouts.HorizontalFloatLayout;
import org.vaadin.firitin.rad.PrettyPrinter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Route(layout = DefaultLayout.class)
@MenuItem(icon = VaadinIcon.PRINT)
@Addon("flow-viritin")
public class PrettyPrintingView extends VVerticalLayout {

    public PrettyPrintingView() {
        add(new H1("Visualize Object Structures on the Screen with Oneliner"));

        Person person = getPerson();

        add(new RichText().withMarkDown("""
                ## The old hack that we all have used...
                                
                ..., pre-formatted pretty printed json serialization, that I want to get rid of:
                                
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dto);
                """));

        add(new Pre(toPrettyJson(person)));

        add(new Paragraph("Problems: ugly presentation (at least if you ask non-developers), needs sometimes extra configurations, like in example with the circular reference."));


        add(new RichText().withMarkDown("""
                ## PrettyPrinter, basic usage
                
                PrettyPrinter utility class creates a more human readable presentation of the object (as a Vaadin 
                component composition).
                
                    add(PrettyPrinter.toVaadin(person));
                
                The implementation uses class called `DtoDisplay`. Via it's API you can customize the presentation of 
                the object. Naming, API and visualisations are still in early stage. Current plan is to use 
                PrettyPrinter as the main name and access point for the API. Provide [all feedback you 
                have](https://github.com/viritin/flow-viritin/issues).
                """));

        add(PrettyPrinter.toVaadin(person));

        Locale[] locales = new Locale[] {
                Locale.US, Locale.UK, new Locale("it"), new Locale("fi")
        };
        var localeBtns = new HorizontalFloatLayout();
        for(Locale l : locales) {
            localeBtns.add(new Button("Show with locale " + l.toString(), e-> {
                UI.getCurrent().setLocale(l);
                add(new H3("Person with locale " + l));
                var display = PrettyPrinter.toVaadin(person);
                add(display);
                display.scrollIntoView();
            }));
        }
        add(localeBtns);


    }

    private static @NotNull Person getPerson() {
        ComplexPerson person = new ComplexPerson();
        person.setId(678);
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAge(42);

        person.setJoinTime(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        DayOfWeek dayOfWeek = person.getJoinTime().getDayOfWeek();

        Address address = new Address();
        address.setStreet("Some street 123");
        address.setCity("Springfield");
        address.setZipCode(12345);
        address.setType(Address.AddressType.Home);

        person.getAddresses().add(address);

        person.setDoubleValue(1234567.891);

        // Make a circular dep for demo...
        person.setSupervisor(person);
        return person;
    }

    public static String toPrettyJson(Object dto) {
        try {
            ObjectMapper mrJackson = new ObjectMapper();
            mrJackson.registerModule(new JavaTimeModule());
            return mrJackson.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

