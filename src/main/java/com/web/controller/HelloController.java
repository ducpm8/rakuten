package com.web.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.web.util.SampleClass;
import com.web.util.SendAttachmentInEmail;
import com.web.util.config.CommonProperties;
import com.web.data.AmazonScraper;
import com.web.data.ImageProcess;
import com.web.data.RakutenUpdate;
import com.web.data.RakutenUpdateNewLayout;

@Controller
@RequestMapping(value = "/home")
public class HelloController {
	
	public AmazonScraper amazonScraper = new AmazonScraper();
	public RakutenUpdate ru = new RakutenUpdate();
	public ImageProcess aip = new ImageProcess();
	
	final static Logger logger = Logger.getLogger(HelloController.class);
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String amzGather(ModelMap model) throws IOException, URISyntaxException {
		
		AmazonScraper amazonScraper = new AmazonScraper();
    	RakutenUpdate ru = new RakutenUpdate();
    	ImageProcess aip = new ImageProcess();
    	
    	amazonScraper.amzGather(false);
    	
    	int rktGatherFirstStatus = 0;
    	int rktGatherSecondStatus = 0;
		
    	rktGatherFirstStatus = ru.rakutenGather(true);
    	
    	//rktGatherSecondStatus = ru.rakutenGather(false);
    	
    	rktGatherFirstStatus = rktGatherFirstStatus + rktGatherSecondStatus;
		
		int aipStatus = 0;
		
//		if (rktGatherStatus > 0) {
//			aipStatus = aip.updateImage();
//			if (aipStatus == 9999) {
//				aipStatus = 0;
//				System.out.println("0 image downloaded");
//			}
//		}
		
		try {
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//dd/MM/yyyy
			Date now = new Date();
			String strDate = sdfDate.format(now);
			
			String mailBody = "";
			mailBody = mailBody + "処理日 : " + strDate + "\r\n";
			mailBody = mailBody + "記載変更済み商品 : " + rktGatherFirstStatus + "\r\n";
			mailBody = mailBody + "画像変更済み商品 : " + aipStatus + "\r\n";
			
			SendAttachmentInEmail.sendMail(strDate, mailBody, new ArrayList<String>());
			logger.info("Email sent.");
		} catch (IOException e) {
			logger.error("Email exception");
		} catch (URISyntaxException e) {
			logger.error("Email exception");
		}
		
		return "project1";
	}
	
	public void initializeScraper() throws IOException, URISyntaxException {
		
		CommonProperties.loadProperties();
		
		Calendar today = Calendar.getInstance();
		//2AM
		today.set(Calendar.HOUR_OF_DAY, CommonProperties.getRunTime());
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		//today.add(Calendar.DATE, 1);
		
		Timer timer = new Timer();
		
        //Each 12 hours
        //timer.schedule(new MainProcess(), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
	}
	
	@RequestMapping(value = "/ho", method = RequestMethod.GET)
	public void hoGather(ModelMap model) throws IOException, URISyntaxException {
		
		SampleClass.gatherHardOff();
	}
	
	@RequestMapping(value = "/rkt", method = RequestMethod.GET)
	public void rest(ModelMap model) throws IOException, URISyntaxException {
		
		RakutenUpdate ru = new RakutenUpdate();
		ru.rakutenGather(false);
	}
}