package com.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SampleClass {
	public static void gatherHardOff() throws IOException {
																	  
		URL url = new URL("http://netmall.hardoff.co.jp/cate/30000011?pageno=&mode=&sort_save=1&product_id=&AtoZ=&AtoZ_save=&upsort_list=1&disp_number=100");
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("POST");
	    connection.setDoOutput(true);
	    connection.setDoInput(true);

	    // important: get output stream before input stream
	    OutputStream out = connection.getOutputStream();
	    out.write(1);
	    out.close();        

        // now you can get input stream and read.
	    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
	    String line = null;
	    
	    StringBuilder builder = new StringBuilder();
	    while ((line = reader.readLine()) != null) {
	    	builder.append(line);
	    }
	    
	    Document doc = Jsoup.parse(builder.toString());
		Element content = doc.getElementById("itemBoxContainer");
		Elements itemDivs = content.getElementsByClass("itemBox");
		
		for (Element itemDiv : itemDivs) {
			
			String brandValue = "";
			String category = "";
			String productName = "";
			String productRank = "";
			String nameCombine = "";
			String productURL = "";
			
			Elements ranks = itemDiv.select("img[class=item_fk_link]");
			for (Element rank : ranks) {
				productRank = rank.attr("alt");
			}
			
//			if (StringUtils.isEmpty(productRank) || !StringUtils.endsWithAny(productRank, new String[] {"N","S","A"})) {
//				continue;
//			}
			
			//URL
			Element a = itemDiv.select("a").first();
			productURL = a.attr("href");
			productURL = "http://netmall.hardoff.co.jp" + productURL;
			
			Elements brands = itemDiv.getElementsByClass("itemBox_brand");
			for (Element brand : brands) {
				brandValue = brand.text();
			}
			
			Elements cates = itemDiv.getElementsByClass("itemBox_category");
			for (Element cate : cates) {
				category = cate.text();
			}
			
			Elements names = itemDiv.getElementsByClass("itemBox_name");
			for (Element name : names) {
				productName = name.text();
			}
			
			nameCombine = "[" + brandValue + "][" +  category + "][" +  productName + "]";
			
			System.out.println(nameCombine);
			System.out.println(productRank);
			System.out.println(productURL);
			
		}
		
		System.out.println(builder.toString());
	}
	
}
