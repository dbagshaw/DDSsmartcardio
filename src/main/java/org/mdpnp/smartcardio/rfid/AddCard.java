package org.mdpnp.smartcardio.rfid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import javax.smartcardio.CardTerminal;

import org.mdpnp.smartcardio.activity.ActivityLog;
import org.mdpnp.smartcardio.db.EmployeeManager;
import org.mdpnp.smartcardio.dto.CardDTO;
import org.mindrot.jbcrypt.BCrypt;

public class AddCard {

	static RemoveCard Remove = new RemoveCard();
	static ActivityLog Log = new ActivityLog();
	static EmployeeManager eManager = new EmployeeManager();
	
	static boolean masterTag;

	static String key = "key.txt";
	static String database = "key_database.csv";
	static String activity_log = "ActivityLog.csv";

	public static String getMasterTag() {
		String MT = null;
		String line = null;
		try (BufferedReader in = new BufferedReader(new FileReader(key))) {
			while ((line = in.readLine()) != null) {
				MT = line;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return MT;
	}

	/**
	 * Compare method enables the program to tell the user whether or not the
	 * card they are added is already present in the database or not so they can
	 * decide if they want to continue the process.
	 * 
	 * @param UID
	 * @return
	 */
	public static boolean Compare(String UID) {

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
	public static void addCard(CardTerminal terminal) {

		try {

			// if (terminal.waitForCardPresent(5000) == false) {
			// Auth.Access(UID, masterTag);
			// } else {

			terminal.waitForCardPresent(0);

			// Makes UID a string variable
			String UID = ReadCard.getUID();
			terminal.waitForCardAbsent(2000);
			masterTag = BCrypt.checkpw(UID, getMasterTag());

			/**
			 * To add or remove a card's UID from the database then the master
			 * tag must be presented once more for confirmation
			 */
			// if (!UID.equals(masterTag)) {
			if (masterTag == false) {
				if (Compare(UID) == true)
					System.out.println("Please Confirm Addition to Database");
				else
					System.out.println("Please Confirm Removal From Database");

				terminal.waitForCardAbsent(0);
				String UID2 = ReadCard.getUID();

				masterTag = BCrypt.checkpw(UID2, getMasterTag());
				// if (!UID2.equals(masterTag)) {
				if (masterTag == false)
					System.out.println("Wrong Card. Try Again");
				else
					AddCard.Add(UID, masterTag);
			} else
				Authenticate.Access(UID, masterTag);
			// }
		} catch (Throwable t) {
			// t.printStackTrace();
			System.out.println("Bad Read. Try Again.");
		}
	}

	public static void Add(String UID, Boolean masterTag) {
		boolean accessGranted = fileReader(UID);
		fileReader(UID);
		if (!accessGranted) {
			System.out.println("Type User's Name");
			Scanner input = new Scanner(System.in);
			String name = input.nextLine();

			System.out.println("Is This Person Allowed Clinical Access?");
			// Scanner input2 = new Scanner(System.in);
			String access = input.next().toLowerCase();

			if (access.startsWith("y"))
				access = "y";
			else if (access.startsWith("n"))
				access = "n";

			System.out.println(access);

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

		cardDto.setUserName(name);
		cardDto.setCardNumber(UID);
		cardDto.setClinicalAccess(access);

		eManager.create(cardDto);
	}

	public static boolean fileReader(String UID) {

		boolean accessGranted = false;

		List<CardDTO> myList = eManager.findByAll();

		for (CardDTO cardDto : myList)
			if (cardDto.getCardNumber().equals(UID))
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