package Main;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;

public class MethodProvider {
	
	public static Main s; //This is set in the main class
	
	public static void antiBan(Skill skill) throws InterruptedException {
		int rand = Main.random(1, 40);
    	
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
								
				s.getSkills().hoverSkill(skill);
				s.log("Hovering over skill being trained. (Antiban)");	
				
				Main.sleep(Main.random(400, 600));
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
				switch(Main.random(1, 2)){
					case 1: 						
						for(int i = 0; i < Main.random(1,7); i++) {
							s.mouse.scrollUp();
							Main.sleep(Main.random(100,400));
						}
						s.log("Scrolling up in quest tab random amount of times. (Antiban)");
						break;
					case 2:						
						for(int i = 0; i < Main.random(1,7); i++) {
							s.mouse.scrollDown();
							Main.sleep(Main.random(100,400));
						}
						s.log("Scrolling down in quest tab random amount of times. (Antiban)");
						break;
				}	
				Main.sleep(Main.random(1000, 2000));

				break;
			case 6:
				s.camera.movePitch(50 + Main.random(1, 70));
				s.log("Moving camera pitch between 50 - 120. (Antiban)");
				break;
			case 7:
				s.camera.movePitch(150 + (Main.random(30, 70)));
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
				Main.sleep(Main.random(5000, 10000));
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
				for(int i = 0; i < Main.random(1,5); i++) {
					s.mouse.moveRandomly();
				}			
				break;
    	} 		
		Main.sleep(Main.random(700, 2200));
		if (s.tabs.getOpen() != Tab.INVENTORY && Main.random(1, 2) == 1) {
				s.log("Returning to inventory.");
				s.tabs.open(Tab.INVENTORY);
		}
	}
	
	public static final String formatTime(final long ms){//Converts milliseconds to hh:mm:ss. It will also leave out hh if its is 0.
        long s = ms / 1000, m = s / 60, h = m / 60, d = h / 24;
        s %= 60; m %= 60; h %= 24;

        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
               h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
               String.format("%02d:%02d", m, s);
    }
	
	public static boolean isStackable(final GroundItem item) {
		return item.getDefinition().getNotedId() == -1; //All unstackable items should have unnoted id of -1
	}
	
	//buys item from g.e **code by OSBot user uyfgfarOS commented by Vlad**
	public static void buyItem(String searchTerm, String price, String quantityAmount) throws InterruptedException {
		s.grandExchange.collect(); //free up exchange slots by collecting accepted offers
		Main.sleep(Main.random(500, 1000));
		RS2Widget buyButton = s.widgets.get(465, 7, 26); //the buy button to click to search for item to buy
		if (buyButton != null)
		{
			buyButton.interact(); //click it
			Main.sleep(Main.random(500, 1000));
			s.keyboard.typeString(searchTerm); //type the search term
			Main.sleep(Main.random(500, 1000));
			RS2Widget buySelection = s.widgets.get(162, 38, 1); //the first item in list
			
			if (buySelection != null)
				{
				buySelection.interact(); // click that first item
				Main.sleep(Main.random(700, 1300));
				RS2Widget buyPrice = s.widgets.get(465, 24, 12); //button to type in buy price **if bot writes to chatbox instead of buy price, make sure the widget number hasn't changed**
				RS2Widget quantity = s.widgets.get(465, 24, 7); //button to type in quantity **same applies as above**
				
				if (buyPrice != null)
				{
					buyPrice.interact(); //click on the buy price button
					Main.sleep(Main.random(500, 1000));	
					s.keyboard.typeString(price); //type the buy price
					s.keyboard.pressKey(13); //press enter
					Main.sleep(Main.random(500, 1200));	
					
					if (quantity != null)
					{
						
						quantity.interact(); //click on quantity button
						Main.sleep(Main.random(600, 700));	
						s.keyboard.typeString(quantityAmount);	//type quanitity
						s.keyboard.pressKey(13); //press enter
						Main.sleep(Main.random(400, 1000));
						s.grandExchange.confirm(); //confirm buy offer

					}			
				}
			}
		} 
	}
	
	//Walks to the Grand Exchange and buys each item in the array passed.
	//Each String array in the array itemsToBuy is expected to follow this format: {Item Name, Price to Buy at, QuantityNeeded}
	//Returns: if bot was able to buy required items
	public static boolean useGrandExchange(String itemsToBuy[][]) throws InterruptedException {
		s.log("Walking to Grand Exchange");
		s.getWalking().webWalk(Banks.GRAND_EXCHANGE); // Walk to the G.E
		
		int coinsNeeded = 0; //will be total amount needed to buy all the items
		for (String[] item : itemsToBuy) //iterate through list of items to buy
			coinsNeeded += Integer.parseInt(item[1]) * Integer.parseInt(item[2]); //item[1] holds price and item[2] holds quantity needed so multiply to find total cost
		
		if (!invContainsItem("Coins", coinsNeeded)) { //if we dont have enough coins
			s.log("Not enough coins in inventory.");
			@SuppressWarnings("unchecked")
			NPC banker = s.npcs.closest(new Filter<NPC>() //get the closest banker
					{				          
						public boolean match (NPC npc)				          
						{				                  
							return npc != null && npc.getName().equals("Banker") && npc.hasAction("Bank") && s.map.canReach(npc);				          
						}				  
			});
			
			s.log("Opening bank");
			banker.interact("Bank"); //open the bank
			
			new ConditionalSleep(2000) { //wait to open bank booth
				@Override
				public boolean condition() throws InterruptedException {
					return s.bank.isOpen();
				}
			}.sleep();
			
			s.log("Withdrawing " + coinsNeeded + " coins.");
			s.bank.withdraw("Coins", coinsNeeded); //get the amount of coins needed
		}
		
		if (!invContainsItem("Coins", coinsNeeded))  //if at this point we still dont have enough coins
			return false; //return false to exit method because we dont have enough coins to buy items
			
		s.log("Have coins. Accessing Grand Exchange.");
		
		@SuppressWarnings("unchecked")
		NPC clerk = s.npcs.closest(new Filter<NPC>() //get the closest grand exchange clerk
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
		
		s.grandExchange.collect();
		s.log("Items bought.");
		
		return true;
	}
	
	public static boolean invContainsItem(String itemName, int quantityNeeded) {
		int quantityCounted = 0;
		for (Item invItem : s.inventory.getItems()) { // for each item in the inventory
			if (invItem != null && invItem.getName().equals(itemName)) { //if the item has the name passed
				quantityCounted += invItem.getAmount(); //add to the amount counted the amount of the stack of items
				
				if (quantityCounted >= quantityNeeded) //if we have enough, stop counting and return true
					return true;
								
				if (invItem.getAmount() > 1) //if it's a stack, return false because there won't be another stack of that item
											 //and if that stack was enough, it shouldnt get to this point in the code
					return false;			
			}
		}
		
		return false; //it should have already returned true if it contained the required amount
	}
	
	public static boolean invContainsItem(String itemName) { //overload for above method. this makes the quantity optional
		return invContainsItem(itemName, 1); //call above method with a quantity of 1
	}
}
