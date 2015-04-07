
package org.mdpnp.smartcardio;

import java.util.Date;

import javax.smartcardio.CardTerminal;

import org.mdpnp.smartcardio.activity.ActivityLogger;
import org.mdpnp.smartcardio.db.ActivityManager;
import org.mdpnp.smartcardio.dto.CardDTO;
import org.mdpnp.smartcardio.email.SendEmail;
import org.mdpnp.smartcardio.lock.LockScreen;
import org.mdpnp.smartcardio.rfid.ReadCard;

public class Test_DB {

	static ReadCard Read = new ReadCard();
	static CardTerminal terminal = null;

	public static void main(String[] args) {

		/*EmployeeManager manager = new EmployeeManager();*/

		CardDTO cardDto = new CardDTO();
		/* cardDto.setUserName("testuser");
		 cardDto.setCardNumber("000001");

		manager.delete(cardDto);*/
		SendEmail.Email("0000001", "testuser");
//		LockScreen.BreakGlass("000001", "testuser");
		
//		ActivityManager am = new ActivityManager();
//		ActivityLogger logger = new ActivityLogger();
//		
//		logger.setUserName("diego");
//		logger.setDate(new Date());
//		logger.setEmergencyButton(false);
//		logger.setAccess("Granted");
//		
//		am.create(logger);
		
		
		
	}

}
