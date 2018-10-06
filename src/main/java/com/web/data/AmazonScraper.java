package com.web.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import com.google.common.base.Joiner;
import com.web.entity.*;
import com.web.util.config.CommonProperties;
import com.web.util.SendAttachmentInEmail;
import com.web.util.UtilFunction;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AmazonScraper {
	
	private AmazonProduct amazonProduct = new AmazonProduct();
	final static Logger logger = Logger.getLogger(AmazonScraper.class);
	
	public static String homepage = "https://sellercentral.amazon.co.jp";
	
	public Map<String, String> exlainMap = new HashMap<String, String>();

	public void amzGather(boolean testFlag)
    {
		List<AmazonProduct> amazonProductList = new ArrayList<AmazonProduct>();
		System.setProperty("webdriver.chrome.driver", "C:\\wd\\chromedriver.exe");
		ChromeDriver driver = null;
		Connection connection = null;
		Statement statement = null;
		
        try {
        	logger.info("Amazon information collecting.");
        	
        	CommonProperties.loadProperties();

    		final String connectionString = CommonProperties.getConnectionString();
    		final String downloadFilepath = CommonProperties.getDownloadPath();
        	
        	Class.forName("org.sqlite.JDBC");
    	    
    	    connection = DriverManager.getConnection(connectionString);
    	    statement = connection.createStatement();
    	    
    	    //Delete all old data
    	    try {
    	    	statement.executeUpdate("DELETE FROM amazon_product_info");
    	    } catch (Exception ex) {
    	    	logger.error("Error in DELETE FROM amazon_product_info");
    	    } finally {
        	    try { statement.close(); } catch (Exception e) { /* ignored */ }
                try { connection.close(); } catch (Exception e) { /* ignored */ }
    	    }
    	    
    	    //statement.setQueryTimeout(30);  // set timeout to 30 sec.
    	    
    	    HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
    	    chromePrefs.put("profile.default_content_settings.popups", 0);
    	    chromePrefs.put("download.default_directory", downloadFilepath);
    	    ChromeOptions optionsChrome = new ChromeOptions();
    	    optionsChrome.setExperimentalOption("prefs", chromePrefs);
    	    
    	    //optionsChrome.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
    	    optionsChrome.addArguments("no-sandbox");
    	    
    	    DesiredCapabilities cap = DesiredCapabilities.chrome();
    	    cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
    	    cap.setCapability(ChromeOptions.CAPABILITY, optionsChrome);
    	    driver = new ChromeDriver(cap);
    	    
    		driver.manage().window().maximize();
    	    
    	    driver.navigate().to(homepage);
    	    
    	    driver.findElement(By.id("signInSubmit")).click();
    	    
    	    while (true) {
    	    	if (driver.findElements(By.id("ap_email")).size() > 0) {
    	    		driver.findElement(By.id("ap_email")).clear();
    	    	    driver.findElement(By.id("ap_email")).sendKeys(CommonProperties.getAmazonAccount());
    	    	    
    	    	    driver.findElement(By.id("ap_password")).clear();
    	    	    driver.findElement(By.id("ap_password")).sendKeys(CommonProperties.getAmazonPassword());
    	    	    
    	    	    driver.findElement(By.id("signInSubmit")).click();
    	    	    
    	    	    List<WebElement> eles = driver.findElements(By.id("auth-mfa-otpcode"));
    	    	    
    	    	    if (eles.size() > 0) {
    	    	    	break;
    	    	    } else {
    	    	    	Thread.sleep(200);
    	    	    }
    	    	} else {
    	    		Thread.sleep(200);
    	    	}
    	    }
    	    
    	    Thread.sleep(1000);
    	    
    	    if (driver.findElements(By.id("merchant-picker-btn-skip-for-now-announce")).size() > 0) {
    	    	logger.info("Authen requierd");
    	    	
    	    	driver.findElement(By.id("merchant-picker-btn-skip-for-now-announce")).click();
    	    }
    	    
    	    //2 steps authenticate detect
    	    int retryCount = 0;
    	    while (true) {
    	    	List<WebElement> eles = driver.findElements(By.id("auth-mfa-otpcode"));
    	    	if (eles.size() > 0) {
    	    		retryCount++;
    	    		
    	    		TimeUnit.MINUTES.sleep(20);
//    	    		TimeUnit.SECONDS.sleep(70);
    	    		
    	    		//Remain each 60 minutes
    	    		if ((retryCount % 3) == 0) {
    	    			String mailBody = "";
    	    			mailBody = mailBody + "2段階認証コードが入力必要 \r\n";
    	    			SendAttachmentInEmail.sendMail("認証コードお知らせ", mailBody, new ArrayList<String>());
    	    		}
    	    	} else {
    	    		break;
    	    	}
    	    }
    	    
    	    //Download Delivery Report
    	    downloadReport(driver, downloadFilepath);
    	    
    	    driver.findElement(By.xpath("//ul[@id='sc-top-nav-root']/li[2]/a")).click();
    	    
    	    List<WebElement> eles = new ArrayList<WebElement>();
    	    
    	    eles = driver.findElements(By.xpath("//input[@name='FULFILLMENT']"));
    	    
    	    for (WebElement ele : eles) {
    	    	//FBA only
    	    	if (ele.getAttribute("value").trim().equals("FulfilledByAmazon") && ele.getAttribute("type").equals("radio")) {
    	    		while (true) {
	    	    		try {
	    	    			ele.click();
	    	    			break;
	    	    		} catch (Exception e) {
	    	    			continue;
	    	    		}
    	    		}
    	    		break;
    	    	}
    	    }
    	    
//	        boolean deliverRadioFlag = true;
//	        while (deliverRadioFlag) {
//	        	try {
//		        	Thread.sleep(1000);
//		        	eles = driver.findElements(By.xpath("//li[@id='LISTINGS_VIEW_SECTION']//li"));
//			        
//			        for (WebElement ele : eles) {
//		    	    	//In stock only
//		    	    	if (ele.getText().trim().equals("出品中")) {
//		    	    		ele.click();
//		    	    		deliverRadioFlag = false;
//		    	    		break;
//		    	    	}
//		    	    }
//			        
//			        eles = driver.findElements(By.xpath("//li[@id='LISTINGS_VIEW_SECTION-floating']//li"));
//			        
//			        for (WebElement ele : eles) {
//		    	    	//In stock only
//		    	    	if (ele.getText().trim().equals("出品中")) {
//		    	    		ele.click();
//		    	    		deliverRadioFlag = false;
//		    	    		break;
//		    	    	}
//		    	    }
//			        
//	        	} catch (Exception ex) {
//	        		continue;
//	        	}
//	        }
	        
    	    //Show 250 products per page
    	    eles = driver.findElements(By.xpath("//div[@class='mt-records-per-page']//option"));
    	    //Last option
    	    eles.get(eles.size()-1).click();
    	    Thread.sleep(2000);
    	    
    	    JavascriptExecutor jsx = (JavascriptExecutor)driver;
    	    jsx.executeScript("window.scrollBy(0,111000)", "");
    	    
    	    List<WebElement> productList;
    	    productList = new ArrayList<WebElement>();
    	    
    	    productList = driver.findElements(By.xpath("//table[@class='a-bordered a-horizontal-stripes  mt-table']/tbody/tr"));
    	    
    	    boolean doTheLoop = true;
    	    int h = 0;
    	    while (doTheLoop) {
    	        h = h+1;
    	        Thread.sleep(2000);
    	        
    	        jsx.executeScript("window.scrollBy(0,111000)", "");
    	        productList = driver.findElements(By.xpath("//table[@class='a-bordered a-horizontal-stripes  mt-table']/tbody/tr"));
    	        
    	        if (h>30){
    	            doTheLoop = false;
    	        }
    	        if (productList.size() > 100){
    	            doTheLoop = false;
    	        }     
        	}
    	    
        	int totalPage;
        	eles = driver.findElements(By.xpath("//ul[@class='a-pagination']/li"));
        	totalPage = Integer.parseInt(eles.get(eles.size()-2).getText());
        	
        	int totalProduct = Integer.valueOf(driver.findElement(By.id("mt-header-count-value")).getText());
	        int productLastPage = totalProduct - (250*(totalPage-1)); 
	        
	        logger.info("Amazon product collected ： " + totalProduct);
        	
        	String htmlCode = "";
        	String htmlCodeSub = "";
        	Document doc;
        	Document docSub;
        	Elements tds;
        	Elements options;
        	String tmp = "";
        	String tmp2 = "";
        	ArrayList<String> mainTag = new ArrayList<String>(Arrays.asList("sku", "title", "quantity", "price", "action"));
        	
        	for (int i=0; i<totalPage; i++) {
        		
        		logger.info("Start process page ： " + (i+1));
        		
        		//Product list
        		productList = new ArrayList<WebElement>();
        		productList = driver.findElements(By.xpath("//table[@class='a-bordered a-horizontal-stripes  mt-table']/tbody/tr"));
        	    
        	    h = 0;
        	    while (true) {
        	        h = h+1;
        	        Thread.sleep(2000);
        	        
        	        jsx.executeScript("window.scrollBy(0,111000)", "");
        	        productList = driver.findElements(By.xpath("//table[@class='a-bordered a-horizontal-stripes  mt-table']/tbody/tr"));
        	        
        	        if (h>30){
        	            break;
        	        }
        	        if (i<totalPage - 1) {
        	        	if (productList.size() > 240){
        	        		break;
            	        }
        	        } else {
        	        	if (productList.size() > productLastPage - 2){
        	        		break;
            	        }
        	        }
        	        logger.warn("Looped tr amount = " + productList.size());
        	        logger.warn("Retry until loop full tr in page");
            	}
        	    
        	    logger.info("Loop tr finish normally");
        	    logger.info("Product in page ： " + productList.size());
        		
        		jsx.executeScript("window.scrollBy(0,0)", "");
        		int productCount = 0;
        		
        		for (WebElement product : productList) {
        			try {
        				productCount++;
        			
	        			if (!product.getAttribute("id").equals("head-row")) {
	        				
	        				amazonProduct = new AmazonProduct();
	        				
	        				htmlCode = product.getAttribute("innerHTML");
	        				
	        				htmlCode = "<table><tr>" + htmlCode + "</tr></table>";
	    					
	    					doc = Jsoup.parse(htmlCode);
	    					tds = doc.select("td");
	    					
	    					for (Element td : tds) {
	    						
	    						tmp = td.attr("data-column");
	    						tmp2 = "";
	    						
	    						if (!mainTag.contains(tmp)) continue;
	    						
	    						
	    						htmlCodeSub = "<table><tbody><tr><td>" + td.html() + "</td></tr></tbody></table>";
	    						
	    						htmlCodeSub = htmlCodeSub.replaceAll("\"", "'");
	        					
	    						docSub = null;
	        					docSub = Jsoup.parse(htmlCodeSub);
	        					
	        					amazonProduct.setAmazonProcessFlag("0");
	    						
	    						if (tmp.equals("sku")) {
	    							//SKU
	    							String skuTmp = docSub.select("span[class=mt-text-content mt-table-main]").text();
	    							amazonProduct.setSku(skuTmp);
	    							
	    						} else if (tmp.equals("title")) {
	    							//Product Name
	    							amazonProduct.setProductName(docSub.select("a[class=a-link-normal mt-table-main]").text().replaceAll("'", ""));
	    							
	    							//ASIN
	    							amazonProduct.setAsin(docSub.select("span[class=mt-text-content mt-table-detail]").text());
	    							
	    							//Product URL
	    							amazonProduct.setAmazonProductLink(docSub.select("a[class=a-link-normal mt-table-main]").attr("href"));
	    						} else if (tmp.equals("quantity")) {
	    							
	    							//Stock amount
	    							tmp2 = docSub.select("span[class=mt-text-content]").text();
	    							
	    							String tmp3 = docSub.select("span[class=mt-text-content mt-table-main]").text();
	    							
	    							String tmp4 = docSub.select("input[class=a-input-text main-entry mt-input-text]").attr("value");
	    							
	    							if (!StringUtils.isEmpty(tmp2)) {
	    								amazonProduct.setStockAmount(tmp2);
	    							} else if (!StringUtils.isEmpty(tmp3)) {
	    								amazonProduct.setStockAmount(tmp3);
	    							} else if (!StringUtils.isEmpty(tmp4)) {
	    								amazonProduct.setStockAmount(tmp4);
	    							}
	    							
	    						} else if (tmp.equals("price")) {
	    							//price
	    							amazonProduct.setPrice(docSub.select("input[class=a-input-text main-entry mt-icon-input mt-input-text]").attr("value"));
	    						} else if (tmp.equals("action")) {
	    							
	    							options = docSub.select("select > option");
	    							
	    							for (Element option : options) {
	    								if (option.attr("id").contains("-edit")) {
	    									tmp2 = option.attr("data-action-string");
	    	    							tmp2 = tmp2.replaceAll("&amp;", "&");
	    	    							break;
	    								}
	    							}
	    							
	    							//Edit link
	    							amazonProduct.setAmazonProductEditLink(tmp2);
	    						}
	    					}
	    					amazonProductList.add(amazonProduct);
	        			}
        			} catch (Exception e) {
        				logger.error("Error in loop product. Page " + i + " Product " + productCount);
        				logger.error(e.getMessage());
        				continue;
        			}
        		}
        		
        		//Gather Explain
        		explainGatherByCSV(driver, amazonProductList);
        		
        		//Register to DB
        		updateDB(amazonProductList, connectionString);
        		
        		amazonProductList = new ArrayList<AmazonProduct>();
        		
        		//Next page
        		eles.get(eles.size()-1).click();
        		Thread.sleep(2000);
        		eles = new ArrayList<WebElement>();
        		eles = driver.findElements(By.xpath("//ul[@class='a-pagination']/li"));
        		if (testFlag) {
        			break;
        		}
        	}
        	
        } catch(SQLException ioe) {
        	logger.error("Error in main function SQLException "+ioe.getMessage());
            //ioe.printStackTrace();
        } catch(Exception ex) {
        	//ex.printStackTrace();
        	logger.error("Error in main function"+ex.getMessage());
        } finally {
        	driver.close();
            driver.quit();
        }
        
    }
	
	public void updateDB(List<AmazonProduct> objLst, String cntString) throws SQLException, UnsupportedEncodingException {
		String result;
		String sum = "";
		
		int countRound = (int) Math.ceil(objLst.size()/20.0);
		int start=0;
		int end=0;
		
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
    	    
			connection = DriverManager.getConnection(cntString);
			statement = connection.createStatement();
			
			for (int i=1; i<=countRound; i++){
				sum = "";
				end = i*20;
				if (end >= objLst.size()) end = objLst.size() - 1;
				
				for (int k=start; k<=end; k++) {
					result = Joiner.on("', '").join(Arrays.asList(
							objLst.get(k).getSku(), 
							objLst.get(k).getAsin(), 
							objLst.get(k).getProductName(), 
							objLst.get(k).getStockAmount(), 
							objLst.get(k).getExplain(), 
							objLst.get(k).getPrice(), 
							objLst.get(k).getImageName(), 
							objLst.get(k).getAmazonProductLink(), 
							objLst.get(k).getAmazonProductEditLink(), 
							objLst.get(k).getImageLocalPath(), 
							objLst.get(k).getAmazonProcessFlag(), 
							objLst.get(k).getRakutenProcessFlag()));
					if (!result.isEmpty()) {
					  result = "('" + result + "'),";
					  sum = sum + result;
					}
				}
				
				if (!sum.isEmpty()) {
					sum = sum.substring(0, sum.length() -1);
					sum = "insert into amazon_product_info VALUES" + sum;
					
					statement.executeUpdate(sum);
					start = end + 1;
				}
			}
		} catch (Exception e) {
			logger.error("Error in updateDB");
			logger.error(sum);
		} finally {
			try { statement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
		}
	}
	
	public void downloadReport(WebDriver driver, String downloadFilepath) throws IOException, InterruptedException {

	    List<WebElement> as = new ArrayList<WebElement>();
	    File reportFile;

	    driver.navigate().to("https://sellercentral.amazon.co.jp/listing/reports");
	    
	    while (true) {
	    	try {
	    		//click to get latest file
	    	    //Selectbox
	    	    Select select = new Select(driver.findElement(By.name("reportVariant")));
	    		select.selectByValue("300");
	    		break;
	    	} catch (Exception npe) {
	    		Thread.sleep(500);
	    	}
	    }
	    
		driver.findElement(By.name("report-request-button")).click();
		
		driver.navigate().refresh();
		
		TimeUnit.MINUTES.sleep(2);
		boolean processing = false;
		
		while (true) {
			if (driver.findElements(By.xpath("//table[@class='a-bordered a-horizontal-stripes  mt-table']/tbody/tr[2]/td[5]")).size() > 0) {
				if (driver.findElement(By.xpath("//table[@class='a-bordered a-horizontal-stripes  mt-table']/tbody/tr[2]/td[5]")).getText().equals("処理中") ||
						driver.findElement(By.xpath("//table[@class='a-bordered a-horizontal-stripes  mt-table']/tbody/tr[2]/td[4]")).getText().equals("未処理")) {
					processing = true;
				}
				if (driver.findElement(By.xpath("//table[@class='a-bordered a-horizontal-stripes  mt-table']/tbody/tr[2]/td[5]")).getText().equals("準備完了")) {
					break;
				}
			}
			
			Thread.sleep(10000);
			driver.navigate().refresh();
		}
		
		driver.navigate().refresh();
	    
	    as = driver.findElements(By.xpath("//table[@class='a-bordered a-horizontal-stripes  mt-table']/tbody/tr/td[6]//a"));
	    
	    for (WebElement a : as ) {
	    	if (a.getAttribute("href").contains("reports")) {
	    		a.click();
	    		break;
	    	}
	    }
	    
	    while (true) {
	    	try {
	    		reportFile = UtilFunction.lastFileModified(downloadFilepath);
	    		
	    		if (reportFile == null || !reportFile.getName().endsWith(".txt")) {
		    		continue;
		    	}
	    		
			    //Read file and match with DB
			    //BufferedReader buf = new BufferedReader(new FileReader(reportFile));
			    
	    		FileInputStream is = new FileInputStream(reportFile);
			    BufferedReader buf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			    
		        String lineJustFetched = null;
		        String[] wordsArray;
		        String tmpExplain ="";
		
		        while (true) {
		        	tmpExplain = "";
		            lineJustFetched = buf.readLine();
		            if(lineJustFetched == null){  
		                break; 
		            } else {
		                wordsArray = lineJustFetched.split("\t");
		                //SKU at column 3 and Explain at column 8
		                if (wordsArray.length > 7 & !StringUtils.isAnyEmpty(wordsArray[2], wordsArray[7])) {
		                	tmpExplain = wordsArray[7];
		                	tmpExplain = tmpExplain.replaceAll("'", "");
		                	exlainMap.put(wordsArray[2], tmpExplain);
		                }
		            }
		        }
		        
		        buf.close();
		        Files.deleteIfExists(reportFile.toPath()); 
		        break;
		    } catch (FileSystemException fse) {
		    	continue;
		    } catch (Exception e) {
		    	continue;
		    }
	    }
        
	}
	
	public void explainGatherByCSV(WebDriver driver, List<AmazonProduct> amazonProductList) throws IOException {
        for (int i=0; i<amazonProductList.size(); i++) {
        	if (exlainMap.containsKey(amazonProductList.get(i).getSku())) {
        		amazonProductList.get(i).setExplain(exlainMap.get(amazonProductList.get(i).getSku()));
        	}
        }
	}
	
//	public static void main(String[] args) {
//		File reportFile;
//		
//		while (true) {
//	    	try {
//	    		reportFile = UtilFunction.lastFileModified("C:\\Users\\NECVN\\Downloads\\1");
//	    		
//	    		if (reportFile == null || !reportFile.getName().endsWith(".txt")) {
//		    		continue;
//		    	}
//	    		
//			    //Read file and match with DB
//			    //BufferedReader buf = new BufferedReader(new FileReader(reportFile));
//			    
//	    		FileInputStream is = new FileInputStream(reportFile);
////			    BufferedReader buf = new BufferedReader(new InputStreamReader(is, "Shift-JIS"));
//			    BufferedReader buf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//			    
//		        String lineJustFetched = null;
//		        String[] wordsArray;
//		        String tmpExplain ="";
//		
//		        while (true) {
//		        	tmpExplain = "";
//		            lineJustFetched = buf.readLine();
//		            if(lineJustFetched == null){  
//		                break; 
//		            } else {
//		                wordsArray = lineJustFetched.split("\t");
//		                //SKU at column 3 and Explain at column 8
//		                if (wordsArray.length > 7 & !StringUtils.isAnyEmpty(wordsArray[2], wordsArray[7])) {
//		                	tmpExplain = wordsArray[7];
//		                	tmpExplain = tmpExplain.replaceAll("'", "");
////		                	exlainMap.put(wordsArray[2], tmpExplain);
//		                	System.out.println(tmpExplain);
//		                }
//		            }
//		        }
//		        
//		        buf.close();
//		        Files.deleteIfExists(reportFile.toPath()); 
//		        break;
//		    } catch (FileSystemException fse) {
//		    	continue;
//		    } catch (Exception e) {
//		    	continue;
//		    }
//	    }
//	}
	
}

