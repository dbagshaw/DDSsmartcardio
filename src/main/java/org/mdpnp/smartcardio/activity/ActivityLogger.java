package org.mdpnp.smartcardio.activity;

import java.util.Date;

public class ActivityLogger {

	private Long id;
	private String userName;
	private String access;
	private boolean emergencyButton;
	private Date date;

	public ActivityLogger() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public boolean isEmergencyButton() {
		return emergencyButton;
	}

	public void setEmergencyButton(boolean emergencyButton) {
		this.emergencyButton = emergencyButton;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
