import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task10Test {
    private static WebDriver webDriver;
    private static WebDriverWait wait;

    @Before
    public void start() {
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        webDriver.get("http://localhost/litecart/");
    }

    @Test
    public void checkNameProductTest() {
        WebElement firstProductNameElement = webDriver.findElement(By.id("box-campaigns")).findElement(By.className("name"));
        String productNameOnMainPage = firstProductNameElement.getText();
        firstProductNameElement.click();
        String productNameOnProductPage = webDriver.findElement(By.xpath("//h1[@class='title']")).getText();
        Assert.assertEquals("Не совпадает текст названия товара на главной странице и на странице товара", productNameOnMainPage, productNameOnProductPage);
    }

    @Test
    public void checkPriceProductTest() {
        By campaignsBoxOnMainPage = By.id("box-campaigns");
        By productBoxOnProductPage = By.id("box-product");
        By regularPrice = By.className("regular-price");
        By campaignPrice = By.className("campaign-price");

        WebElement regularPriceProductElement = webDriver.findElement(campaignsBoxOnMainPage).findElement(regularPrice);
        WebElement campaignPriceProductElement = webDriver.findElement(campaignsBoxOnMainPage).findElement(campaignPrice);
        String regularPriceProductOnMainPage = regularPriceProductElement.getText();
        String campaignPriceProductOnMainPage = campaignPriceProductElement.getText();
        regularPriceProductElement.click();

        String regularPriceProductOnProductPage = webDriver.findElement(productBoxOnProductPage).findElement(regularPrice).getText();
        String campaignPriceProductOnProductPage = webDriver.findElement(productBoxOnProductPage).findElement(campaignPrice).getText();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(regularPriceProductOnProductPage).as("Не совпадает обычная цена товара на главной странице и на странице товара")
                .isEqualTo(regularPriceProductOnMainPage);
        assertions.assertThat(campaignPriceProductOnProductPage).as("Не совпадает акционная цена товара на главной странице и на странице товара")
                .isEqualTo(campaignPriceProductOnMainPage);
        assertions.assertAll();
    }

    @Test
    public void checkStyleRegularPriceProductTest() {
        By campaignsBoxOnMainPage = By.id("box-campaigns");
        By productBoxOnProductPage = By.id("box-product");
        By regularPrice = By.className("regular-price");

        WebElement regularPriceProductOnMainPage = webDriver.findElement(campaignsBoxOnMainPage).findElement(regularPrice);
        String colorRegularPriceOnMainPage = regularPriceProductOnMainPage.getCssValue("color");
        String textDecorationRegularPriceOnMainPage = regularPriceProductOnMainPage.getCssValue("text-decoration");
        regularPriceProductOnMainPage.click();

        String colorRegularPriceOnProductPage = webDriver.findElement(productBoxOnProductPage).findElement(regularPrice).getCssValue("color");
        String textDecorationRegularPriceOnProductPage = webDriver.findElement(productBoxOnProductPage).findElement(regularPrice).getCssValue("text-decoration");

        SoftAssertions assertions = new SoftAssertions();
        List<String> rgbPriceOnMainPage = getRGB(colorRegularPriceOnMainPage);
        assertions.assertThat(rgbPriceOnMainPage.get(0)).as("Цвет обычной цены продукта на главной странице не серый").isEqualTo(rgbPriceOnMainPage.get(1)).isEqualTo(rgbPriceOnMainPage.get(2));
        assertions.assertThat(textDecorationRegularPriceOnMainPage).as("Oбычная цена на главной странице не зачёркнутая").contains("line-through");

        List<String> rgbPriceOnProductPage = getRGB(colorRegularPriceOnProductPage);
        assertions.assertThat(rgbPriceOnProductPage.get(0)).as("Цвет обычной цены продукта на странице продукта не серый").isEqualTo(rgbPriceOnProductPage.get(1)).isEqualTo(rgbPriceOnProductPage.get(2));
        assertions.assertThat(textDecorationRegularPriceOnProductPage).as("Oбычная цена на странице продукта не зачёркнутая").contains("line-through");

        assertions.assertAll();
    }

    @Test
    public void checkStyleCampaignPriceProductTest() {
        By campaignsBoxOnMainPage = By.id("box-campaigns");
        By productBoxOnProductPage = By.id("box-product");
        By campaignPrice = By.className("campaign-price");

        WebElement campaignPriceProductOnMainPage = webDriver.findElement(campaignsBoxOnMainPage).findElement(campaignPrice);
        String colorCampaignPriceOnMainPage = campaignPriceProductOnMainPage.getCssValue("color");
        String fontWeightCampaignPriceOnMainPage = campaignPriceProductOnMainPage.getCssValue("font-weight");
        campaignPriceProductOnMainPage.click();

        String colorCampaignPriceOnProductPage = webDriver.findElement(productBoxOnProductPage).findElement(campaignPrice).getCssValue("color");
        String fontWeightCampaignPriceOnProductPage = webDriver.findElement(productBoxOnProductPage).findElement(campaignPrice).getCssValue("font-weight");

        SoftAssertions assertions = new SoftAssertions();
        List<String> rgbPriceOnMainPage = getRGB(colorCampaignPriceOnMainPage);
        assertions.assertThat(rgbPriceOnMainPage.get(1)).as("Цвет акционной цены продукта на главной странице не красный").isEqualTo(rgbPriceOnMainPage.get(2)).isEqualTo("0");
        assertions.assertThat(fontWeightCampaignPriceOnMainPage).as("Шрифт акционной цены на главной странице не жирный").containsAnyOf("700", "800", "900", "bold");

        List<String> rgbPriceOnProductPage = getRGB(colorCampaignPriceOnProductPage);
        assertions.assertThat(rgbPriceOnProductPage.get(1)).as("Цвет акционной цены продукта на странице продукта не красный").isEqualTo(rgbPriceOnProductPage.get(2)).isEqualTo("0");
        assertions.assertThat(fontWeightCampaignPriceOnProductPage).as("Шрифт акционной цены на странице продукта не жирный").containsAnyOf("700", "800", "900", "bold");

        assertions.assertAll();
    }

    @Test
    public void checkFontSizePriceProductTest() {
        By campaignsBoxOnMainPage = By.id("box-campaigns");
        By productBoxOnProductPage = By.id("box-product");
        By regularPrice = By.className("regular-price");
        By campaignPrice = By.className("campaign-price");

        WebElement campaignPriceProductOnMainPage = webDriver.findElement(campaignsBoxOnMainPage).findElement(campaignPrice);
        WebElement regularPriceProductOnMainPage = webDriver.findElement(campaignsBoxOnMainPage).findElement(regularPrice);

        String fontSizeCampaignPriceOnMainPage = campaignPriceProductOnMainPage.getCssValue("font-size").replaceAll("px", "");
        String fontSizeRegularPriceOnMainPage = regularPriceProductOnMainPage.getCssValue("font-size").replaceAll("px", "");
        campaignPriceProductOnMainPage.click();

        String fontSizeRegularPriceOnProductPage = webDriver.findElement(productBoxOnProductPage).findElement(regularPrice).getCssValue("font-size").replaceAll("px", "");
        String fontSizeCampaignPriceOnProductPage = webDriver.findElement(productBoxOnProductPage).findElement(campaignPrice).getCssValue("font-size").replaceAll("px", "");

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(Float.parseFloat(fontSizeCampaignPriceOnMainPage)).as("Размер шрифта акционной цены на главной странице меньше размера обычной цены").isGreaterThan(Float.parseFloat(fontSizeRegularPriceOnMainPage));
        assertions.assertThat(Float.parseFloat(fontSizeCampaignPriceOnProductPage)).as("Размер шрифта акционной цены на странице продукта меньше размера обычной цены").isGreaterThan(Float.parseFloat(fontSizeRegularPriceOnProductPage));

        assertions.assertAll();
    }

    private List<String> getRGB(String cssValueColorElement) {
        Pattern patternColorPrice = Pattern.compile("\\((\\d{1,3}),\\s(\\d{1,3}),\\s(\\d{1,3})[\\)|,\\s\\d\\)]");
        Matcher matcherColorPrice = patternColorPrice.matcher(cssValueColorElement);
        Assert.assertTrue(matcherColorPrice.find());

        List<String> rgb = new ArrayList<>();
        rgb.add(matcherColorPrice.group(1));
        rgb.add(matcherColorPrice.group(2));
        rgb.add(matcherColorPrice.group(3));
        return rgb;
    }

    @After
    public void stop() {
        webDriver.quit();
        webDriver = null;
    }
}
