package hexlet.code.page_object.menu.labels;

import hexlet.code.page_object.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("unused")
public class LabelFormPage extends HomePage {

    private static final String ALERT = ".MuiSnackbarContent-message";

    @FindBy(css = "input[name='name']")
    private WebElement nameInput;

    @FindBy(css = "button[type='submit']")
    private WebElement saveButton;

    public LabelFormPage(WebDriver driver) {
        super(driver);
    }

    public void fillName(String name) {
        typeText(nameInput, name);
    }

    public void verifyFormElementsVisible() {
        checkVisibility(nameInput, "Name");
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

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton))
                .click();
    }

    public LabelsPage createLabelAndGoToList(String name) {
        fillName(name);
        clickSave();
        verifySuccessCreateMessage();
        return openMenuLabels();
    }

    public LabelsPage editLabelAndGoToList(String newName) {
        fillName(newName);
        clickSave();
        verifySuccessEditMessage();
        return new LabelsPage(driver);
    }
}
