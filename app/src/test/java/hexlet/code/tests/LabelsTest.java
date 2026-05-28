package hexlet.code.tests;

import hexlet.code.page_object.HomePage;
import hexlet.code.page_object.LoginPage;
import hexlet.code.page_object.menu.labels.LabelFormPage;
import hexlet.code.page_object.menu.labels.LabelsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LabelsTest extends BaseTest {

    private LabelsPage labelsPage;

    @BeforeEach
    public void loginAndGoToLabels() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL);
        HomePage homePage = loginPage.login("user382", "pass84567");
        labelsPage = homePage.openMenuLabels();
    }

    @Test
    public void testLabelsTableContains() {
        // Проверьте, что таблица меток корректно отображает все записи.
        labelsPage.verifyTableVisible();
        labelsPage.verifyRequiredColumnsVisible();
    }

    @Test
    public void testCreateLabel() {
        int countBefore = labelsPage.getLabelsCount();
        LabelFormPage form = labelsPage.openCreateLabelForm();

        // Проверьте, что форма добавления открывается и отображает нужные поля.
        form.verifyFormElementsVisible();

        // Создайте новую метку и проверьте, что она появляется в списке.
        String name = "create";
        labelsPage = form.createLabelAndGoToList(name);

        assertTrue(labelsPage.isLabelExist(name),
                "Новая метка в списке не появилась");
        assertEquals(countBefore + 1, labelsPage.getLabelsCount(),
                "Количество меток не увеличилось на 1");
    }

    @Test
    public void testEditLabel() {
        // Создаём новую метку
        LabelFormPage form = labelsPage.openCreateLabelForm();
        String initialName = "new";
        labelsPage = form.createLabelAndGoToList(initialName);

        // Измените существующую метку и подтвердите, что обновления сохранены.
        form = labelsPage.openLastLabel();
        String newName = "update";
        labelsPage = form.editLabelAndGoToList(newName);

        assertTrue(labelsPage.isLabelExist(newName),
                "После редактирования у метки не отображается новое значение: " + newName);
        assertFalse(labelsPage.isLabelExist(initialName),
                "После редактирования у метки отображается начальное значение: " + initialName);
    }

    @Test
    public void testDeleteLabel() {
        // Создаём новую метку
        LabelFormPage form = labelsPage.openCreateLabelForm();
        String name = "ToDelete";
        labelsPage = form.createLabelAndGoToList(name);
        int countBefore = labelsPage.getLabelsCount();

        // Удалите одну или несколько меток и убедитесь, что они исчезли из списка.
        labelsPage.deleteLastLabel();
        labelsPage.verifySuccessRowDeleteMessage();

        assertFalse(labelsPage.isLabelExist(name),
                "Метка не удалена");
        assertEquals(countBefore - 1, labelsPage.getLabelsCount(),
                "Количество статусов не уменьшилось на 1");
    }
}