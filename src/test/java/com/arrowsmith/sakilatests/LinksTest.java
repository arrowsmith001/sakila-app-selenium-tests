package com.arrowsmith.sakilatests;

import com.arrowsmith.base.SakilaAppTestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LinksTest extends SakilaAppTestBase
{

    @Parameters("url")
    @Test(enabled = true)
    private void testNoBrokenImageLinks(String url)  {

        getUrlAndMaximize(url);
        skipIntro();

        final LinkTestHelper helper = new LinkTestHelper();

        WebElement root = new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> d.findElement(By.id("whats-hot-loaded-root")));
        List<WebElement> thumbnails = root.findElements(By.className("thumbnail"));

        for(WebElement thumb : thumbnails)
        {
            final String imgSrc = thumb.getAttribute("src");
            if(helper.isValidLink(imgSrc))
            {
                helper.verifyLink(imgSrc);
            }
        }

        helper.printReport();

        Assert.assertEquals(helper.broken, 0, "Some links broken - number broken: " + helper.broken);
        Assert.assertEquals(helper.failed, 0, "Some links failed - number failed: " + helper.failed);

    }


    @Parameters({"url"})
    @Test(enabled = true, priority = 1)
    private void testMovieDetailLinks(String url)
    {
        getUrlAndMaximize(url);
        skipIntro();

        final LinkTestHelper helper = new LinkTestHelper();

        WebElement root = new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> d.findElement(By.id("whats-hot-loaded-root")));

        List<WebElement> movie = root.findElements(By.className("movie-container"));
        int size  = movie.size();


        for(int i = 0; i < size; i++)
        {
            movie = root.findElements(By.className("movie-container"));
            final WebElement m = movie.get(i);
            //final WebElement m = new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> d.findElement(By.id("movie-103")));

            // Get title from the movie list view
            WebElement title1 = m.findElement(By.className("movie-title"));
            WebElement title2 = title1.findElement(By.tagName("p"));
            final String titleString1 = title2.getText();

            // Navigate and wait for load
            final WebElement ahref = m.findElement(By.tagName("a"));
            driver.navigate().to(ahref.getAttribute("href"));

            new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> d.findElement(By.id("splash")));
            skipIntro();

            WebElement detailRoot = new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> d.findElement(By.id("movie-detail-container")));

            // Get title from the detail view
            WebElement title = detailRoot.findElement(By.className("title"));
            final String titleString2 = title.getText();

            Assert.assertEquals(titleString1, titleString2, "Titles do not match: 1 - '" + titleString1 + "', 2 - '" + titleString2 + "'");

            driver.navigate().back();

            new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> d.findElement(By.id("splash")));
            skipIntro();

            root = new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> d.findElement(By.id("whats-hot-loaded-root")));
        }

    }



}



class LinkTestHelper {

    final Pattern linkPattern = Pattern.compile("^http://");

    public int total = 0;
    public int succeeded = 0;
    public int failed = 0;
    public int broken = 0;

    public boolean isValidLink(String url) {
        return linkPattern.matcher(url).find();
    }

    public void verifyLink(String url) {
        try {
            URL link = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                System.out.println(url + " - " + httpURLConnection.getResponseMessage());
                succeeded++;
            } else {
                System.out.println(url + " - " + httpURLConnection.getResponseMessage() + " - " + "FAILED - response code: " + httpURLConnection.getResponseCode());
                failed++;
            }
        } catch (Exception e) {
            System.out.println(url + " - " + "FAILED - no response");
            broken++;
        }

        total++;
    }

    public void printReport() {
        System.out.println(total + " links tested:");
        System.out.println("\t" + succeeded + " succeeded");
        System.out.println("\t" + failed + " failed");
        System.out.println("\t" + broken + " broken");
    }
}
