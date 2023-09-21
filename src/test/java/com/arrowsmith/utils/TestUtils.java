package com.arrowsmith.utils;

import org.openqa.selenium.WebElement;

import java.util.Arrays;

public class TestUtils {


    public static boolean isElementActive(WebElement element) {
        return doesElementContainClass(element, "active");
    }

    public static boolean doesElementContainClass(WebElement element, String className)
    {
        String classAttribute = element.getAttribute("class");
        String[] classes = classAttribute.split(" ");
        return Arrays.asList(classes).contains(className);
    }
}
