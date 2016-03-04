package org.mdpnp.smartcardio.rfid;

import org.mdpnp.smartcardio.db.NotificationPopUp;
import org.mdpnp.smartcardio.lock.LockScreen;
import org.mindrot.jbcrypt.BCrypt;

public class Main {

	static boolean masterTag;

	public static void main(String[] args) {
		try {
			
			String searchNotice = "Searching for Terminals...";
			NotificationPopUp.getInstance().terminalNotification(searchNotice);

			LockScreen.WindowLock();
			System.out.println(LockScreen.isLocked());

			while (true) {

				// Makes UID a string variable
				String UID = CardReader.getUID();

				// System.out.println(UID);
				// System.out.println(BCrypt.hashpw(UID, BCrypt.gensalt()));

				/**
				 * If the UID and the masterTag are the same then the program
				 * will allow the user to add a new card to the database for
				 * access at another time
				 */
				masterTag = BCrypt.checkpw(UID, AddCard.getMasterTag());
				if (masterTag == true)
					CardReader.Master(UID);
				else
					Authenticate.Access(UID, masterTag);

				System.out.println(UID + " : " + LockScreen.isLocked());

				/**
				 * After accessing the system you can rescan any registered
				 * badge to re-lock the system
				 */

				if(!LockScreen.isLocked())
					CardReader.Lock(masterTag);
				else if(LockScreen.isLocked())
					CardReader.Unlock(masterTag);
				else
					return;
			}

		} catch (Throwable t) {
			// t.printStackTrace();
			// System.out.println("Reader Not Present.");
		}
	}
}
