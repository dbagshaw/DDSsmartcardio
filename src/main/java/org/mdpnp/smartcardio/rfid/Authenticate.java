package org.mdpnp.smartcardio.rfid;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.mdpnp.smartcardio.db.ActivityLog;
import org.mdpnp.smartcardio.db.Manager;
import org.mdpnp.smartcardio.dto.CardDTO;
import org.mdpnp.smartcardio.lock.LockUnlock;

public class Authenticate {

	ActivityLog Log = new ActivityLog();

	String database = "key_database.csv";
	String activity_log = "ActivityLog.csv";

	public void Access(String UID, Boolean masterTag) throws Exception {


		if (masterTag == true) {
			System.out.print("Access Granted: ");
			LockUnlock.Unlock();
			try (PrintWriter actlog = new PrintWriter(new BufferedWriter(
					new FileWriter(activity_log, true)))) {
				actlog.print("Access Granted:,");
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.dateTime(UID, null);
		} else {
			Manager mg = new Manager();
			CardDTO myList = mg.findUID(UID);
								
				if (myList == null) {

					System.out.print("Access Denied: ");

					try (PrintWriter actlog = new PrintWriter(
							new BufferedWriter(new FileWriter(activity_log,
									true)))) {
						actlog.print("Access Denied:,");
					} catch (IOException e) {
						e.printStackTrace();
					}
					Log.dateTime(UID, null);
				} else {
					System.out.print("Access Granted: ");
					LockUnlock.Unlock();
					try (PrintWriter actlog = new PrintWriter(
							new BufferedWriter(new FileWriter(activity_log,
									true)))) {
						actlog.print("Access Granted:,");
					} catch (IOException e) {
						e.printStackTrace();
					}
					Log.dateTime(UID, null);
				}

		}

	}
}
