import net.bytebuddy.utility.RandomString;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class Task12Test {

    private static WebDriver webDriver;

    @Before
    public void start() {
        webDriver = new ChromeDriver();
    }

    @Test
    public void clickAllItemsInMenuAndCheckHeaders() throws InterruptedException, IOException {
        webDriver.get("http://localhost/litecart/admin/");
        webDriver.findElement(By.name("username")).sendKeys("admin");
        webDriver.findElement(By.name("password")).sendKeys("admin");
        webDriver.findElement(By.name("login")).click();

        webDriver.findElement(By.xpath("//ul[@id='box-apps-menu']//span[@class='name' and text()='Catalog']")).click();
        webDriver.findElement(By.xpath("//a[text()=' Add New Product']")).click();

        WebElement tabGeneral = webDriver.findElement(By.id("tab-general"));
        String nameProduct = randomString("Happy duck");
        tabGeneral.findElement(By.xpath("//input[@name='status' and @value='1']")).click();
        tabGeneral.findElement(By.name("name[en]")).sendKeys(nameProduct);
        tabGeneral.findElement(By.name("code")).sendKeys(randomString("code"));
        tabGeneral.findElements(By.name("product_groups[]")).get(randomInt(0, 2)).click();
        tabGeneral.findElement(By.name("quantity")).sendKeys(randomInt(1, 100).toString());

        new Select(tabGeneral.findElement(By.name("quantity_unit_id")))
                .selectByIndex(1);
        new Select(tabGeneral.findElement(By.name("delivery_status_id")))
                .selectByIndex(1);
        new Select(tabGeneral.findElement(By.name("sold_out_status_id")))
                .selectByIndex(randomInt(1, 2));

        File file = new File("src/test/resources/duck.jpeg");
        String canonicalPath = file.getCanonicalPath();
        tabGeneral.findElement(By.name("new_images[]")).sendKeys(canonicalPath);
        tabGeneral.findElement(By.name("date_valid_from")).sendKeys(generateDate(0));
        tabGeneral.findElement(By.name("date_valid_to")).sendKeys(generateDate(1));

        webDriver.findElement(By.xpath("//a[@href='#tab-information']")).click();
        Thread.sleep(2000);
        WebElement tabInformation = webDriver.findElement(By.id("tab-information"));
        new Select(tabInformation.findElement(By.name("manufacturer_id")))
                .selectByIndex(1);
        new Select(tabInformation.findElement(By.name("supplier_id")))
                .selectByIndex(0);

        tabInformation.findElement(By.name("keywords")).sendKeys(randomString("keywords"));
        tabInformation.findElement(By.name("short_description[en]")).sendKeys(randomString("short_description"));
        tabInformation.findElement(By.xpath(".//*[@name='description[en]']/../div[@contenteditable='true']")).sendKeys(randomString("desc"));

        tabInformation.findElement(By.name("head_title[en]")).sendKeys(randomString("head_title"));
        tabInformation.findElement(By.name("meta_description[en]")).sendKeys(randomString("meta_description"));

        webDriver.findElement(By.xpath("//a[@href='#tab-prices']")).click();
        Thread.sleep(2000);
        WebElement tabPrices = webDriver.findElement(By.id("tab-prices"));
        tabPrices.findElement(By.name("purchase_price")).sendKeys("10");
        new Select(tabPrices.findElement(By.name("purchase_price_currency_code")))
                .selectByIndex(1);
        new Select(tabPrices.findElement(By.name("tax_class_id")))
                .selectByIndex(0);
        tabPrices.findElement(By.name("prices[USD]")).sendKeys(randomInt(1, 100).toString());
        tabPrices.findElement(By.name("prices[EUR]")).sendKeys(randomInt(1, 100).toString());
        webDriver.findElement(By.name("save")).click();

        Assert.assertTrue(webDriver.findElement(By.name("catalog_form")).findElement(By.xpath(String.format(".//a[text()='%s']",nameProduct))).isDisplayed());

    }

    private String randomString(String prefix) {
        return prefix.concat(RandomString.make(5).toLowerCase());
    }

    private Integer randomInt(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to);
    }

    private String generateDate(int plusMonth) {
        return LocalDateTime.now().plusMonths(plusMonth).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @After
    public void stop() {
        webDriver.quit();
        webDriver = null;
    }
}
