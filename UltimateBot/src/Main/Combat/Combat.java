package Main.Combat;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import org.osbot.rs07.api.Skills;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.api.util.ExperienceTracker;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import Main.Main;
 
public class Combat {
 
	private String monsterType;
	private String foodType;
	private int minHp;
	private int levelGoals[];
	private boolean started = false;
	private long startTime;
	private int skillToTrain;
	private boolean shouldEat = true; //if bot should eat
	private boolean shouldBank = true; //if the bot should bank
	private Skill[] combatSkills = new Skill[3];
	
	private NPC monster;
	
	private Area bankArea; 
	private Position startingPosition;
	
	private Main s;
	
	public Combat(Main s) {
		this.s = s;
	}
	
    public void onStart() throws InterruptedException {
        s.log("Starting Combat.");
        
        CombatMenu menu = new CombatMenu();
        menu.setVisible(true);
        
        if(menu.exit) {
        	s.log("Script aborted. Exiting.");
        	s.stop(false);
        }
        
        started = true;
        startTime = System.currentTimeMillis();//Gets time in milliseconds and stores it in a variable.
        
        s.log("BeatThemUp running.");
        
        combatSkills[0] = Skill.ATTACK; //
        combatSkills[1] = Skill.STRENGTH; //
        combatSkills[2] = Skill.DEFENCE; //
        
        monsterType = menu.monsterType;
        foodType = menu.foodType;
        minHp = menu.minHp;
        levelGoals = menu.levelGoals;
        bankArea = menu.bank;
        
        if (foodType == "None")
        	shouldEat = false;
        
        if (bankArea != null && shouldEat)
        	shouldBank = true;
        else 
        	shouldBank = false;
        
        skillToTrain = determineSkill();
        changeStyle(combatSkills[skillToTrain]);
        
        startingPosition = s.myPlayer().getPosition();
    }
    
    public enum State {
		ATTACK, EAT, ANTIBAN, CHANGESTYLES, BANK, WAIT
	};
	
	@SuppressWarnings("unchecked")
	public State getState() {
		monster = s.npcs.closest(new Filter<NPC>()
				{				          
					public boolean match (NPC npc)				          
					{				                  
						return npc != null && npc.getName().contains(monsterType) && npc.getHealthPercent() > 0 && npc.isAttackable() && npc.hasAction("Attack") && s.map.canReach(npc);				          
					}				  
		});	
		if (s.getSkills().getDynamic(Skill.HITPOINTS) <= minHp && shouldEat) //If below minimum hp and bot set to eat
			return State.EAT;
		
		if (!s.myPlayer().isUnderAttack() && !s.myPlayer().isAnimating()) { //If player not busy
			if (!(s.inventory.contains(foodType)) && shouldBank) //If out of food and supposed to eat
				return State.BANK;		
			if (monster != null) //If monster exists
				return State.ATTACK;
		} 	
		
		if (s.getSkills().getStatic(combatSkills[skillToTrain]) >= levelGoals[skillToTrain]) { //If the goal has been reached
			s.log("Goal reached. Changing styles.");
			return State.CHANGESTYLES;
		}
		
		for (int i = 0; i < 3; i++) {
			if(s.getSkills().getStatic(combatSkills[skillToTrain]) - s.getSkills().getStatic(combatSkills[i]) >= 5 && s.getSkills().getStatic(combatSkills[i]) <= levelGoals[i]) { //Check the three combat skills to make sure they're not behind if they dont meet the goal
				s.log("Level difference over 5. Changing styles.");
				return State.CHANGESTYLES;
			}
		}
		
		if (s.myPlayer().isUnderAttack() || s.myPlayer().isAnimating())
			return State.ANTIBAN;
		
		return State.WAIT;
		
	}
 
