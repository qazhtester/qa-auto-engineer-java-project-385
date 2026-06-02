package hexlet.code.page_object.menu.tasks;

import hexlet.code.page_object.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

@SuppressWarnings("unused")
public class TaskFormPage extends HomePage {

    private static final String ALERT = ".MuiSnackbarContent-message";
    private static final String LIST_FILTER = "[role='listbox']";
    private static final String OPTION_FILTER = "[role='option']";

    @FindBy(css = "input[name='title']")
    private WebElement titleInput;

    @FindBy(css = "textarea[name='content']")
    private WebElement contentTextarea;

    @FindBy(css = ".ra-input-assignee_id [role='combobox']")
    private WebElement assigneeSelect;

    @FindBy(css = ".ra-input-status_id [role='combobox']")
    private WebElement statusSelect;

    @FindBy(css = ".ra-input-label_id [role='combobox']")
    private WebElement labelSelect;

    @FindBy(css = "button[type='submit']")
    private WebElement saveButton;

    @FindBy(css = "button[aria-label='Delete']")
    private WebElement deleteButton;

    public TaskFormPage(WebDriver driver) {
        super(driver);
    }

    public void verifyFormElementsVisible() {
        checkVisibility(assigneeSelect, "Assignee");
        checkVisibility(titleInput, "Title");
        checkVisibility(contentTextarea, "Content");
        checkVisibility(statusSelect, "Status");
        checkVisibility(labelSelect, "Label");
        checkVisibility(saveButton, "Save");
    }

    public void verifySuccessCreateMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(ALERT)));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(ALERT), "Element created"));
    }

    public void verifySuccessEditMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(ALERT)));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(ALERT), "Element updated"));
    }

    public void fillTitle(String title) {
        typeText(titleInput, title);
    }

    public void selectStatus(String statusName) {
        selectOption(statusSelect, statusName);
    }

    public void selectAssignee(String assigneeName) {
        selectOption(assigneeSelect, assigneeName);
    }

    private void selectOption(WebElement combobox, String optionText) {
        combobox.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(LIST_FILTER)));
        List<WebElement> options = driver.findElements(By.cssSelector(OPTION_FILTER));
        for (WebElement option : options) {
            if (option.getText().trim().equals(optionText)) {
                option.click();
                break;
            }
        }
    }

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    public void clickDelete() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
    }

    public TasksPage createTaskAndGoToBoard(String assignee, String title, String status) {
        selectAssignee(assignee);
        fillTitle(title);
        selectStatus(status);
        clickSave();
        verifySuccessCreateMessage();
        return openMenuTasks();
    }

    public TasksPage editTaskAndGoToBoard(String assignee, String title) {
        selectAssignee(assignee);
        fillTitle(title);
        clickSave();
        verifySuccessEditMessage();
        return new TasksPage(driver);
    }

    public TasksPage editStatusAndGoToBoard(String status) {
        selectStatus(status);
        clickSave();
        verifySuccessEditMessage();
        return new TasksPage(driver);
    }

    public TasksPage deleteAndGoToBoard() {
        clickDelete();
        wait.until(ExpectedConditions.invisibilityOf(deleteButton));
        return new TasksPage(driver);
    }
}