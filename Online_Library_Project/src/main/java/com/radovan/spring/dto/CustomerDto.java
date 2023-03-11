package com.radovan.spring.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class CustomerDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer customerId;

	private Date dateOfBirth;

	private String dateOfBirthStr;

	private Timestamp registrationDate;
	
	private String registrationDateStr;

	private Integer userId;

	private Integer cartId;

	private Integer loyaltyCardId;

	private Integer deliveryAddressId;

	private List<Integer> reviewsIds;

	private Integer wishListId;

	private List<Integer> persistenceLoginsIds;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDateOfBirthStr() {
		return dateOfBirthStr;
	}

	public void setDateOfBirthStr(String dateOfBirthStr) {
		this.dateOfBirthStr = dateOfBirthStr;
	}

	public Timestamp getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Timestamp registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getRegistrationDateStr() {
		return registrationDateStr;
	}

	public void setRegistrationDateStr(String registrationDateStr) {
		this.registrationDateStr = registrationDateStr;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Integer getLoyaltyCardId() {
		return loyaltyCardId;
	}

	public void setLoyaltyCardId(Integer loyaltyCardId) {
		this.loyaltyCardId = loyaltyCardId;
	}

	public Integer getDeliveryAddressId() {
		return deliveryAddressId;
	}

	public void setDeliveryAddressId(Integer deliveryAddressId) {
		this.deliveryAddressId = deliveryAddressId;
	}

	public List<Integer> getReviewsIds() {
		return reviewsIds;
	}

	public void setReviewsIds(List<Integer> reviewsIds) {
		this.reviewsIds = reviewsIds;
	}

	public Integer getWishListId() {
		return wishListId;
	}

	public void setWishListId(Integer wishListId) {
		this.wishListId = wishListId;
	}

	public List<Integer> getPersistenceLoginsIds() {
		return persistenceLoginsIds;
	}

	public void setPersistenceLoginsIds(List<Integer> persistenceLoginsIds) {
		this.persistenceLoginsIds = persistenceLoginsIds;
	}

	
}
