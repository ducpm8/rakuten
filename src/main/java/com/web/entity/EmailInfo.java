package com.web.entity;

import java.util.ArrayList;
import java.util.List;

public class EmailInfo {
	private List<String> recipient = new ArrayList<String>();
	private String title;
	private String content;
	private List<String> filePath = new ArrayList<String>();
	/**
	 * @return the recipient
	 */
	public List<String> getRecipient() {
		return recipient;
	}
	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(List<String> recipient) {
		this.recipient = recipient;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the filePath
	 */
	public List<String> getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(List<String> filePath) {
		this.filePath = filePath;
	}
	
}
