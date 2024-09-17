import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class Task13Test {

    static WebDriver webDriver;
    static WebDriverWait wait;

    @BeforeClass
    public static void start() {
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    @Test
    public void checkRemoveProductTest() {
        webDriver.get("http://localhost/litecart/");

        for (int i = 1; i < 4; i++) {
            webDriver.findElement(By.cssSelector("li[class^='product']")).click();
            WebElement quantity = webDriver.findElement(By.id("cart")).findElement(By.className("quantity"));

            if (isElementPresent(webDriver, By.name("options[Size]"))) {
                new Select(webDriver.findElement(By.name("options[Size]")))
                        .selectByIndex(1);
            }
            webDriver.findElement(By.name("add_cart_product")).click();
            wait.until(attributeContains(quantity, "textContent", String.valueOf(i)));

            webDriver.navigate().back();
        }

        webDriver.findElement(By.id("cart")).findElement(By.xpath("//a[text()='Checkout »']")).click();
        webDriver.findElements(By.name("cart_form")).forEach(e -> {
            List<WebElement> elements = webDriver.findElements(By.xpath("//div[@id='order_confirmation-wrapper']//td[@class='sku']"));
            wait.until(visibilityOfElementLocated(By.name("remove_cart_item"))).click();
            wait.until(stalenessOf(elements.get(0)));
        });
    }

    boolean isElementPresent(WebDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    @After
    public void stop() {
        webDriver.quit();
        webDriver = null;
    }
}
