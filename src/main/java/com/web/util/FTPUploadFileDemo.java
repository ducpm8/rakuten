package com.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.web.util.config.CommonProperties;

public class FTPUploadFileDemo {
	public static void ftpProcess(String localDirPath) throws IOException, URISyntaxException{
		
		CommonProperties.loadProperties();
		
		String server = CommonProperties.getrCabinetServerName();
        int port = Integer.valueOf(CommonProperties.getrCabinetServerPort());
        String user = CommonProperties.getrCabinetServerUser();
        String pass = CommonProperties.getrCabinetServerPassword();
 
        FTPClient ftpClient = new FTPClient();
 
        try {
            // connect and login to the server
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
 
            // use local passive mode to pass firewall
            ftpClient.enterLocalPassiveMode();
 
            System.out.println("Connected RCabinet FTP");
 
            String remoteDirPath = "/cabinet/images";
 
            uploadDirectory(ftpClient, remoteDirPath, localDirPath, "");
 
            // log out and disconnect from the server
            ftpClient.logout();
            ftpClient.disconnect();
            
            System.out.println("Disconnected RCabinet FTP");
            
            //Delete file
            File index = new File(localDirPath);
            String[] entries = index.list();
            for(String s: entries){
                File currentFile = new File(index.getPath(),s);
                currentFile.delete();
            }
            index.delete();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}

	public static void uploadDirectory(FTPClient ftpClient,
	        String remoteDirPath, String localParentDir, String remoteParentDir)
	        throws IOException {
	 
	    System.out.println("LISTING directory: " + localParentDir);
	 
	    File localDir = new File(localParentDir);
	    File[] subFiles = localDir.listFiles();
	    if (subFiles != null && subFiles.length > 0) {
	        for (File item : subFiles) {
	        	try {
		            String remoteFilePath = remoteDirPath + "/" + remoteParentDir
		                    + "/" + item.getName();
		            if (remoteParentDir.equals("")) {
		                remoteFilePath = remoteDirPath + "/" + item.getName();
		            }
		 
		            if (item.isFile()) {
		                // upload the file
		                String localFilePath = item.getAbsolutePath();
		                //System.out.println("About to upload the file: " + localFilePath);
		                boolean uploaded = uploadSingleFile(ftpClient,
		                        localFilePath, remoteFilePath);
		                if (uploaded) {
		                    System.out.println("UPLOADED a file to: "
		                            + remoteFilePath);
		                } else {
		                    System.out.println("COULD NOT upload the file: "
		                            + localFilePath);
		                }
		            }
	        	} catch (Exception ex) {
	        		System.out.println("FTP upload exception. " + ex.getMessage());
	        		continue;
	        	}
	        }
	    }
	}
	
	public static boolean uploadSingleFile(FTPClient ftpClient,
	        String localFilePath, String remoteFilePath) throws IOException {
	    File localFile = new File(localFilePath);
	 
	    InputStream inputStream = new FileInputStream(localFile);
	    try {
	        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	        return ftpClient.storeFile(remoteFilePath, inputStream);
	    } finally {
	        inputStream.close();
	    }
	}
}