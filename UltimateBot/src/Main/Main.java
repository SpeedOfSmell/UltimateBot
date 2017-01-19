package Main;
import java.awt.Graphics2D;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import Main.Combat.Combat;
import Main.Woodcutting.Woodcutting;
 
@ScriptManifest(author = "SpeedOfSmell", info = "", name = "UltimateBot", version = 0, logo = "")
public class Main extends Script {
	
	private String runningScript;
	private Combat combatScript;
	private Woodcutting woodcuttingScript;
 
    @Override
    public void onStart() throws InterruptedException {
        log("Let's get started!");
        
        Menu menu = new Menu(); //Open up the menu to select script to run. *IMPORTANT* Make sure the menu is set to be modal (refer to Menu class)
        menu.setVisible(true);
        
        MethodProvider.s = this; //For accesss to OSBot API
        
        try {
	        runningScript = menu.scriptToRun;
	           
	        switch(runningScript) { //Grab the script to run
	        	case "Combat":
	        		combatScript = new Combat(this); //Pass a reference to this class. Refer to constructor for more information
	        		combatScript.onStart(); //Run the combat scripts onStart method
	        		break;     	
	        	case "Woodcutting":
	        		woodcuttingScript = new Woodcutting(this); //Pass a reference to this class. Refer to constructor for more information
	        		woodcuttingScript.onStart(); //Run the woodcutting scripts onStart method
	        		break;     	
	        }      
        } catch (Exception e) {
        	log(e.getMessage());
        }
    }
 
    @Override
    public int onLoop() throws InterruptedException {
    	switch(runningScript) {
			case "Combat":
				combatScript.onLoop(); //Run the combat script's onLoop method
				break;
			case "Woodcutting":
				woodcuttingScript.onLoop();
				break;
    	} 
    	
        return 0;
    }
 
    @Override
    public void onExit() {
    	switch(runningScript) {
			case "Combat":
				combatScript.onExit(); //Run the combat script's onExit method
				break;
			case "Woodcutting":
				woodcuttingScript.onExit();
				break;
				
    	}
    }
 
    @Override
    public void onPaint(Graphics2D g) {
    	switch(runningScript) {
			case "Combat":
				combatScript.onPaint(g); //Run the combat script's onPaint method
				break;
			case "Woodcutting":
				woodcuttingScript.onPaint(g);
				break;
    	}
    }
 
}