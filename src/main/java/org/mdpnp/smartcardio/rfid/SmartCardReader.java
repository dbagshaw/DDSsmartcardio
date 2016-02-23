package org.mdpnp.smartcardio.rfid;

import javax.smartcardio.CardTerminal;

import org.mdpnp.smartcardio.lock.LockScreen;
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

/*
 * Add Reliability!
 */

public class SmartCardReader {

	static boolean masterTag;
	// CardChannel channel;
	// static CardTerminal terminal = null;

	// static AddCard Add = new AddCard();
	// static ReadCard Read = new ReadCard();
	// static Authenticate Auth = new Authenticate();
	// static AESEncryption AES = new AESEncryption();
	static boolean run = true;
	static boolean lock = false;

	public static void main(String[] args) {

		try {

			LockScreen.WindowLock();
			CardTerminal terminal = ReadCard.TerminalSetUp();

			while (run) {

				// Makes UID a string variable
				String UID = null;
				if (terminal != null) {
					UID = ReadCard.getUID();
					// else
					// ReadCard.TerminalSetUp();

					// System.out.println(UID);
					// System.out.println(BCrypt.hashpw(UID, BCrypt.gensalt()));

					/**
					 * @DDS method creates a connection to Mongo DB server and
					 *      publishes the card UID.
					 */
					// DDS(UID);

					/**
					 * If the UID and the masterTag are the same then the
					 * program will allow the user to add a new card to the
					 * database for access at another time
					 */
					masterTag = BCrypt.checkpw(UID, AddCard.getMasterTag());
					// if (UID.equals(masterTag)) {
					if (masterTag == true)
						ReadCard.Master(terminal, UID);
					else
						Authenticate.Access(UID, masterTag);

					/**
					 * After accessing the system you can rescan any registered
					 * badge to re-lock the system
					 */
					if (!lock)
						ReadCard.reLock(lock, masterTag/*, terminal*/);

					// if(lock)
					// ReadCard.reLock(lock, masterTag, terminal);

					// terminal.waitForCardAbsent(0);
				}
			}

		} catch (Throwable t) {
			// t.printStackTrace();
			// System.out.println("Reader Not Present.");
		}
	}

	@SuppressWarnings("unused")
	private void DDS(String UID) {
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

}