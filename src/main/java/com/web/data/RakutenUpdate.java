package com.web.data;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.web.controller.HelloController;
import com.web.entity.AmazonProduct;
import com.web.util.config.CommonProperties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RakutenUpdate {
	
	public static String homepage = "https://glogin.rms.rakuten.co.jp";
	final static Logger logger = Logger.getLogger(RakutenUpdate.class);
	
//	public static void main(String[] args) {
//		int test = rakutenGather(true);
//	}
	
	public int rakutenGather(boolean firstRun) {
		System.setProperty("webdriver.chrome.driver", "C:\\wd\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		int updatedProductCount = 0;
		List<String> processSKU = new ArrayList<String>();
		
        try {
        	
        	CommonProperties.loadProperties();

    		final String connectionString = CommonProperties.getConnectionString();
        	
        	Class.forName("org.sqlite.JDBC");
    	    
    	    connection = DriverManager.getConnection(connectionString);
    	    
    	    driver.navigate().to(homepage);
    	    
    	    driver.findElement(By.id("rlogin-username-ja")).clear();
    	    driver.findElement(By.id("rlogin-username-ja")).sendKeys(CommonProperties.getRakutenAccount());
    	    
    	    driver.findElement(By.id("rlogin-password-ja")).clear();
    	    driver.findElement(By.id("rlogin-password-ja")).sendKeys(CommonProperties.getRakutenPassword());
    	    
    	    driver.findElement(By.name("submit")).click();
    	    
    	    if (driver.findElements(By.id("rlogin-username-2-ja")).size() > 0) {
	    	    driver.findElement(By.id("rlogin-username-2-ja")).clear();
	    	    driver.findElement(By.id("rlogin-username-2-ja")).sendKeys(CommonProperties.getRakutenAccount2());
	    	    
	    	    driver.findElement(By.id("rlogin-password-2-ja")).clear();
	    	    driver.findElement(By.id("rlogin-password-2-ja")).sendKeys(CommonProperties.getRakutenPassword2());
	    	    driver.findElement(By.name("submit")).click();
    	    }
    	    
    	    if (driver.findElements(By.name("submit")).size() > 0) {
    	    	driver.findElement(By.name("submit")).click();
    	    }
    	    
    	    if (driver.findElements(By.xpath("//input[@type='submit']")).size() > 0) {
    	    	driver.findElement(By.xpath("//input[@type='submit']")).click();
    	    }
    	    
    	    if (driver.findElements(By.xpath("//a[@class='rf-medium']")).size() > 0) {
    	    	for (WebElement aLink : driver.findElements(By.xpath("//a[@class='rf-medium']"))) {
    	    		if (aLink.getAttribute("href").contains("rms")) {
    	    			driver.navigate().to("https://mainmenu.rms.rakuten.co.jp/?act=login&sp_id=1");
    	    		}
    	    	}
    	    }
    	    
    	    if (driver.findElements(By.xpath("//input[@type='submit']")).size() > 0) {
    	    	driver.findElement(By.xpath("//input[@type='submit']")).click();
    	    }
    	    
    	    String sql = "SELECT sku, product_name, stock_amount, price, explain FROM amazon_product_info WHERE rakuten_process_flag<>'1' ";
    	    
    	    if (firstRun) {
    	    	sql = "SELECT sku, product_name, stock_amount, price, explain FROM amazon_product_info";
    	    }
    	    
    	    stmt  = connection.createStatement();
    	    rs    = stmt.executeQuery(sql);

    	    List<WebElement> trs = new ArrayList<WebElement>();
    	    List<WebElement> names;
    	    List<WebElement> timeoutDetects;
    	    List<String> lackImageList = new ArrayList<String>();
    	    String sku = "";
    	    String productName = "";
    	    String stockAmount = "";
    	    String price = "";
    	    String explain = "";
    	    
    	    String productNameCurrent = "";
    	    String stockAmountCurrent = "";
//    	    String priceCurrent = "";
    	    String explainPCCurrent = "";
    	    String explainMobileCurrent = "";
    	    String explainSPCurrent = "";
    	    String pcSellExplain = "";
    	    String catchCopyCurrent = "";
    	    
    	    String imageCurrent = "";
    	    
    	    boolean productNameChanged = false;
    	    boolean stockAmountChanged = false;
//    	    boolean priceChanged = false;
    	    boolean explainChanged = false;
    	    boolean catchCopyChanged = false;
    	    
    	    //boolean notFound = false;
    	    
    	    List<AmazonProduct> amzList = new ArrayList<AmazonProduct>();
    	    while (rs.next()) {
    	    	AmazonProduct amzP = new AmazonProduct();
    	    	amzP.setSku(rs.getString("sku"));
    	    	amzP.setStockAmount(rs.getString("stock_amount"));
    	    	amzP.setExplain(rs.getString("explain"));
    	    	amzP.setPrice(rs.getString("price"));
    	    	amzP.setProductName(rs.getString("product_name"));
    	    	
    	    	amzList.add(amzP);
    	    }
    	    
    	    if (amzList.size() < 1) {
    	    	return 0;
    	    }
    	    
    	    rs.close();
    	    stmt.close();
    	    
    	    // loop through the result set
    	    //while (rs.next()) {
    	    for (AmazonProduct a : amzList) {
    	    	try {
	    	    	productNameChanged = false;
	        	    stockAmountChanged = false;
	        	    //priceChanged = false;
	        	    explainChanged = false;
	        	    catchCopyChanged = false;
	    	    	
	    	    	sku = a.getSku();
	    	    	sku = sku.toLowerCase();
	    	    	
	    	    	productName = a.getProductName();
	    	    	
	    	    	productName = productName.replaceAll("[\\[\\]<>〓ⅡⅣⅠⅢⅤⅥⅦⅧⅨⅩ]", "");
	    	    	
	    	    	stockAmount = a.getStockAmount();
	    	    	
	    	    	price = a.getPrice();
	    	    	price = price.replaceAll("\\D+","");
	    	    	
	    	    	explain = a.getExplain();
	    	    	
	    	    	if (StringUtils.isAnyEmpty(sku, productName, stockAmount, price)) {
	    	    		continue;
	    	    	}
	    	    	
//	    	    	if (StringUtils.isAnyEmpty(sku, productName, stockAmount)) {
//	    	    		continue;
//	    	    	}
	    	    	String productUrl = "";
	    	    	try {
		    	    	productUrl = driver.findElement(By.id("com_gnavi0101")).getAttribute("href");
		    	    	driver.navigate().to(productUrl);
		    	    	
		    	    	productUrl = driver.findElement(By.id("mm_sub0101_04")).getAttribute("href");
		    	    	driver.navigate().to(productUrl);
	    	    	} catch (Exception e) {
		    	    	List<WebElement> lis = driver.findElements(By.tagName("a"));
		    	    	for (int ixx=0; ixx<lis.size(); ixx++) {
		    	    		if (lis.get(ixx).getText().equals("別の商品の更新を商品一覧よりおこなう")) {
		    	    			lis.get(ixx).click();
		    	    			break;
		    	    		}
		    	    	}
	    	    	}
	    	    	
//	    	    	List<WebElement> lis = driver.findElements(By.xpath("//li[@class='first']"));
//	    	    	
//	    	    	for (int i=0; i<lis.size(); i++) {
//	    	    		if (lis.get(i).getText().contains("商品登録")) {
//	    	    			int t = i+1;
//	    	    			driver.findElement(By.xpath("//li[@class='first'][" + t + "]/a")).click();
//	    	    			break;
//	    	    		}
//	    	    	}
//	    	    	
//	    	    	lis = driver.findElements(By.xpath("//a[@class='btnLink btnMiddle']"));
//	    	    	
//	    	    	for (int i=0; i<lis.size(); i++) {
//	    	    		if (lis.get(i).getText().contains("商品一覧")) {
//	    	    			lis.get(i).click();
//	    	    			break;
//	    	    		}
//	    	    	}
	    	    	
    	    		//driver.navigate().to("https://item.rms.rakuten.co.jp/rms/mall/rsf/item/vc?__event=RI33_001_001&shop_bid=307311&proc_type=normal");
	    	    	
	    	    	timeoutDetects = driver.findElements(By.xpath("//input[@type='submit']"));
	    	    	
	    	    	if (timeoutDetects.size() > 0) {
	    	    		for (WebElement timeoutDetect : timeoutDetects) {
	    	    			if (timeoutDetect.getAttribute("value").contains("商品ページ設定メニューへ")) {
	    	    				timeoutDetect.click();
//	    	    				driver.navigate().to("https://item.rms.rakuten.co.jp/rms/mall/rsf/item/vc?__event=RI33_001_001&shop_bid=307311&proc_type=normal");
	    	    				driver.navigate().to(productUrl);
	    	    				break;
	    	    			}
	    	    		}
	    	    	} 
	    	    	
	    	    	int errorCount = 0;
	    	    	while (true) {
	    	    		errorCount++;
	    	    		if (errorCount > 5) {
	    	    			logger.error("Loop 5 times but still failed.");
	    	    			throw new IllegalAccessException();
	    	    		}
	    	    		try {
		    	    		driver.findElement(By.id("mngNumberSearchId")).click();
		    	    		break;
		    	    	} catch (Exception e) {
//		    	    		driver.navigate().to("https://mainmenu.rms.rakuten.co.jp/");
//		    	    		driver.navigate().to("https://item.rms.rakuten.co.jp/rms/mall/rsf/item/vc?__event=RI33_001_001&shop_bid=307311&proc_type=normal");
//			    	    	
//			    	    	timeoutDetects = driver.findElements(By.xpath("//input[@type='submit']"));
//			    	    	
//			    	    	if (timeoutDetects.size() > 0) {
//			    	    		for (WebElement timeoutDetect : timeoutDetects) {
//			    	    			if (timeoutDetect.getAttribute("value").contains("商品ページ設定メニューへ")) {
//			    	    				timeoutDetect.click();
//			    	    				driver.navigate().to("https://item.rms.rakuten.co.jp/rms/mall/rsf/item/vc?__event=RI33_001_001&shop_bid=307311&proc_type=normal");
//			    	    				break;
//			    	    			}
//			    	    		}
//			    	    	}
			    	    	Thread.sleep(1000);
		    	    	}
	    	    	}
	    	    	
	        	    driver.findElement(By.id("_searchText")).clear();
	    	    	
	        	    if (sku.length() > 32) {
	        	    	sku = sku.substring(0, 32);
	        	    }
	        	    
	    	    	driver.findElement(By.id("_searchText")).sendKeys(sku);
	    	    	//TEST PURPOSE
	        	    //driver.findElement(By.id("_searchText")).sendKeys("vf-201x-a2poxxxxxx");
	    	    	
	    	    	try {
	    	    		
	    	    		System.out.println("Wait initial");
	    	    		while (true) {
		    	    		if (driver.findElement(By.xpath("//div[@class='fixed-table-loading']")).getAttribute("style").contains("none")) {
		    	    			break;
		    	    		}
		    	    		Thread.sleep(2000);
		    	    		
		    	    		System.out.println("Jump into wait initial1");
		    	    	}
	    	    		
	    	    		System.out.println("Start search again");
	    	    		
	    	    		driver.findElement(By.id("mngNumberSearchId")).submit();
	    	    		
	    	    		while (true) {
		    	    		if (driver.findElement(By.xpath("//div[@class='fixed-table-loading']")).getAttribute("style").contains("none")) {
		    	    			break;
		    	    		}
		    	    		Thread.sleep(2000);
		    	    		
		    	    		System.out.println("Still searching");
		    	    	}
	    	    		
	    	    	} catch (Exception e) {
	    	    		System.out.println("Search exception ");
	    	    		
	    	    		logger.error("Search button failed");
	    	    		logger.error(e.getMessage());
	    	    		
	    	    		((JavascriptExecutor)driver).executeScript( "window.onbeforeunload = function(e){};");
	    	    		
	    	    		Thread.sleep(200);
	    	    	}
	    	    	
	    	    	System.out.println("Search finished ");
	    	    	
	    	    	
	    	    	while (true) {
	    	    		try {
	    	    			if (driver.findElements(By.xpath("//tr[@class='no-records-found']")).size() > 0) {
	    	    	    		System.out.println("4");
	    	    	    		System.out.println(driver.findElements(By.xpath("//tr[@class='no-records-found']")).get(0).getText());
	    	    	    		//New Product
	    	    	    		trs = new ArrayList<WebElement>();
	    	    	    	} else {
	    	    	    		System.out.println("5");
	    	    	    		trs = driver.findElements(By.xpath("//table[21]//table//tr/td[1]/font/font[1]//a"));
	    	    	    	}
	    	    			break;
	    	    		} catch (Exception e) {
	    	    			Robot rb = new Robot();

		              		//press enter
		              		rb.keyPress(KeyEvent.VK_ENTER);
		              		rb.keyRelease(KeyEvent.VK_ENTER);
		    	    		
		    	    		Thread.sleep(200);
		    	    		
		    	    		driver.findElement(By.id("mngNumberSearchId")).submit();
	    	    		}
	    	    	}
	    	    	
	    	    	List<WebElement> notFound = new ArrayList<WebElement>();
	    	    	
    	    		while (true) {
	    	    		try {
	    	    			Thread.sleep(3000);
	    	    			//trs = driver.findElements(By.xpath("//table[21]//table//tr/td[1]/font/font[1]//a"));
	    	    			trs = driver.findElements(By.xpath("//p[@class='p-edit']//a"));
	    	    			notFound = driver.findElements(By.xpath("//tr[@class='no-records-found']"));
	    	    			
	    	    			if ((trs.size() > 0 && notFound.size() < 1) || (trs.size() < 1 && notFound.size() > 0)) {
	    	    				//existCount++;
	    	    				break;
	    	    			}	    	    			
	    	    			
	    	    		} catch (Exception e) {
	    	    			logger.error("Detect Update/New failed");
		    	    		logger.error(e.getMessage());
	    	    			
	    	    			Robot rb = new Robot();

		              		//press enter
		              		rb.keyPress(KeyEvent.VK_ENTER);
		              		rb.keyRelease(KeyEvent.VK_ENTER);
		    	    		
		    	    		Thread.sleep(200);
	    	    		}
	    	    	}
    	    		
    	    		logger.info("Passed detect Update/New");
	    	    	
	        	    if (trs.size() > 0 && notFound.size() < 1) {
	        	    	if (trs.get(0).getText().equals("変更")) {
	        	    		trs.get(0).click();
	        	    		//notFound = false;
	        	    	}
	        	    } else if (trs.size() < 1 && notFound.size() > 0) {
	        	    	//product not found
	        	    	try {
	        	    		createNewProduct(driver, sku, productName, stockAmount, price, explain);
	        	    		stmt  = connection.createStatement();
	    	        	    String updateSQL = "UPDATE amazon_product_info SET rakuten_process_flag='1'" 
	    	        	    		+ " WHERE sku in('" + sku.toUpperCase() + "')";
	    	    			stmt.executeUpdate(updateSQL);
	    	    			stmt.close();
	        	    		
	        	    	} catch (Exception e) {
	        	    		//notFound = false;
	        	    		logger.error("Create new product failed " + e.getMessage());
	        	    	} finally {
	        	    		continue;
	        	    	}
	        	    }
	        	    
	        	    boolean doTheLoop = true;
	        	    int h = 0;
	        	    while (doTheLoop) {
	        	    	try {
	        	    		names = driver.findElements(By.name("item_name"));
	        	    		
		        	        h = h+1;
		        	        Thread.sleep(200);
		        	        
		        	        if (h>30){
		        	            doTheLoop = false;
		        	        }
		        	        if (names.size() > 0){
		        	            doTheLoop = false;
		        	            break;
		        	        }     
		        	        
	        	    	} catch (Exception ex) {
	        	    		continue;
	        	    	}
	            	}
	        	    
	        	    productNameCurrent = driver.findElement(By.name("item_name")).getAttribute("value");
	        	    
	        	    if (!productNameCurrent.equals(productName)) {
	        	    	driver.findElement(By.name("item_name")).clear();
	            	    driver.findElement(By.name("item_name")).sendKeys(productName);
	            	    productNameChanged = true;
	        	    }
	        	    
	        	    catchCopyCurrent = driver.findElement(By.name("catch_copy")).getAttribute("value");
	        	    if (!catchCopyCurrent.equals("■純正品■アウトレット品■未使用品■安心安全の保証付きです！")) {
	        	    	driver.findElement(By.name("catch_copy")).clear();
	            	    driver.findElement(By.name("catch_copy")).sendKeys("■純正品■アウトレット品■未使用品■安心安全の保証付きです！");
	            	    catchCopyChanged = true;
	        	    }
	        	    
//	        	    priceCurrent = driver.findElement(By.name("price")).getAttribute("value");
//	        	    
//	        	    if (!priceCurrent.equals(price)) {
//	        	    	driver.findElement(By.name("price")).clear();
//	            	    driver.findElement(By.name("price")).sendKeys(price);
//	            	    priceChanged = true;
//	        	    }
	        	    
	        	    stockAmountCurrent = driver.findElement(By.id("invcur_sp01")).getText();
	        	    stockAmountCurrent = stockAmountCurrent.replaceAll("\\D+","");
	        	    
	        	    if (!stockAmountCurrent.equals(stockAmount)) {
	        	    	driver.findElement(By.id("invnew_in01")).clear();
	            	    driver.findElement(By.id("invnew_in01")).sendKeys(stockAmount);
	            	    stockAmountChanged = true;
	        	    }
	        	    
	        	    if (!StringUtils.isEmpty(explain)) {
	        	    	explainPCCurrent = driver.findElement(By.id("catalog_caption")).getAttribute("value");
	            	    explainMobileCurrent = driver.findElement(By.name("mobile_caption")).getAttribute("value");
	            	    explainSPCurrent = driver.findElement(By.id("smart_caption")).getAttribute("value");
	            	    pcSellExplain = driver.findElement(By.name("display_caption")).getAttribute("value");
	            	    
	            	    if (!explainPCCurrent.equals(explain)) {
	            	    	driver.findElement(By.id("catalog_caption")).clear();
	                	    driver.findElement(By.id("catalog_caption")).sendKeys(explain);
	                	    explainChanged = true;
	            	    }
	            	    
	            	    if (!explainMobileCurrent.equals(explain)) {
	            	    	driver.findElement(By.name("mobile_caption")).clear();
	                	    driver.findElement(By.name("mobile_caption")).sendKeys(explain);
	                	    explainChanged = true;
	            	    }
	            	    
	            	    if (!explainSPCurrent.equals(explain)) {
	            	    	driver.findElement(By.id("smart_caption")).clear();
	                	    driver.findElement(By.id("smart_caption")).sendKeys(explain);
	                	    explainChanged = true;
	            	    }
	            	    
	            	    if (!pcSellExplain.equals("")) {
	            	    	driver.findElement(By.name("display_caption")).clear();
	                	    driver.findElement(By.name("display_caption")).sendKeys(explain);
	                	    explainChanged = true;
	            	    }
	        	    	
	        	    }
	        	    
	        	    imageCurrent = driver.findElement(By.id("image_url1")).getAttribute("value");
	        	    
	        	    if (!imageCurrent.contains("https:")) {
	        	    	imageCurrent = imageCurrent.replace("http:", "https:");
	        	    }
	        	    
	        	    driver.findElement(By.id("image_url1")).clear();
	        	    driver.findElement(By.id("image_url1")).sendKeys(imageCurrent);
	        	    
	        	    if (productNameChanged == true
	        	    		|| stockAmountChanged == true
//		    				|| priceChanged == true
							|| explainChanged == true 
							|| catchCopyChanged == true) {
	        	    	//Save
        	    		driver.findElement(By.id("submitButton")).click();
        	    		logger.info("Updated "+sku);
	        	    	updatedProductCount++;
	        	    } else {
	        	    	logger.info("Product has no change");
	        	    }
	        	    
	        	    processSKU.add(sku);
	        	    
	        	    stmt  = connection.createStatement();
	        	    String updateSQL = "UPDATE amazon_product_info SET rakuten_process_flag='1'" 
	        	    		+ " WHERE sku in('" + sku.toUpperCase() + "')";
	    			stmt.executeUpdate(updateSQL);
	    			stmt.close();
	        	    
    	    	} catch (Exception ex) {
//    	    		if (unClearErrorHandler > 10) {
//    	    			logger.info("Relogin at " + sku);
//    	    			//Login again
//    	    			reLogin(driver);
//    	    			unClearErrorHandler = 0;
//    	    		}
    	    		logger.error("Failed sku ="+sku.toUpperCase());
    	    		logger.error("Error in Rakuten Update loop." + ex.getMessage());
    	    		//notFound = false;
    	    		continue;
    	    	}
    	    }
    	    
    	    if (processSKU.size() < 1) {
    	    	return 0;
    	    }
    	    
			//"SELECT amazonProductLink, sku from amazon_product_info"
    	    // WHERE sku in (lackImageList)
			
//			String selectAmazonURL = "UPDATE amazon_product_info SET amazon_process_flag = '1' " 
//    	    		+ "WHERE sku in('" + StringUtils.join(lackImageList,"','") + "')";
//    	    stmt.executeUpdate(selectAmazonURL);
    	    
    	    //stmt.close();
			
        } catch(SQLException ioe) {
            ioe.printStackTrace();
        } catch(Exception ex) {
        	ex.printStackTrace();
        } finally {
        	logger.info("Rakuten product processed : " + processSKU.size());
    	    logger.info("Rakuten product updated : " + updatedProductCount);
    	    driver.close();
            driver.quit();
        	try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
        
        return updatedProductCount;
    }
	
	public void createNewProduct(WebDriver driver, String sku, String productName, String stockAmount, String price, String explain) {
		
		//driver.navigate().to("https://item.rms.rakuten.co.jp/rms/mall/rsf/item/vc?__event=RI00_001_004&shop_bid=307311&detail_sell_type=0");
		
		logger.info("Start Create new");
		
		registerPage(driver);
		
		driver.findElement(By.name("mng_number")).clear();
	    driver.findElement(By.name("mng_number")).sendKeys(sku);
	    
	    driver.findElement(By.name("item_number")).clear();
	    driver.findElement(By.name("item_number")).sendKeys(sku);
	    
	    driver.findElement(By.name("catch_copy")).clear();
	    driver.findElement(By.name("catch_copy")).sendKeys("■純正品■アウトレット品■未使用品■安心安全の保証付きです！");
	    
	    driver.findElement(By.name("mobile_catch_copy")).clear();
	    driver.findElement(By.name("mobile_catch_copy")).sendKeys("■純正品■アウトレット品■未使用品■安心安全の保証付きです！");
	    
		driver.findElement(By.name("item_name")).clear();
//		productName = productName.replaceAll("<", "");
//		productName = productName.replaceAll(">", "");
		productName = productName.replaceAll("[\\[\\]<>〓Ⅱ]", "");
//		productName = productName.replaceAll("〓", "");
//		productName = productName.replaceAll("Ⅱ", "");
		
	    driver.findElement(By.name("item_name")).sendKeys(productName);
	    
	    driver.findElement(By.name("price")).clear();
	    driver.findElement(By.name("price")).sendKeys(price);
	    
	    //Free ship radio
	    driver.findElement(By.id("r06")).click();
	    
	    driver.findElement(By.id("invnew_in01")).clear();
	    driver.findElement(By.id("invnew_in01")).sendKeys(stockAmount);
	    
	    //Selectbox
	    Select select = new Select(driver.findElement(By.name("normal_delvdate_id")));
		select.selectByValue("1000");
		
    	driver.findElement(By.id("catalog_caption")).clear();
    	explain = explain.replaceAll("<", "");
    	explain = explain.replaceAll(">", "");
	    driver.findElement(By.id("catalog_caption")).sendKeys(explain);
    
    	driver.findElement(By.name("mobile_caption")).clear();
	    driver.findElement(By.name("mobile_caption")).sendKeys(explain);
	    
    	driver.findElement(By.id("smart_caption")).clear();
	    driver.findElement(By.id("smart_caption")).sendKeys(explain);
	    
//	    driver.findElement(By.name("display_caption")).sendKeys(Keys.TAB);
//	    driver.findElement(By.name("display_caption")).clear();
//	    driver.findElement(By.name("display_caption")).sendKeys("なし");
	    
	    driver.findElement(By.name("genre_id")).clear();
	    driver.findElement(By.name("genre_id")).sendKeys("208552");
	    
	    driver.findElement(By.name("rcatalog_id")).clear();
	    driver.findElement(By.name("rcatalog_id")).sendKeys("4960999277950");
	    
	    
	    JavascriptExecutor jsx = (JavascriptExecutor)driver;
	    jsx.executeScript("window.scrollBy(0,11110)", "");
	    
	    driver.findElement(By.id("submitButton")).click();
	    
	    if (driver.findElements(By.name("mng_number")).size() > 0) {
	    	logger.error("created failed " +sku);
	    } else {
	    	logger.info("created success " +sku);
	    }
	    
	}
	
	public void registerPage(WebDriver driver) {
		driver.navigate().to("https://mainmenu.rms.rakuten.co.jp/");
		
		String productUrl = driver.findElement(By.id("com_gnavi0101")).getAttribute("href");
    	driver.navigate().to(productUrl);
    	
    	//商品ページ設定
    	productUrl = driver.findElement(By.id("mm_sub0101_05")).getAttribute("href");
    	driver.navigate().to(productUrl);
		
//		List<WebElement> lis = driver.findElements(By.xpath("//li[@class='first']"));
    	
//    	for (int i=0; i<lis.size(); i++) {
//    		if (lis.get(i).getText().contains("商品登録")) {
//    			int t = i+1;
//    			driver.findElement(By.xpath("//li[@class='first'][" + t + "]/a")).click();
//    			break;
//    		}
//    	}
//    	
//    	lis = driver.findElements(By.xpath("//a[@class='btnLink btnMiddle']"));
//    	
//    	for (int i=0; i<lis.size(); i++) {
//    		if (lis.get(i).getText().contains("商品ページ設定")) {
//    			lis.get(i).click();
//    			break;
//    		}
//    	}
    	
		List<WebElement> lis = driver.findElements(By.xpath("//a"));
    	
    	for (int i=0; i<lis.size(); i++) {
    		if (lis.get(i).getText().contains("商品個別登録")) {
    			lis.get(i).click();
    			break;
    		}
    	}
    	
	}
	
	public void reLogin(WebDriver driver) throws IOException, URISyntaxException {
		CommonProperties.loadProperties();

	    driver.navigate().to(homepage);
	    
	    if (driver.findElements(By.id("rlogin-username-ja")).size() > 0) {
	    	driver.findElement(By.id("rlogin-username-ja")).clear();
		    driver.findElement(By.id("rlogin-username-ja")).sendKeys(CommonProperties.getRakutenAccount());
		    
		    driver.findElement(By.id("rlogin-password-ja")).clear();
		    driver.findElement(By.id("rlogin-password-ja")).sendKeys(CommonProperties.getRakutenPassword());
	    }
	    
	    if (driver.findElements(By.name("submit")).size() > 0) {
	    	driver.findElement(By.name("submit")).click();
	    }
	    
	    if (driver.findElements(By.id("rlogin-username-2-ja")).size() > 0) {
    	    driver.findElement(By.id("rlogin-username-2-ja")).clear();
    	    driver.findElement(By.id("rlogin-username-2-ja")).sendKeys(CommonProperties.getRakutenAccount2());
    	    
    	    driver.findElement(By.id("rlogin-password-2-ja")).clear();
    	    driver.findElement(By.id("rlogin-password-2-ja")).sendKeys(CommonProperties.getRakutenPassword2());
	    }
	    
	    if (driver.findElements(By.name("submit")).size() > 0) {
	    	driver.findElement(By.name("submit")).click();
	    }
	    
	    if (driver.findElements(By.name("submit")).size() > 0) {
	    	driver.findElement(By.name("submit")).click();
	    }
	    
	    if (driver.findElements(By.xpath("//input[@type='submit']")).size() > 0) {
	    	driver.findElement(By.xpath("//input[@type='submit']")).click();
	    }
	    
	    if (driver.findElements(By.xpath("//a[@class='rf-medium']")).size() > 0) {
	    	for (WebElement aLink : driver.findElements(By.xpath("//a[@class='rf-medium']"))) {
	    		if (aLink.getAttribute("href").contains("rms")) {
	    			driver.navigate().to("https://mainmenu.rms.rakuten.co.jp/?act=login&sp_id=1");
	    		}
	    	}
	    }
	    
	    if (driver.findElements(By.xpath("//input[@type='submit']")).size() > 0) {
	    	driver.findElement(By.xpath("//input[@type='submit']")).click();
	    }
	}
	
}

