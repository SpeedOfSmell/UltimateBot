package Main.Questing;

import org.osbot.rs07.script.Script;

import Main.Main;

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
				Quest questToDo = null;
				switch (q) { //check the quest to do
					case "IMP CATCHER":
						questToDo = new ImpCatcher(s, shouldBuyItems);
						break;
				}
				if (questToDo != null)
					questToDo.execute();
				
				break;
			}
		}
		
		s.log("Quest complete. Aborting.");
		s.stop(false);
		return 0;
	}
	
}
