package com.allinone.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="tbl_multi_size_product_price")
public class MultiSizeProductPrice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="msppid")
	private Long msppId;
	
	@Column(name="quantity")
	private Long quantity;
	
	@OneToOne
	@JoinColumn(name="productsizetypeid")
	private ProductSizeType ProductSizeType;

	public Long getMsppId() {
		return msppId;
	}

	public void setMsppId(Long msppId) {
		this.msppId = msppId;
	}
	

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public ProductSizeType getProductSizeType() {
		return ProductSizeType;
	}

	public void setProductSizeType(ProductSizeType productSizeType) {
		ProductSizeType = productSizeType;
	}

	

}
