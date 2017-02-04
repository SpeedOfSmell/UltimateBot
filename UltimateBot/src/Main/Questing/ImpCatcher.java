package Main.Questing;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.utility.ConditionalSleep;

import Main.Main;
import Main.MethodProvider;

public class ImpCatcher extends Quest{

	private Main s;
	
	private boolean shouldBuyItems;
	
	public ImpCatcher(Main s, boolean shouldBuyItems) {
		this.s = s;
		this.shouldBuyItems = shouldBuyItems;
		
	}
	
	@Override
	public String[][] getItemsToBuy() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void execute() throws InterruptedException {
		if (shouldBuyItems) { // If we need to buy the items 
		     // searchTerm, price, quantity 
			String itemsToBuy[][] = new String[][] {
					// search term, price, amount
					{"Black bead", "5000", "1"},
					{"Yellow bead", "5000", "1"},
					{"Red bead", "5000", "1"},
					{"White bead", "5000", "1"}
			};
			
			if(!MethodProvider.useGrandExchange(itemsToBuy)) { //if wasnt able to buy items
				s.log("Unable to buy items. Aborting.");
				s.stop(false); //abort script
			}					
		} 
		
		Main.sleep(Main.random(500, 1000));
		
		if (s.magic.canCast(Spells.NormalSpells.LUMBRIDGE_TELEPORT)) {
			s.log("Teleporting to Lumbridge.");
			s.magic.castSpell(Spells.NormalSpells.HOME_TELEPORT);	
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
			if (s.myPlayer().isAnimating()) { //if we are indeed teleporting
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
