package Main.Woodcutting;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import Main.Main;
import Main.MethodProvider;

public class Woodcutting extends Script{

	private Main s;
	
	private boolean started;
	private long startTime;
	
	private boolean shouldBank;
	private boolean shouldWorldHop;
	private Area area;
	private Area bankArea;
	private String treeType;
	private String woodcuttingArea;
	
	public Woodcutting(Main s) {  //Make a reference to the main class. Need it to access OSBot API
		this.s = s; //You can now access OSBot API by putting 's.' in front. Example: log("") becomes s.log(""). Static methods are accessed with Main. instead of s.
	}
	
	public void onStart() {
		WoodcuttingMenu menu = new WoodcuttingMenu();
		menu.setVisible(true); // If program doesnt wait for this menu to close, make sure the Jdialog menu above is set to modal = true
		
		if(menu.exit) {
        	s.log("Script aborted. Exiting.");
        	s.stop(false);
        }
		
		started = true;
        startTime = System.currentTimeMillis();//Gets time in milliseconds and stores it in a variable.
        
        s.log("Woodcutting script running.");
        
        shouldBank = menu.shouldBank;
        shouldWorldHop = menu.shouldWorldHop;
        area = menu.area;
        treeType = menu.treeType;
        bankArea = menu.bankArea;    
        woodcuttingArea = menu.woodcuttingArea;
        
        s.experienceTracker.start(Skill.WOODCUTTING);
	}
	
	@SuppressWarnings("unchecked")
	public int onLoop() throws InterruptedException{
		switch(getState()) {
			case WOODCUT:
				Entity tree = s.objects.closest(area, treeType);
				tree.interact("Chop down");
				Main.sleep(Main.random(1000, 2000));
				break;
			case ANTIBAN:
				MethodProvider.antiBan(Skill.WOODCUTTING);
				break;
			case CLEARINVENTORY:
				if (shouldBank) {
					if (bankArea.contains(s.myPlayer())) {
			    		if (!s.bank.isOpen()) {
			    			Entity bankBooth = s.objects.closest(new Filter<RS2Object>() {

								@Override
								public boolean match(RS2Object booth) {
									return booth.getName().equals("Bank booth") && booth != null && booth.hasAction("Bank") && s.map.canReach(booth);
								}
			    				
			    			});
			    			
			    			if (bankBooth != null) {
			    				s.log("Opening bank booth.");
				    			bankBooth.interact("Bank");
				    						    			
				    			new ConditionalSleep(2000) { //wait to open bank booth
									@Override
									public boolean condition() throws InterruptedException {
										return s.bank.isOpen();
									}
								}.sleep();
			    			}
			    		} else {
			    			s.log("Depositing inventory.");
			    			s.bank.depositAllExcept(1351, 1355, 1357, 1359, 6739); //Bank everything but axes
			    			
			    			Main.sleep(Main.random(500, 1000));
			    			
			    			s.log("Closing bank.");
			    			s.bank.close();
			    			
			    			Main.sleep(Main.random(800, 1000));
			    			
			    			s.log("Walking back to woodcutting area.");
			    			s.getWalking().webWalk(area);
			    		}
			    	} else {
			    		s.log("Walking to bank area.");
			    		s.getWalking().webWalk(bankArea);
			    	}
				} else {
					s.inventory.dropAllExcept(1351, 1355, 1357, 1359, 6739); //Drop all except axes
				}
			case WAIT:
				Main.sleep(Main.random(800, 2000));
				break;
				
			case WORLDHOP:
				break;	
				
			default:
				break;
		}
		
		return 0;
	}
	
	public enum State {
		WOODCUT, ANTIBAN, CLEARINVENTORY, WAIT, WORLDHOP
	};
	
	public State getState() {
		Entity tree = s.objects.closest(area, treeType);
		
		if (s.inventory.isFull())
				return State.CLEARINVENTORY;
		else if (tree != null && !s.myPlayer().isAnimating())
			return State.WOODCUT;
		else if (s.myPlayer().isAnimating())
			return State.ANTIBAN;
		
		return State.WAIT;	
	}
	
	public void onExit(){
		s.log("Exiting woodcutting script.");
	}
	
	 public void onPaint(Graphics2D g) {
	    	if (started) {
	    		long runTime = System.currentTimeMillis() - startTime;
	    		
	    		Font header = new Font("OCR A Extended", Font.BOLD, 18);  		
	    		Font main = new Font("OCR A Extended", Font.PLAIN, 12);
	    		
	    		g.setFont(header);
	    		g.setColor(Color.WHITE);
	    	
	    		g.drawString("ChopThemUp", 10, 50);	
	    		g.drawLine(10, 55, 130, 55);
	    		
	    		g.setFont(main);
	    		
	    		g.drawString("Run Time - " + MethodProvider.formatTime(runTime), 10, 85);
	    		g.drawString("Experience/hr - " + s.experienceTracker.getGainedXPPerHour(Skill.WOODCUTTING), 10, 100);
	    		g.drawString("Experience to next level - " + (s.getSkills().getExperienceForLevel(s.getSkills().getStatic(Skill.WOODCUTTING) + 1) - s.getSkills().getExperience(Skill.WOODCUTTING)), 10, 115);
	    		g.drawString(Skill.WOODCUTTING + " levels gained - " + s.experienceTracker.getGainedLevels(Skill.WOODCUTTING), 10, 130);
	    		g.drawString(Skill.WOODCUTTING + " experience gained - " + s.experienceTracker.getGainedXP(Skill.WOODCUTTING), 10, 145);
	    		
	    		g.drawString("Current State - " + getState(), 10, 245);
	    		  		
	    		g.drawString("Area - " + woodcuttingArea, 10, 300); 
	    		g.drawString("Banking - " + shouldBank, 10, 315);
	    		g.drawString("World Hop - " + shouldWorldHop, 10, 330);
	    		
	    	}
	 }
}
