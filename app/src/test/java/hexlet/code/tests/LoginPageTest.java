package hexlet.code.tests;

import hexlet.code.page_object.HomePage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPageTest extends BaseTest {

    @Test
    public void testSuccessfulLogin() {
        HomePage homePage = performLogin();
        assertTrue(homePage.isUserLoggedIn(), "Пользователь не находится на главной странице");
    }
}
