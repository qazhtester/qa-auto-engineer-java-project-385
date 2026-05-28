package hexlet.code.utils;

import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class InputHelper {

    // В данном приложении без Robot значения установить невозможно. React возвращает всё к первоначальному значению.
    public static void inputValue(WebElement element, String inputText) {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(100); // задержка между командами

            // 1. Клик и фокус на поле
            element.click();
            robot.delay(500);

            // 2. Очистка: Ctrl+A, Delete
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.delay(500);
            robot.keyPress(KeyEvent.VK_DELETE);
            robot.keyRelease(KeyEvent.VK_DELETE);
            robot.delay(500);

            // 3. Ввод inputText: проще через буфер обмена (для длинных строк)
            StringSelection stringSelection = new StringSelection(inputText);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

            // Ctrl+V
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.delay(500);

            // 4. Потеря фокуса (Tab)
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.delay(500);
        } catch (AWTException e) {
            throw new RuntimeException("Robot not supported", e);
        }
    }
}
