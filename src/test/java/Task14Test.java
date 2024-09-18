import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Task14Test {

    private static WebDriver webDriver;
    private static WebDriverWait wait;

    @BeforeClass
    public static void start() {
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    @Test
    public void clickAllItemsInMenuAndCheckHeaders() {
        webDriver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        webDriver.findElement(By.name("username")).sendKeys("admin");
        webDriver.findElement(By.name("password")).sendKeys("admin");
        webDriver.findElement(By.name("login")).click();


        webDriver.findElement(By.xpath("//a[@class='button' and text()=' Add New Country']")).click();
        String mainHandle = webDriver.getWindowHandle();
        Set<String> windowHandles = webDriver.getWindowHandles();

        List<String> fieldNames = Arrays.asList("iso_code_2", "iso_code_3", "tax_id_format", "address_format", "postcode_format", "currency_code", "phone_code");
        fieldNames.forEach(fieldName -> {
            webDriver.findElement(By.name(fieldName)).findElement(By.xpath("//a[@target='_blank']"))
                    .click();
            String newWindow = wait.until(anyWindowOtherThan(windowHandles));
            webDriver.switchTo().window(newWindow);
            webDriver.close();
            webDriver.switchTo().window(mainHandle);
        });
    }

    private ExpectedCondition<String> anyWindowOtherThan(Set<String> oldWindows) {
        return driver -> {
            Set<String> handles = driver.getWindowHandles();
            handles.removeAll(oldWindows);
            return handles.size() > 0 ? handles.iterator().next() : null;
        };
    }

    @AfterClass
    public static void stop() {
        webDriver.quit();
        webDriver = null;
    }
}
