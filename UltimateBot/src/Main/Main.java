package Main;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
 





import Main.Combat.Combat;
import Main.Combat.CombatMenu;

import java.awt.*;
 
@ScriptManifest(author = "SpeedOfSmell", info = "", name = "UltimateBot", version = 0, logo = "")
public class Main extends Script {
	
	private String runningScript;
	private Combat combat;
 
    @Override
    public void onStart() throws InterruptedException {
        log("Let's get started!");
        
        Menu menu = new Menu(); //Open up the menu to select script to run. *IMPORTANT* Make sure the menu is set to be modal (refer to Menu class)
        menu.setVisible(true);
        
        switch(runningScript = menu.scriptToRun) { //Grab the script to run
        	case "Combat":
        		combat = new Combat(this); //Pass a reference to this class. Refer to constructor for more information
        		combat.onStart(); //Run the combat scripts onStart method
        		break;     		
        }     
        
    }
 
    @Override
    public int onLoop() throws InterruptedException {
    	switch(runningScript) {
			case "Combat":
				combat.onLoop(); //Run the combat script's onLoop method
    	} 
    	
        return 0;
    }
 
    @Override
    public void onExit() {
    	switch(runningScript) {
			case "Combat":
				combat.onExit(); //Run the combat script's onExit method
    	}
    }
 
    @Override
    public void onPaint(Graphics2D g) {
    	switch(runningScript) {
			case "Combat":
				combat.onPaint(g); //Run the combat script's onPaint method
    	}
    }
 
}