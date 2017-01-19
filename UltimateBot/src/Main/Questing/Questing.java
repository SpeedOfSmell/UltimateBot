package Main.Questing;

import org.osbot.rs07.script.Script;

import Main.Main;

public class Questing extends Script{

	private Main s;
	
	private String quest;
	private boolean shouldBuyItems;
	
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
		
		return 0;
	}
	
	
}
