package hexlet.code.page_object.menu.users;

import hexlet.code.page_object.HomePage;
import hexlet.code.utils.Utils;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("unused")
public class UserFormPage extends HomePage {

    @FindBy(css = "[name='email']")
    private WebElement emailInput;

    @FindBy(css = "[name='firstName']")
    private WebElement firstNameInput;

    @FindBy(css = "[name='lastName']")
    private WebElement lastNameInput;

    @FindBy(css = "[aria-label='Save']")
    private WebElement saveButton;

    @FindBy(css = ".MuiSnackbarContent-message")
    private WebElement alert;

    @FindBy(css = "p.Mui-error")
    private WebElement validateErrorText;

    public UserFormPage(WebDriver driver) {
        super(driver);
    }

    public void verifyFormElementsVisible() {
        checkVisibility(emailInput, "Email");
        checkVisibility(firstNameInput, "First name");
        checkVisibility(lastNameInput, "Last name");
        checkVisibility(saveButton, "Save");
    }

    public void fillForm(String email, String firstName, String lastName) {
        typeText(emailInput, email);
        typeText(firstNameInput, firstName);
        typeText(lastNameInput, lastName);
    }

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        saveButton.click();
    }

    public String getEmailValue() {
        return emailInput.getAttribute("value");
    }

    public String getFirstNameValue() {
        return firstNameInput.getAttribute("value");
    }

    public String getLastNameValue() {
        return lastNameInput.getAttribute("value");
    }

    public void enterEmail(String email) {
        typeText(emailInput, email);
    }

    public void verifySuccessCreateMessage() {
        wait.until(ExpectedConditions.visibilityOf(alert));
        wait.until(ExpectedConditions.textToBePresentInElement(alert, "Element created"));
    }

    public void verifySuccessEditMessage() {
        wait.until(ExpectedConditions.visibilityOf(alert));
        wait.until(ExpectedConditions.textToBePresentInElement(alert, "Element updated"));
    }

    public void verifyValidationErrorMessage(String expectedAlertText, String expectedFieldErrorText) {
        wait.until(ExpectedConditions.visibilityOf(alert));
        wait.until(ExpectedConditions.visibilityOf(validateErrorText));

        wait.until(ExpectedConditions.textToBePresentInElement(alert, expectedAlertText));
        wait.until(ExpectedConditions.textToBePresentInElement(validateErrorText, expectedFieldErrorText));
    }

    public UsersPage createUserAndGoToList(String email, String firstName, String lastName) {
        fillForm(email, firstName, lastName);
        clickSave();
        verifySuccessCreateMessage();
        return openMenuUsers();
    }

    public UsersPage editUserAndGoToList(String email, String firstName, String lastName) {
        fillForm(email, firstName, lastName);
        clickSave();
        verifySuccessEditMessage();
        return new UsersPage(driver);
    }

    private void typeText(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        Utils.inputValue(element, text);
    }

    private void checkVisibility(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            throw new RuntimeException("Form element was not visible: " + elementName, e);
        }
    }
}
