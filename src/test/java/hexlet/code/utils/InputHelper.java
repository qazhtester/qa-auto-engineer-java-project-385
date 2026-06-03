package hexlet.code.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class InputHelper {

    private InputHelper() {
    }

    public static void inputValue(WebDriver driver, WebElement element, String inputText) {
        element.click();

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Нативный сеттер value, чтобы React зафиксировал изменение
        js.executeScript(
                "var nativeInputValueSetter = "
                        + "Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;"
                        + "nativeInputValueSetter.call(arguments[0], arguments[1]);"
                        + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                element, inputText
        );

        // Небольшая пауза для обновления состояния (можно заменить на явное ожидание)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Убираем фокус, чтобы форма валидировалась
        js.executeScript(
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));"
                        + "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                element
        );
    }
}
