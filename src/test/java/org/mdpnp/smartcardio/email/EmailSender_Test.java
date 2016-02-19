package org.mdpnp.smartcardio.email;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.mdpnp.smartcardio.email.EmailSender;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the Email Sender class
 * @author diego
 * 
 * @V1.0 there is not much to test. Currently we are only making sure that the parameters 
 * needed to specify the smtp server are there. 
 * 
 * With Javax.mail there is no way of telling if the email really went through but better test cases might be written 
 * for this class
 *
 */
public class EmailSender_Test{
	
	String config_file_name = "email_config.properties";
	
	
	@Test
	public void testReadConfigFile() throws IOException{
		EmailSender email_sender = new EmailSender(config_file_name);
		String host_name = email_sender.getUser_name();
		
		//get host name from file
		Properties prop = new Properties();
		InputStream input = getClass().getClassLoader().getResourceAsStream(config_file_name);
		  
		  if (null != input ){
			  prop = new Properties();
			  // load a properties file
			  prop.load(input);
		  }else{
			  throw new FileNotFoundException("Property file "+ config_file_name+ " not found in classpath");
		  }
		
		  assertEquals(host_name, prop.getProperty("USER_NAME"));
	
			
	}
	


}


