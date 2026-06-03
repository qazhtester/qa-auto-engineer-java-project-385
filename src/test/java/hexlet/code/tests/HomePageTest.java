package hexlet.code.tests;

import hexlet.code.page_object.HomePage;
import hexlet.code.page_object.LoginPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomePageTest extends BaseTest {

    @Test
    public void testHomePageTitle() {
        driver.get(baseUrl);
        String actualTitle = driver.getTitle();
        assertEquals("Task manager", actualTitle, "Заголовок страницы не совпадает");
    }

    @Test
    public void testLogout() {
        // Сначала входим
        HomePage homePage = performLogin();

        // Затем выходим
        LoginPage returnedLoginPage = homePage.logout();

        assertTrue(returnedLoginPage.isLoginPageDisplayed(), "Страница входа не открыта");
    }
}
