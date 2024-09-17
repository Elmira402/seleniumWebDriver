import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Task7Test {
    private static WebDriver webDriver;
    private static WebDriverWait wait;

    @BeforeClass
    public static void start() {
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    @Test
    public void clickAllItemsInMenuAndCheckHeaders() {
        webDriver.get("http://localhost/litecart/");

        By allProductsXpath = By.xpath("//li[contains(@class,'product')]");
        By stickersXpath = By.xpath(".//div[contains(@class,'sticker')]");

        List<WebElement> products = webDriver.findElements(allProductsXpath);
        Assertions.assertThat(products).as("Не найдены элементы продуктов").isNotEmpty();
        SoftAssertions assertions = new SoftAssertions();

        for (int i = 0; i < products.size(); i++) {
            assertions.assertThat(
                    products.get(i).findElements(stickersXpath)).as(String.format("У продукта [%d] количество стикеров не равно 1", i)).hasSize(1);
        }
        assertions.assertAll();
    }


    @After
    public void stop() {
        webDriver.quit();
        webDriver = null;
    }
}
