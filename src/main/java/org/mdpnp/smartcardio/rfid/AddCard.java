package org.mdpnp.smartcardio.rfid;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.mdpnp.smartcardio.db.ActivityLog;
import org.mdpnp.smartcardio.db.Manager;
import org.mdpnp.smartcardio.dto.CardDTO;
import org.mindrot.jbcrypt.BCrypt;

public class AddCard {

	static RemoveCard Remove = new RemoveCard();
	static ActivityLog Log = new ActivityLog();

	static String database = "key_database.csv";
	static String activity_log = "ActivityLog.csv";

	/**
	 * Compare method enables the program to tell the user whether or not the
	 * card they are added is already present in the database or not so they can
	 * decide if they want to continue the process.
	 * 
	 * @param UID
	 * @return
	 */
	public boolean Compare(String UID) {

		// Set<String> s = new HashSet<String>();
		// String line;
		boolean accessGranted = fileReader(UID);
		// if hash set doesn't contain the new UID add it to the file
		// if (!s.contains(UID)) { // this is original code
		if (!accessGranted) {
			accessGranted = true;
		} else {
			accessGranted = false;
		}

		return accessGranted;
	}

	/**
	 * Add method will add the new user to the database if they are not already
	 * present. If they are already in the database they will be removed via the
	 * RemoveCard class.
	 * 
	 * @param UID
	 */
	public static void Add(String UID, Boolean masterTag) {
		boolean accessGranted = fileReader(UID);
		fileReader(UID);
		if (!accessGranted) {
			System.out.println("Type User's Name");
			Scanner input = new Scanner(System.in);
			String name = input.next();

			System.out.println("Is This Person Allowed Clinical Access?");
			// Scanner input2 = new Scanner(System.in);
			String access = input.next();

			System.out.println("");
			fileWriter(access, name, UID, masterTag); // access may not be
														// necessary here

			// cardDto.setCardNumber(BCrypt.hashpw(UID, BCrypt.gensalt()));
			input.close();
			
			addToDataBase(access, name, UID);

		} else {
			// if hashset does contain UID, remove it from the database
			Remove.Remove(UID);
		}
	}

	public static void addToDataBase(String access, String name, String UID) {
		CardDTO cardDto = new CardDTO();
		Manager myManager = new Manager();

		cardDto.setUserName(name);
		cardDto.setCardNumber(UID);
		cardDto.setClinicalAccess(access);

		myManager.create(cardDto);

	}

	public static boolean fileReader(String UID) {

		boolean accessGranted = false;

		Manager mg = new Manager();
		List<CardDTO> myList = mg.findByAll();

		for (CardDTO card : myList)
			if (card.getCardNumber().equals(UID))
				accessGranted = true;

		return accessGranted;
	}

	public static void fileWriter(String access, String name, String UID,
			Boolean masterTag) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(database, true)))) {

			out.println(BCrypt.hashpw(UID, BCrypt.gensalt()));
			System.out.print(name + "'s Card Added to Database: ");

			try (PrintWriter actlog = new PrintWriter(new BufferedWriter(
					new FileWriter(activity_log, true)))) {
				actlog.print("Card Added:,");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("NCP");
			}
			Log.dateTime(UID, name, null);

		} catch (Exception e) {
		}
	}

}