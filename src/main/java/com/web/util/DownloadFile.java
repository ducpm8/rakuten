package com.web.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadFile {
	public String downloadFile(String imageLink, String imageSubPath, String imageName) throws IOException {
		
		URL website;
		ReadableByteChannel rbc;
		
		
		String extension = "";
		String imageActualName = "";
		imageActualName = imageLink.substring(imageLink.lastIndexOf("/") + 1, imageLink.length());
		int pos = imageActualName.lastIndexOf(".");
		if (pos > 0) {
			extension = imageActualName.substring(pos, imageActualName.length());
			imageName = imageName + extension;
		} 
		
		String localPath = imageSubPath + File.separator  + imageName;
		
		website = new URL(imageLink);
		rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(localPath);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		
		fos.close();
		
		return localPath;
	}
}
