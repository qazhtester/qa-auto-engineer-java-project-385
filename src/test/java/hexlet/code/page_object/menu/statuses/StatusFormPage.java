package hexlet.code.page_object.menu.statuses;

import hexlet.code.page_object.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("unused")
public class StatusFormPage extends HomePage {

    private static final String ALERT = ".MuiSnackbarContent-message";

    @FindBy(css = "input[name='name']")
    private WebElement nameInput;

    @FindBy(css = "input[name='slug']")
    private WebElement slugInput;

    @FindBy(css = "button[type='submit']")
    private WebElement saveButton;

    public StatusFormPage(WebDriver driver) {
        super(driver);
    }

    public void fillName(String name) {
        typeText(nameInput, name);
    }

    public void fillSlug(String slug) {
        typeText(slugInput, slug);
    }

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton))
                .click();
    }

    public void verifyFormElementsVisible() {
        checkVisibility(nameInput, "Name");
        checkVisibility(slugInput, "Slug");
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

    public TaskStatusesPage createStatusAndGoToList(String name, String slug) {
        fillName(name);
        fillSlug(slug);
        clickSave();
        verifySuccessCreateMessage();
        return openMenuTaskStatuses();
    }

    public TaskStatusesPage editStatusAndGoToList(String newName, String newSlug) {
        fillName(newName);
        fillSlug(newSlug);
        clickSave();
        verifySuccessEditMessage();
        return new TaskStatusesPage(driver);
    }
}
