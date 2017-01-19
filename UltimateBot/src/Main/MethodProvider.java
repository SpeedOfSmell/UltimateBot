package Main;

import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;

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
	
	//buys item from g.e **code by OSBot user uyfgfarOS**
	public static void buyItem(String searchTerm, String price, String quantityAmount) throws InterruptedException {
		s.grandExchange.collect();
		Main.sleep(Main.random(500, 1000));
		RS2Widget buyButton = s.widgets.get(465, 7, 26);
		if (buyButton != null)
		{
			buyButton.interact();
			Main.sleep(Main.random(500, 1000));
			s.keyboard.typeString(searchTerm);
			Main.sleep(Main.random(500, 1000));
			RS2Widget buySelection = s.widgets.get(162, 38, 1);
			
			if (buySelection != null)
				{
				buySelection.interact();
				Main.sleep(Main.random(700, 1300));
				RS2Widget buyPrice = s.widgets.get(465, 24, 12);
				RS2Widget quantity = s.widgets.get(465, 24, 7);
				
				if (buyPrice != null)
				{
					buyPrice.interact();
					Main.sleep(Main.random(500, 1000));	
					s.keyboard.typeString(price);
					s.keyboard.pressKey(13);
					Main.sleep(Main.random(500, 1200));	
					
					if (quantity != null)
					{
						
						quantity.interact();
						Main.sleep(Main.random(600, 700));	
						s.keyboard.typeString(quantityAmount);
						s.keyboard.pressKey(13);
						Main.sleep(Main.random(400, 1000));
						s.grandExchange.confirm();

					}			
				}
			}
		} 
	}
}
