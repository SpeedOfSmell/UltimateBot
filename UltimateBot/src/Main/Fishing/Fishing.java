package Main.Fishing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import Main.Main;
import Main.MethodProvider;
import Main.Fishing.FishingMenu.FishingArea;

public class Fishing extends Script{

	private Main s;
	
	private boolean shouldBank;
	private boolean started;
	private long startTime;
	private String areaChoice;
	
	private FishingArea fishingArea;
	
	public Fishing (Main s) {
		this.s = s;
	}
	
	public void onStart() {
		FishingMenu menu = new FishingMenu();
		menu.setVisible(true);
		
		started = true;
        startTime = System.currentTimeMillis();//Gets time in milliseconds and stores it in a variable.
		
		if(menu.exit) {
        	s.log("Script aborted. Exiting.");
        	s.stop(false);
        }
		
		shouldBank = menu.shouldBank;
		fishingArea = menu.fishingArea;
		areaChoice = menu.areaChoice;
		
		s.experienceTracker.start(Skill.FISHING);
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		switch(getState()) {
			case FISH:
				@SuppressWarnings("unchecked")
				NPC spot = s.npcs.closest(new Filter<NPC>() {
					public boolean match(NPC spot) {
						return spot != null && spot.hasAction(fishingArea.fishType) && spot.hasAction(fishingArea.secondaryFishType) && s.map.canReach(spot);
					}
					
				});
				spot.interact(fishingArea.fishType);
				Main.sleep(Main.random(1000, 2000));
				break;
			case ANTIBAN:
				MethodProvider.antiBan(Skill.FISHING);
				break;
			case CLEARINVENTORY:
				if (shouldBank) {
					if (fishingArea.bankArea.contains(s.myPlayer())) {
			    		if (!s.bank.isOpen()) {
			    			@SuppressWarnings("unchecked")
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
			    			s.bank.depositAllExcept(303, 309, 314, 301, 311); //Bank everything but fishing stuff
			    			
			    			Main.sleep(Main.random(500, 1000));
			    			
			    			s.log("Closing bank.");
			    			s.bank.close();
			    			
			    			Main.sleep(Main.random(800, 1000));
			    			
			    			s.log("Walking back to fishing area.");
			    			s.getWalking().webWalk(fishingArea.area);
			    		}
			    	} else {
			    		s.log("Walking to bank area.");
			    		s.getWalking().webWalk(fishingArea.bankArea);
			    	}
				} else {
					s.inventory.dropAllExcept(303, 309, 314, 301, 311); //Drop everything but fishing stuff
				}
			case WAIT:
				Main.sleep(Main.random(800, 2000));
				break;
				
			default:
				break;
		}
		
		return 0;
	}
	
	public enum State {
		FISH, ANTIBAN, CLEARINVENTORY, WAIT
	};
	
	public State getState() {
		@SuppressWarnings("unchecked")
		NPC spot = s.npcs.closest(new Filter<NPC>() {
			public boolean match(NPC spot) {
				return spot != null && spot.hasAction(fishingArea.fishType) && spot.hasAction(fishingArea.secondaryFishType) && s.map.canReach(spot);
			}
			
		});
		
		if (s.inventory.isFull())
				return State.CLEARINVENTORY;
		else if (spot != null && !s.myPlayer().isAnimating())
			return State.FISH;
		else if (s.myPlayer().isAnimating())
			return State.ANTIBAN;
		
		return State.WAIT;	
	}
	
	 public void onPaint(Graphics2D g) {
    	if (started) {
    		long runTime = System.currentTimeMillis() - startTime;
    		
    		Font header = new Font("OCR A Extended", Font.BOLD, 18);  		
    		Font main = new Font("OCR A Extended", Font.PLAIN, 12);
    		
    		g.setFont(header);
    		g.setColor(Color.WHITE);
    	
    		g.drawString("FishThemUp", 10, 50);	
    		g.drawLine(10, 55, 130, 55);
    		
    		g.setFont(main);
    		
    		g.drawString("Run Time - " + MethodProvider.formatTime(runTime), 10, 85);
    		g.drawString("Experience/hr - " + s.experienceTracker.getGainedXPPerHour(Skill.FISHING), 10, 100);
    		g.drawString("Experience to next level - " + (s.getSkills().getExperienceForLevel(s.getSkills().getStatic(Skill.FISHING) + 1) - s.getSkills().getExperience(Skill.FISHING)), 10, 115);
    		g.drawString(Skill.FISHING + " levels gained - " + s.experienceTracker.getGainedLevels(Skill.FISHING), 10, 130);
    		g.drawString(Skill.FISHING + " experience gained - " + s.experienceTracker.getGainedXP(Skill.FISHING), 10, 145);
    		
    		g.drawString("Current State - " + getState(), 10, 245);
    		  		
    		g.drawString("Area - " + areaChoice, 10, 315); 
    		g.drawString("Banking - " + shouldBank, 10, 330);
    	}
	 }

}
