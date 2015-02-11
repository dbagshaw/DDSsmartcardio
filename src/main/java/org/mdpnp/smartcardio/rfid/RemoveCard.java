package org.mdpnp.smartcardio.rfid;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.mdpnp.smartcardio.db.ActivityLog;
import org.mindrot.jbcrypt.BCrypt;

public class RemoveCard {

	ActivityLog Log = new ActivityLog();
	String database = "key_database.csv";
	String activity_log = "ActivityLog.csv";

	public void Remove(String UID, Boolean masterTag) {
		Set<String> s = new HashSet<String>();
		String line;

		// read the key_database and adds each line to hash set
		try (BufferedReader in = new BufferedReader(new FileReader(database))) {
			while ((line = in.readLine()) != null) {
//				if (!line.equals(UID)) {
				if (BCrypt.checkpw(UID, line) != true) {
					s.add(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// read the key_database and adds each line to hash set
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(database)))) {
			for (String str : s) {
				out.println(str);
			}
			System.out.print("Card Removed From Database: ");
			try (PrintWriter actlog = new PrintWriter(new BufferedWriter(
					new FileWriter(activity_log, true)))) {
				actlog.print("Card Removed:,");
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.dateTime(UID, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
