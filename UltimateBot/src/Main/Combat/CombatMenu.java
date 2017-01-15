package Main.Combat;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.text.NumberFormatter;

import org.osbot.rs07.api.map.Area;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;


@SuppressWarnings("serial")
public class CombatMenu extends JDialog {

	public String monsterType;
	public String foodType;
	public int minHp;	
	public Area bank;
	public int amountOfFood;
	
	public String[] lootItems;
	public String[]invItems;
	
	public int levelGoals[] = new int[3];
	public boolean exit;
	
	private final JSeparator separator = new JSeparator();	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CombatMenu dialog = new CombatMenu();
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
	public CombatMenu() {
		final Map<String, Area> BANKS = new HashMap<String, Area>(); //load up the bank areas
		BANKS.put("Edgeville", new Area(3098, 3499, 3091, 3488));
		BANKS.put("Varrock East", new Area(3257, 3420, 3250, 3423));
		BANKS.put("Varrock West", new Area(3180, 3433, 3185, 3447));
		
		setModal(true);
		
		setBounds(100, 100, 275, 376);
		getContentPane().setLayout(null);
		
		JLabel lblMonster = new JLabel("Monster:");
		lblMonster.setBounds(10, 11, 61, 14);
		getContentPane().add(lblMonster);
		
		JComboBox<String> cmbMonster = new JComboBox<String>();
		cmbMonster.setModel(new DefaultComboBoxModel<String>(new String[] {"Man", "Woman", "Goblin", "Cow", "Guard", "Hill Giant"}));
		cmbMonster.setEditable(true);
		cmbMonster.setBounds(10, 30, 95, 20);
		getContentPane().add(cmbMonster);
		
		JLabel lblFood = new JLabel("Food:");
		lblFood.setBounds(115, 11, 46, 14);
		getContentPane().add(lblFood);
		
		JComboBox<String> cmbFood = new JComboBox<String>();
		cmbFood.setModel(new DefaultComboBoxModel<String>(new String[] {"None", "Shrimp", "Sardine", "Bread", "Trout", "Cod", "Salmon", "Tuna", "Lobster", "Bass"}));
		cmbFood.setEditable(true);
		cmbFood.setBounds(115, 30, 71, 20);
		getContentPane().add(cmbFood);
		
		JLabel lblMinHp = new JLabel("Min HP:");
		lblMinHp.setBounds(137, 61, 46, 14);
		getContentPane().add(lblMinHp);
		
		 NumberFormat format = NumberFormat.getInstance();
		 NumberFormatter formatter = new NumberFormatter(format);
		 formatter.setValueClass(Integer.class);
		 formatter.setMinimum(0);
		 formatter.setMaximum(99);
		 formatter.setAllowsInvalid(false);
		
		JFormattedTextField txtHp = new JFormattedTextField(formatter);
		txtHp.setText("20");
		txtHp.setBounds(137, 80, 46, 20);
		getContentPane().add(txtHp);
		
		JFormattedTextField txtAtt = new JFormattedTextField(formatter);
		txtAtt.setText("70");
		txtAtt.setBounds(31, 262, 25, 20);
		getContentPane().add(txtAtt);
		
		JFormattedTextField txtStr = new JFormattedTextField(formatter);
		txtStr.setText("99");
		txtStr.setBounds(114, 262, 25, 20);
		getContentPane().add(txtStr);
		
		JFormattedTextField txtDef = new JFormattedTextField(formatter);
		txtDef.setText("45");
		txtDef.setBounds(193, 262, 25, 20);
		getContentPane().add(txtDef);
			
		JLabel lblGoals = new JLabel("Goals:");
		lblGoals.setBounds(10, 213, 46, 14);
		getContentPane().add(lblGoals);
		
		JLabel lblAttack = new JLabel("Attack:");
		lblAttack.setBounds(25, 237, 46, 14);
		getContentPane().add(lblAttack);
		
		JLabel lblStrength = new JLabel("Strength:");
		lblStrength.setBounds(93, 237, 46, 14);
		getContentPane().add(lblStrength);
		
		JLabel lblDefense = new JLabel("Defense:");
		lblDefense.setBounds(172, 237, 46, 14);
		getContentPane().add(lblDefense);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 111, 259, 2);
		getContentPane().add(separator_1);
		
		JLabel lblBank = new JLabel("Bank:");
		lblBank.setBounds(10, 61, 46, 14);
		getContentPane().add(lblBank);
		
		JComboBox<String> cmbBank = new JComboBox<String>();
		cmbBank.setModel(new DefaultComboBoxModel<String>(new String[] {"None", "Edgeville", "Varrock East", "Varrock West"}));
		cmbBank.setBounds(10, 80, 107, 20);
		getContentPane().add(cmbBank);
		
		JLabel lblItems = new JLabel("Loot Items:");
		lblItems.setBounds(137, 121, 77, 14);
		getContentPane().add(lblItems);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(137, 141, 107, 48);
		getContentPane().add(scrollPane);
		
		JTextPane txtItems = new JTextPane();
		scrollPane.setViewportView(txtItems);
		
		JLabel lblAmountOfFood = new JLabel("Food amount:");
		lblAmountOfFood.setBounds(182, 61, 77, 14);
		getContentPane().add(lblAmountOfFood);
		
		JFormattedTextField txtAmount = new JFormattedTextField(formatter);
		txtAmount.setText("14");
		txtAmount.setBounds(199, 80, 46, 20);
		getContentPane().add(txtAmount);
		
		JLabel lblInventoryItems = new JLabel("Inventory Items:");
		lblInventoryItems.setBounds(10, 121, 95, 14);
		getContentPane().add(lblInventoryItems);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 141, 107, 48);
		getContentPane().add(scrollPane_1);
		
		JTextPane txtInvItems = new JTextPane();
		scrollPane_1.setViewportView(txtInvItems);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit = true;
				dispose();
			}
		});
		btnExit.setBounds(155, 305, 89, 23);
		getContentPane().add(btnExit);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monsterType = (String) cmbMonster.getSelectedItem();
				
				foodType = (String) cmbFood.getSelectedItem();
				minHp = Integer.parseInt(txtHp.getText());
				amountOfFood = Integer.parseInt(txtAmount.getText());
				
				levelGoals[0] = Integer.parseInt(txtAtt.getText());
				levelGoals[1] = Integer.parseInt(txtStr.getText());
				levelGoals[2] = Integer.parseInt(txtDef.getText());
				
				if ((String) cmbBank.getSelectedItem() != "None")
					bank = BANKS.get((String) cmbBank.getSelectedItem());
				else
					bank = null;
				
				lootItems = txtItems.getText().split("\n"); //Split the items by line
				invItems = txtInvItems.getText().split("\n");
				
				for (int i = 0; i < lootItems.length; i++) {
					lootItems[i] = lootItems[i].trim(); //trim all the spaces
				}
				
				for (int i = 0; i < invItems.length; i++) {
					invItems[i] = invItems[i].trim(); //trim all the spaces
				}
				
				exit = false;
				dispose();
			}
		});
		btnStart.setBounds(10, 305, 89, 23);
		getContentPane().add(btnStart);
		separator.setBounds(0, 200, 259, 2);
		getContentPane().add(separator);
		
		
		
		
		
		
	}
}
