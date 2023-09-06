package org.example;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
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
        assertThat(page.getByText("Welcome }> add-on demo project")).isVisible();
    }

}