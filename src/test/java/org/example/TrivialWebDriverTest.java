package org.example;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("IntegrationTest")
public class TrivialWebDriverTest {


    @LocalServerPort
    private int port;

    static Playwright playwright;

    @BeforeAll
    public static void setup() {
        playwright = Playwright.create();
    }

    @Test
    public void smokeTest() {
        Browser browser = playwright.firefox().launch();
        Page page = browser.newPage();
        page.navigate("http://localhost:" + port + "/");
        assertThat(page.getByText("}> add-on demo project")).isVisible();
    }

    @Test
    public void checkAllViewsLoadProperly() {
        Browser browser = playwright.firefox().launch();
        Page page = browser.newPage();
        page.navigate("http://localhost:" + port + "/");

        assertThat(page.getByText("}> add-on demo project")).isVisible();

        MutableInt count = new MutableInt(0);

        Locator items = page.locator("vaadin-side-nav-item");
        items.all().forEach(item -> {
            String viewTitle = item.textContent();
            System.out.println("Checking view " + viewTitle);
            item.click();
            // Make sure the h2 element changes to reflect the view
            //  at least the view is then opened
            assertThat(page.locator("//h2[contains(text(),'%s')]".formatted(viewTitle)))
                    .isVisible();
            count.increment();
            // TODO figure out how to check for possible errors in browser/server

        });

        System.out.println("Checked " + count.intValue() + " views");

    }

}