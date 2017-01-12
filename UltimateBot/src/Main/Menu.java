package Main;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Menu extends JDialog {

	public String scriptToRun;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu dialog = new Menu();
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
	public Menu() {
		setModal(true); // *VERY IMPORTANT* Without this, the class calling it will not wait for you to close out of the menu
		setBounds(100, 100, 154, 148);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Script:");
		lblNewLabel.setBounds(10, 11, 46, 14);
		getContentPane().add(lblNewLabel);
		
		JComboBox cmbScript = new JComboBox();
		cmbScript.setModel(new DefaultComboBoxModel(new String[] {"Combat"}));
		cmbScript.setBounds(10, 36, 92, 20);
		getContentPane().add(cmbScript);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scriptToRun = cmbScript.getSelectedItem().toString();
				System.out.println(scriptToRun);
				dispose();
			}
		});
		btnStart.setBounds(13, 75, 89, 23);
		getContentPane().add(btnStart);

	}

}
