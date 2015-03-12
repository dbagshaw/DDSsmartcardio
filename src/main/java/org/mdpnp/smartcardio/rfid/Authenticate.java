package org.mdpnp.smartcardio.rfid;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.mdpnp.smartcardio.activity.ActivityLog;
import org.mdpnp.smartcardio.db.EmployeeManager;
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

	EmployeeManager eManager = new EmployeeManager();

	public void Access(String UID, Boolean masterTag) {
		CardDTO cardDto = eManager.findByUID(UID);

		if (masterTag == true)
			MasterKeyCardAccess(UID);
		else {

			CardDTO myList = eManager.findByUID(UID);

			if (myList == null)
				AccessDenied(UID);
			else if (cardDto.getClinicalAccess().equals("y"))
				AccessGranted(UID);
			else if (cardDto.getClinicalAccess().equals("n"))
				InitialAccessDenied(UID);
		}

	}

	public boolean MasterKeyCardAccess(String UID) {
		LockUnlock.WindowUnlock();
		NotificationPopUp.MasterNotification(ag);
		System.out.print(ag + "Master Key Card. ");
		
//		Log.accessGrantedLog();
		
		
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
	public boolean AccessGranted(String UID) {
		CardDTO cardDto = eManager.findByUID(UID);
		LockUnlock.WindowUnlock();
		String username = cardDto.getUserName();
		NotificationPopUp.AccessGrantedNotification(ag, cardDto.getCardNumber());
		System.out.print(ag + username + ". ");

		Log.accessGrantedLog(UID);
		
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
		CardDTO cardDto = eManager.findByUID(UID);
		String username = cardDto.getUserName();
		
		LockUnlock.BreakGlass(cardDto.getCardNumber(), username);
		
		NotificationPopUp.InitialAccessDeniedNotification(iad,
				cardDto.getCardNumber());
		System.out.print(iad + username + ". ");
		
		Log.accessDeniedLog(UID);

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
		
		Log.unknownDeniedLog(UID);

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
