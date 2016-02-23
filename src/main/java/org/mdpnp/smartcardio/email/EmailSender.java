package org.mdpnp.smartcardio.email;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 
 * @author diego
 * 
 * This class sends emails using a SMTP server and the Javax.mail API.
 * 
 * Port for TLS/STARTTLS: 587
 * Port for SSL: 465 
 * 
 * v1.0 Using gmail as SMTP server. TODO test with other providers
 *
 */
public class EmailSender {
	
	  private  String user_name;  // if GMail user name (just the part before "@gmail.com")
	  private  String password; // GMail password
	  
	  private String configurationFileName;
	  
	  private Properties server_props; //configuration of SMTP server properties
	  
	  
	  private final String host = "smtp.gmail.com";
	  //XXX is final at this v1.0. May change to expand this class' functionality to other SMTP servers
	  
	  public EmailSender(String configurationFileName){
		  
		  this.configurationFileName = configurationFileName;
		  
		  try{
			  Properties prop = loadPropertiesFile(configurationFileName);
			  
			  //configure email server properties 
			  if(null != prop){
				  user_name = prop.getProperty("USER_NAME");
				  password = prop.getProperty("PASSWORD");
			  }
			 
		      server_props = System.getProperties();		      
		      server_props.put("mail.smtp.starttls.enable", "true");
		      server_props.put("mail.smtp.host", host);
		      server_props.put("mail.smtp.user", user_name);
		      server_props.put("mail.smtp.password", password);
		      server_props.put("mail.smtp.port", "587");
		      server_props.put("mail.smtp.auth", "true");

		  }catch(IOException e){
			  e.printStackTrace();
		  }
	  }
	
	  
	  //setters
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public void setPassword(String password) {
			this.password = password;
		}



	public String getUser_name() {
			return user_name;
		}


	/**
	   * Sends an email using the server configuration of this class
	   * @param to
	   * @param subject
	   * @param body
	   */
	  public void sendMail(String[] to, String subject, String body) {
		  if(null == user_name || null == password || user_name.trim().equals("") || password.trim().equals(""))
			  return;

	        Session session = Session.getDefaultInstance(server_props);
	        MimeMessage message = new MimeMessage(session);

	        try {
	            message.setFrom(new InternetAddress(user_name));
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
	            transport.connect(host, user_name, password);
	            transport.sendMessage(message, message.getAllRecipients());
	            transport.close();
	        }
	        catch (AddressException ae) {
	            ae.printStackTrace();
	        }
	        catch (MessagingException me) {
	            me.printStackTrace();
	        }
	    }
	  
	  
	  //here just for quick test purposes
//	  public static void main (String[] args){
//		  EmailSender es = new EmailSender("email_config.properties");
//		  System.out.println("done!");
//	  }
//	  
	  
	  //TODO consider this functionality to be part of an UTILS class
	  /**
	   * Loads a properties file with the server config. parameters (username, password)
	   * @param configurationFileName
	   * @return a Properties file 
	   * @throws IOException
	   */
	  private Properties loadPropertiesFile(String configurationFileName) throws IOException{
		  Properties prop;
		  InputStream input = null;
		  
		  input = getClass().getClassLoader().getResourceAsStream(configurationFileName);
		  
		  if (null != input ){
			  prop = new Properties();
			  // load a properties file
			  prop.load(input);
		  }else{
			  throw new FileNotFoundException("Property file "+ configurationFileName+ " not found in classpath");
		  }
		  
		  return prop;
	  }

}
