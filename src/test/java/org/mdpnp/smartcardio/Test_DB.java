
package org.mdpnp.smartcardio;

import java.util.Date;

import javax.smartcardio.CardTerminal;

import org.mdpnp.smartcardio.activity.ActivityLogger;
import org.mdpnp.smartcardio.db.ActivityManager;
import org.mdpnp.smartcardio.rfid.ReadCard;

public class Test_DB {

	static ReadCard Read = new ReadCard();
	static CardTerminal terminal = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*EmployeeManager manager = new EmployeeManager();

		CardDTO cardDto = new CardDTO();
		 cardDto.setUserName("testuser");
		 cardDto.setCardNumber("000001");

		manager.delete(cardDto);*/
		
		ActivityManager am = new ActivityManager();
		ActivityLogger logger = new ActivityLogger();
		
		logger.setUserName("diego");
		logger.setDate(new Date());
		logger.setEmergencyButton(false);
		logger.setAccess("Granted");
		
		am.create(logger);
		
		
		
	}

}
