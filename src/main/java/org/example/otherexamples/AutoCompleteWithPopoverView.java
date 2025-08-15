package org.example.otherexamples;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import org.example.ExamplesLayout;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Route(layout = ExamplesLayout.class)
public class AutoCompleteWithPopoverView extends VerticalLayout {

    public AutoCompleteWithPopoverView() {

        add(new AutoCompleteTextField() {{
            addSuggestions("Java", "JavaScript", "Python", "C++", "C#", "Ruby", "Go", "Rust", "Kotlin", "Swift");
            setLabel("Type a programming language");
            setWidth(300, Unit.PIXELS);
        }});
    }

    public static class AutoCompleteTextField extends TextField {

        private Set<String> suggestions = new HashSet<>();
        private final Popover popover = new Popover();
        private final VerticalLayout options = new VerticalLayout();
        private Paragraph focused;

        public AutoCompleteTextField() {
            super();
            setValueChangeMode(ValueChangeMode.LAZY);

            // Do NOT DO This here yet, opens without asking....
            //popover.setTarget(this);
            popover.add(options);
            options.setSpacing(false);
            options.setMinWidth(300, Unit.PIXELS);
            popover.setOpened(false);
            addValueChangeListener(event -> {
                filter(event.getValue());
            });

            addKeyDownListener(Key.ARROW_DOWN, event -> {
                focusNext();
            });
            addKeyDownListener(Key.ARROW_UP, event -> {
                focusPrev();
            });
            addKeyDownListener(Key.ENTER, event -> {;
                selectCompletion();
            });
            addKeyDownListener(Key.TAB, event -> {;
                selectCompletion();
            });
        }

        private void selectCompletion() {
            if (focused != null) {
                setValue(focused.getText());
                popover.close();
            }
        }

        private void filter(String input) {
            // NOTE: consider limiting suggestions or using Grid instead of
            // a list of Paragraphs if there are many suggestions, this might
            // "choke" the browser with huge amount of results.
            List<String> filteredSuggestions = suggestions.stream()
                    .filter(suggestion -> suggestion.toLowerCase().startsWith(input.toLowerCase()))
                    .toList();
            options.removeAll();
            if (filteredSuggestions.isEmpty()) {
                popover.setOpened(false);
                return;
            }
            filteredSuggestions.forEach(suggestion -> {
                Paragraph paragraph = new Paragraph(suggestion);
                paragraph.addClickListener(clickEvent -> {
                    setValue(suggestion);
                    popover.close();
                });
                options.add(paragraph);
            });
            if(filteredSuggestions.size() == 1) {
                // If only one suggestion, select it automatically
                focusNext();
            }
            popover.setTarget(this);
            popover.setOpened(true);
        }

        private void focusNext() {
            if(!popover.isOpened()) {
                filter(""); // Show all suggestions if popover is not opened
            }
            if(focused == null) {
                focus(options.getComponentAt(0));
            } else {
                Component next = options.getComponentAt((options.indexOf(focused) + 1) % options.getComponentCount());
                focus(next);
            }
        }

        private void focus(Component paragraph) {
            Paragraph newFocus = (Paragraph) paragraph;
            if (focused != null) {
                focused.getStyle().setFontWeight(Style.FontWeight.NORMAL);
            }
            newFocus.getStyle().setFontWeight(Style.FontWeight.BOLD);
            focused = newFocus;
        }

        private void focusPrev() {
            if(focused == null) {
                focus(options.getComponentAt(options.getComponentCount() - 1));
            } else {
                Component next = options.getComponentAt((options.indexOf(focused) - 1 + options.getComponentCount()) % options.getComponentCount());
                focus(next);
            }
        }

        public void setAutocomplete(String value) {
            getElement().setAttribute("autocomplete", value);
        }

        public AutoCompleteTextField addSuggestions(String... suggestions) {
            this.suggestions.addAll(Arrays.asList(suggestions));
            return this;
        }
    }
}