    public int onLoop() throws InterruptedException {
    	switch (getState()) {
			case ATTACK:
				if (monster != null) {	
					while(!s.map.canReach(monster.getPosition())) { //while player can't reach the npc
						s.log("Handling door");
		    			s.doorHandler.handleNextObstacle(monster);
	    				s.sleep(s.random(800, 1000));
	    			}
					monster.interact("Attack");
				}
				s.camera.toEntity(monster);
				s.log("Attacking " + monsterType + ".");
				s.sleep(s.random(400, 600));		
				break;
				
			case EAT:	    
				if (s.tabs.getOpen() != Tab.INVENTORY)
					s.tabs.open(Tab.INVENTORY);
					
				s.inventory.interact("Eat", foodType);
				s.log("Eating " + foodType + ".");
				s.sleep(s.random(400, 600));
				break;
				
			case ANTIBAN:
				antiBan();
				s.sleep(s.random(700, 2200));
				if (s.tabs.getOpen() != Tab.INVENTORY && s.random(1, 2) == 1) {
						s.log("Returning to inventory.");
						s.tabs.open(Tab.INVENTORY);
				}
				break;
				
			case CHANGESTYLES:
				skillToTrain = determineSkill();
		        changeStyle(combatSkills[skillToTrain]);
				break;
				
			case BANK:
				if (bankArea.contains(s.myPlayer())) {
		    		if (!s.bank.isOpen()) {
		    			Entity bankBooth = s.objects.closest("Bank booth");
		    			if (bankBooth != null) {
			    			bankBooth.interact("Bank");
			    			s.log("Opening bank booth.");
			    			s.sleep(s.random(700, 800));
		    			}
		    		} else {
		    			s.log("Withdrawing all " + foodType + ".");
		    			s.bank.withdrawAll(foodType);
		    			
		    			s.log("Closing bank.");
		    			s.bank.close();
		    			
		    			s.sleep(s.random(800, 1000));
		    			
		    			s.log("Walking back to monster area.");
		    			while(!s.map.canReach(startingPosition)) { //while player can't reach the monster area
		    				s.doorHandler.handleNextObstacle(startingPosition);
		    				s.sleep(s.random(800, 1000));
		    			}
		    			s.walking.walk(startingPosition);
		    		}
		    	} else {
		    		s.log("Walking to bank area.");
		    		while(!s.map.canReach(bankArea.getRandomPosition())) { //while player can't reach the bank
		    			s.doorHandler.handleNextObstacle(bankArea);
		    			s.sleep(s.random(800, 1000));
		    		}
		    		s.walking.walk(bankArea.getRandomPosition());
		    	}
				break;
				
			case WAIT:
				s.sleep(s.random(800, 2000));
				break;
    	}
    	
        return s.random(200, 300);
    }
    
