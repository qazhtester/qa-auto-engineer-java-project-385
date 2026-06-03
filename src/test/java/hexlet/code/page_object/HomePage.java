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

    private static final String ATTRIBUTE_CLASS = "class";
    private static final String ACTIVE_MENU_ITEM_CLASS = "RaMenuItemLink-active";

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
                ATTRIBUTE_CLASS,
                ACTIVE_MENU_ITEM_CLASS));
        return new UsersPage(driver);
    }

    public TaskStatusesPage openMenuTaskStatuses() {
        wait.until(ExpectedConditions.elementToBeClickable(menuTaskStatusesButton))
                .click();
        wait.until(ExpectedConditions.attributeContains(menuTaskStatusesButton,
                ATTRIBUTE_CLASS,
                ACTIVE_MENU_ITEM_CLASS));
        return new TaskStatusesPage(driver);
    }

    public LabelsPage openMenuLabels() {
        wait.until(ExpectedConditions.elementToBeClickable(menuLabelsButton))
                .click();
        wait.until(ExpectedConditions.attributeContains(menuLabelsButton,
                ATTRIBUTE_CLASS,
                ACTIVE_MENU_ITEM_CLASS));
        return new LabelsPage(driver);
    }

    public TasksPage openMenuTasks() {
        wait.until(ExpectedConditions.elementToBeClickable(menuTasksButton))
                .click();
        wait.until(ExpectedConditions.attributeContains(menuTasksButton,
                ATTRIBUTE_CLASS,
                ACTIVE_MENU_ITEM_CLASS));
        return new TasksPage(driver);
    }
}
