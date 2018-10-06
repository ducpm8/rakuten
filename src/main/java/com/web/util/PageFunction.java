package com.web.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

public class PageFunction {
	public static void waitForLoad(WebDriver driver) {
		(new WebDriverWait(driver, 10)).until( new Predicate<WebDriver>() { 
			public boolean apply(WebDriver driver) { 
				return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete"); 
				} 
			});
	}
}
