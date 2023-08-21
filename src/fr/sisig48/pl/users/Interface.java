package fr.sisig48.pl.users;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Interface {
	public static void launch() {
		SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Fenêtre Swing");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(300, 200);
            frame.setVisible(true);
        });
	}
}
