package hexlet.code.tests;

import hexlet.code.page_object.HomePage;
import hexlet.code.page_object.LoginPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPageTest extends BaseTest {

    @Test
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL);

        HomePage homePage = loginPage.login("user12312433", "pass124654");
        assertTrue(homePage.isUserLoggedIn(), "Пользователь не находится на главной странице");
    }
}
