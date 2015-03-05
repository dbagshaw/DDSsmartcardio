package org.mdpnp.smartcardio.rfid;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.mdpnp.smartcardio.db.ActivityLog;
import org.mdpnp.smartcardio.db.Manager;
import org.mdpnp.smartcardio.db.NotificationPopUp;
import org.mdpnp.smartcardio.dto.CardDTO;
import org.mdpnp.smartcardio.lock.LockUnlock;

public class Authenticate {

	ActivityLog Log = new ActivityLog();

	String database = "key_database.csv";
	String activity_log = "ActivityLog.csv";
	String ag = "Welcome, ";
	String ad = "Access Denied To: ";
	String iad = "Initial Access Denied To: ";

	Boolean accessGranted;

	Manager manager = new Manager();

	public void Access(String UID, Boolean masterTag) {
		CardDTO cardDto = manager.findByUID(UID);

		if (masterTag == true)
			AccessGranted(UID);
		else {

			Manager mg = new Manager();
			CardDTO myList = mg.findByUID(UID);

			if (myList == null)
				AccessDenied(UID);
			else if (cardDto.getClinicalAccess().equals("y"))
				AccessGranted(UID);
			else if (cardDto.getClinicalAccess().equals("n"))
				InitialAccessDenied(UID);
		}

	}

	// if (!accessGranted) {
	// LockUnlock.BreakGlass(UID, null);
	// }

	public boolean AccessGranted(String UID) {
		CardDTO cardDto = manager.findByUID(UID);
		LockUnlock.WindowUnlock();
		NotificationPopUp.AccessGrantedNotification(ag, cardDto.getUserName());
		System.out.print(ag + cardDto.getUserName());

		try (PrintWriter actlog = new PrintWriter(new BufferedWriter(
				new FileWriter(activity_log, true)))) {
			actlog.print("Access Granted:,");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.dateTime(UID, null, null);

		accessGranted = true;
		return accessGranted;
	}

	public boolean InitialAccessDenied(String UID) {
		CardDTO cardDto = manager.findByUID(UID);
		LockUnlock.BreakGlass(cardDto.getCardNumber(), cardDto.getUserName());
		NotificationPopUp.InitialAccessDeniedNotification(iad, cardDto.getUserName());
		System.out.print(iad);

		try (PrintWriter actlog = new PrintWriter(new BufferedWriter(
				new FileWriter(activity_log, true)))) {
			actlog.print("Access Denied:,");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.dateTime(UID, null, null);

		accessGranted = false;
		return accessGranted;

	}

	public boolean AccessDenied(String UID) {
		NotificationPopUp.AccessDeniedNotification(ad, UID);
		System.out.print(ad);

		try (PrintWriter actlog = new PrintWriter(new BufferedWriter(
				new FileWriter(activity_log, true)))) {
			actlog.print("Access Denied:,");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.dateTime(UID, null, null);

		accessGranted = false;
		return accessGranted;

	}
}
