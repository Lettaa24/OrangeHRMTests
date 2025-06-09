package org.orangehrm;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Tests {

    private WebDriver driver;

    @BeforeAll
    void setupDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void init() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("1. Перевірка успішного входу")
    void testSuccessfulLogin() throws InterruptedException {
        Thread.sleep(1000);

        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        usernameField.sendKeys("Admin");
        passwordField.sendKeys("admin123");
        loginButton.click();

        Thread.sleep(2000);

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/dashboard"), "Повинен відбутись перехід на /dashboard");
    }

    @Test
    @DisplayName("2. Перевірка помилкового входу (неправильний пароль)")
    void testInvalidLogin() throws InterruptedException {
        Thread.sleep(1000);

        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        usernameField.sendKeys("Admin");
        passwordField.sendKeys("wrongPassword");
        loginButton.click();

        Thread.sleep(2000);

        WebElement errorMessage = driver.findElement(By.cssSelector(".oxd-alert-content-text"));
        assertTrue(errorMessage.isDisplayed(), "Повідомлення про помилку має з’явитися");
        assertEquals("Invalid credentials", errorMessage.getText(), "Очікуваний текст повідомлення про помилку");
    }

    @Test
    @DisplayName("3. Перевірка переходу на правильну сторінку після входу")
    void testDashboardRedirection() throws InterruptedException {
        Thread.sleep(1000);

        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        usernameField.sendKeys("Admin");
        passwordField.sendKeys("admin123");
        loginButton.click();

        Thread.sleep(2000);

        WebElement dashboardHeader = driver.findElement(By.tagName("h6"));
        assertTrue(dashboardHeader.isDisplayed(), "Заголовок Dashboard має бути видно");
        assertEquals("Dashboard", dashboardHeader.getText(), "Має бути напис 'Dashboard'");
    }
}
