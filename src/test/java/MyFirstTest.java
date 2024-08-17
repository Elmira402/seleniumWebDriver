import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MyFirstTest {

    private static WebDriver webDriver;
    private static WebDriverWait wait;

    @BeforeClass
    public static void start(){
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    @Test
    public void myFirstTest(){
        webDriver.get("https://www.google.com");
        webDriver.findElement(By.name("q")).sendKeys("webDriver");
        webDriver.findElement(By.name("q")).sendKeys(Keys.ENTER);
        System.out.println(webDriver.getTitle());
        Assert.assertEquals(webDriver.getTitle(), "webDriver - Поиск в Google");
    }

    @AfterClass
    public static void stop(){
        webDriver.quit();
        webDriver = null;
    }
}
