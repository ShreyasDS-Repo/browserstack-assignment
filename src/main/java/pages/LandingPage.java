package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import rest.api;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LandingPage {
    WebDriver driver;
    By notice = By.cssSelector("div[id='didomi-notice']");
    By agreeButton = By.cssSelector("button[id='didomi-notice-agree-button']");
    By htmlTag = By.tagName("html");
    By hamburgerMenu = By.cssSelector("button[id='btn_open_hamburger']");
    By opinion = By.xpath("//a[text()='Opinión' and @cmp-ltrk='header_hamburguesa']");

    //    By articleHeaders = By.xpath("//p[contains(@class,'c_d')]/.. //h2[contains(@class,'c_t')]/a");
    By articleHeaders = By.xpath("//h2[contains(@class,'c_t')]/a");
    By articleDescription = By.xpath("//p[contains(@class,'c_d')]");

    By articleContent = By.xpath("//div[@class='a_c clearfix']");
    By articleContent2 = By.tagName("h2");

    List<String> titlesString;

    public LandingPage(WebDriver driver) {
        this.driver = driver;
    }

    public void acceptNotice() {
        if (driver.findElement(notice).isDisplayed()) {
            System.out.println("Notice is present, clicking on agree button");
            driver.findElement(agreeButton).click();
        }
    }

    public void verifySpanishLanguage() {
        String lang = driver.findElement(htmlTag).getAttribute("lang");
        Assert.assertTrue(lang.contains("es"), "Content is not in spanish language" +
                "\nExpected : es\nActual : " + lang);
    }

    public void navigateToOpinionSection() {
        System.out.println("Navigating to opinion section");
        driver.findElement(hamburgerMenu).click();
        driver.findElement(opinion).click();
    }

    public List<String> printTitleAndContent() {
        titlesString = new ArrayList<>();
        List<WebElement> titles = driver.findElements(articleHeaders);
        int limit = Math.min(titles.size(), 5);

        String currentUrl = driver.getCurrentUrl();

        for (int i = 0; i < limit; i++) {
            titles = driver.findElements(articleHeaders);
            System.out.println("Title of article : " + (i + 1) + " is :\n" + titles.get(i).getText());

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            wait.until(ExpectedConditions.urlToBe(currentUrl));

            titlesString.add(titles.get(i).getText());
            //navigating to the article

            titles.get(i).click();
            System.out.println("Content of the article is : ");

//            try {
//                if (driver.findElement(articleContent).isDisplayed()) {
//                    System.out.println(driver.findElement(articleContent).getText());
//                }
//            } catch (NoSuchElementException e) {
//                System.out.println("Trying to get the content using different xpath");
//                if (driver.findElement(articleContent2).isDisplayed()) {
//                    System.out.println(driver.findElement(articleContent2).getText());
//                }
//            }

            try {
                List<WebElement> articleContentList = driver.findElements(articleContent);
                if (!articleContentList.isEmpty()) {
                    System.out.println(driver.findElement(articleContent).getText());
                } else {
                    System.out.println("Trying to get the content using different xpath");
                    if (driver.findElement(articleContent2).isDisplayed()) {
                        System.out.println(driver.findElement(articleContent2).getText());
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception occurred");
            }

            //download image
            if (driver.findElement(By.tagName("img")).isDisplayed()) {
                String imgUrl = driver.findElement(By.tagName("img")).getAttribute("src");
                api.downloadImage(imgUrl, "downloads/" + titlesString.get(i) + ".jpg");
            }

            driver.navigate().back();
        }
        return titlesString;
    }

    public List<String> translateToEnglishAndPrint(List<String> titles) {
        System.out.println("Translating the titles ");
        List<String> translated = api.translateApi(titles);
        System.out.println("Titles in english are : \n" + translated);

        return translated;
    }
}
