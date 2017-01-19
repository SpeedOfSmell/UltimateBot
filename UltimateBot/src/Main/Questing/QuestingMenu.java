package Main.Questing;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class QuestingMenu extends JDialog {
	
	public String quest;
	public boolean shouldBuyItems;
	public boolean exit;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuestingMenu dialog = new QuestingMenu();
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
	public QuestingMenu() {
		setModal(true);
		setBounds(100, 100, 246, 181);
		getContentPane().setLayout(null);
		
		JLabel lblQuest = new JLabel("Quest:");
		lblQuest.setBounds(10, 11, 46, 14);
		getContentPane().add(lblQuest);
		
		JComboBox<String> cmbQuest = new JComboBox<String>();
		cmbQuest.setModel(new DefaultComboBoxModel<String>(new String[] {"Imp Catcher", "Witch's Potion"}));
		cmbQuest.setBounds(10, 36, 122, 20);
		getContentPane().add(cmbQuest);
		
		JLabel lblBuyItems = new JLabel("Buy items:");
		lblBuyItems.setBounds(165, 11, 60, 14);
		getContentPane().add(lblBuyItems);
		
		JCheckBox chkBuyItems = new JCheckBox("");
		chkBuyItems.setSelected(true);
		chkBuyItems.setBounds(199, 36, 26, 23);
		getContentPane().add(chkBuyItems);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quest = cmbQuest.getSelectedItem().toString();
				shouldBuyItems = chkBuyItems.isSelected();
				
				exit = false;
				dispose();
			}
		});
		btnStart.setBounds(10, 108, 89, 23);
		getContentPane().add(btnStart);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit = true;
				dispose();
			}
		});
		btnExit.setBounds(131, 108, 89, 23);
		getContentPane().add(btnExit);

	}
}
