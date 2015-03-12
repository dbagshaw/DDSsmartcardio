package org.mdpnp.smartcardio.rfid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

import org.mdpnp.smartcardio.activity.ActivityLog;
import org.mdpnp.smartcardio.lock.LockUnlock;
import org.mindrot.jbcrypt.BCrypt;

import smartcardio.CardRead;
import smartcardio.CardReadDataWriter;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.domain.DomainParticipantQos;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.infrastructure.TransportBuiltinKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.topic.Topic;

public class SmartCardReader {

	static Boolean masterTag;
	static CardChannel channel;
	// static CardTerminal terminal = null;

	static AddCard Add = new AddCard();
	static ReadCard Read = new ReadCard();
	static Authenticate Auth = new Authenticate();
	// static AESEncryption AES = new AESEncryption();
	static boolean run = true;
	static boolean lock = false;
//	static String UID;

	static String key = "key.txt";

	public static void main(String[] args) {

		try {

			LockUnlock.WindowLock(lock);

			CardTerminal terminal = TerminalSetUp();

			while (run) {

				// Makes UID a string variable
				String UID = bytesToHex(Read.Reader(terminal).getData());
				// System.out.println(BCrypt.hashpw(UID, BCrypt.gensalt()));

				/**
				 * @DDS method creates a connection to Mongo DB server and
				 *      publishes the card UID.
				 */
				DDS(UID);

				/**
				 * If the UID and the masterTag are the same then the program
				 * will allow the user to add a new card to the database for
				 * access at another time
				 */
				masterTag = BCrypt.checkpw(UID, getMasterTag());
				// if (UID.equals(masterTag)) {
				if (masterTag == true) {
					terminal.waitForCardAbsent(0);
					/**
					 * If the user wants to use the master tag to access the
					 * device then they simply tap again to be granted access
					 */
					System.out
							.print("Please Present New Badge or Tap Again for Access\n");

					try {

						// if (terminal.waitForCardPresent(5000) == false) {
						// Auth.Access(UID, masterTag);
						// } else {

						terminal.waitForCardPresent(0);

						// Makes UID a string variable
						UID = bytesToHex(Read.Reader(terminal).getData());
						terminal.waitForCardAbsent(2000);
						masterTag = BCrypt.checkpw(UID, getMasterTag());

						/**
						 * To add or remove a card's UID from the database then
						 * the master tag must be presented once more for
						 * confirmation
						 */
						// if (!UID.equals(masterTag)) {
						if (masterTag == false) {
							if (Add.Compare(UID) == true)
								System.out
										.println("Please Confirm Addition to Database");
							else
								System.out
										.println("Please Confirm Removal From Database");

							terminal.waitForCardAbsent(0);
							String UID2 = bytesToHex(Read.Reader(terminal)
									.getData());
							masterTag = BCrypt.checkpw(UID2, getMasterTag());
							// if (!UID2.equals(masterTag)) {
							if (masterTag == false)
								System.out.println("Wrong Card. Try Again");
							else
								AddCard.Add(UID, masterTag);
						} else
							Auth.Access(UID, masterTag);
						// }
					} catch (Exception ex) {
						ex.printStackTrace();
						// System.out.println("Bad Read. Try Again.");
					}

				} else
					Auth.Access(UID, masterTag);
				
				/**
				 * After accessing the system you can rescan your badge to
				 * re-lock the system
				 */
				if (!lock) {
					String matcher = UID;
					terminal.waitForCardAbsent(0);
					UID = bytesToHex(Read.Reader(terminal).getData());
					if (UID.equals(matcher)) {
						LockUnlock.WindowLock(lock);
						ActivityLog Log = new ActivityLog();
						Log.windowLockLog(UID);
					}
				}

				terminal.waitForCardAbsent(0);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			// System.out.println("Reader Not Present.");
		}
	}

	private static CardTerminal TerminalSetUp() {
		CardTerminal terminal = null;

		try {
			// get the list of available terminals
			TerminalFactory factory = TerminalFactory
					.getInstance("PC/SC", null);
			List<CardTerminal> terminalList = factory.terminals().list();

			// take the first terminal in the list
			terminal = (CardTerminal) terminalList.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			// System.out.println("Reader Not Present.");
		}

		return terminal;

	}

	private static void DDS(String UID) {
		// DDSifying
		DomainParticipantFactory domainParticipantFactory = DomainParticipantFactory
				.get_instance();

		DomainParticipantQos pQos = new DomainParticipantQos();
		domainParticipantFactory.get_default_participant_qos(pQos);
		pQos.transport_builtin.mask = TransportBuiltinKind.UDPv4;
		pQos.discovery.initial_peers.clear();
		pQos.discovery.initial_peers.add("udpv4://239.255.0.1");

		DomainParticipant domainParticipant = domainParticipantFactory
				.create_participant(14, pQos, null, StatusKind.STATUS_MASK_NONE);
		pQos = null;

		smartcardio.CardReadTypeSupport.register_type(domainParticipant,
				smartcardio.CardReadTypeSupport.get_type_name());

		Topic cardReadTopic = domainParticipant.create_topic(
				smartcardio.CardReadTopic.VALUE,
				smartcardio.CardReadTypeSupport.get_type_name(),
				DomainParticipant.TOPIC_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);

		smartcardio.CardReadDataWriter cardReadDataWriter = (CardReadDataWriter) domainParticipant
				.create_datawriter(cardReadTopic,
						Publisher.DATAWRITER_QOS_DEFAULT, null,
						StatusKind.STATUS_MASK_NONE);

		// DDS Stuff
		smartcardio.CardRead cardRead = (CardRead) smartcardio.CardRead
				.create();
		cardRead.unique_identifier = BCrypt.hashpw(UID, BCrypt.gensalt());
		// cardRead.unique_identifier = UID
		cardReadDataWriter.write(cardRead, InstanceHandle_t.HANDLE_NIL);

	}

	private static String getMasterTag() {
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

	private static final char wordToHexChar(byte b) {
		if (b < 10) {
			return (char) ('0' + b);
		} else {
			return (char) ('A' + (b - 10));
		}
	}

	private static final String bytesToHex(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			sb.append(wordToHexChar((byte) (0x0F & (b[i] >> 4))));
			sb.append(wordToHexChar((byte) (0x0F & b[i])));
			sb.append(' ');
			if (0 == ((i + 1) % 10)) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}

}