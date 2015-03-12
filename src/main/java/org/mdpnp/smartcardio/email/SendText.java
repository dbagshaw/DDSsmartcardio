package org.mdpnp.smartcardio.email;

//Package Imports
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mdpnp.smartcardio.db.EmployeeManager;
import org.mdpnp.smartcardio.dto.CardDTO;

//Public Class
@SuppressWarnings("unused")
public class SendText {

	// Global Variables
	String TO;
	String FROM;
	String SUBJECT;
	String TEXT;
	String MAILHOST;
	String LASTERROR;

	// Main function executed
	public static void Sender(String UID) {
		SendText SMS = new SendText();
		EmployeeManager eManager = new EmployeeManager();
		CardDTO cardDto = eManager.findByUID(UID);
		String username = cardDto.getCardNumber();

	}
}