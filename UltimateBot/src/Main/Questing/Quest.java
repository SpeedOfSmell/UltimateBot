package Main.Questing;

public abstract class Quest {
	
	public abstract String[][] getItemsToBuy();
	
	public abstract void execute() throws InterruptedException;
	
}
