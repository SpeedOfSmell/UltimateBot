package Main.Cooking;

import java.awt.EventQueue;

import javax.swing.JDialog;

public class CookingMenu extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CookingMenu dialog = new CookingMenu();
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
	public CookingMenu() {
		setBounds(100, 100, 450, 300);

	}

}
