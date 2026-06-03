package hexlet.code.page_object.menu.users;

import hexlet.code.page_object.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("unused")
public class UserFormPage extends HomePage {

    private static final String ATTRIBUTE_VALUE = "value";
    private static final String ALERT = ".MuiSnackbarContent-message";

    @FindBy(css = "[name='email']")
    private WebElement emailInput;

    @FindBy(css = "[name='firstName']")
    private WebElement firstNameInput;

    @FindBy(css = "[name='lastName']")
    private WebElement lastNameInput;

    @FindBy(css = "[aria-label='Save']")
    private WebElement saveButton;

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
        wait.until(ExpectedConditions.elementToBeClickable(saveButton))
                .click();
    }

    public String getEmailValue() {
        return emailInput.getAttribute(ATTRIBUTE_VALUE);
    }

    public String getFirstNameValue() {
        return firstNameInput.getAttribute(ATTRIBUTE_VALUE);
    }

    public String getLastNameValue() {
        return lastNameInput.getAttribute(ATTRIBUTE_VALUE);
    }

    public void enterEmail(String email) {
        typeText(emailInput, email);
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

    public void verifyValidationErrorMessage(String expectedAlertText, String expectedFieldErrorText) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(ALERT)));
        wait.until(ExpectedConditions.visibilityOf(validateErrorText));

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(ALERT), expectedAlertText));
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
}
