package hexlet.code.page_object;

import hexlet.code.page_object.menu.labels.LabelsPage;
import hexlet.code.page_object.menu.statuses.TaskStatusesPage;
import hexlet.code.page_object.menu.tasks.TasksPage;
import hexlet.code.page_object.menu.users.UsersPage;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("unused")
public class HomePage extends BasePage {

    @FindBy(css = "[class~='RaUserMenu-userButton']")
    private WebElement profileButton;

    @FindBy(css = "[class~='logout']")
    private WebElement logoutButton;

    @FindBy(css = "[href$='users']")
    private WebElement menuUsersButton;

    @FindBy(css = "[href$='task_statuses']")
    private WebElement menuTaskStatusesButton;

    @FindBy(css = "[href$='labels']")
    private WebElement menuLabelsButton;

    @FindBy(css = "[href$='tasks']")
    private WebElement menuTasksButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isUserLoggedIn() {
        try {
            wait.until(ExpectedConditions.visibilityOf(profileButton));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public LoginPage logout() {
        wait.until(ExpectedConditions.elementToBeClickable(profileButton))
                .click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton))
                .click();
        return new LoginPage(driver);
    }

    public UsersPage openMenuUsers() {
        wait.until(ExpectedConditions.elementToBeClickable(menuUsersButton))
                .click();
        wait.until(ExpectedConditions.attributeContains(menuUsersButton,
                "class",
                "RaMenuItemLink-active"));
        return new UsersPage(driver);
    }

    public TaskStatusesPage openMenuTaskStatuses() {
        wait.until(ExpectedConditions.elementToBeClickable(menuTaskStatusesButton))
                .click();
        wait.until(ExpectedConditions.attributeContains(menuTaskStatusesButton,
                "class",
                "RaMenuItemLink-active"));
        return new TaskStatusesPage(driver);
    }

    public LabelsPage openMenuLabels() {
        wait.until(ExpectedConditions.elementToBeClickable(menuLabelsButton))
                .click();
        wait.until(ExpectedConditions.attributeContains(menuLabelsButton,
                "class",
                "RaMenuItemLink-active"));
        return new LabelsPage(driver);
    }

    public TasksPage openMenuTasks() {
        wait.until(ExpectedConditions.elementToBeClickable(menuTasksButton))
                .click();
        wait.until(ExpectedConditions.attributeContains(menuTasksButton,
                "class",
                "RaMenuItemLink-active"));
        return new TasksPage(driver);
    }
}
