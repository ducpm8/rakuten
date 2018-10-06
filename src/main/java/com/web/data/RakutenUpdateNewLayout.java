package com.web.data;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.web.entity.AmazonProduct;
import com.web.util.config.CommonProperties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RakutenUpdateNewLayout {
	
	public static String homepage = "https://glogin.rms.rakuten.co.jp";
	final static Logger logger = Logger.getLogger(RakutenUpdateNewLayout.class);
	
	public static void main(String[] args) {
		int xxx = rakutenGather(true);
	}
	
	
	public static int rakutenGather(boolean firstRun)
    {
		System.setProperty("webdriver.chrome.driver", "C:\\wd\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		int updatedProductCount = 0;
		int processSKU = 0;
		
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
    	    
    	    if (driver.findElements(By.xpath("//div[@class='popTitle']//a")).size() > 0) {
    	    	driver.findElement(By.xpath("//div[@class='popTitle']//a")).click();
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

    	    List<WebElement> timeoutDetects;
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
    	    
    	    HashMap<String, String> stockList = new HashMap<String, String>();
    	    
    	    for (AmazonProduct a : amzList) {
    	    	String skuT;
    	    	skuT = a.getSku();
    	    	skuT = skuT.toLowerCase();
    	    	
    	    	stockList.put(skuT, a.getStockAmount());
    	    	
    	    }
    	    logger.info("Total stockList size " + stockList.size());
    	    
    	    List<WebElement> liTenpoSettei = driver.findElements(By.id("rmsTop-navsetting"));
    	    
    	    if (liTenpoSettei.size() > 0) {
    	    	
    	    	driver.navigate().to("https://mainmenu.rms.rakuten.co.jp/?left_navi=11");
    	    	
    	    	//店舗設定
//    	    	liTenpoSettei.get(0).click();
    	    	
    	    	//商品登録
//    	    	driver.findElement(By.id("com_gnavi0101")).click();
    	    	
    	    	//商品一覧
    	    	driver.findElement(By.id("mm_sub0101_04")).click();
    	    } else {
    	    	List<WebElement> lis = driver.findElements(By.xpath("//li[@class='first']"));
    	    	
    	    	for (int i=0; i<lis.size(); i++) {
    	    		if (lis.get(i).getText().contains("商品登録")) {
    	    			int t = i+1;
    	    			driver.findElement(By.xpath("//li[@class='first'][" + t + "]/a")).click();
    	    			break;
    	    		}
    	    	}
    	    	
    	    	lis = driver.findElements(By.xpath("//a[@class='btnLink btnMiddle']"));
    	    	
    	    	for (int i=0; i<lis.size(); i++) {
    	    		if (lis.get(i).getText().contains("商品一覧")) {
    	    			lis.get(i).click();
    	    			break;
    	    		}
    	    	}
    	    }
    	    
    		//driver.navigate().to("https://item.rms.rakuten.co.jp/rms/mall/rsf/item/vc?__event=RI33_001_001&shop_bid=307311&proc_type=normal");
	    	
	    	timeoutDetects = driver.findElements(By.xpath("//input[@type='submit']"));
	    	
	    	if (timeoutDetects.size() > 0) {
	    		for (WebElement timeoutDetect : timeoutDetects) {
	    			if (timeoutDetect.getAttribute("value").contains("商品ページ設定メニューへ")) {
	    				timeoutDetect.click();
	    				driver.navigate().to("https://item.rms.rakuten.co.jp/rms/mall/rsf/item/vc?__event=RI33_001_001&shop_bid=307311&proc_type=normal");
	    				break;
	    			}
	    		}
	    	} 
    	    
    	    //Wait until search finish
    	    while (true) {
    	    	Thread.sleep(2000);
    	    	if (driver.findElements(By.xpath("//table[@id='tableBootstrap']//tbody/tr")).size() > 1) {
    	    		break;
    	    	}
    	    }
    	    
    	    //Display amount dropbox
    	    driver.findElement(By.xpath("//span[@class='btn-group dropdown']")).click();
    	    
    	    List<WebElement> displayAmounts = driver.findElements(By.xpath("//ul[@class='dropdown-menu']/li/a"));
	    	
    	    //Display 100 products each page
    	    for (int i=0; i<displayAmounts.size(); i++) {
    	    	Thread.sleep(2000);
	    		if (displayAmounts.get(i).getText().contains("100")) {
	    			displayAmounts.get(i).click();
	    			break;
	    		}
	    	}
    	    
    	    int totalPage;
    	    totalPage = Integer.parseInt(driver.findElement(By.xpath("//li[@class='page-last']")).getText());
    	    
    	    int loopTest = 0;
    	    
    	    for (int j=0; j<totalPage; j++) {
    	    	try {
	    	    	loopTest = 0;
	    	    	List<WebElement> trList = driver.findElements(By.xpath("//table[@id='tableBootstrap']//tbody/tr"));
	    	    	
	    	    	while (true) {
	    	    		if (driver.findElement(By.xpath("//div[@class='fixed-table-loading']")).getAttribute("style").contains("none")) {
	    	    			break;
	    	    		}
	    	    		Thread.sleep(2000);
	    	    	}
	    	    	
	    	    	//Re-try until tr loop reach at least 95 rows - except for last page
	    	    	while (true) {
	    	    		trList = driver.findElements(By.xpath("//table[@id='tableBootstrap']//tbody/tr"));
	        	    	
	    	    		logger.info("Start process page " + (j+1) + " with <tr> amount : " + trList.size());
	    	    		for (int i=0; i<trList.size(); i++) {
	    	    			
	    	    			try {
	    	    				int k = i+1;
	                	    	
	                	    	String compareSku;
	                	    	compareSku = driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr["+k+"]/td[6]")).getText();
	                	    	loopTest++;
	                	    	if (stockList.containsKey(compareSku)) {
	                	    		if (driver.findElements(By.xpath("//table[@id='tableBootstrap']//tbody/tr["+k+"]/td[7]/input")).size() > 0) {
	                	    			String stockAmountProduct;
	                    	    		stockAmountProduct = stockList.get(compareSku);
	                    	    		
	                    	    		String currentAmount;
	                    	    		currentAmount = driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr["+k+"]/td[7]/input")).getAttribute("value");
	                    	    		
	                    	    		if (!currentAmount.equals(stockAmountProduct)) {
	                    	    			
	                    	    			int loopC = 0;
	                    	    			boolean updated = false;
	                    	    			
	                    	    			logger.info("Sku " + compareSku);
	                    	    			
	                    	    			while (loopC < 2) {
	                    	    				driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr["+k+"]/td[7]/input")).clear();
	                            	    		driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr["+k+"]/td[7]/input")).sendKeys(stockAmountProduct);
	                            	    		driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr["+k+"]/td[2]/input")).click();
	                            	    		
	                            	    		if (driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr["+k+"]/td[7]/span")).getText().contains("更新しました")) {
	                            	    			updated = true;
	                            	    			break;
	                            	    		}
	                            	    		Thread.sleep(1000);
	                            	    		loopC++;
	                            	    		
	                            	    		logger.info("Update failed, re-trying by jQuery clear command ");
	                            	    		
	                            	    		JavascriptExecutor jsx = (JavascriptExecutor)driver;
	                    	    			    jsx.executeScript("$('.input-upd').removeClass('input-upd')", "");
	                    	    			    
	                    	    			    logger.info("Clear command executed");
	                            	    		
	                    	    			}
	                    	    			
	                    	    			if (updated) {
	                        	    			updatedProductCount++;
	                            	    		processSKU++;
	                            	    		logger.info(" Current " + currentAmount);
	                        	    			logger.info(" New " + stockAmountProduct);
	                        	    		}
	                    	    		}
	                	    		}
	                	    		stockList.remove(compareSku);
	                	    	}
	                	    	
	                	    	if (stockList.size() < 1) {
	                	    		break;
	                	    	}
	    	    			} catch (Exception e) {
	    	    				logger.error("Exception when loop TR " + i + " page " + j);
	    	    				logger.error(e.getMessage());
	    	    			}
	            	    }
	    	    		
	    	    		if (j <= totalPage-2 && loopTest > 95) {
	    	    			break;
	    	    		} else if (j > totalPage-2 && loopTest > 1) {
	    	    			break;
	    	    		}
	    	    		
	    	    		logger.warn("Do not loop all of tr");
	    	    		logger.warn("Actual loop " + loopTest);
	    	    		logger.warn("Start loop tr again");
	    	    		
	    	    		Thread.sleep(2000);
	    	    	}
	    	    	
    	    		if (j < totalPage-1) {
    	    			while (true) {
    	    				try {
        	    				logger.info("Next page "+(j+2));
                    	    	//Next page
        	    				while (true) {
        	    					if (driver.findElements(By.xpath("//li[@class='page-next']/a")).size() > 0) {
        	    						logger.info("Trying click next page");
        	    						driver.findElement(By.xpath("//li[@class='page-next']/a")).click();
        	    						break;
        	    					}
        	    				}
                        	    
                        	    //Wait until search finish
                        	    while (true) {
                        	    	Thread.sleep(2000);
                        	    	logger.info("Wait until next page done");
                        	    	if (driver.findElement(By.xpath("//div[@class='fixed-table-loading']")).getAttribute("style").contains("none") &&
                        	    			driver.findElements(By.xpath("//table[@id='tableBootstrap']//tbody/tr")).size() > 1 && 
                        	    				!driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr[1]/td[6]")).getText().isEmpty() &&
                        	    			loopTest > 0) {
                        	    		break;
                        	    	}
                        	    }
                        	    
                        	    break;
        	    			} catch (Exception e) {
        	    				logger.error("Next page error " + e.getMessage());
        	    				
        	    				JavascriptExecutor jsx = (JavascriptExecutor)driver;
        	    			    jsx.executeScript("$('.input-upd').removeClass('input-upd')", "");
        	    				
        	    			    Thread.sleep(2000);
        	    			    
        	    			    //Next page
                        	    //driver.findElement(By.xpath("//li[@class='page-next']/a")).click();
                        	    
                        	    //Wait until search finish
//                        	    while (true) {
//                        	    	Thread.sleep(2000);
//                        	    	if (driver.findElements(By.xpath("//table[@id='tableBootstrap']//tbody/tr")).size() > 1 && 
//                        	    			!driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr[1]/td[6]")).getText().isEmpty() &&
//                        	    			loopTest > 0) {
//                        	    		break;
//                        	    	}
//                        	    }
//                        	    logger.info("Next page retry OK ");
        	    			}
    	    			}
            	    }
    	    	} catch (Exception e) {
    	    		logger.error("Exception when loop page " + j);
    	    		logger.error(e.getMessage());
    	    		
    	    		Robot rb = new Robot();

              		//press enter
              		rb.keyPress(KeyEvent.VK_ENTER);
              		rb.keyRelease(KeyEvent.VK_ENTER);
    	    		
    	    		Thread.sleep(200);
    	    	}
    	    }
    	    
    	    logger.info("After update, stockList size " + stockList.size());
    	    logger.info("Update by method 2");
    	    
    	    //Update by method 2
    	    HashMap<String, String> createList = new HashMap<String, String>();
    	    
    	    for(String skuChild : stockList.keySet()){
    	    	try {
    	    		
    	    		while (true) {
    	    			Thread.sleep(2000);
        	    		if (driver.findElements(By.xpath("//div[@class='fixed-table-loading']")).size() > 0 && 
        	    				driver.findElement(By.xpath("//div[@class='fixed-table-loading']")).getAttribute("style").contains("none")) {
        	    			break;
        	    		}
        	    	}
    	    		
    	    		driver.findElement(By.id("_searchText")).clear();
    	    		driver.findElement(By.id("mngNumberSearchId")).click();
    	    		driver.findElement(By.id("_searchText")).sendKeys(skuChild);
    	    		driver.findElement(By.id("mngNumberSearchId")).submit();
    	    		
    	    		while (true) {
    	    			Thread.sleep(2000);
        	    		if (driver.findElements(By.xpath("//div[@class='fixed-table-loading']")).size() > 0 && 
        	    				driver.findElement(By.xpath("//div[@class='fixed-table-loading']")).getAttribute("style").contains("none")) {
        	    			break;
        	    		}
        	    	}
    	    		
    	    	} catch (Exception e) {
    	    		logger.error("Search button failed");
    	    		logger.error(e.getMessage());
    	    		
    	    		((JavascriptExecutor)driver).executeScript( "window.onbeforeunload = function(e){};");
    	    		
    	    		Thread.sleep(200);
    	    	}
    	    	
    	    	
    	    	while (true) {
    	    		try {
    	    			List<WebElement> tdList = driver.findElements(By.xpath("//table[@id='tableBootstrap']//tbody/tr/td"));
    	    	    	
    	    	    	if (tdList.size() > 5) {
    	    	    		//found - update immediately
    	    	    		
    	    	    		if (driver.findElements(By.xpath("//table[@id='tableBootstrap']//tbody/tr/td[7]/input")).size() > 0) {
    	    	    			String stockAmountProduct;
    	        	    		stockAmountProduct = stockList.get(skuChild);
    	        	    		
    	        	    		String currentAmount;
    	        	    		currentAmount = driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr/td[7]/input")).getAttribute("value");
    	        	    		
    	        	    		if (!currentAmount.equals(stockAmountProduct)) {
    	        	    			
    	        	    			int loopC = 0;
    	        	    			boolean updated = false;
    	        	    			
    	        	    			logger.info("Sku " + skuChild);
    	        	    			
    	        	    			while (loopC < 2) {
    	        	    				driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr/td[7]/input")).clear();
    	                	    		driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr/td[7]/input")).sendKeys(stockAmountProduct);
    	                	    		driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr/td[2]/input")).click();
    	                	    		
    	                	    		if (driver.findElement(By.xpath("//table[@id='tableBootstrap']//tbody/tr/td[7]/span")).getText().contains("更新しました")) {
    	                	    			updated = true;
    	                	    			break;
    	                	    		}
    	                	    		Thread.sleep(1000);
    	                	    		loopC++;
    	                	    		
    	                	    		logger.info("Update failed, re-trying by jQuery clear command ");
    	                	    		
    	                	    		JavascriptExecutor jsx = (JavascriptExecutor)driver;
    	        	    			    jsx.executeScript("$('.input-upd').removeClass('input-upd')", "");
    	        	    			    
    	        	    			    logger.info("Clear command executed");
    	                	    		
    	        	    			}
    	        	    			
    	        	    			if (updated) {
    	            	    			updatedProductCount++;
    	                	    		processSKU++;
    	                	    		logger.info(" Current " + currentAmount);
    	            	    			logger.info(" New " + stockAmountProduct);
    	            	    		}
    	        	    		}
    	    	    		}
    	    	    	} else {
    	    	    		//not found - need to create new
    	    	    		logger.info("Searched " + skuChild + " but not found. Remain in list to create new.");
    	    	    		createList.put(skuChild, stockList.get(skuChild));
    	    	    	}
    	    	    	
    	    	    	break;
    	    		} catch (Exception e) {
    	    			logger.error("Error when wait for search result");
    	    			logger.error(e.getMessage());
    	    			Thread.sleep(2000);
    	    		}
    	    	}
    	    }
    	    
    	    logger.info("Product remain " + createList.size());
    	    logger.info("Start to create new");
    	    
    	    for (int i=0; i<amzList.size(); i++) {
    	    	
    	    	AmazonProduct oriProduc = amzList.get(i);
    	    	String sku = oriProduc.getSku().toLowerCase();
    	    	
    	    	if (createList.containsKey(sku)) {
    	    	    String productName = "";
    	    	    String stockAmount = "";
    	    	    String price = "";
    	    	    String explain = "";
    	    		
    	    		productName = oriProduc.getProductName();
	    	    	productName = productName.replaceAll("[\\[\\]<>〓ⅡⅣⅠⅢⅤⅥⅦⅧⅨⅩ]", "");
	    	    	
	    	    	stockAmount = oriProduc.getStockAmount();
	    	    	
	    	    	price = oriProduc.getPrice();
	    	    	price = price.replaceAll("\\D+","");
	    	    	
	    	    	explain = oriProduc.getExplain();
	    	    	
	    	    	if (StringUtils.isAnyEmpty(sku, productName, stockAmount, price)) {
	    	    		continue;
	    	    	}
	    	    	
	    	    	try {
//	    	    		createNewProduct(driver, sku, productName, stockAmount, price, explain);
		    			processSKU++;
	    	    	} catch (Exception e) {
	    	    		//notFound = false;
	    	    		logger.error("Create new product failed " + e.getMessage());
	    	    		
	    	    		continue;
	    	    	}
    	    	}
    	    }
        } catch(SQLException ioe) {
        	logger.error("SQLException : " + ioe.getMessage());
        } catch(Exception ex) {
        	logger.error("Exception : " + ex.getMessage());
        } finally {
        	logger.info("Rakuten product processed : " + processSKU);
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
	    
//	    driver.findElement(By.name("display_caption")).clear();
//	    driver.findElement(By.name("display_caption")).sendKeys("<a href='http://www.rakuten.co.jp/ism-group/'><img src='http://image.rakuten.co.jp/ism-group/cabinet/04041969/img62460604.jpg '></a>");
	    
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
		
		List<WebElement> lis = driver.findElements(By.xpath("//li[@class='first']"));
    	
    	for (int i=0; i<lis.size(); i++) {
    		if (lis.get(i).getText().contains("商品登録")) {
    			int t = i+1;
    			driver.findElement(By.xpath("//li[@class='first'][" + t + "]/a")).click();
    			break;
    		}
    	}
    	
    	lis = driver.findElements(By.xpath("//a[@class='btnLink btnMiddle']"));
    	
    	for (int i=0; i<lis.size(); i++) {
    		if (lis.get(i).getText().contains("商品ページ設定")) {
    			lis.get(i).click();
    			break;
    		}
    	}
    	
    	lis = driver.findElements(By.xpath("//a"));
    	
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

