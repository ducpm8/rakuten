package com.web.entity;

public class AmazonOrderReport {
	
	private String OrderId = "";
	private String OrderItemId = "";
	private String ProductName = "";
	private String TotalPruchasePrice = "";
	private String TotalCustomerSpendonOrder = "";
	private String MarginDollar = "";
	private String MarginPercent = "";
	private String VendorOrdernumber = "";
	private String TrackingNumber = "";
	private String PaymentsDate = "";
	private String PurchaseDate = "";
	private String ReportingDate = "";
	private String PromiseDate = "";
	private String DaysPastPromise = "";
	private String BuyerEmail = "";
	private String BuyerName = "";
	private String BuyerPhoneNumber = "";
	private String Sku = "";
	private String ProductName2 = "";
	private String QuantityPurchased = "";
	private String QuantityShipped = "";
	private String QuantityToShip = "";
	private String ShipServiceLevel = "";
	private String RecipientName = "";
	private String ShipAddress1 = "";
	private String ShipAddress2 = "";
	private String ShipAddress3 = "";
	private String ShipCity = "";
	private String ShipState = "";
	private String ShipStateFull = "";
	private String ShipPostalCode = "";
	private String ShipCountry = "";
	private String Warehouse = "";
	
	//ShipmentStatus
	private String ShipmentStatus = "";
	private String ShipmentStatusDate = "";
	private String EmailContent = "";
	
