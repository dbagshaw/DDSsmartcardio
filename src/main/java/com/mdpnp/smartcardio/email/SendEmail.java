package com.mdpnp.smartcardio.email;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.mdpnp.smartcardio.db.Manager;
import org.mdpnp.smartcardio.db.NotificationPopUp;

public class SendEmail {

    private static String USER_NAME = "dmbaggy18@yahoo.com";
    private static String PASSWORD = "Adidas 18";
    private static String RECIPIENT = "bagshawd@wit.edu";

    
    //fix the null cardnumber
    public static void Email(String cardnumber, String username) {
    	Manager manager = new Manager();
    	manager.findByUID(cardnumber);
    	manager.findByName(username);
        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = { RECIPIENT }; // list of recipient email addresses
        String subject = "Emergency Override";
        String body = username + " (" + cardnumber + ") " + "has used the Emergency button";

        sendFromYahoo(from, pass, to, subject, body);
    }

    private static void sendFromYahoo(String from, String pass, String[] to, String subject, String body) {
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
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            String notice = "Notification Sent to Supervisor";
//            System.out.println(notice);
            NotificationPopUp.NotificationSent(notice);
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}