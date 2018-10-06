package com.web.data;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class P2SubmitData {
	private String cardNo;
	private String expMonth;
	private String expYear;
	private String cvs;
	private String cardHolderName1;
	private String cardHolderName2;
	private String country;
	private String add1;
	private String add2;
	private String city;
	private String state;
	private String postcode;
	private String projectId;
	private String sheetName;
	private String account;
	private String password;
	private String store;
	private String vendor;
	
	private String accountPP;
	private String passwordPP;
	
	private String fourLastDigit;
	
	private MultipartFile file;
	
	private List<MultipartFile> filePricing;
	
	/**
	 * @return the fourLastDigit
	 */
	public String getFourLastDigit() {
		return fourLastDigit;
	}
	/**
	 * @param fourLastDigit the fourLastDigit to set
	 */
	public void setFourLastDigit(String fourLastDigit) {
		this.fourLastDigit = fourLastDigit;
	}
	/**
	 * @return the accountPP
	 */
	public String getAccountPP() {
		return accountPP;
	}
	/**
	 * @param accountPP the accountPP to set
	 */
	public void setAccountPP(String accountPP) {
		this.accountPP = accountPP;
	}
	/**
	 * @return the passwordPP
	 */
	public String getPasswordPP() {
		return passwordPP;
	}
	/**
	 * @param passwordPP the passwordPP to set
	 */
	public void setPasswordPP(String passwordPP) {
		this.passwordPP = passwordPP;
	}
	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}
	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	
	
	/**
	 * @return the file
	 */
	public MultipartFile getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	/**
	 * @return the filePricing
	 */
	public List<MultipartFile> getFilePricing() {
		return filePricing;
	}
	/**
	 * @param filePricing the filePricing to set
	 */
	public void setFilePricing(List<MultipartFile> filePricing) {
		this.filePricing = filePricing;
	}
	/**
	 * @return the store
	 */
	public String getStore() {
		return store;
	}
	/**
	 * @param store the store to set
	 */
	public void setStore(String store) {
		this.store = store;
	}
	/**
	 * @return the cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}
	/**
	 * @param cardNo the cardNo to set
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	/**
	 * @return the expMonth
	 */
	public String getExpMonth() {
		return expMonth;
	}
	/**
	 * @param expMonth the expMonth to set
	 */
	public void setExpMonth(String expDate) {
		this.expMonth = expDate;
	}
	/**
	 * @return the expYear
	 */
	public String getExpYear() {
		return expYear;
	}
	/**
	 * @param expYear the expYear to set
	 */
	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}
	/**
	 * @return the cvs
	 */
	public String getCvs() {
		return cvs;
	}
	/**
	 * @param cvs the cvs to set
	 */
	public void setCvs(String cvs) {
		this.cvs = cvs;
	}
	/**
	 * @return the cardHolderName1
	 */
	public String getCardHolderName1() {
		return cardHolderName1;
	}
	/**
	 * @param cardHolderName1 the cardHolderName1 to set
	 */
	public void setCardHolderName1(String cardHolderName1) {
		this.cardHolderName1 = cardHolderName1;
	}
	/**
	 * @return the cardHolderName2
	 */
	public String getCardHolderName2() {
		return cardHolderName2;
	}
	/**
	 * @param cardHolderName2 the cardHolderName2 to set
	 */
	public void setCardHolderName2(String cardHolderName2) {
		this.cardHolderName2 = cardHolderName2;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the add1
	 */
	public String getAdd1() {
		return add1;
	}
	/**
	 * @param add1 the add1 to set
	 */
	public void setAdd1(String add1) {
		this.add1 = add1;
	}
	/**
	 * @return the add2
	 */
	public String getAdd2() {
		return add2;
	}
	/**
	 * @param add2 the add2 to set
	 */
	public void setAdd2(String add2) {
		this.add2 = add2;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		return postcode;
	}
	/**
	 * @param postcode the postcode to set
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	/**
	 * @return the projectId
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the sheetName
	 */
	public String getSheetName() {
		return sheetName;
	}
	/**
	 * @param sheetName the sheetName to set
	 */
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
