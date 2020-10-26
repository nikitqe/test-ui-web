import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Test2 {

    public WebDriver driver;
    public String url = "https://www.dns-shop.ru/";

    @BeforeEach
    public void initDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void runTest() throws InterruptedException {
        driver.get(url);
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement computerElement = driver.findElement(By.xpath("//a[text()='Компьютеры']"));
        actions.moveToElement(computerElement).perform();

        WebElement processElement = driver.findElement(By.xpath("//a[text()='Процессоры']"));
        actions.moveToElement(processElement).perform();
        processElement.click();

        WebElement openListOrder = driver.findElement(By.xpath("//div[@data-id='order']//span[@class='top-filter__selected']/.."));
        WebElement minPriceFirstPage = driver.findElement(By.xpath("//div[@data-position-index='0']//div[@class='product-min-price__current']"));
        String minPriceASC = minPriceFirstPage.getText().replaceAll("\\s[₽]+","");
        System.out.println("Цена первого товара на странице по возрастанию " + minPriceASC);

        actions.moveToElement(openListOrder).perform();
        openListOrder.click();

        WebElement selectDESCOrder = driver.findElement(By.xpath("//span[@class='ui-radio__content' and text()='По убыванию цены']"));
        wait.until(ExpectedConditions.elementToBeClickable(selectDESCOrder));
        actions.moveToElement(selectDESCOrder).perform();
        selectDESCOrder.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='loader' and @data-role='ajax-loader-circular']")));

        WebElement goPageBar = driver.findElement(By.xpath("//ul[@class='pagination-widget__pages']"));
        actions.moveToElement(goPageBar).perform();

        WebElement goToLastPage = driver.findElement(By.xpath("//a[@class='pagination-widget__page-link pagination-widget__page-link_last ']"));
        goToLastPage.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='loader' and @data-role='ajax-loader-circular']")));

        WebElement minPriceLastPage = driver.findElement(By.xpath("//div[@data-position-index='6']//div[@class='product-min-price__current']"));
        String minPriceDESC = minPriceLastPage.getText().replaceAll("\\s[₽]+","");
        System.out.println("Цена последнего товара на странице по убыванию " + minPriceDESC);

        Assertions.assertEquals(minPriceASC, minPriceDESC, " Цены товаров не равны");
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
    }
}