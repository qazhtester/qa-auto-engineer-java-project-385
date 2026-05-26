package hexlet.code.page_object;

import hexlet.code.page_object.menupage.UsersPage;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("unused")
public class HomePage extends BasePage {

    @FindBy(css = "[class~='RaUserMenu-userButton']")
    private WebElement profileButton;

    @FindBy(css = "[class~='logout']")
    private WebElement logoutButton;

    @FindBy(css = "[href*='users']")
    private WebElement menuUsersButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isUserLoggedIn() {
        try {
            wait.until(ExpectedConditions.visibilityOf(profileButton));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @SuppressWarnings("ConstantConditions")
    public LoginPage logout() {
        wait.until(ExpectedConditions.visibilityOf(profileButton))
                .click();
        wait.until(ExpectedConditions.visibilityOf(logoutButton))
                .click();
        return new LoginPage(driver);
    }

    @SuppressWarnings("ConstantConditions")
    public UsersPage openMenuUsers() {
        wait.until(ExpectedConditions.elementToBeClickable(menuUsersButton))
                .click();
        wait.until(ExpectedConditions.attributeContains(menuUsersButton,
                "class",
                "RaMenuItemLink-active"));
        return new UsersPage(driver);
    }
}
