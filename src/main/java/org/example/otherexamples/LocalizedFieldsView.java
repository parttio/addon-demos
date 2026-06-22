package org.example.otherexamples;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.example.ExamplesLayout;
import org.example.views.viritin.ViritinMenuGroup;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.fields.localized.LocalizedTextArea;
import org.vaadin.firitin.fields.localized.LocalizedTextField;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Demonstrates Viritin's {@link LocalizedTextField} and {@link LocalizedTextArea}.
 * <p>
 * These fields edit a {@code Map<Locale, String>}: one text per language, with a
 * language selector built in. If a {@code Translator} bean is available in the
 * Spring context (see {@code MistralTranslatorConfig}), the fields also show a
 * "translate" button that fills the other languages from the one you typed.
 */
@Route(layout = DefaultLayout.class)
@MenuItem(icon = VaadinIcon.GLOBE, parent = ViritinMenuGroup.class)
@Addon("flow-viritin")
public class LocalizedFieldsView extends VerticalLayout {

    // The languages the user can provide content for.
    private static final List<Locale> LOCALES = List.of(
            Locale.ENGLISH,
            Locale.of("fi"),
            Locale.of("sv"),
            Locale.of("de"));

    private final Binder<Product> binder = new Binder<>(Product.class);
    private final Pre value = new Pre();

    public LocalizedFieldsView() {
        setMaxWidth("600px");

        add(new Paragraph("""
                Each field below edits one value per language (English, Finnish, \
                Swedish, German). Pick a language from the selector and type. If a \
                Mistral API key is configured, a "translate" button appears that \
                fills the remaining languages automatically."""));

        var name = new LocalizedTextField("Product name", LOCALES);
        var description = new LocalizedTextArea("Description", LOCALES);
        add(name, description);

        binder.forField(name).bind("name");
        binder.forField(description).bind("description");
        binder.addValueChangeListener(e -> updateValueDisplay());

        binder.setBean(new Product());
        updateValueDisplay();

        add(new Paragraph("Bean value (Map<Locale, String> per field):"), value);
    }

    private void updateValueDisplay() {
        Product p = binder.getBean();
        value.setText("name        = " + format(p.getName()) + "\n"
                + "description = " + format(p.getDescription()));
    }

    private static String format(Map<Locale, String> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        return map.entrySet().stream()
                .map(e -> e.getKey() + "=\"" + e.getValue() + "\"")
                .collect(Collectors.joining(", ", "{", "}"));
    }

    /**
     * A localizable product. Each localizable property is a map from a
     * {@link Locale} to the text in that language — exactly what the localized
     * fields produce and consume.
     */
    public static class Product {
        private Map<Locale, String> name = Map.of();
        private Map<Locale, String> description = Map.of();

        public Map<Locale, String> getName() {
            return name;
        }

        public void setName(Map<Locale, String> name) {
            this.name = name;
        }

        public Map<Locale, String> getDescription() {
            return description;
        }

        public void setDescription(Map<Locale, String> description) {
            this.description = description;
        }
    }
}
