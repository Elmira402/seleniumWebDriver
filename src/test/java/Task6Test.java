import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class Task6Test {

    private static WebDriver webDriver;

    @Before
    public void start() {
        webDriver = new ChromeDriver();
    }

    @Test
    public void clickAllItemsInMenuAndCheckHeaders() {
        webDriver.get("http://localhost/litecart/admin/");
        webDriver.findElement(By.name("username")).sendKeys("admin");
        webDriver.findElement(By.name("password")).sendKeys("admin");
        webDriver.findElement(By.name("login")).click();

        By menuItemsXpath = By.xpath("//li[@id='app-']//span[@class='name']");
        By menuSubItemsXpath = By.xpath("//ul[@class='docs']//span[@class='name']");
        By headerXpath = By.xpath("//h1");

        Assert.assertTrue("Не найдены элементы пунктов в меню", areElementsPresent(webDriver, menuItemsXpath));
        List<String> itemNames = getTextFromElements(webDriver, menuItemsXpath);
        itemNames.forEach(itemName -> {
            webDriver.findElement(getMenuElementByText(itemName)).click();
            Assert.assertTrue("Не найден элемент заголовка раздела ".concat(itemName), isElementPresent(webDriver, headerXpath));
            if (areElementsPresent(webDriver, menuSubItemsXpath)) {
                List<String> subRegistryNames = getTextFromElements(webDriver, menuSubItemsXpath);
                subRegistryNames.forEach(subRegistryName -> {
                    webDriver.findElement(getMenuElementByText(subRegistryName)).click();
                    Assert.assertTrue("Не найден элемент заголовка подраздела ".concat(subRegistryName),isElementPresent(webDriver, headerXpath));
                });
            }
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

    boolean areElementsPresent(WebDriver driver, By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    List<String> getTextFromElements(WebDriver driver, By locator) {
        List<WebElement> elements = driver.findElements(locator);
        List<String> elementsText = new ArrayList<>();
        elements.forEach(registry -> elementsText.add(registry.getText()));
        return elementsText;
    }

    By getMenuElementByText(String itemName) {
        return By.xpath(String.format("//li[@id='app-']//span[@class='name' and text()='%s']", itemName));
    }

    @After
    public void stop() {
        webDriver.quit();
        webDriver = null;
    }
}
