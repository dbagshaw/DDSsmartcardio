package org.mdpnp.smartcardio.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mdpnp.smartcardio.db.EmployeeManager;
import org.mdpnp.smartcardio.db.NotificationPopUp;
import org.mdpnp.smartcardio.dto.CardDTO;

public class SendEmail {

	private static String USER_NAME = "enter your email here";
	private static String PASSWORD = "enter your password here";
	private static String textSprint = "messaging.sprintpcs.com";
	private static String textTMobile = "@tmomail.net";
	private static String textVerizon = "@vtext.com";
	private static String textAtt = "@txt.att.net";

	public static void Email(String cardnumber, String username) {
		EmployeeManager eManager = new EmployeeManager();
		List<CardDTO> supervisorList = new ArrayList<CardDTO>();
		// List<String> supervisorsContact = new ArrayList<String>();
		eManager.findByUID(cardnumber);
		eManager.findByName(username);
		supervisorList = eManager.findSupervisor();
		String[] to = new String[supervisorList.size()];

		// to[0] = RECIPIENT;
		if (null != supervisorList) {
			// for(CardDTO supervisor : supervisorsList){
			String phone = null;
			for (int i = 0; i < supervisorList.size(); i++) {
				CardDTO cardDto = supervisorList.get(i);

				if (cardDto.getCarrier().equals("verizon"))
					phone = cardDto.getPhoneNumber() + textVerizon;
				else if (cardDto.getCarrier().equals("att"))
					phone = cardDto.getPhoneNumber() + textAtt;
				else if (cardDto.getCarrier().equals("sprint"))
					phone = cardDto.getPhoneNumber() + textSprint;
				else if (cardDto.getCarrier().equals("tmobile"))
					phone = cardDto.getPhoneNumber() + textTMobile;
				// supervisorsContact.add(s);
				to[i] = phone;
			}
		}

		// supervisorsContact.isEmpty();

		String from = USER_NAME;
		String pass = PASSWORD;
		// String[] to = { RECIPIENT, supervisorsContact }; // list of recipient
		// email addresses
		String subject = "Emergency Override";
		String body = username + " used the emergency button - " + new Date();

		sendFromYahoo(from, pass, to, subject, body);
	}

	private static void sendFromYahoo(String from, String pass, String[] to,
			String subject, String body) {
		Properties props = System.getProperties();
		String host = "smtp.mail.yahoo.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.length];

			// To get the array of addresses
			for (int i = 0; i < to.length; i++) {
				toAddress[i] = new InternetAddress(to[i]);
			}

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}

			message.setSubject(subject);
			message.setText(body);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			String notice = "Notification Sent to Supervisor";
			// System.out.println(notice);
			NotificationPopUp.getInstance().notificationSent(notice);
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
}