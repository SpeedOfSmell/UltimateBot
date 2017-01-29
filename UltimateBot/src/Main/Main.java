package Main;
import java.awt.Graphics2D;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import Main.Combat.Combat;
import Main.Fishing.Fishing;
import Main.Mining.Mining;
import Main.Questing.Questing;
import Main.Woodcutting.Woodcutting;
 
@ScriptManifest(author = "SpeedOfSmell", info = "", name = "UltimateBot", version = 0, logo = "")
public class Main extends Script {
	
	private String runningScript;
	private Script script;
 
    @Override
    public void onStart() throws InterruptedException {
        log("Let's get started!");
        
        Menu menu = new Menu(); //Open up the menu to select script to run. *IMPORTANT* Make sure the menu is set to be modal (refer to Menu class)
        menu.setVisible(true);
        
        MethodProvider.s = this; //For accesss to OSBot API
      
        runningScript = menu.scriptToRun;
           
        switch(runningScript) { //Grab the script to run
        	case "Combat":
        		script = new Combat(this); //Pass a reference to this class. Refer to constructor for more information
        		break;     	
        	case "Woodcutting":
        		script = new Woodcutting(this); //Pass a reference to this class. Refer to constructor for more information
        		break; 
        	case "Questing":
        		script = new Questing(this);
        		break;
        	case "Fishing":
        		script = new Fishing(this);
        		break;
        	case "Mining":
        		script = new Mining(this);
        		break;
        }
        
        log("Starting " + runningScript);
        script.onStart(); // Run the script's onStart method
    }
 
    @Override
    public int onLoop() throws InterruptedException {
    	script.onLoop();  	
        return random(300, 600);
    }
 
    @Override
    public void onExit() throws InterruptedException {
    	script.onExit();
    }
 
    @Override
    public void onPaint(Graphics2D g) {
    	script.onPaint(g);
    }
 
}