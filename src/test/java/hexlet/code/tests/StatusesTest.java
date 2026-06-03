package hexlet.code.tests;

import hexlet.code.page_object.HomePage;
import hexlet.code.page_object.menu.statuses.StatusFormPage;
import hexlet.code.page_object.menu.statuses.TaskStatusesPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StatusesTest extends BaseTest {

    private TaskStatusesPage statusesPage;

    @BeforeEach
    public void loginAndGoToStatuses() {
        HomePage homePage = performLogin();
        statusesPage = homePage.openMenuTaskStatuses();
    }

    @Test
    public void testStatusesTableContains() {
        // Убедитесь, что таблица загружает все статусы
        statusesPage.verifyTableVisible();

        // Проверьте отображение ключевых полей: название и slug
        statusesPage.verifyRequiredColumnsVisible();
    }

    @Test
    public void testCreateStatus() {
        int countBefore = statusesPage.getStatusesCount();
        StatusFormPage statusForm = statusesPage.openCreateStatusForm();

        // Проверьте, что форма добавления открывается и отображает нужные поля.
        statusForm.verifyFormElementsVisible();

        // Заполните название и slug, подтвердите создание и убедитесь, что запись появилась в списке.
        String name = "In Progress";
        String slug = "in_progress";
        statusesPage = statusForm.createStatusAndGoToList(name, slug);

        assertTrue(statusesPage.isStatusExist(name, slug),
                "Новый статус в списке не появился");
        assertEquals(countBefore + 1, statusesPage.getStatusesCount(),
                "Количество статусов не увеличилось на 1");
    }

    @Test
    public void testEditStatus() {
        // Создаём новый статус
        String initialName = "New";
        String initialSlug = "new";
        createStatus(initialName, initialSlug);

        // Откройте форму редактирования, измените данные и убедитесь, что обновления сохранены.
        StatusFormPage statusForm = statusesPage.openLastStatus();

        String newName = "Updated";
        String newSlug = "updated";
        statusesPage = statusForm.editStatusAndGoToList(newName, newSlug);

        assertTrue(statusesPage.isStatusExist(newName, newSlug),
                "После редактирования у статуса не отображаются новые значения: " + newName + ", "
                        + newSlug);
        assertFalse(statusesPage.isStatusExist(initialName, initialSlug),
                "После редактирования у статуса отображаются начальные значения: " + initialName + ", "
                        + initialSlug);
    }

    @Test
    public void testDeleteStatus() {
        // Создаём новый статус
        String name = "To Delete";
        String slug = "to_delete";
        createStatus(name, slug);

        // Удалите один или несколько статусов и проверьте, что они исчезли из списка.
        int countBefore = statusesPage.getStatusesCount();
        statusesPage.deleteLastStatus();
        statusesPage.verifySuccessRowDeleteMessage();

        assertFalse(statusesPage.isStatusExist(name, slug),
                "Статус не удалён");
        assertEquals(countBefore - 1, statusesPage.getStatusesCount(),
                "Количество статусов не уменьшилось на 1");
    }

    @Test
    public void testDeleteAllStatuses() {
        // Убедимся, что есть хотя бы один пользователь
        if (statusesPage.getStatusesCount() == 0) {
            createStatus("All Delete", "all_delete");
        }

        // Выделите все статусы и выполните групповое удаление, затем убедитесь, что список пуст.
        int countBefore = statusesPage.getStatusesCount();
        statusesPage.deleteAllStatuses();
        statusesPage.verifySuccessAllStatusesDelete(countBefore);
    }

    private void createStatus(String name, String slug) {
        StatusFormPage form = statusesPage.openCreateStatusForm();
        statusesPage = form.createStatusAndGoToList(name, slug);
    }
}
