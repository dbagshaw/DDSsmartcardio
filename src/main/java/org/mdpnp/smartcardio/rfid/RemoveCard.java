package org.mdpnp.smartcardio.rfid;

/**
 * need to update this class so it removes UIDs from mySQL
 */

import org.mdpnp.smartcardio.db.ActivityLog;
import org.mdpnp.smartcardio.db.Manager;
import org.mdpnp.smartcardio.dto.CardDTO;

public class RemoveCard {

	Manager myManager = new Manager();
	ActivityLog Log = new ActivityLog();
	String database = "key_database.csv";
	String activity_log = "ActivityLog.csv";

	public void Remove(String UID) {
		CardDTO cardDto = new CardDTO();
		cardDto.setCardNumber(UID);
		myManager.delete(cardDto);
			}
}
