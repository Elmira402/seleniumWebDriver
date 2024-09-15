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

public class Task8Test {

    static WebDriver webDriver;
    static WebDriverWait wait;

    @BeforeClass
    public static void start() {
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    @Test
    public void checkSortCountriesAndZonesTest() {
        webDriver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        webDriver.findElement(By.name("username")).sendKeys("admin");
        webDriver.findElement(By.name("password")).sendKeys("admin");
        webDriver.findElement(By.name("login")).click();

        WebElement countriesForm = webDriver.findElement(By.name("countries_form"));
        String cellIndexNameCountry = webDriver.findElement(By.xpath("//tr[@class='header']/th[text()='Name']")).getAttribute("cellIndex");
        String cellIndexZonesCountry = webDriver.findElement(By.xpath("//tr[@class='header']/th[text()='Zones']")).getAttribute("cellIndex");

        List<WebElement> countries = countriesForm.findElements(By.xpath(".//a[@href and text()]"));
        List<String> actualTextFromCountries = getTextFromElements(countries);
        Assertions.assertThat(actualTextFromCountries).isSorted().as("Список стран расположен не в алфавитном порядке")
                .isEqualTo(actualTextFromCountries);

        List<WebElement> countryTableRows = countriesForm.findElements(By.cssSelector("tr.row"));
        List<String> countriesWithZone = new ArrayList<>();
        countryTableRows.forEach(row -> {
            List<WebElement> cells = row.findElements(By.xpath(".//td"));
            if (!cells.get(Integer.parseInt(cellIndexZonesCountry)).getText().equals("0")) {
                WebElement cellWithCountryName = cells.get(Integer.parseInt(cellIndexNameCountry));
                countriesWithZone.add(cellWithCountryName.findElement(By.xpath(".//a[@href]")).getText());
            }
        });
        countriesWithZone.forEach(countryWithZone -> {
            webDriver.findElement(getCountryElementByText(countryWithZone)).click();
            List<WebElement> zones = webDriver.findElements(By.xpath("//input[contains(@name,'][name]')]/.."));
            List<String> actualTextFromZones = getTextFromElements(zones);
            Assertions.assertThat(actualTextFromZones).isSorted().as("Список зон расположен не в алфавитном порядке")
                    .isEqualTo(actualTextFromZones);
            webDriver.navigate().back();
        });
    }


    private List<String> getTextFromElements(List<WebElement> elements) {
        List<String> elementsText = new ArrayList<>();
        elements.forEach(registry -> elementsText.add(registry.getText()));
        return elementsText;
    }

    private By getCountryElementByText(String itemName) {
        return By.xpath(String.format("//form[@name='countries_form']//a[@href and text()='%s']", itemName));
    }

    @AfterClass
    public static void stop() {
        webDriver.quit();
        webDriver = null;
    }
}
