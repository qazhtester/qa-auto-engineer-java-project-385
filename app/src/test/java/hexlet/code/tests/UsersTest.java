package hexlet.code.tests;

import hexlet.code.page_object.HomePage;
import hexlet.code.page_object.LoginPage;
import hexlet.code.page_object.menupage.UsersPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersTest extends BaseTest{

    private UsersPage usersPage;
    private HomePage homePage;

    @BeforeEach
    public void loginAndGoToUsers() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL);
        homePage = loginPage.login("user23", "pass45");
        usersPage = homePage.openMenuUsers();
    }

    @Test
    public void testUsersTableContains() {
        // Проверьте, что таблица пользователей загружается полностью.
        assertTrue(usersPage.isLoadUserTable(), "Таблица пользователей должна быть полностью загружена");

        // Удостоверьтесь, что отображаются ключевые поля: Email, First name, Last name.
        assertTrue(usersPage.isTableContainsRequiredFields(),
                "Таблица должна отображать обязательные поля: Email, First name, Last name");
    }

    @Test
    public void testCreateUser() {
        int countBefore = usersPage.getUsersCount();
        usersPage.openCreateUsersForm();
        // Убедитесь, что форма создания открывается корректно.
        assertTrue(usersPage.isCreateUsersFormCorrectDisplayed(),
                "Должна быть открыта форма создания пользователя");

        //Заполните данные нового пользователя и проверьте, что карточка появляется в списке.
        String email = "dima@example.com";
        String firstName = "Dima";
        String lastName = "Bell";
        usersPage.fillAndSaveCreateUsersForm(email, firstName, lastName);
        homePage.openMenuUsers();

        assertTrue(usersPage.isUserPresent(email, firstName, lastName),
                "Новый пользователь должен появиться в списке");
        assertEquals(countBefore + 1, usersPage.getUsersCount(),
                "Количество пользователей должно увеличиться на 1");
    }
}
