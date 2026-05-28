package hexlet.code.page_object.menu.statuses;

import hexlet.code.page_object.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public class TaskStatusesPage extends HomePage {

    private static final String TABLE_ROWS_CSS = "[class~='RaDatagrid-selectable']";
    private static final String ROW_CHECKBOX_CSS = "tbody [type='checkbox']";
    private static final String ALERT = ".MuiSnackbarContent-message";

    @FindBy(css = "[class~='RaDatagrid-table']")
    private WebElement statusesTable;

    @FindBy(css = "[class~='MuiTableRow-head']")
    private WebElement tableHead;

    @FindBy(css = "[class~='RaDatagrid-tbody']")
    private WebElement tableBody;

    @FindBy(css = TABLE_ROWS_CSS)
    private List<WebElement> tableRows;

    @FindBy(css = "th[class~='column-name']")
    private WebElement nameColumn;

    @FindBy(css = "th[class~='column-slug']")
    private WebElement slugColumn;

    @FindBy(css = "[href*='/task_statuses/create']")
    private WebElement createStatusButton;

    @FindBy(css = "[aria-label='Select all']")
    private WebElement headCheckbox;

    @FindBy(css = "[aria-label='Delete']")
    private WebElement deleteButton;

    @FindBy(css = ".MuiSnackbarContent-message")
    private WebElement alert;

    @FindBy(css = ".RaList-noResults")
    private WebElement noResultsBlock;

    public TaskStatusesPage(WebDriver driver) {
        super(driver);
    }

    public void verifyTableVisible() {
        checkVisibility(statusesTable, "Statuses table");
        checkVisibility(tableHead, "Table head");
        checkVisibility(tableBody, "Table body");
    }

    public void verifyRequiredColumnsVisible() {
        checkVisibility(nameColumn, "Name column");
        checkVisibility(slugColumn, "Slug column");
    }

    public void verifySuccessRowDeleteMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(ALERT)));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(ALERT), "Element deleted"));
    }

    public void verifySuccessSomeDeleteMessage(int numberRows) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(ALERT)));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(ALERT), numberRows + " elements deleted"));
    }

    public void verifySuccessAllStatusesDelete(int numberRows) {
        verifySuccessSomeDeleteMessage(numberRows);

        checkVisibility(noResultsBlock, "noResultsBlock");
        wait.until(ExpectedConditions.textToBePresentInElement(noResultsBlock, "No Task statuses yet."));
        wait.until(ExpectedConditions.textToBePresentInElement(noResultsBlock, "Do you want to add one?"));
    }

    public int getStatusesCount() {
        wait.until(ExpectedConditions.visibilityOf(statusesTable));
        return tableRows.size();
    }

    public boolean isStatusExist(String name, String slug) {
        wait.until(ExpectedConditions.visibilityOf(statusesTable));
        return tableRows.stream().anyMatch(row -> {
            // ищем ячейки внутри строки row
            List<WebElement> nameCells = row.findElements(By.cssSelector("td.column-name"));
            List<WebElement> slugCells = row.findElements(By.cssSelector("td.column-slug"));

            // проверяем, что все три ячейки найдены и их текст совпадает
            return !nameCells.isEmpty() && nameCells.getFirst().getText().trim().equals(name)
                    && !slugCells.isEmpty() && slugCells.getFirst().getText().trim().equals(slug);
        });
    }

    public StatusFormPage openCreateStatusForm() {
        wait.until(ExpectedConditions.elementToBeClickable(createStatusButton)).click();
        wait.until(ExpectedConditions.invisibilityOf(statusesTable));
        return new StatusFormPage(driver);
    }

    public StatusFormPage openLastStatus() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector(TABLE_ROWS_CSS), 0));
        WebElement lastRow = tableRows.getLast();
        wait.until(ExpectedConditions.elementToBeClickable(lastRow)).click();
        return new StatusFormPage(driver);
    }

    public void deleteLastStatus() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(TABLE_ROWS_CSS), 0));
        WebElement lastRow = tableRows.getLast();
        lastRow.findElement(By.cssSelector(ROW_CHECKBOX_CSS))
                .click();

        wait.until(ExpectedConditions.elementToBeClickable(deleteButton))
                .click();
    }

    public void deleteAllStatuses() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(TABLE_ROWS_CSS), 0));

        headCheckbox.click();
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton))
                .click();
    }
}