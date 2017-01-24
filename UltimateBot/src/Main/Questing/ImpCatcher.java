package Main.Questing;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;

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
		
		if (mizgog != null) 
			mizgog.interact("Talk-to");
		else {
			s.log("Can't find Mizgog. Aborting.");
			s.stop(false);
		}		
	}
	
}
