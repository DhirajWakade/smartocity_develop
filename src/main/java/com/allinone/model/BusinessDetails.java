package com.allinone.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.json.JSONPropertyIgnore;

import com.allinone.model.business.BusinessTypes;
import com.allinone.model.product.ProductDetails;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="tbl_business_details")
//@NamedQueries({  
//@NamedQuery(name = "findBusinessByBusinessManId",query = "SELECT b FROM BusinessDetails b where b.businessmanUser.bmId = :businessManId")  
//})  

public class BusinessDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="business_id")
	private Long businessId;
	
	@Column(name="business_name")
	private String businessName;

	@Column(name="pancard_no")
	private String panCardNo;
	
	@Column(name="pancard_name")
	private String businessNameAaPerPancard;
	
	@OneToOne(cascade=CascadeType.ALL)	
	private BusinessmanUser businessmanUser;  
	
		
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name ="businessTypeId")
	private BusinessTypes businessType;
	
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name ="addressId")
	private AddressDetails addressDetails;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name ="bankdetails_id")
	private BankDetails bankDetails;
	
	@Column(name="gst_number")
	private String gstNumber;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)	
	@JoinTable(name = "tbl_business_products",
		    joinColumns = @JoinColumn(name = "business_id"),
		    inverseJoinColumns = @JoinColumn(name = "prod_details_id"))
	private Set<ProductDetails>productDetails=new HashSet<>();
	
	@Column(name="business_email_id")
	private String businessEmailId;
	
	@Column(name="shop_img_url")
	private String shopImgUrl;
	
	@Column(name="isTACAgreed")
	private Boolean isTermAndConditionAgreed;
	
	@Column(name="min_amt_order")
	private Double minAmtOrderExpected;
	
	@Column(name="cod")
	private String provideCashOnDelivery;
	
	
	
	public Double getMinAmtOrderExpected() {
		return minAmtOrderExpected;
	}

	public void setMinAmtOrderExpected(Double minAmtOrderExpected) {
		this.minAmtOrderExpected = minAmtOrderExpected;
	}

	public String getProvideCashOnDelivery() {
		return provideCashOnDelivery;
	}

	public void setProvideCashOnDelivery(String provideCashOnDelivery) {
		this.provideCashOnDelivery = provideCashOnDelivery;
	}

	public Boolean getIsTermAndConditionAgreed() {
		return isTermAndConditionAgreed;
	}

	public void setIsTermAndConditionAgreed(Boolean isTermAndConditionAgreed) {
		this.isTermAndConditionAgreed = isTermAndConditionAgreed;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getBusinessEmailId() {
		return businessEmailId;
	}

	public void setBusinessEmailId(String businessEmailId) {
		this.businessEmailId = businessEmailId;
	}

	public String getShopImgUrl() {
		return shopImgUrl;
	}

	public void setShopImgUrl(String shopImgUrl) {
		this.shopImgUrl = shopImgUrl;
	}

	public BusinessTypes getBusinessType() {
		return businessType;
	}

	public void setBusinessType(BusinessTypes businessType) {
		this.businessType = businessType;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getPanCardNo() {
		return panCardNo;
	}

	public void setPanCardNo(String panCardNo) {
		this.panCardNo = panCardNo;
	}

	public String getBusinessNameAaPerPancard() {
		return businessNameAaPerPancard;
	}

	public void setBusinessNameAaPerPancard(String businessNameAaPerPancard) {
		this.businessNameAaPerPancard = businessNameAaPerPancard;
	}

	

	public AddressDetails getAddressDetails() {
		return addressDetails;
	}

	public void setAddressDetails(AddressDetails addressDetails) {
		this.addressDetails = addressDetails;
	}

	public BankDetails getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(BankDetails bankDetails) {
		this.bankDetails = bankDetails;
	}

	public Set<ProductDetails> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(Set<ProductDetails> productDetails) {
		this.productDetails = productDetails;
	}

	public BusinessmanUser getBusinessmanUser() {
		return businessmanUser;
	}

	public void setBusinessmanUser(BusinessmanUser businessmanUser) {
		this.businessmanUser = businessmanUser;
	}
	
	public void addProductDetail(ProductDetails pd)
	{
		this.productDetails.add(pd);
	}
	
}
