package org.mdpnp.smartcardio.rfid;

import org.mdpnp.smartcardio.activity.ActivityLog;
import org.mdpnp.smartcardio.db.EmployeeManager;
import org.mdpnp.smartcardio.dto.CardDTO;

public class RemoveCard {

	EmployeeManager manager = new EmployeeManager();
	ActivityLog Log = new ActivityLog();
	String database = "key_database.csv";
	String activity_log = "ActivityLog.csv";

	public void Remove(String UID) {
		CardDTO cardDto = new CardDTO();
		cardDto.setCardNumber(UID);
		manager.delete(cardDto);
			}
}
