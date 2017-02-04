package Main.Fishing;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class FishingMenu extends JDialog {

	public boolean exit;
	public boolean shouldBank;
	
	public String areaChoice;
	
	public FishingArea fishingArea;
	
	class FishingArea {
		public Area area;
		public Area bankArea;
		public String fishType;
		public String secondaryFishType;
		
		public FishingArea(Area area, Area bankArea, String fishType, String secondaryFishType){
			this.area = area;
			this.bankArea = bankArea;
			this.fishType = fishType;
			this.secondaryFishType = secondaryFishType;
		}
	}
	
	private FishingArea[] fishingAreas = {
			new FishingArea(new Area(3091, 3226, 3082, 3239), Banks.DRAYNOR, "Net", "Bait"),
			new FishingArea(new Area(3104, 3437, 3108, 3426), Banks.GRAND_EXCHANGE, "Lure", "Bait")
	};
	
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
		setModal(true);
		setBounds(100, 100, 239, 147);
		getContentPane().setLayout(null);
		
		JLabel lblArea = new JLabel("Area:");
		lblArea.setBounds(10, 11, 46, 14);
		getContentPane().add(lblArea);
		
		JComboBox<String> cmbArea = new JComboBox<String>();
		cmbArea.setModel(new DefaultComboBoxModel<String>(new String[] {"Draynor Village - Net", "Barbarian Village - Lure"}));
		cmbArea.setBounds(10, 33, 151, 20);
		getContentPane().add(cmbArea);
		
		JLabel lblBank = new JLabel("Bank:");
		lblBank.setBounds(189, 11, 46, 14);
		getContentPane().add(lblBank);
		
		JCheckBox chkBank = new JCheckBox("");
		chkBank.setSelected(true);
		chkBank.setBounds(199, 33, 21, 20);
		getContentPane().add(chkBank);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				fishingArea = fishingAreas[cmbArea.getSelectedIndex()];
				areaChoice = cmbArea.getSelectedItem().toString();
				
				shouldBank = chkBank.isSelected();
				
				exit = false;
				dispose();
			}
		});
		btnStart.setBounds(10, 74, 89, 23);
		getContentPane().add(btnStart);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit = true;
				dispose();
			}
		});
		btnExit.setBounds(124, 74, 89, 23);
		getContentPane().add(btnExit);

	}
}
