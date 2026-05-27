package hexlet.code.page_object.menu.users;

import hexlet.code.page_object.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public class UsersPage extends HomePage {

    @FindBy(css = "[class~='RaDatagrid-table']")
    private WebElement usersTable;

    @FindBy(css = "[class~='MuiTableRow-head']")
    private WebElement tableHead;

    @FindBy(css = "[class~='RaDatagrid-tbody']")
    private WebElement tableBody;

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

    public UsersPage(WebDriver driver) {
        super(driver);
    }

    public void verifyUserTableVisible() {
        checkVisibility(usersTable, "Table");
        checkVisibility(tableHead, "Table Head");
        checkVisibility(tableHead, "Table Body");
    }

    public void verifyRequiredColumnsVisible() {
        checkVisibility(emailColumn, "Email column");
        checkVisibility(firstNameColumn, "First name column");
        checkVisibility(lastNameColumn, "Last name column");
    }

    public int getUsersCount() {
        wait.until(ExpectedConditions.visibilityOf(usersTable));
        return tableRows.size();
    }

    public boolean isUserExist(String email, String firstName, String lastName) {
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

    public UserFormPage openCreateUserForm() {
        wait.until(ExpectedConditions.elementToBeClickable(createUserButton))
                .click();
        wait.until(ExpectedConditions.invisibilityOf(usersTable));
        return new UserFormPage(driver);
    }

    public UserFormPage openLastUser() {
        WebElement lastRow = tableRows.getLast();
        wait.until(ExpectedConditions.elementToBeClickable(lastRow))
                .click();
        return new UserFormPage(driver);
    }

    private void checkVisibility(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            throw new RuntimeException("Element not visible: " + elementName, e);
        }
    }
}
