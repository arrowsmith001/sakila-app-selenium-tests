package com.arrowsmith.base;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;
import java.util.Set;

public class TestBase {

    protected WebDriver driver;

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    protected void setUp(@Optional("chrome") String browser)
    {
        switch(browser)
        {
            case "chrome":
                setChromeDriver();
                break;
            case "edge":
                setEdgeDriver();
                break;
            default: setChromeDriver();
        }
    }

    private void setChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
    }
    private void setEdgeDriver() {
        System.setProperty("webdriver.edge.driver", "src/test/resources/msedgedriver.exe");
        driver = new EdgeDriver();
    }

    protected Set<Cookie> getCookies()
    {
        return driver.manage().getCookies();
    }

    protected void getUrlAndMaximize(String url)
    {
        driver.get(url);
        driver.manage().window().maximize();
    }
    protected WebDriverWait getWait(int seconds){
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    @AfterMethod(enabled = true, alwaysRun = true)
    protected void tearDown()
    {
       driver.quit();
    }
}
