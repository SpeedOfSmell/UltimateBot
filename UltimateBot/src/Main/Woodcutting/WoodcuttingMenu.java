package Main.Woodcutting;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;

@SuppressWarnings("serial")
public class WoodcuttingMenu extends JDialog {

	public boolean shouldBank;
	public boolean shouldWorldHop;
	public Area area;
	public Area bankArea;
	public String treeType;
	public boolean exit;
	public String woodcuttingArea;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WoodcuttingMenu dialog = new WoodcuttingMenu();
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
	public WoodcuttingMenu() {
		final Map<String, Area> AREAS = new HashMap<String, Area>(); //load up the areas
		AREAS.put("Lumbridge - Tree", new Area(3196, 3217, 3179, 3236));
		AREAS.put("Lumbridge - Oak", new Area(3207, 3238, 3201, 3249));
		AREAS.put("Draynor Village - Willow", new Area(3091, 3226, 3082, 3239));
		AREAS.put("Grand Exchange - Yew", new Area(3203, 3506, 3223, 3498));
		
		final Map<String, Area> BANKS = new HashMap<String, Area>(); //load up the banks
		BANKS.put("Lumbridge - Tree", Banks.LUMBRIDGE_UPPER);
		BANKS.put("Lumbridge - Oak", Banks.LUMBRIDGE_UPPER);
		BANKS.put("Draynor Village - Willow", Banks.DRAYNOR);
		BANKS.put("Grand Exchange - Yew", Banks.GRAND_EXCHANGE);
		
		setModal(true);
		setBounds(100, 100, 345, 150);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Area:");
		lblNewLabel.setBounds(10, 11, 27, 14);
		getContentPane().add(lblNewLabel);
		
		JComboBox<String> cmbArea = new JComboBox<String>();
		cmbArea.setModel(new DefaultComboBoxModel<String>(new String[] {"Lumbridge - Tree", "Lumbridge - Oak", "Draynor Village - Willow", "Grand Exchange - Yew"}));
		cmbArea.setBounds(10, 31, 133, 20);
		getContentPane().add(cmbArea);
		
		JLabel lblBank = new JLabel("Bank:");
		lblBank.setBounds(169, 11, 46, 14);
		getContentPane().add(lblBank);
		
		JCheckBox chkBank = new JCheckBox("");
		chkBank.setSelected(true);
		chkBank.setBounds(186, 25, 27, 30);
		getContentPane().add(chkBank);
		
		JLabel lblWorldHop = new JLabel("World hop:");
		lblWorldHop.setBounds(258, 7, 73, 14);
		getContentPane().add(lblWorldHop);
		
		JCheckBox chkWorldHop = new JCheckBox("");
		chkWorldHop.setSelected(true);
		chkWorldHop.setBounds(298, 21, 27, 30);
		getContentPane().add(chkWorldHop);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				woodcuttingArea = cmbArea.getSelectedItem().toString(); // String in the combobox				
				area = AREAS.get(woodcuttingArea);
				bankArea = BANKS.get(woodcuttingArea);
				treeType = woodcuttingArea.substring(woodcuttingArea.indexOf('-') + 2); // Everything past the dash
				
				shouldBank = chkBank.isSelected();
				shouldWorldHop = chkWorldHop.isSelected();
				
				exit = false;
				dispose();
			}
		});
		btnStart.setBounds(10, 69, 89, 23);
		getContentPane().add(btnStart);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit = true;
				dispose();
			}
		});
		btnExit.setBounds(230, 69, 89, 23);
		getContentPane().add(btnExit);

	}
}
