package com.radovan.spring.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "customers")
public class CustomerEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "customer_id")
	private Integer customerId;
	
	
	@Column(name = "date_of_birth")
	private Timestamp dateOfBirth;
	
	@Column(name = "registration_date")
	private Timestamp registrationDate;
	
	@OneToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "cart_id")
	private CartEntity cart;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "wishlist_id")
	private WishListEntity wishList;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "loyalty_card_id")
	private LoyaltyCardEntity loyaltyCard;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "delivery_address_id")
	private DeliveryAddressEntity deliveryAddress;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "author",orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	private List<ReviewEntity> reviews;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "customer")
	@Fetch(FetchMode.SUBSELECT)
	private List<PersistenceLoginEntity> persistenceLogins;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Timestamp getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Timestamp dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Timestamp getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Timestamp registrationDate) {
		this.registrationDate = registrationDate;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public CartEntity getCart() {
		return cart;
	}

	public void setCart(CartEntity cart) {
		this.cart = cart;
	}

	public WishListEntity getWishList() {
		return wishList;
	}

	public void setWishList(WishListEntity wishList) {
		this.wishList = wishList;
	}

	public LoyaltyCardEntity getLoyaltyCard() {
		return loyaltyCard;
	}

	public void setLoyaltyCard(LoyaltyCardEntity loyaltyCard) {
		this.loyaltyCard = loyaltyCard;
	}

	public DeliveryAddressEntity getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddressEntity deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public List<ReviewEntity> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewEntity> reviews) {
		this.reviews = reviews;
	}

	public List<PersistenceLoginEntity> getPersistenceLogins() {
		return persistenceLogins;
	}

	public void setPersistenceLogins(List<PersistenceLoginEntity> persistenceLogins) {
		this.persistenceLogins = persistenceLogins;
	}

	
	
	
	
	
	

}
