package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LandingPage;
import utility.Constants;
import utility.DriverClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElPaisTest {

    WebDriver driver;
    LandingPage landingPage;
    List<String> titles;
    List<String> translatedTitles;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        titles = new ArrayList<>();
        translatedTitles = new ArrayList<>();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        driver = DriverClass.getWebDriver().get();
        landingPage = new LandingPage(driver);
        System.out.println("Launching the URL : " + Constants.url);
        driver.get(Constants.url);

        landingPage.acceptNotice();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        System.out.println("Closing the browser");
        driver.quit();
    }

    @Test
    public void tc001_VerifyWebsiteLanguage() {
        System.out.println("Verifying if the website is in spanish language ");
        landingPage.verifySpanishLanguage();
        System.out.println("the website is in spanish language ");
    }

    @Test
    public void tc002_PrintTheTitleAndContentOfArticles() {
        landingPage.navigateToOpinionSection();
        titles = landingPage.printTitleAndContent();
    }

    @Test(dependsOnMethods = {"tc002_PrintTheTitleAndContentOfArticles"})
    public void tc003_TranslateTheTitleToEnglish() {
        translatedTitles = landingPage.translateToEnglishAndPrint(titles);
    }

    @Test(dependsOnMethods = {"tc002_PrintTheTitleAndContentOfArticles", "tc003_TranslateTheTitleToEnglish"})
    public void tc004_PrintTheRepeatedWordsWhichOccursMoreThanTwice() {
        StringBuilder sb = new StringBuilder();
        HashMap<String, Integer> hm = new HashMap<>();
        for (String s : translatedTitles) {
            sb.append(s.toLowerCase()).append(" ");
        }

        for (String s : sb.toString().trim().split("\\s")) {
            if (hm.containsKey(s)) {
                int temp = hm.get(s);
                hm.put(s, temp + 1);
            } else {
                hm.put(s, 1);
            }
        }

        boolean exist = false;
        List<String> repeatedWords = new ArrayList<>();
        System.out.println("Checking if any word occurs more than twice : ");
        for (Map.Entry<String, Integer> m : hm.entrySet()) {
            if (m.getValue() > 2) {
                exist = true;
                repeatedWords.add(m.getKey());
            }
            System.out.println(m.getKey() + " occurs : " + m.getValue() + " time(s)");
        }

        if (!exist) {
            System.out.println("there are no words which occurs more than twice with all the headers combined");
            return;
        }
        System.out.println("repeated words are : " + repeatedWords);
    }
}
