package org.mdpnp.smartcardio.rfid;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.mdpnp.smartcardio.activity.ActivityLog;
import org.mdpnp.smartcardio.db.EmployeeManager;
import org.mdpnp.smartcardio.db.NotificationPopUp;
import org.mdpnp.smartcardio.dto.CardDTO;
import org.mdpnp.smartcardio.lock.LockScreen;

public class Authenticate {

	static ActivityLog Log = new ActivityLog();

	static String database = "key_database.csv";
	static String activity_log = "ActivityLog.csv";
	static String ag = "Welcome, ";
	static String ad = "Access Denied To: ";
	static String iad = "Initial Access Denied To: ";

	static Boolean accessGranted;

	static EmployeeManager eManager = new EmployeeManager();

	public static void Access(String UID, Boolean masterTag) {
		CardDTO cardDto = eManager.findByUID(UID);

		if (masterTag == true)
			MasterKeyCardAccess(UID);
		else {

			CardDTO myList = eManager.findByUID(UID);

			if (myList == null)
				AccessDenied(UID);
			else if (cardDto.getPermissions().equals("y"))
				AccessGranted(UID);
			else if (cardDto.getPermissions().equals("n"))
				InitialAccessDenied(UID);
		}

	}

	public static boolean MasterKeyCardAccess(String UID) {
		LockScreen.WindowUnlock();
		// NotificationPopUp.MasterNotification(ag);
		NotificationPopUp.getInstance().accessNotification(ag, UID);
		System.out.print(ag + "Master Key Card. ");

		// Log.accessGrantedLog();

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

	public static boolean AccessGranted(String UID) {
		CardDTO cardDto = eManager.findByUID(UID);
		// LockScreen.WindowUnlock();
		String username = cardDto.getUserName();
		// NotificationPopUp.AccessGrantedNotification(ag,
		// cardDto.getCardNumber());
		NotificationPopUp.getInstance().accessNotification(ag, cardDto.getCardNumber());
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

	public static boolean InitialAccessDenied(String UID) {
		CardDTO cardDto = eManager.findByUID(UID);
		String username = cardDto.getUserName();

		LockScreen.BreakGlass(cardDto.getCardNumber(), username);

		// NotificationPopUp.InitialAccessDeniedNotification(iad,
		// cardDto.getCardNumber());
		NotificationPopUp.getInstance().accessNotification(iad, cardDto.getCardNumber());
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

	public static boolean AccessDenied(String UID) {
		// NotificationPopUp.AccessDeniedNotification(ad, UID);
		NotificationPopUp.getInstance().accessNotification(ad, UID);
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
