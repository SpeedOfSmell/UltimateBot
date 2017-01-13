package Main.Combat;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;

import Main.Main;
import Main.MethodProvider;
 
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
	private NPC monsterFighting;
	private Position monsterPosition;
	
	private Area bankArea; 
	private Position startingPosition;
	
	private Main s; 
	
	public Combat(Main s) { //Make a reference to the main class. Need it to access OSBot API
		this.s = s; //You can now access OSBot API by putting 's.' in front. Example: log("") becomes s.log(""). Static methods are accessed with Main. instead of s.
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
		ATTACK, EAT, ANTIBAN, CHANGESTYLES, BANK, WAIT, LOOT
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
		
		if (monsterFighting != null && monsterFighting.getHealthPercent() <= 0 && !s.inventory.isFull()) { //when the monster is dead and inv not full
			return State.LOOT;
		}
			
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
	    				Main.sleep(Main.random(800, 1000));
	    			}
					monster.interact("Attack");
								
					new ConditionalSleep(3000) { //sleep until in combat or 3 seconds 
						@Override
						public boolean condition() throws InterruptedException {
							return s.myPlayer().isUnderAttack();
						}
					}.sleep();
					
					monsterPosition = monster.getPosition();
				}
				s.camera.toEntity(monster);
				s.log("Attacking " + monsterType + ".");
				monsterFighting = monster;
				Main.sleep(Main.random(400, 600));		
				break;
				
			case EAT:	    
				if (s.tabs.getOpen() != Tab.INVENTORY)
					s.tabs.open(Tab.INVENTORY);
					
				s.inventory.interact("Eat", foodType);
				s.log("Eating " + foodType + ".");
				
				new ConditionalSleep(3000) { //sleep until hp above threshold or 3 seconds (aka done eating)
					@Override
					public boolean condition() throws InterruptedException {
						return s.getSkills().getDynamic(Skill.HITPOINTS) > minHp;
					}
				}.sleep();
				
				break;
				
			case ANTIBAN:
				MethodProvider.antiBan(combatSkills[skillToTrain]);
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
			    			Main.sleep(Main.random(700, 800));
		    			}
		    		} else {
		    			s.log("Withdrawing all " + foodType + ".");
		    			s.bank.withdrawAll(foodType);
		    			
		    			s.log("Closing bank.");
		    			s.bank.close();
		    			
		    			Main.sleep(Main.random(800, 1000));
		    			
		    			s.log("Walking back to monster area.");
		    			while(!s.map.canReach(startingPosition)) { //while player can't reach the monster area
		    				s.doorHandler.handleNextObstacle(startingPosition);
		    				Main.sleep(Main.random(800, 1000));
		    			}
		    			s.walking.walk(startingPosition);
		    		}
		    	} else {
		    		s.log("Walking to bank area.");
		    		while(!s.map.canReach(bankArea.getRandomPosition())) { //while player can't reach the bank
		    			s.doorHandler.handleNextObstacle(bankArea);
		    			Main.sleep(Main.random(800, 1000));
		    		}
		    		s.walking.walk(bankArea.getRandomPosition());
		    	}
				break;
				
			case LOOT:
				if (monsterFighting.exists()) {
					monsterPosition = monsterFighting.getPosition(); //update pos as it's dying
					
					new ConditionalSleep(1000) { //wait to die
						@Override
						public boolean condition() throws InterruptedException {
							return !monsterFighting.exists();
						}
					}.sleep();
					Main.sleep(500);
				} 	
				
				for (GroundItem item : s.groundItems.get(monsterPosition.getX(), monsterPosition.getY())) {
					if (item.getName().equals("Bones") && s.map.canReach(item)) {
						item.interact("Take");
						
						new ConditionalSleep(3000) {
							@Override
							public boolean condition() throws InterruptedException {
								return !item.exists();
							}
						}.sleep();
						s.log("Picked up " + item.getName());
						
						if (s.inventory.contains("Bones")) {
							s.inventory.getItem("Bones").interact("Bury");
							s.log("Buried bones");
						}
					}
				}				
				
				
				monsterFighting = null;
				break;
				
			case WAIT:
				Main.sleep(Main.random(800, 2000));
				break;
    	}
    	
        return Main.random(200, 300);
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
			Main.sleep(Main.random(400, 1000));
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
			
		Main.sleep(Main.random(400, 1000));			
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
    		
    		g.drawString("Run Time - " + MethodProvider.formatTime(runTime), 10, 85);
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
    
    
 
}