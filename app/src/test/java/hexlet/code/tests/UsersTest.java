package hexlet.code.tests;

import hexlet.code.page_object.HomePage;
import hexlet.code.page_object.LoginPage;
import hexlet.code.page_object.menu.users.UserFormPage;
import hexlet.code.page_object.menu.users.UsersPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersTest extends BaseTest {

    private UsersPage usersPage;

    @BeforeEach
    public void loginAndGoToUsers() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL);
        HomePage homePage = loginPage.login("user23", "pass45");
        usersPage = homePage.openMenuUsers();
    }

    @Test
    public void testUsersTableContains() {
        // Проверьте, что таблица пользователей загружается полностью.
        usersPage.verifyUserTableVisible();

        // Удостоверьтесь, что отображаются ключевые поля: Email, First name, Last name.
        usersPage.verifyRequiredColumnsVisible();
    }

    @Test
    public void testCreateUser() {
        int countBefore = usersPage.getUsersCount();
        UserFormPage userFormPage = usersPage.openCreateUserForm();

        // Убедитесь, что форма создания открывается корректно.
        userFormPage.verifyFormElementsVisible();

        //Заполните данные нового пользователя и проверьте, что карточка появляется в списке.
        String email = "dima@example.com";
        String firstName = "Dima";
        String lastName = "Bell";
        usersPage = userFormPage.createUserAndGoToList(email, firstName, lastName);

        assertTrue(usersPage.isUserExist(email, firstName, lastName),
                "Новый пользователь в списке не появился");
        assertEquals(countBefore + 1, usersPage.getUsersCount(),
                "Количество пользователей не увеличилось на 1");
    }

    @Test
    public void testEditUser() {
        // Создаём нового пользователя
        UserFormPage userFormPage = usersPage.openCreateUserForm();
        String initialEmail = "kate@example.com";
        String initialFirstName = "Kate";
        String initialLastName = "Brown";
        usersPage = userFormPage.createUserAndGoToList(initialEmail, initialFirstName, initialLastName);

        // Откройте форму редактирования и убедитесь, что данные подставляются верно.
        userFormPage = usersPage.openLastUser();

        assertEquals(initialEmail, userFormPage.getEmailValue(),
                "Email из таблицы не совпадает с Email из формы редактирования");
        assertEquals(initialFirstName, userFormPage.getFirstNameValue(),
                "First name из таблицы не совпадает с First name из формы редактирования");
        assertEquals(initialLastName, userFormPage.getLastNameValue(),
                "Last name из таблицы не совпадает с Last name из формы редактирования");

        //Измените значения и проверьте, что обновления сохранены.
        String newEmail = "alice@example.com";
        String newFirstName = "Alice";
        String newLastName = "Smith";
        usersPage = userFormPage.editUserAndGoToList(newEmail, newFirstName, newLastName);

        assertTrue(usersPage.isUserExist(newEmail, newFirstName, newLastName),
                "После редактирования у пользователя не отображаются новые значения: " + newEmail + ", "
                        + newFirstName + ", "
                        + newLastName);
        assertFalse(usersPage.isUserExist(initialEmail, initialFirstName, initialLastName),
                "После редактирования у пользователя отображаются начальные значения: " + initialEmail + ", "
                        + initialFirstName + ", "
                        + initialLastName);

        // Дополнительно проверьте валидацию, в частности формат email.
        userFormPage = usersPage.openLastUser();
        userFormPage.enterEmail("efd");
        userFormPage.clickSave();
        userFormPage.verifyValidationErrorMessage("The form is not valid. Please check for errors",
                "Incorrect email format");
    }
}
