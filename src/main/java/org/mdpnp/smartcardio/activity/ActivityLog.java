package org.mdpnp.smartcardio.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.mdpnp.smartcardio.db.ActivityManager;
import org.mdpnp.smartcardio.db.EmployeeManager;
import org.mdpnp.smartcardio.dto.CardDTO;
import org.mdpnp.smartcardio.rfid.AddCard;

public class ActivityLog {

	Date date = new Date();
	AddCard AddDB = new AddCard();
	EmployeeManager eManager = new EmployeeManager();
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS z");
	DateFormat dateOutFormat = new SimpleDateFormat(
			"EEEEE, MMMMM dd, yyyy @ HH:mm:ss z");

	String activity_log = "ActivityLog.csv";

	public void dateTime(String UID, String name, String access) {
		date.setTime(System.currentTimeMillis());
		String stringDate = dateFormat.format(date);
		String stringOutDate = dateOutFormat.format(date);

		Set<String> s = new HashSet<String>();
		String line;

		try (BufferedReader in = new BufferedReader(
				new FileReader(activity_log))) {
			while ((line = in.readLine()) != null) {
				s.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(activity_log, true)))) {
			out.println(UID + "," + stringDate);
			System.out.println(stringOutDate);
			// AddCard.addToDataBase(access, name, UID);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ActivityLogger accessGrantedLog(String UID) {
		ActivityManager aManager = new ActivityManager();
		CardDTO cardDto = new CardDTO();
		
		cardDto = eManager.findByUID(UID);
		String username = cardDto.getUserName();
		
		ActivityLogger logger = new ActivityLogger();
		logger.setEmergencyButton(false);
		logger.setUserName(username);
		logger.setAccess("Granted");
		
		aManager.create(logger);
		
//		ActivityLogger logger = aManager.granted(null, username);
		
		return logger;
	}

	public ActivityLogger accessDeniedLog(String UID) {
		ActivityManager aManager = new ActivityManager();
		
		CardDTO cardDto = eManager.findByUID(UID);
		String username = cardDto.getUserName();
		
		ActivityLogger logger = new ActivityLogger();
		logger.setEmergencyButton(false);
		logger.setUserName(username);
		logger.setAccess("Denied");
		
		aManager.create(logger);
		
//		ActivityLogger logger = aManager.emergency(null, username);
		
		return logger;
	}

	public ActivityLogger emergencyButtonLog(String UID) {
		ActivityManager aManager = new ActivityManager();
		
		CardDTO cardDto = eManager.findByUID(UID);
		String username = cardDto.getUserName();
		
		ActivityLogger logger = new ActivityLogger();
		logger.setEmergencyButton(true);
		logger.setUserName(username);
		logger.setAccess("Emergency Access Granted");
		
		aManager.create(logger);
		
//		ActivityLogger logger = aManager.emergency(null, username);
		
		return logger;
	}

	public ActivityLogger unknownDeniedLog(String UID) {
		ActivityManager aManager = new ActivityManager();

		ActivityLogger logger = new ActivityLogger();
		logger.setEmergencyButton(false);
		logger.setUserName(UID);
		logger.setAccess("Denied");
		
		aManager.create(logger);
		
//		ActivityLogger logger = aManager.unknown(null, UID);
		
		return logger;
	}
}