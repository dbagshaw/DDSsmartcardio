package org.mdpnp.smartcardio.dto;

import java.util.Date;

/**
 * This class is a POJO representing a card in the system for the Hibernate
 * framework to persist the entities.
 * <p>
 * 
 * @author diego@mdpnp.org
 * 
 */
public class CardDTO {

	private String cardNumber; // uniqueID
	private String userName;
	private String permissions;
	private String supervisor;
	private String phoneNumber;
	private String carrier;
	private Date creationDate;
	private Date modificationDate;

	// constructors
	public CardDTO() {
	}

	public CardDTO(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

}
