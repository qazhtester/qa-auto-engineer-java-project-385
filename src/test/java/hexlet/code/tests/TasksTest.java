package hexlet.code.tests;

import hexlet.code.page_object.HomePage;
import hexlet.code.page_object.menu.tasks.TaskFormPage;
import hexlet.code.page_object.menu.tasks.TasksPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TasksTest extends BaseTest {

    private static final String DEFAULT_ASSIGNEE_EMAIL = "michael@example.com";
    private static final String STATUS_PUBLISHED = "Published";

    private TasksPage tasksPage;

    @BeforeEach
    public void loginAndGoToTasks() {
        HomePage homePage = performLogin();
        tasksPage = homePage.openMenuTasks();
    }

    @Test
    public void testBoardDisplaysAllTasks() {
        tasksPage.verifyBoardVisible();
        assertFalse(tasksPage.getAllTaskNames().isEmpty(),
                "На доске нет ни одной задачи");
    }

    @Test
    public void testFilterByStatus() {
        String status = "To Review";
        tasksPage.filterByStatus(status);
        tasksPage.verifyAllCardsInColumn(status);

        assertFalse(tasksPage.getAllTaskNames().isEmpty(),
                "После фильтрации должен быть хотя бы один результат");
    }

    @Test
    public void testFilterByAssignee() {
        List<String> before = tasksPage.getAllTaskNames();
        tasksPage.filterByAssignee("peter@outlook.com");
        tasksPage.verifyCardListChanged(before);
    }

    @Test
    public void testFilterByLabel() {
        List<String> before = tasksPage.getAllTaskNames();
        tasksPage.filterByLabel("bug");
        tasksPage.verifyCardListChanged(before);
    }

    @Test
    public void testCreateTask() {
        int countBefore = tasksPage.getAllTaskNames().size();
        TaskFormPage form = tasksPage.openCreateTaskForm();

        form.verifyFormElementsVisible();

        String title = "Create Task";
        String status = STATUS_PUBLISHED;
        tasksPage = form.createTaskAndGoToBoard(DEFAULT_ASSIGNEE_EMAIL, title, status);

        assertTrue(tasksPage.isTaskPresent(title),
                "Новая задача не появилась на доске");
        assertEquals(countBefore + 1, tasksPage.getAllTaskNames().size(),
                "Количество карточек не увеличилось на 1");
        assertEquals(status, tasksPage.getColumnContainingCard(title),
                "Задача не отображается в колонке " + status);
    }

    @Test
    public void testEditTask() {
        String initialTitle = "Edit Me";
        createTask(initialTitle);

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
        String initialTitle = "Move Me";
        createTask(initialTitle);

        TaskFormPage form = tasksPage.openEditTaskByName(initialTitle);
        String newStatus = "To Be Fixed";
        tasksPage = form.editStatusAndGoToBoard(newStatus);

        assertEquals(newStatus, tasksPage.getColumnContainingCard(initialTitle),
                "Задача не переместилась в колонку " + newStatus);
    }

    @Test
    public void testDeleteTask() {
        String title = "Delete Me";
        createTask(title);

        int countBefore = tasksPage.getAllTaskNames().size();
        TaskFormPage form = tasksPage.openEditTaskByName(title);
        tasksPage = form.deleteAndGoToBoard();
        tasksPage.verifySuccessDeleteMessage();

        assertEquals(countBefore - 1, tasksPage.getAllTaskNames().size(),
                "Количество карточек не уменьшилось на 1");
        assertFalse(tasksPage.isTaskPresent(title),
                "Отображается удалённая задача " + title);
    }

    private void createTask(String title) {
        TaskFormPage form = tasksPage.openCreateTaskForm();
        tasksPage = form.createTaskAndGoToBoard(TasksTest.DEFAULT_ASSIGNEE_EMAIL, title, TasksTest.STATUS_PUBLISHED);
    }
}
