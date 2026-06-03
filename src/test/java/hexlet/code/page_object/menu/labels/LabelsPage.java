package hexlet.code.page_object.menu.labels;

import hexlet.code.page_object.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public class LabelsPage extends HomePage {

    private static final String TABLE_ROWS_CSS = "[class~='RaDatagrid-selectable']";
    private static final String ROW_CHECKBOX_CSS = "tbody [type='checkbox']";
    private static final String ALERT = ".MuiSnackbarContent-message";

    @FindBy(css = "[class~='RaDatagrid-table']")
    private WebElement labelsTable;

    @FindBy(css = "[class~='MuiTableRow-head']")
    private WebElement tableHead;

    @FindBy(css = "[class~='RaDatagrid-tbody']")
    private WebElement tableBody;

    @FindBy(css = TABLE_ROWS_CSS)
    private List<WebElement> tableRows;

    @FindBy(css = "th[class~='column-name']")
    private WebElement nameColumn;

    @FindBy(css = "th[class~='column-id']")
    private WebElement id;

    @FindBy(css = "th[class~='column-createdAt']")
    private WebElement createdAt;

    @FindBy(css = "[href*='/labels/create']")
    private WebElement createLabelButton;

    @FindBy(css = "[aria-label='Delete']")
    private WebElement deleteButton;

    public LabelsPage(WebDriver driver) {
        super(driver);
    }

    public void verifyTableVisible() {
        checkVisibility(labelsTable, "Labels table");
        checkVisibility(tableHead, "Table head");
        checkVisibility(tableBody, "Table body");
    }

    public void verifyRequiredColumnsVisible() {
        checkVisibility(nameColumn, "Name column");
        checkVisibility(id, "Id");
        checkVisibility(createdAt, "Created at");
    }

    public void verifySuccessRowDeleteMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(ALERT)));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(ALERT), "Element deleted"));
    }

    public int getLabelsCount() {
        wait.until(ExpectedConditions.visibilityOf(labelsTable));
        return tableRows.size();
    }

    public boolean isLabelExist(String name) {
        try {
            return wait.until(driver -> {
                // ищем ячейки внутри строки row
                List<WebElement> rows = driver.findElements(By.cssSelector(TABLE_ROWS_CSS));

                // проверяем, что текст совпадает
                return rows.stream().anyMatch(row -> {
                    List<WebElement> nameCells = row.findElements(By.cssSelector("td.column-name"));
                    return !nameCells.isEmpty() && nameCells.getFirst().getText().trim().equals(name);
                });
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    public LabelFormPage openCreateLabelForm() {
        wait.until(ExpectedConditions.elementToBeClickable(createLabelButton)).click();
        wait.until(ExpectedConditions.invisibilityOf(labelsTable));
        return new LabelFormPage(driver);
    }

    public LabelFormPage openLastLabel() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector(TABLE_ROWS_CSS), 0));
        WebElement lastRow = tableRows.getLast();
        wait.until(ExpectedConditions.elementToBeClickable(lastRow))
                .click();
        return new LabelFormPage(driver);
    }

    public void deleteLastLabel() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector(TABLE_ROWS_CSS), 0));
        WebElement lastRow = tableRows.getLast();
        lastRow.findElement(By.cssSelector(ROW_CHECKBOX_CSS))
                .click();

        wait.until(ExpectedConditions.elementToBeClickable(deleteButton))
                .click();
    }
}