    public void antiBan() throws InterruptedException {
    	int rand = s.random(1, 40);
    	
    	switch (rand) {	
	    	case 1:
				s.camera.movePitch(50);
				s.log("Rotating camera. (Antiban)");
				break;
	    	case 2:
	    		s.camera.toBottom();
	    		s.log("Moving camera to bottom. (Antiban)");
				break;
			case 3:
				if (s.tabs.getOpen() != Tab.SKILLS)
					s.tabs.open(Tab.SKILLS);
								
				s.getSkills().hoverSkill(combatSkills[skillToTrain]);
				s.log("Hovering over skill being trained. (Antiban)");	
				
				s.sleep(s.random(400, 600));
				s.tabs.open(Tab.INVENTORY);
				break;
			case 4:
				s.log("Opening combat tab. (Antiban)");
				if (s.tabs.getOpen() != Tab.ATTACK)
					s.tabs.open(Tab.ATTACK);
							
				break;
			case 5:
				if (s.tabs.getOpen() != Tab.QUEST)
					s.tabs.open(Tab.QUEST);
				
				s.mouse.move(600, 300); //move mouse into quest tab so it can scroll
				switch(s.random(1, 2)){
					case 1: 						
						for(int i = 0; i < s.random(1,7); i++) {
							s.mouse.scrollUp();
							s.sleep(s.random(100,400));
						}
						s.log("Scrolling up in quest tab random amount of times. (Antiban)");
						break;
					case 2:						
						for(int i = 0; i < s.random(1,7); i++) {
							s.mouse.scrollDown();
							s.sleep(s.random(100,400));
						}
						s.log("Scrolling down in quest tab random amount of times. (Antiban)");
						break;
				}	
				s.sleep(s.random(1000, 2000));

				break;
			case 6:
				s.camera.movePitch(50 + s.random(1, 70));
				s.log("Moving camera pitch between 50 - 120. (Antiban)");
				break;
			case 7:
				s.camera.movePitch(150 + (s.random(30, 70)));
				s.log("Moving camera pitch between 180 - 120. (Antiban)");
				break;
			case 8:
				s.log("Opening clan chat tab. (Antiban)");
				if (s.tabs.getOpen() != Tab.CLANCHAT)
					s.tabs.open(Tab.CLANCHAT);	
				break;
			case 9:
				s.log("Opening friends tab. (Antiban)");
				if (s.tabs.getOpen() != Tab.FRIENDS)
					s.tabs.open(Tab.FRIENDS);		
				break;
			case 10:
				s.mouse.moveOutsideScreen();
				s.log("Moving mouse out of screen. (Antiban)");
				s.sleep(s.random(5000, 10000));
				break;
			/*case 11:
				boolean examined = false;
				List<RS2Object> allEntities = new ArrayList<RS2Object>();
				allEntities = objects.getAll();
				
				do {
					RS2Object obj = allEntities.get(random(0, allEntities.size() - 1));
					log("Name: " + obj.getName());
					if (obj.getName() != null) {
						obj.interact("Examine");
						examined = true;
						log(obj.getName() + " examined. (Antiban)");
					}
				} while (!examined);
				break; */
			case 12:
				s.log("Moving mouse randomly. (Antiban)");			
				for(int i = 0; i < s.random(1,5); i++) {
					s.mouse.moveRandomly();
				}			
				break;
    	} 		
    }
    
    private int determineSkill() {
    	int levels[] = new int[3];
    	levels[0] = s.getSkills().getStatic(Skill.ATTACK);
    	levels[1] = s.getSkills().getStatic(Skill.STRENGTH);
    	levels[2] = s.getSkills().getStatic(Skill.DEFENCE);
    	
		if(levels[0] >= levelGoals[0]) //if at the goal
			levels[0] = 99; //set it to 99 so it doesn't get picked to train
		if(levels[1] >= levelGoals[1]) //if at the goal
			levels[1] = 99; //set it to 99 so it doesn't get picked to train
		if(levels[2] >= levelGoals[2]) //if at the goal
			levels[2] = 99; //set it to 99 so it doesn't get picked to train
		
		boolean all99 = true; //if all 99, that means all goals are obtained
		
		for(int level : levels) {
			if (level != 99)
				all99 = false;
		}
		
		if (all99) { //Nothing to train. Abort.
			s.log("All goals are met. Aborting.");
			s.stop(false);
		}
		
		return findLowest(levels);
    }
    
    private int findLowest(int levels[]) {
    	int lowest = 0;
    	int lowestValue = 99;
    	
    	for(int i = 0; i < 3; i ++) {
    		if (levels[i] < lowestValue) {
    			lowestValue = levels[i];
    			lowest = i;
    		}
    	}
    		
		return lowest;    	
    }
    
    private void changeStyle(Skill style) throws InterruptedException {
    	if (s.tabs.getOpen() != Tab.ATTACK) {
    		s.tabs.open(Tab.ATTACK);
			s.sleep(s.random(400, 1000));
    	}
		
		switch(style) {				
			case ATTACK:
				s.widgets.get(593, 3).interact(); //switch to attack
				s.log("Style switched to attack.");
				break;		
			case DEFENCE:
				s.widgets.get(593, 15).interact(); //switch to defense
				s.log("Style switched to defence.");
				break;
			default:
				s.widgets.get(593, 7).interact(); //switch to strength
				s.log("Style switched to strength.");
				break;	
		}
			
		s.sleep(s.random(400, 1000));			
		s.tabs.open(Tab.INVENTORY);
		
		s.experienceTracker.start(style); //start tracking xp for new skill
    }
 
