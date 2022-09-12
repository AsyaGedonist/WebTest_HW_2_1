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
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void ShouldFailNameV1AllEmpty() {
        driver.get("http://localhost:9999");
        driver.findElement(By.className("button__text")).click();

        List<WebElement> elements = driver.findElements(By.className("input__sub"));
        String text = elements.get(0).getText();

        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void ShouldFailNameV2NameEmpty() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79301111111");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();

        List<WebElement> elements = driver.findElements(By.className("input__sub"));
        String text = elements.get(0).getText();

        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void ShouldFailNameV3English() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("fff");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79301111111");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();

        List<WebElement> elements = driver.findElements(By.className("input__sub"));
        String text = elements.get(0).getText();

        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void ShouldFailNameV4Symbol() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Андрей%");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79301111111");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();

        List<WebElement> elements = driver.findElements(By.className("input__sub"));
        String text = elements.get(0).getText();

        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void ShouldFailPhoneV1Ten() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Андрей");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+7930111111");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();

        List<WebElement> elements = driver.findElements(By.className("input__sub"));
        String text = elements.get(1).getText();

        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void ShouldFailPhoneV2WithoutPlus() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Андрей");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("793011111111");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();

        List<WebElement> elements = driver.findElements(By.className("input__sub"));
        String text = elements.get(1).getText();

        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void ShouldFailPhoneV3Empty() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Андрей");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();

        List<WebElement> elements = driver.findElements(By.className("input__sub"));
        String text = elements.get(1).getText();

        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void ShouldFailCheckbox() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Андрей");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79301111111");
        driver.findElement(By.className("button__text")).click();

        String text = driver.findElement(By.className("input_invalid")).getText();

        assertEquals("Я соглашаюсь с условиями обработки и " +
                "использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text);
    }
}
