package utility;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class DriverClass {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static ThreadLocal<WebDriver> getWebDriver() {
        WebDriverManager.chromedriver().clearResolutionCache().driverVersion("131.0.6778.205").setup();

        WebDriver driver1 = new ChromeDriver(ChromeOptionsClass.getChromeOptions());
        driver1.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));


        driver.set(driver1);
        return driver;
    }
}
