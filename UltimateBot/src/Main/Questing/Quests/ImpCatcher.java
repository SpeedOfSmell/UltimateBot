package Main.Questing.Quests;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.utility.ConditionalSleep;

import Main.Main;
import Main.MethodProvider;
import Main.Questing.Quest;

public class ImpCatcher extends Quest{

	private Main s;

	private final String itemsToBuy[][] = new String[][] {
			// id, search term, price, amount
			{"1474", "Black bead", "5000", "1"},
			{"1472", "Yellow bead", "5000", "1"},
			{"1470", "Red bead", "5000", "1"},
			{"1476", "White bead", "5000", "1"}
	};
	
	@Override
	public String[][] getItemsToBuy() {
		return itemsToBuy;
	}
	
	public ImpCatcher(Main s) {
		this.s = s;		
	}
	
	@Override
	public void execute() throws InterruptedException {
		if (s.magic.canCast(Spells.NormalSpells.LUMBRIDGE_TELEPORT)) {
			s.log("Teleporting to Lumbridge.");
			s.magic.castSpell(Spells.NormalSpells.LUMBRIDGE_TELEPORT);	
			Area lumbridgeCourtyard = new Area(3218, 3213, 3225, 3222);
			new ConditionalSleep(15000) { //wait to tp to lumby
				@Override
				public boolean condition() throws InterruptedException {
					return lumbridgeCourtyard.contains(s.myPlayer());
				}
			}.sleep();
		} else {
			s.magic.castSpell(Spells.NormalSpells.HOME_TELEPORT);
			Main.sleep(Main.random(500, 1000));
			if (s.myPlayer().isAnimating()) { //if we are indeed teleporting (spell could be on cooldown so skip this)
				s.log("Teleporting home.");
				Area lumbridgeCourtyard = new Area(3218, 3213, 3225, 3222);
				new ConditionalSleep(15000) { //wait to tp to lumby
					@Override
					public boolean condition() throws InterruptedException {
						return lumbridgeCourtyard.contains(s.myPlayer());
					}
				}.sleep();
			}
		}
		
		s.log("Walking to Wizard Mizgog in Wizard's Tower.");
		s.getWalking().webWalk(new Position(3104, 3163, 2)); 
		
		Main.sleep(Main.random(500, 1000));
		
		@SuppressWarnings("unchecked")
		NPC mizgog = s.npcs.closest(new Filter<NPC>() //get wizard mizgog
				{				          
					public boolean match (NPC npc)				          
					{				                  
						return npc != null && npc.getName().equals("Wizard Mizgog") && s.map.canReach(npc);				          
					}				  
		});
		
		if (mizgog != null) {
			s.log("Talking to Wizard Mizgog.");
			mizgog.interact("Talk-to");
		} else {
			s.log("Can't find Mizgog. Aborting.");
			s.stop(false);
		}
				
		new ConditionalSleep(2000) { //wait to talk to wizard
			@Override
			public boolean condition() throws InterruptedException {
				return s.dialogues.inDialogue();
			}
		}.sleep();
		MethodProvider.clickContinueAsNeeded();		

		Main.sleep(Main.random(1000, 1300));
		s.log("Selecting first option.");
		s.getDialogues().selectOption(1); //Select "Give me a quest please"
		
		Main.sleep(Main.random(800, 1200));
		MethodProvider.clickContinueAsNeeded();
		
		Main.sleep(Main.random(1000, 1300));
		s.log("Selecting first option.");
		s.getDialogues().selectOption(1); //Select "Ill give it a try"
		
		Main.sleep(Main.random(800, 1200));
		MethodProvider.clickContinueAsNeeded();
		
		Main.sleep(Main.random(1000, 1300));
		s.log("Talking to Wizard Mizgog.");
		mizgog.interact("Talk-to");
		
		new ConditionalSleep(2000) { //wait to talk to wizard
			@Override
			public boolean condition() throws InterruptedException {
				return s.dialogues.inDialogue();
			}
		}.sleep();
		MethodProvider.clickContinueAsNeeded();
		
		new ConditionalSleep(15000) { //sleep until given quest reward, signifying the end
			@Override
			public boolean condition() throws InterruptedException {
				return s.inventory.contains("Amulet of accuracy");
			}
		}.sleep();
		
		//quest complete
		
	}
	
}
