package com.arrowsmith.sakilatests;

import com.arrowsmith.base.SakilaAppTestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Objects;

public class LoginTest extends SakilaAppTestBase {

    @Parameters({"url", "email"})
    @Test
    private void testUnsuccessfulExistingCustomerSignIn(String url, String email)
    {
        getUrlAndMaximize(url);
        skipIntro();

        WebElement emailInput = driver.findElement(By.id("existing-email-input"));
        emailInput.sendKeys(email);

        WebElement emailButton = driver.findElement(By.id("submit-existing-email"));

        String errorString = driver.findElement(By.id("existing-email-error")).getText();
        Assert.assertEquals(errorString, "", "Error string should be empty");

        emailButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> !Objects.equals(d.findElement(By.id("existing-email-error")).getText(), ""));

    }
}