	//Error SKU
	private String reason = "";
	private String line = "";
	private String orderNumber = "";
	private String URL = "";
	
	
	/**
	 * @return the shipStateFull
	 */
	public String getShipStateFull() {
		return ShipStateFull;
	}
	/**
	 * @param shipStateFull the shipStateFull to set
	 */
	public void setShipStateFull(String shipStateFull) {
		ShipStateFull = shipStateFull;
	}
	/**
	 * @return the warehouse
	 */
	public String getWarehouse() {
		return Warehouse;
	}
	/**
	 * @param warehouse the warehouse to set
	 */
	public void setWarehouse(String warehouse) {
		Warehouse = warehouse;
	}
	/**
	 * @return the shipmentStatus
	 */
	public String getShipmentStatus() {
		return ShipmentStatus;
	}
	/**
	 * @param shipmentStatus the shipmentStatus to set
	 */
	public void setShipmentStatus(String shipmentStatus) {
		ShipmentStatus = shipmentStatus;
	}
	/**
	 * @return the emailContent
	 */
	public String getEmailContent() {
		return EmailContent;
	}
	/**
	 * @param emailContent the emailContent to set
	 */
	public void setEmailContent(String emailContent) {
		EmailContent = emailContent;
	}
	/**
	 * @return the shipmentStatusDate
	 */
	public String getShipmentStatusDate() {
		return ShipmentStatusDate;
	}
	/**
	 * @param shipmentStatusDate the shipmentStatusDate to set
	 */
	public void setShipmentStatusDate(String shipmentStatusDate) {
		ShipmentStatusDate = shipmentStatusDate;
	}
	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}
	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**
	 * @return the purchaseDate
	 */
	public String getPurchaseDate() {
		return PurchaseDate;
	}
	/**
	 * @param purchaseDate the purchaseDate to set
	 */
	public void setPurchaseDate(String purchaseDate) {
		PurchaseDate = purchaseDate;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the line
	 */
	public String getLine() {
		return line;
	}
	/**
	 * @param line the line to set
	 */
	public void setLine(String line) {
		this.line = line;
	}
	/**
	 * @return the uRL
	 */
	public String getURL() {
		return URL;
	}
	/**
	 * @param uRL the uRL to set
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return OrderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
	/**
	 * @return the orderItemId
	 */
	public String getOrderItemId() {
		return OrderItemId;
	}
	/**
	 * @param orderItemId the orderItemId to set
	 */
	public void setOrderItemId(String orderItemId) {
		OrderItemId = orderItemId;
	}
	/**
	 * @return the productName1
	 */
	public String getProductName() {
		return ProductName;
	}
	/**
	 * @param productName1 the productName1 to set
	 */
	public void setProductName(String productName1) {
		ProductName = productName1;
	}
	/**
	 * @return the totalPruchasePrice
	 */
	public String getTotalPruchasePrice() {
		return TotalPruchasePrice;
	}
	/**
	 * @param totalPruchasePrice the totalPruchasePrice to set
	 */
	public void setTotalPruchasePrice(String totalPruchasePrice) {
		TotalPruchasePrice = totalPruchasePrice;
	}
	/**
	 * @return the totalCustomerSpendonOrder
	 */
	public String getTotalCustomerSpendonOrder() {
		return TotalCustomerSpendonOrder;
	}
	/**
	 * @param totalCustomerSpendonOrder the totalCustomerSpendonOrder to set
	 */
	public void setTotalCustomerSpendonOrder(String totalCustomerSpendonOrder) {
		TotalCustomerSpendonOrder = totalCustomerSpendonOrder;
	}
	/**
	 * @return the marginDollar
	 */
	public String getMarginDollar() {
		return MarginDollar;
	}
	/**
	 * @param marginDollar the marginDollar to set
	 */
	public void setMarginDollar(String marginDollar) {
		MarginDollar = marginDollar;
	}
	/**
	 * @return the marginPercent
	 */
	public String getMarginPercent() {
		return MarginPercent;
	}
	/**
	 * @param marginPercent the marginPercent to set
	 */
	public void setMarginPercent(String marginPercent) {
		MarginPercent = marginPercent;
	}
	/**
	 * @return the vendorOrdernumber
	 */
	public String getVendorOrdernumber() {
		return VendorOrdernumber;
	}
	/**
	 * @param vendorOrdernumber the vendorOrdernumber to set
	 */
	public void setVendorOrdernumber(String vendorOrdernumber) {
		VendorOrdernumber = vendorOrdernumber;
	}
	/**
	 * @return the trackingNumber
	 */
	public String getTrackingNumber() {
		return TrackingNumber;
	}
	/**
	 * @param trackingNumber the trackingNumber to set
	 */
	public void setTrackingNumber(String trackingNumber) {
		TrackingNumber = trackingNumber;
	}
	/**
	 * @return the paymentsDate
	 */
	public String getPaymentsDate() {
		return PaymentsDate;
	}
	/**
	 * @param paymentsDate the paymentsDate to set
	 */
	public void setPaymentsDate(String paymentsDate) {
		PaymentsDate = paymentsDate;
	}
	/**
	 * @return the reportingDate
	 */
	public String getReportingDate() {
		return ReportingDate;
	}
	/**
	 * @param reportingDate the reportingDate to set
	 */
	public void setReportingDate(String reportingDate) {
		ReportingDate = reportingDate;
	}
	/**
	 * @return the promiseDate
	 */
	public String getPromiseDate() {
		return PromiseDate;
	}
	/**
	 * @param promiseDate the promiseDate to set
	 */
	public void setPromiseDate(String promiseDate) {
		PromiseDate = promiseDate;
	}
	/**
	 * @return the daysPastPromise
	 */
	public String getDaysPastPromise() {
		return DaysPastPromise;
	}
	/**
	 * @param daysPastPromise the daysPastPromise to set
	 */
	public void setDaysPastPromise(String daysPastPromise) {
		DaysPastPromise = daysPastPromise;
	}
	/**
	 * @return the buyerEmail
	 */
	public String getBuyerEmail() {
		return BuyerEmail;
	}
	/**
	 * @param buyerEmail the buyerEmail to set
	 */
	public void setBuyerEmail(String buyerEmail) {
		BuyerEmail = buyerEmail;
	}
	/**
	 * @return the buyerName
	 */
	public String getBuyerName() {
		return BuyerName;
	}
	/**
	 * @param buyerName the buyerName to set
	 */
	public void setBuyerName(String buyerName) {
		BuyerName = buyerName;
	}
	/**
	 * @return the buyerPhoneNumber
	 */
	public String getBuyerPhoneNumber() {
		return BuyerPhoneNumber;
	}
	/**
	 * @param buyerPhoneNumber the buyerPhoneNumber to set
	 */
	public void setBuyerPhoneNumber(String buyerPhoneNumber) {
		BuyerPhoneNumber = buyerPhoneNumber;
	}
	/**
	 * @return the sku
	 */
	public String getSku() {
		return Sku;
	}
	/**
	 * @param sku the sku to set
	 */
	public void setSku(String sku) {
		Sku = sku;
	}
	/**
	 * @return the productName2
	 */
	public String getProductName2() {
		return ProductName2;
	}
	/**
	 * @param productName2 the productName2 to set
	 */
	public void setProductName2(String productName2) {
		ProductName2 = productName2;
	}
	/**
	 * @return the quantityPurchased
	 */
	public String getQuantityPurchased() {
		return QuantityPurchased;
	}
	/**
	 * @param quantityPurchased the quantityPurchased to set
	 */
	public void setQuantityPurchased(String quantityPurchased) {
		QuantityPurchased = quantityPurchased;
	}
	/**
	 * @return the quantityShipped
	 */
	public String getQuantityShipped() {
		return QuantityShipped;
	}
	/**
	 * @param quantityShipped the quantityShipped to set
	 */
	public void setQuantityShipped(String quantityShipped) {
		QuantityShipped = quantityShipped;
	}
	/**
	 * @return the quantityToShip
	 */
	public String getQuantityToShip() {
		return QuantityToShip;
	}
	/**
	 * @param quantityToShip the quantityToShip to set
	 */
	public void setQuantityToShip(String quantityToShip) {
		QuantityToShip = quantityToShip;
	}
	/**
	 * @return the shipServiceLevel
	 */
	public String getShipServiceLevel() {
		return ShipServiceLevel;
	}
	/**
	 * @param shipServiceLevel the shipServiceLevel to set
	 */
	public void setShipServiceLevel(String shipServiceLevel) {
		ShipServiceLevel = shipServiceLevel;
	}
	/**
	 * @return the recipientName
	 */
	public String getRecipientName() {
		return RecipientName;
	}
	/**
	 * @param recipientName the recipientName to set
	 */
	public void setRecipientName(String recipientName) {
		RecipientName = recipientName;
	}
	/**
	 * @return the shipAddress1
	 */
	public String getShipAddress1() {
		return ShipAddress1;
	}
	/**
	 * @param shipAddress1 the shipAddress1 to set
	 */
	public void setShipAddress1(String shipAddress1) {
		ShipAddress1 = shipAddress1;
	}
	/**
	 * @return the shipAddress2
	 */
	public String getShipAddress2() {
		return ShipAddress2;
	}
	/**
	 * @param shipAddress2 the shipAddress2 to set
	 */
	public void setShipAddress2(String shipAddress2) {
		ShipAddress2 = shipAddress2;
	}
	/**
	 * @return the shipAddress3
	 */
	public String getShipAddress3() {
		return ShipAddress3;
	}
	/**
	 * @param shipAddress3 the shipAddress3 to set
	 */
	public void setShipAddress3(String shipAddress3) {
		ShipAddress3 = shipAddress3;
	}
	/**
	 * @return the shipCity
	 */
	public String getShipCity() {
		return ShipCity;
	}
	/**
	 * @param shipCity the shipCity to set
	 */
	public void setShipCity(String shipCity) {
		ShipCity = shipCity;
	}
	/**
	 * @return the shipState
	 */
	public String getShipState() {
		return ShipState;
	}
	/**
	 * @param shipState the shipState to set
	 */
	public void setShipState(String shipState) {
		ShipState = shipState;
	}
	/**
	 * @return the shipPostalCode
	 */
	public String getShipPostalCode() {
		return ShipPostalCode;
	}
	/**
	 * @param shipPostalCode the shipPostalCode to set
	 */
	public void setShipPostalCode(String shipPostalCode) {
		ShipPostalCode = shipPostalCode;
	}
	/**
	 * @return the shipCountry
	 */
	public String getShipCountry() {
		return ShipCountry;
	}
	/**
	 * @param shipCountry the shipCountry to set
	 */
	public void setShipCountry(String shipCountry) {
		ShipCountry = shipCountry;
	}

	
}
