package Main.Questing;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import Main.Main;
import Main.MethodProvider;

public class Questing extends Script{

	private Main s;
	
	private String quest;
	private boolean shouldBuyItems;
	
	private final String quests[] = {"IMP CATCHER", "WITCH'S POTION"};
	
	public Questing(Main s) {
		this.s = s;
	}
	
	public void onStart() {
		QuestingMenu menu = new QuestingMenu();
		menu.setVisible(true);
		
		if(menu.exit) {
        	s.log("Script aborted. Exiting.");
        	s.stop(false);
        }
		
		quest = menu.quest;
		shouldBuyItems = menu.shouldBuyItems;
	}

	@Override
	public int onLoop() throws InterruptedException {
		for (String q : quests) { //For each item q in quests
			if (q.equals(quest.toUpperCase())) { //Check if it matches the current quest
				s.log("Starting " + q + ".");
				if (q.equals("IMP CATCHER")) { // If quest is imp catcher
					if (shouldBuyItems) { // If we need to buy the items 
						     // searchTerm, price, quantity 
						String itemsToBuy[][] = new String[][] {
								// search term, price, amount
								{"Black bead", "5000", "1"},
								{"Yellow bead", "5000", "1"},
								{"Red bead", "5000", "1"},
								{"White bead", "5000", "1"}
						};
						
						s.getWalking().webWalk(Banks.GRAND_EXCHANGE); // Walk to the G.E
						
						@SuppressWarnings("unchecked")
						NPC clerk = s.npcs.closest(new Filter<NPC>()
								{				          
									public boolean match (NPC npc)				          
									{				                  
										return npc != null && npc.getName().equals("Grand Exchange Clerk") && npc.hasAction("Exchange") && s.map.canReach(npc);				          
									}				  
						});
						
						clerk.interact("Exchange"); //open exchange
						
						new ConditionalSleep(2000) { //wait to open exchange booth
							@Override
							public boolean condition() throws InterruptedException {
								return s.grandExchange.isOpen();
							}
						}.sleep();
						
						for (String[] item : itemsToBuy) { //buy each item
							MethodProvider.buyItem(item[0], item[1], item[2]);
							Main.sleep(2000);
						}
						
						
					} 
				} else if (q.equals("WITCH'S POTION")) {
					
				}
			}
		}
		
		return 0;
	}
	
	
	
}
