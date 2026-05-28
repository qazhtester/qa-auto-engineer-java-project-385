package hexlet.code.tests;

import hexlet.code.page_object.HomePage;
import hexlet.code.page_object.menu.tasks.TaskFormPage;
import hexlet.code.page_object.menu.tasks.TasksPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TasksTest extends BaseTest {

    private TasksPage tasksPage;

    @BeforeEach
    public void loginAndGoToTasks() {
        HomePage homePage = performLogin();
        tasksPage = homePage.openMenuTasks();
    }

    @Test
    public void testBoardDisplaysAllTasks() {
        // Откройте список задач и убедитесь, что загружаются все записи.
        tasksPage. verifyBoardVisible();
        assertFalse(tasksPage.getAllTaskNames().isEmpty(),
                "На доске нет ни одной задачи");
    }

    @Test
    public void testFilterByStatus() {
        // Проверить фильтр по статусу и убедиться, что результаты обновляются.
        String status = "To Review";
        tasksPage.filterByStatus(status);
        tasksPage.verifyAllCardsInColumn(status);

        assertFalse(tasksPage.getAllTaskNames().isEmpty(),
                "После фильтрации должен быть хотя бы один результат");
    }

    @Test
    public void testFilterByAssignee() {
        // Проверить фильтр по исполнителю и убедиться, что результаты обновляются.
        List<String> before = tasksPage.getAllTaskNames();
        tasksPage.filterByAssignee("peter@outlook.com");
        tasksPage.verifyCardListChanged(before);
    }

    @Test
    public void testFilterByLabel() {
        // Проверить фильтр по меткам и убедиться, что результаты обновляются.
        List<String> before = tasksPage.getAllTaskNames();
        tasksPage.filterByLabel("bug");
        tasksPage.verifyCardListChanged(before);
    }

    @Test
    public void testCreateTask() {
        int countBefore = tasksPage.getAllTaskNames().size();
        TaskFormPage form = tasksPage.openCreateTaskForm();

        // Проверьте, что форма создания отображается корректно
        form.verifyFormElementsVisible();

        // Проверьте, что форма создания позволяет заполнить обязательные поля (название, статус, исполнитель).
        // Сохраните карточку и убедитесь, что она появилась в нужной колонке
        String assignee = "michael@example.com";
        String title = "Create Task";
        String status = "Published";
        tasksPage = form.createTaskAndGoToBoard(assignee, title, status);

        assertTrue(tasksPage.isTaskPresent(title),
                "Новая задача не появилась на доске");
        assertEquals(countBefore + 1, tasksPage.getAllTaskNames().size(),
                "Количество карточек не увеличилось на 1");
        assertEquals(status, tasksPage.getColumnContainingCard(title),
                "Задача не отображается в колонке " + status);
    }

    @Test
    public void testEditTask() {
        // Создаём новую задачу
        String initialAssignee = "michael@example.com";
        String initialTitle = "Edit Me";
        String initialStatus = "Published";
        createTask(initialAssignee, initialTitle, initialStatus);

        // Обновите данные существующей задачи и подтвердите, что изменения сохранены и отображаются.
        TaskFormPage form = tasksPage.openEditTaskByName(initialTitle);
        String newAssignee = "sarah@example.com";
        String newTitle = "Update Task";
        tasksPage = form.editTaskAndGoToBoard(newAssignee, newTitle);

        assertTrue(tasksPage.isTaskPresent(newTitle),
                "Отредактированная задача не отображается с новым названием " + newTitle);
        assertFalse(tasksPage.isTaskPresent(initialTitle),
                "Присутствует задача со старым названием " + initialTitle);
    }

    @Test
    public void testMoveTaskBetweenColumns() {
        // Создаём новую задачу
        String initialAssignee = "michael@example.com";
        String initialTitle = "Edit Me";
        String initialStatus = "Published";
        createTask(initialAssignee, initialTitle, initialStatus);

        // Перетащите карточку в другой статус или используйте встроенные действия, затем проверьте, что отображаемый статус обновился.
        TaskFormPage form = tasksPage.openEditTaskByName(initialTitle);
        String newStatus = "To Be Fixed";
        tasksPage = form.editStatusAndGoToBoard(newStatus);

        assertEquals(newStatus, tasksPage.getColumnContainingCard(initialTitle),
                "Задача не переместилась в колонку " + newStatus);
    }

    @Test
    public void testDeleteTask() {
        // Создаём задачу
        String assignee = "michael@example.com";
        String title = "Delete Me";
        String status = "Published";
        createTask(assignee, title, status);

        // Удалите задачу и убедитесь, что она исчезла из списка и с доски.
        int countBefore = tasksPage.getAllTaskNames().size();
        TaskFormPage form = tasksPage.openEditTaskByName(title);
        tasksPage = form.deleteAndGoToBoard();
        tasksPage.verifySuccessDeleteMessage();

        assertEquals(countBefore - 1, tasksPage.getAllTaskNames().size(),
                "Количество карточек не уменьшилось на 1");
        assertFalse(tasksPage.isTaskPresent(title),
                "Отображается удалённая задача " + title);
    }

    private void createTask(String assignee, String title, String status) {
        TaskFormPage form = tasksPage.openCreateTaskForm();
        tasksPage = form.createTaskAndGoToBoard(assignee, title, status);
    }
}