import net.bytebuddy.utility.RandomString;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Task11Test {
    private static WebDriver webDriver;
    private static WebDriverWait wait;

    @BeforeClass
    public static void start() {
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    @Test
    public void checkNewUserRegistrationTest() {
        String email = randomEmail();
        String password = randomString();

        webDriver.get("http://localhost/litecart/");
        webDriver.findElement(By.xpath("//div[@id='box-account-login']//a[contains(@href,'create_account')]"));
        webDriver.findElement(By.cssSelector("#box-account-login a[href*='create_account']")).click();

        webDriver.findElement(By.name("firstname")).sendKeys(randomString());
        webDriver.findElement(By.name("lastname")).sendKeys(randomString());
        webDriver.findElement(By.name("address1")).sendKeys(randomString());
        webDriver.findElement(By.name("postcode")).sendKeys("11111");
        webDriver.findElement(By.name("city")).sendKeys(randomString());

        WebElement countryElement = webDriver.findElement(By.xpath("//select[@name='country_code']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].selectedIndex = 224; arguments[0].dispatchEvent(new Event('change'))", countryElement);

        WebElement zone = webDriver.findElement(By.xpath("//select[@name='zone_code']"));
        JavascriptExecutor js2 = (JavascriptExecutor) webDriver;
        js2.executeScript("arguments[0].selectedIndex = 2", zone);

        webDriver.findElement(By.name("email")).sendKeys(email);
        WebElement phoneElement = webDriver.findElement(By.name("phone"));
        new Actions(webDriver)
                .click(phoneElement)
                .keyDown(Keys.HOME)
                .sendKeys("+12345678901")
                .perform();

        webDriver.findElement(By.name("password")).sendKeys(password);
        webDriver.findElement(By.name("confirmed_password")).sendKeys(password);

        webDriver.findElement(By.xpath("//button[@name='create_account']")).click();
        webDriver.findElement(By.xpath("//div[@id='box-account']//a[contains(@href,'logout')]")).click();

        webDriver.findElement(By.name("email")).sendKeys(email);
        webDriver.findElement(By.name("password")).sendKeys(password);
        webDriver.findElement(By.name("login")).click();
        webDriver.findElement(By.xpath("//div[@id='box-account']//a[contains(@href,'logout')]")).click();
    }

    private String randomString() {
        return RandomString.make(5).toLowerCase();
    }

    private String randomEmail() {
        return String.format("%s@gmail.com", randomString());
    }

    @AfterClass
    public static void stop() {
        webDriver.quit();
        webDriver = null;
    }
}
