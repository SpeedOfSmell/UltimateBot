package Main.Fishing;

import java.awt.EventQueue;

import javax.swing.JDialog;

@SuppressWarnings("serial")
public class FishingMenu extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FishingMenu dialog = new FishingMenu();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public FishingMenu() {
		setBounds(100, 100, 450, 300);

	}

}
