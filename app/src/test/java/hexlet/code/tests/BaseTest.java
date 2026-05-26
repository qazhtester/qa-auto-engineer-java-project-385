package hexlet.code.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class BaseTest {
    protected static String BASE_URL;
    protected WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        BASE_URL = System.getenv("APP_BASE_URL");
        if (BASE_URL == null || BASE_URL.isEmpty()) {
            BASE_URL = "http://localhost:5173/";
        }
    }

    @BeforeEach
    public void setupTest() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
