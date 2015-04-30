import java.util.List;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

public class terminalTest {
	static List<CardTerminal> terminalList;

	

	static private CardTerminal TerminalSetUp() {
		CardTerminal terminal = null;

		try {
			// get the list of available terminals
			TerminalFactory factory = TerminalFactory
					.getInstance("PC/SC", null);
			List<CardTerminal> terminalList = factory.terminals().list();

			while (terminalList.isEmpty()){
			// take the first terminal in the list
			terminal = (CardTerminal) terminalList.get(0);
			}
			// System.out.println("Connecting to Terminal");

			if (terminalList.isEmpty())
				System.out.println("No Terminals Found");

			else
				System.out.println("Connected to: " + terminal);

		} catch (Exception ex) {
			// ex.printStackTrace();

		}

		return terminal;

	}
	
	public static void main(String[] args) {

		while (true)
			TerminalSetUp();

	}
}
