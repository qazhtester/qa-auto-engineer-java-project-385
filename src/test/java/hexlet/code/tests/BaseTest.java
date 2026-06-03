package hexlet.code.tests;

import hexlet.code.page_object.HomePage;
import hexlet.code.page_object.LoginPage;
import hexlet.code.utils.TestDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class BaseTest {
    protected static String baseUrl;
    protected static String testLogin;
    protected static String testPassword;
    protected WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        baseUrl = System.getenv("APP_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "http://localhost:5173/";
        }

        testLogin = TestDataGenerator.randomLogin();
        testPassword = TestDataGenerator.randomPassword();
    }

    @BeforeEach
    public void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--nо-sandbox", "--headless");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected HomePage performLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(baseUrl);
        return loginPage.login(testLogin, testPassword);
    }
}
