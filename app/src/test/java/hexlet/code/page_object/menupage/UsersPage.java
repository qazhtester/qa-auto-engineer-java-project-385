package hexlet.code.page_object.menupage;

import hexlet.code.page_object.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@SuppressWarnings("unused")
public class UsersPage extends HomePage {

    @FindBy(css = "[class~='RaDatagrid-table']")
    private WebElement usersTable;

    @FindBy(css = "[class~='MuiTableRow-head']")
    private WebElement tableHead;

    @FindBy(css = "[class~='RaDatagrid-selectable']")
    private List<WebElement> tableRows;

    @FindBy(css = "th[class~='column-email']")
    private WebElement emailColumn;

    @FindBy(css = "th[class~='column-firstName']")
    private WebElement firstNameColumn;

    @FindBy(css = "th[class~='column-lastName']")
    private WebElement lastNameColumn;

    @FindBy(css = "[href*='/users/create']")
    private WebElement createUserButton;

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

    public UsersPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoadUserTable() {
        try {
            wait.until(ExpectedConditions.visibilityOf(usersTable));
            wait.until(ExpectedConditions.visibilityOfAllElements(tableRows));
            wait.until(ExpectedConditions.visibilityOf(tableHead));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isTableContainsRequiredFields() {
        try {
            wait.until(ExpectedConditions.visibilityOf(emailColumn));
            wait.until(ExpectedConditions.visibilityOf(firstNameColumn));
            wait.until(ExpectedConditions.visibilityOf(lastNameColumn));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public int getUsersCount() {
        wait.until(ExpectedConditions.visibilityOf(usersTable));
        return tableRows.size();
    }

    @SuppressWarnings("ConstantConditions")
    public void openCreateUsersForm() {
        wait.until(ExpectedConditions.elementToBeClickable(createUserButton))
                .click();
    }

    public boolean isCreateUsersFormCorrectDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(emailInput));
            wait.until(ExpectedConditions.visibilityOf(firstNameInput));
            wait.until(ExpectedConditions.visibilityOf(lastNameInput));
            wait.until(ExpectedConditions.visibilityOf(saveButton));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void fillAndSaveCreateUsersForm(String email, String firstName, String lastName) {
        emailInput.clear();
        emailInput.sendKeys(email);

        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);

        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);

        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        saveButton.click();

        wait.until(ExpectedConditions.textToBePresentInElement(alert, "Element created"));
    }

    public boolean isUserPresent(String email, String firstName, String lastName) {
        wait.until(ExpectedConditions.visibilityOf(usersTable));
        return tableRows.stream().anyMatch(row -> {
            // ищем ячейки внутри строки row
            List<WebElement> emailCells = row.findElements(By.cssSelector("td.column-email"));
            List<WebElement> firstNameCells = row.findElements(By.cssSelector("td.column-firstName"));
            List<WebElement> lastNameCells = row.findElements(By.cssSelector("td.column-lastName"));

            // проверяем, что все три ячейки найдены и их текст совпадает
            return !emailCells.isEmpty() && emailCells.getFirst().getText().trim().equals(email)
                    && !firstNameCells.isEmpty() && firstNameCells.getFirst().getText().trim().equals(firstName)
                    && !lastNameCells.isEmpty() && lastNameCells.getFirst().getText().trim().equals(lastName);
        });
    }
}
