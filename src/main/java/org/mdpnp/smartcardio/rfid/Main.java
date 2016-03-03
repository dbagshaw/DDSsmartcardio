package org.mdpnp.smartcardio.rfid;

import org.mdpnp.smartcardio.db.NotificationPopUp;
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

public class Main {

	static boolean masterTag;

	public static void main(String[] args) {
		try {
			
			String searchNotice = "Searching for Terminals...";
			NotificationPopUp.getInstance().terminalNotification(searchNotice);

			LockScreen.WindowLock();
			System.out.println(LockScreen.isLocked());

			while (true) {

				// Makes UID a string variable
				String UID = CardReader.getUID();

				// System.out.println(UID);
				// System.out.println(BCrypt.hashpw(UID, BCrypt.gensalt()));

				/**
				 * If the UID and the masterTag are the same then the program
				 * will allow the user to add a new card to the database for
				 * access at another time
				 */
				masterTag = BCrypt.checkpw(UID, AddCard.getMasterTag());
				if (masterTag == true)
					CardReader.Master(UID);
				else
					Authenticate.Access(UID, masterTag);

				// lock = LockScreen.isLocked();
				System.out.println(UID + " : " + LockScreen.isLocked());

				// terminal.waitForCardAbsent(0);

				/**
				 * After accessing the system you can rescan any registered
				 * badge to re-lock the system
				 */

				if(!LockScreen.isLocked())
					CardReader.Lock(masterTag);
				else if(LockScreen.isLocked())
					CardReader.Unlock(masterTag);
				else
					return;
					

				// else
				// LockScreen.WindowUnlock();

				// lock =!lock;

				// if (lock)
				// ReadCard.getUID();

				// terminal.waitForCardPresent(0);
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
