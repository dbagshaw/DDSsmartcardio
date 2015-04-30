
package org.mdpnp.smartcardio;

import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.CardTerminal;

import org.mdpnp.smartcardio.db.EmployeeManager;
import org.mdpnp.smartcardio.dto.CardDTO;
import org.mdpnp.smartcardio.rfid.ReadCard;

public class Test_DB {

	static ReadCard Read = new ReadCard();
	static CardTerminal terminal = null;
	
	private static String textSprint = "messaging.sprintpcs.com";
	private static String textTMobile = "@tmomail.net";
	private static String textVerizon = "@vtext.com";
	private static String textAtt = "@txt.att.net";
	
	private static String RECIPIENT = "bagshawd@wit.edu";
	
	// private static String carrier = "verizon";

	public static void main(String[] args) {

		EmployeeManager eManager = new EmployeeManager();
		List<CardDTO> supervisorList = new ArrayList<CardDTO>();
		// List<String> supervisorsContact = new ArrayList<String>();
		supervisorList = eManager.findSupervisor();
		String[] to = new String[supervisorList.size()];
		// to[0] = RECIPIENT;
		if (null != supervisorList) {
			// for(CardDTO supervisor : supervisorsList){
			String contact = "";
			for (int i = 0; i < supervisorList.size(); i++) {
				CardDTO cardDto = supervisorList.get(i);
				
				if (cardDto.getCarrier().equals("verizon"))
					contact = cardDto.getPhoneNumber() + textVerizon;
				else if (cardDto.getCarrier().equals("att"))
					contact = cardDto.getPhoneNumber() + textAtt;
				else if (cardDto.getCarrier().equals("sprint"))
					contact = cardDto.getPhoneNumber() + textSprint;
				else if (cardDto.getCarrier().equals("tmobile"))
					contact = cardDto.getPhoneNumber() + textTMobile;
				else
					contact = RECIPIENT;
				// supervisorsContact.add(s);
				to[i] = contact;
				System.out.println(to[i]);
			}
		}
	}
}
