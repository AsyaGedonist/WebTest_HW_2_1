package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSend () {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей Андрей-Андреевич");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79301111111");
        driver.findElement(By.cssSelector("[data-test-id='agreement'].checkbox")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String text = driver.findElement(By.className("paragraph")).getText();

        assertEquals("  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text);
    }

    @Test
    void allEmptyTestNameAlert() {
        driver.findElement(By.cssSelector("[type='button']")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void allEmptyTestPhoneAlert() {
        driver.findElement(By.cssSelector("[type='button']")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_type_tel .input__sub")).getText();
        assertEquals("На указанный номер моб. тел. будет отправлен смс-код для подтверждения заявки на карту. " +
                "Проверьте, что номер ваш и введен корректно.", text);
    }


    @Test
    void onlyNameEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79301111111");
        driver.findElement(By.cssSelector("[data-test-id='agreement'].checkbox")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void nameEnglish() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("fff");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79301111111");
        driver.findElement(By.cssSelector("[data-test-id='agreement'].checkbox")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void nameSymbol() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей%");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79301111111");
        driver.findElement(By.cssSelector("[data-test-id='agreement'].checkbox")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void phoneTenSymbols() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7930111111");
        driver.findElement(By.cssSelector("[data-test-id='agreement'].checkbox")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_type_tel .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void phoneTwelveSymbols() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+793011111111");
        driver.findElement(By.cssSelector("[data-test-id='agreement'].checkbox")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_type_tel .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void phoneWithoutPlus() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("793011111111");
        driver.findElement(By.cssSelector("[data-test-id='agreement'].checkbox")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_type_tel .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void onlyPhoneEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей");
        driver.findElement(By.cssSelector("[data-test-id='agreement'].checkbox")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_type_tel .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void onlyCheckBoxEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79301111111");
        driver.findElement(By.cssSelector("[type='button']")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).getText();

        assertEquals("Я соглашаюсь с условиями обработки и " +
                "использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text);
    }
}
