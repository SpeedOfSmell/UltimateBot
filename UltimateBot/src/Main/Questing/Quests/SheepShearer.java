package Main.Questing.Quests;

import Main.Questing.Quest;

public class SheepShearer extends Quest{

	private final String itemsToBuy[][] = new String[][] {
			// id, search term, price, amount
			{"1759", "Ball of wool", "250", "20"},
	};
	
	@Override
	public String[][] getItemsToBuy() {
		return itemsToBuy;
	}
	
	@Override
	public void execute() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	

}
