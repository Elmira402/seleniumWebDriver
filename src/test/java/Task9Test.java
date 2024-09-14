import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Task9Test {

    static WebDriver webDriver;
    static WebDriverWait wait;

    @BeforeClass
    public static void start() {
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    @Test
    public void checkSortCountriesAndZonesTest() {
        webDriver.get("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
        webDriver.findElement(By.name("username")).sendKeys("admin");
        webDriver.findElement(By.name("password")).sendKeys("admin");
        webDriver.findElement(By.name("login")).click();

        WebElement geoZonesFormForm = webDriver.findElement(By.name("geo_zones_form"));
        List<WebElement> countries = geoZonesFormForm.findElements(By.xpath(".//a[@href and text()]"));
        for (int i = 0; i < countries.size(); i++) {
            webDriver.findElement(By.xpath(String.format("//a[contains(@href,'geo_zone_id=%d')]", i + 1))).click();
            List<WebElement> actualZones = webDriver.findElements(By.xpath("//select[contains(@name,'[zone_code]')]/option[@selected]"));
            List<String> textFromZones = getTextFromElements(actualZones);
            Assertions.assertThat(textFromZones).isSorted().as("Список геозон расположен не в алфавитном порядке")
                    .isEqualTo(textFromZones);
            webDriver.navigate().back();
        }
    }


    private List<String> getTextFromElements(List<WebElement> elements) {
        List<String> elementsText = new ArrayList<>();
        elements.forEach(registry -> elementsText.add(registry.getText()));
        return elementsText;
    }

    @AfterClass
    public static void stop() {
        webDriver.quit();
        webDriver = null;
    }
}
