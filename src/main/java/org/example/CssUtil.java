package org.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;

import java.util.Base64;

public class CssUtil {
    public static void inject(String css) {
        Page p = UI.getCurrent().getPage();
        p.addStyleSheet("data:text/css;base64," + Base64.getEncoder().encodeToString(css.getBytes()));
    }
}
