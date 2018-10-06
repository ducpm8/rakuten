package com.web.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.web.util.DownloadFile;
import com.web.util.FTPUploadFileDemo;
import com.web.util.config.CommonProperties;

public class ImageProcess {
	
	private DownloadFile downloadFile = new DownloadFile();
	
	public static String rakutenHomePage = "https://glogin.rms.rakuten.co.jp";
	
	public int updateImage()
    {
		System.setProperty("webdriver.chrome.driver", "C:\\wd\\chromedriver.exe");
		Connection connection = null;
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		Statement stmt = null;
		ResultSet rs = null;
		int rakutenImageUpdateCount = 0;
		
		try {
			
			String processDateTime;
	    	
	    	DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	        Date date = new Date();
	        processDateTime = sdf.format(date);
	        
	        CommonProperties.loadProperties();
	        String imagePath = CommonProperties.getDownloadPath() + "\\" + processDateTime;
	        
	        new File(imagePath).mkdirs();
			
			final String connectionString = CommonProperties.getConnectionString();
	    	
	    	Class.forName("org.sqlite.JDBC");
		    
		    connection = DriverManager.getConnection(connectionString);
		    stmt = connection.createStatement();
		    final int amazonLitmitProcess = CommonProperties.getAmazonLitmitProcess();
			
		    //Get lacking image product list, default limit = 10
			String selectAmazonURL = "SELECT amazon_product_link, sku FROM amazon_product_info " 
		    		+ "WHERE amazon_process_flag = '1' LIMIT " + amazonLitmitProcess;
			rs = stmt.executeQuery(selectAmazonURL);
			
		    String amazonProductURL;
		    String amazonImageURL;
		    String sku;
		    
		    List<String> downloadedImage = new ArrayList<String>();
		    
			while (rs.next()) {
				amazonProductURL = "";
				sku = "";
				amazonImageURL = "";
		    	
				sku = rs.getString("sku");
				amazonProductURL = rs.getString("amazon_product_link"); 
				
				if (StringUtils.isEmpty(amazonProductURL) || StringUtils.isEmpty(sku)) {
					continue;
				}
		    	
		    	sku = sku.toLowerCase();
		    	
		    	driver.navigate().to(amazonProductURL);
		    	
		    	//get image src
		    	amazonImageURL = driver.findElement(By.id("landingImage")).getAttribute("src");
		    	
		    	try {
		    		downloadFile.downloadFile(amazonImageURL, imagePath, sku);
		    		downloadedImage.add(sku);
		    	} catch (Exception ex) {
		    		System.out.println("Error in ImageProcess loop : " + ex.getMessage());
		    		continue;
		    	}
			}
			
			if (downloadedImage.size() < 1) {
				return 9999;
			}
			
			System.out.println("Amazon Image downloaded : " + downloadedImage.size());
			
			//Upload file to FTP
			FTPUploadFileDemo.ftpProcess(imagePath);
			
			//Wait 15 min
			TimeUnit.MINUTES.sleep(15);
			
			driver.navigate().to(rakutenHomePage);
    	    
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
    	    
    	    driver.navigate().to("https://cabinet.rms.rakuten.co.jp/");
    	    
    	    boolean notFound = false;
    	    boolean findImageFlag = true;
    	    boolean appearFlag = true;
    	    int countFindImage = 0;
    	    int countAppearFlag = 0;
    	    int foundImage = 0;
    	    String rCabinetURL = "";
    	    Map<String, String> rCabinetMap = new HashMap<String, String>();
    	    List<WebElement> trs;
    	    List<WebElement> timeoutDetects;
    	    
    	    List<WebElement> imageURL;
    	    List<WebElement> searchI;
    	    
    	    boolean doTheLoop = true;
    	    int h = 0;
    	    while (doTheLoop) {
    	    	searchI = driver.findElements(By.xpath("//form[@name='frm_serch_image']//input[@name='keyword']"));
    	        h = h+1;
    	        Thread.sleep(1000);
    	        if (h>10){
    	        	return 9999;
    	        }
    	        if (searchI.size() > 0 ){
    	            doTheLoop = false;
    	            break;
    	        }      
        	}
    	    
			String recheckName = "";
    	    
    	    //Gather all RCabinet image URL
			for (String skuC : downloadedImage) {
				try {
					rCabinetURL = "";
					recheckName = "";
					findImageFlag = true;
					countFindImage = 0;
					countAppearFlag = 0;
					appearFlag = true;
					foundImage = 0;
					
					while (appearFlag) {
						if (countAppearFlag > 10) {
							appearFlag = false;
							break;
						}
						try {
							while (!driver.findElement(By.xpath("//form[@name='frm_serch_image']//input[@name='keyword']")).getAttribute("value").equals(skuC)) {
								driver.findElement(By.xpath("//form[@name='frm_serch_image']//input[@name='keyword']")).clear();
								Thread.sleep(500);
								driver.findElement(By.xpath("//form[@name='frm_serch_image']//input[@name='keyword']")).sendKeys(skuC);
							}
							
							Thread.sleep(500);
							driver.findElement(By.xpath("//form[@name='frm_serch_image']//input[@id='search_image']")).click();
							
							appearFlag = false;
							break;
						} catch (Exception ex) {
							countAppearFlag++;
							continue;
						}
					}
					
					while (findImageFlag) {
						if (countFindImage > 30) {
							findImageFlag = false;
							break;
						}
						
						while (foundImage < 30) {
							if (driver.findElements(By.xpath("//div[@id='image_list']/table/tbody/tr/td/form")).size() != 1) {
								foundImage++;
								Thread.sleep(500);
							} else {
								foundImage = 27;
								break;
							}
						}
						
						if (foundImage > 28) {
							countFindImage++;
							continue;
						}
						
						recheckName = driver.findElement(By.xpath("//div[@id='image_list']/table/tbody/tr/td/form/table[3]/tbody/tr[2]")).getText();
						
						if (skuC.equals(recheckName)) {
							rCabinetURL = driver.findElement(By.xpath("//div[@id='image_list']/table/tbody/tr/td/form//textarea[@name='textarea']")).getAttribute("value");
							
							rCabinetMap.put(skuC, rCabinetURL);
							findImageFlag = false;
							break;
						} else {
							Thread.sleep(500);
							countFindImage++;
						}
					}
					
				} catch (Exception ex) {
					continue;
				}
			}
			
			//Loop each Rakuten product and update image
			for (String key : rCabinetMap.keySet()) {
				try {
	    	    	if (!notFound) {
	    	    		driver.navigate().to("https://item.rms.rakuten.co.jp/rms/mall/rsf/item/vc?__event=RI33_001_001&shop_bid=307311&proc_type=normal");
	    	    	}
	    	    	
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
	    	    	
	        	    driver.findElement(By.id("mngNumberSearchId")).click();
	        	    driver.findElement(By.id("_searchText")).clear();
	    	    	
	    	    	driver.findElement(By.id("_searchText")).sendKeys(key);
	    	    	driver.findElement(By.id("mngNumberSearchId")).submit();
	    	    	
	    	    	trs = driver.findElements(By.xpath("//table[21]//table//tr/td[1]/font/font[1]//a"));
	        	    
	        	    if (trs.size() > 0) {
	        	    	if (trs.get(0).getText().equals("変更")) {
	        	    		trs.get(0).click();
	        	    		notFound = false;
	        	    	}
	        	    } else {
	        	    	//product not found
	        	    	notFound = true;
	        	    	continue;
	        	    }
	        	    
	        	    doTheLoop = true;
	        	    h = 0;
	        	    while (doTheLoop) {
	        	    	try {
	        	    		imageURL = driver.findElements(By.id("image_url1"));
	        	    		
		        	        h = h+1;
		        	        Thread.sleep(2000);
		        	        
		        	        if (h>30){
		        	            doTheLoop = false;
		        	        }
		        	        if (imageURL.size() > 0){
		        	            doTheLoop = false;
		        	            break;
		        	        }     
		        	        
	        	    	} catch (Exception ex) {
	        	    		continue;
	        	    	}
	            	}
	        	    
	        	    driver.findElement(By.id("image_url1")).clear();
	        	    driver.findElement(By.id("image_url1")).sendKeys(rCabinetMap.get(key));
	        	    
	        	    //Save
	        	    driver.findElement(By.id("submitButton")).click();
	        	    
	        	    rakutenImageUpdateCount++;
				} catch (Exception ex) {
					continue;
				}
			}
			
			System.out.println("Rakuten Image updated : " + rakutenImageUpdateCount);
			
		} catch(SQLException ioe) {
            ioe.printStackTrace();
        } catch(Exception ex) {
        	ex.printStackTrace();
        } finally {
        	driver.close();
        	driver.quit();
        	try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
		
		return rakutenImageUpdateCount;
    }
	
}

