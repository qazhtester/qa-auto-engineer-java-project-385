package hexlet.code;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomePageTest extends BaseTest {

    @Test
    public void testHomePageTitle() {
        driver.get(BASE_URL);
        String actualTitle = driver.getTitle();
        assertEquals("Task manager", actualTitle, "Заголовок страницы не совпадает");
    }
}
