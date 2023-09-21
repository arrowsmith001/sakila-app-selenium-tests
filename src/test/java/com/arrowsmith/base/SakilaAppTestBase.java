package com.arrowsmith.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class SakilaAppTestBase extends TestBase
{

    protected void skipIntro(){

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement skipIntro = wait.until(d -> d.findElement(By.id("skip-intro-button")));
        skipIntro.click();
    }
}
