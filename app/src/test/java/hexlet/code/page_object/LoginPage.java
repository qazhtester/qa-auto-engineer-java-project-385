package hexlet.code.page_object;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("unused")
public class LoginPage extends BasePage {

    @FindBy(css = "[name='username']")
    private WebElement usernameField;

    @FindBy(css = "[name='password']")
    private WebElement passwordField;

    @FindBy(css = "[class~='RaLoginForm-button']")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open(String baseUrl) {
        driver.get(baseUrl);
    }

    public boolean isLoginPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(loginButton));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public HomePage login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
        wait.until(ExpectedConditions.invisibilityOf(loginButton));
        return new HomePage(driver);
    }

}
