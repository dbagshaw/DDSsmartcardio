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
	private String clinicalAccess;
	private Date creationDate;
	private Date modificationDate;

	// constructors
	public CardDTO() {
	}

	public CardDTO(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public String getClinicalAccess() {
		return clinicalAccess;
	}

	public void setClinicalAccess(String clinicalAccess) {
		this.clinicalAccess = clinicalAccess;
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

}