    public void onExit() {
    	s.log("Exiting BeatThemUp.");
    }
 
    public void onPaint(Graphics2D g) {
    	if (started) {
    		long runTime = System.currentTimeMillis() - startTime;
    		
    		Font header = new Font("OCR A Extended", Font.BOLD, 18);  		
    		Font main = new Font("OCR A Extended", Font.PLAIN, 12);
    		
    		g.setFont(header);
    		g.setColor(Color.WHITE);
    	
    		g.drawString("BeatThemUp", 10, 50);	
    		g.drawLine(10, 55, 130, 55);
    		
    		g.setFont(main);
    		
    		g.drawString("Run Time - " + formatTime(runTime), 10, 85);
    		g.drawString("Experience/hr - " + s.experienceTracker.getGainedXPPerHour(combatSkills[skillToTrain]), 10, 100);
    		g.drawString("Experience to next level - " + (s.getSkills().getExperienceForLevel(s.getSkills().getStatic(combatSkills[skillToTrain]) + 1) - s.getSkills().getExperience(combatSkills[skillToTrain])), 10, 115);
    		g.drawString(combatSkills[skillToTrain] + " levels gained - " + s.experienceTracker.getGainedLevels(combatSkills[skillToTrain]), 10, 130);
    		g.drawString(combatSkills[skillToTrain] + " experience gained - " + s.experienceTracker.getGainedXP(combatSkills[skillToTrain]), 10, 145);
    		
    		g.drawString("Current State - " + getState(), 10, 245);
    		g.drawString("Currently Training - " + combatSkills[skillToTrain], 10, 260);
    		  		
    		g.drawString("Monster - " + monsterType, 10, 285); 
    		g.drawString("Food - " + foodType, 10, 300);
    		g.drawString("Banking - " + shouldBank, 10, 315);
    		g.drawString("Minimum HP - " + minHp, 10, 330);
    		
    		/*
    		g.drawString("Attack goal   - " + levelGoals[0], 370, 300); 
    		g.drawString("Strength goal - " + levelGoals[1], 370, 315); 
    		g.drawString("Defense goal  - " + levelGoals[2], 370, 330); 
    		*/
    		
    		g.drawString("Experience to " + levelGoals[0] + " attack", 325, 255); 
    		g.drawString("Experience to " + levelGoals[1] + " strength", 325, 285); 
    		g.drawString("Experience to " + levelGoals[2] + " defense", 325, 315); 
    		
    		g.setColor(Color.YELLOW);
    		g.drawString(Integer.toString(Math.max(0, (s.getSkills().getExperienceForLevel(levelGoals[0]) - s.getSkills().getExperience(Skill.ATTACK)))), 355, 270); //uses max to display 0 if goal met
    		g.drawString(Integer.toString(Math.max(0, (s.getSkills().getExperienceForLevel(levelGoals[1]) - s.getSkills().getExperience(Skill.STRENGTH)))), 355, 300); 
    		g.drawString(Integer.toString(Math.max(0, (s.getSkills().getExperienceForLevel(levelGoals[2]) - s.getSkills().getExperience(Skill.DEFENCE)))), 355, 330); 
    	}
    }
    
    public final String formatTime(final long ms){//Converts milliseconds to hh:mm:ss. It will also leave out hh if its is 0.
        long s = ms / 1000, m = s / 60, h = m / 60, d = h / 24;
        s %= 60; m %= 60; h %= 24;

        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
               h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
               String.format("%02d:%02d", m, s);
    }
 
}