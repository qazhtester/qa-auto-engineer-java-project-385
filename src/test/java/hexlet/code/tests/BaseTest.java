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
    protected static String BASE_URL;
    protected static String TEST_LOGIN;
    protected static String TEST_PASSWORD;
    protected WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        BASE_URL = System.getenv("APP_BASE_URL");
        if (BASE_URL == null || BASE_URL.isEmpty()) {
            BASE_URL = "http://localhost:5173/";
        }

        TEST_LOGIN = TestDataGenerator.randomLogin();
        TEST_PASSWORD = TestDataGenerator.randomPassword();
    }

    @BeforeEach
    public void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
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
        loginPage.open(BASE_URL);
        return loginPage.login(TEST_LOGIN, TEST_PASSWORD);
    }
}
