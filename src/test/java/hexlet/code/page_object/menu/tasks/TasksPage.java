package hexlet.code.page_object.menu.tasks;

import hexlet.code.page_object.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class TasksPage extends HomePage {

    private static final String COLUMN = "//div[@data-rfd-droppable-id]/..";
    private static final String CARD = "[data-rfd-draggable-id]";
    private static final String CARD_TITLE = ".MuiTypography-h5";
    private static final String COLUMN_HEADER = "h6";
    private static final String EDIT_BUTTON = "a[aria-label='Edit']";
    private static final String FILTER = "[role='combobox']";
    private static final String LIST_FILTER = "[role='listbox']";
    private static final String OPTION_FILTER = "[role='option']";
    private static final String DATA_SOURCE_TEMPLATE = "[data-source='%s']";
    private static final String COLUMN_ANCESTOR = "ancestor::div[@data-rfd-droppable-id]/..";
    private static final String ALERT = ".MuiSnackbarContent-message";

    @FindBy(css = "a[href='#/tasks/create']")
    private WebElement createTaskButton;

    @FindBy(xpath = COLUMN)
    private List<WebElement> columns;

    public TasksPage(WebDriver driver) {
        super(driver);
    }

    public boolean isTaskPresent(String taskName) {
        try {
            return wait.until(driver -> getAllTaskNames().contains(taskName));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getColumnContainingCard(String taskName) {
        WebElement card = findCardByName(taskName);
        WebElement header = card.findElement(By.xpath(COLUMN_ANCESTOR))
                .findElement(By.cssSelector(COLUMN_HEADER));
        return header.getText().trim();
    }

    public List<String> getAllTaskNames() {
        return getAllCards().stream()
                .map(card -> card.findElement(By.cssSelector(CARD_TITLE)).getText().trim())
                .collect(Collectors.toList());
    }

    public TaskFormPage openCreateTaskForm() {
        wait.until(ExpectedConditions.elementToBeClickable(createTaskButton)).click();
        wait.until(ExpectedConditions.invisibilityOfAllElements(columns));
        return new TaskFormPage(driver);
    }

    public TaskFormPage openEditTaskByName(String taskName) {
        WebElement card = findCardByName(taskName);
        WebElement editButton = card.findElement(By.cssSelector(EDIT_BUTTON));
        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
        wait.until(ExpectedConditions.invisibilityOfAllElements(columns));
        return new TaskFormPage(driver);
    }

    public void filterByStatus(String statusName) {
        selectFilterOption("status_id", statusName);
    }

    public void filterByAssignee(String assigneeName) {
        selectFilterOption("assignee_id", assigneeName);
    }

    public void filterByLabel(String labelName) {
        selectFilterOption("label_id", labelName);
    }

    public void verifyBoardVisible() {
        wait.until(ExpectedConditions.visibilityOfAllElements(columns));
    }

    public void verifySuccessDeleteMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(ALERT)));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(ALERT), "Element deleted"));
    }

    public void verifyAllCardsInColumn(String columnName) {
        wait.until(driver -> {
            List<WebElement> cards = driver.findElements(By.cssSelector(CARD));
            if (cards.isEmpty()) {
                return true;
            }
            for (WebElement card : cards) {
                WebElement column = card.findElement(By.xpath(COLUMN_ANCESTOR));
                String header = column.findElement(By.cssSelector(COLUMN_HEADER)).getText().trim();
                if (!header.equals(columnName)) {
                    return false;
                }
            }
            return true;
        });
    }

    public void verifyCardListChanged(List<String> originalNames) {
        wait.until(driver -> {
            List<String> currentNames = getAllTaskNames();
            return !currentNames.equals(originalNames);
        });
    }

    private void selectFilterOption(String dataSource, String optionText) {
        String selector = String.format(DATA_SOURCE_TEMPLATE, dataSource);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));

        WebElement filter = driver.findElement(By.cssSelector(selector));
        WebElement combobox = filter.findElement(By.cssSelector(FILTER));
        combobox.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(LIST_FILTER)));
        List<WebElement> options = driver.findElements(By.cssSelector(OPTION_FILTER));
        for (WebElement option : options) {
            if (option.getText().trim().equals(optionText)) {
                option.click();
                break;
            }
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(CARD)));
    }

    private List<WebElement> getAllCards() {
        return driver.findElements(By.cssSelector(CARD));
    }

    private WebElement findCardByName(String taskName) {
        return getAllCards().stream()
                .filter(card -> {
                    WebElement titleEl = card.findElement(By.cssSelector(CARD_TITLE));
                    return titleEl.getText().trim().equals(taskName);
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Card not found: " + taskName));
    }
}