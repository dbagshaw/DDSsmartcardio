package org.mdpnp.smartcardio.lock;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JFrame;

public class LockUnlock {
	
	JApplet window = new JApplet();
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    JFrame frame = new JFrame("Jedia");
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setSize(screenSize);
    frame.setVisible(true);    // FIRST visible = true
    frame.setResizable(false); // THEN  resizable = false
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	public static void Lock () throws IOException {
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec("C:\\Windows\\System32\\rundll32.exe user32.dll,LockWorkStation");
	}
	
	public static void Unlock() throws IOException {
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec("C:\\Windows\\System32\\rundll32.exe user32.dll,UnlockWorkStation");
	}	
}