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
        
        Menu menu = new Menu();
        menu.setVisible(true);
        
        runningScript = menu.scriptToRun;
        log(runningScript);
        
        switch(runningScript) {
        	case "Combat":
        		combat = new Combat(this);
        		combat.onStart();
        		break;     		
        }     
        
    }
 
    @Override
    public int onLoop() throws InterruptedException {
    	switch(runningScript) {
			case "Combat":
				combat.onLoop();
    	} 
    	
        return random(200, 300);
    }
 
    @Override
    public void onExit() {
    	switch(runningScript) {
			case "Combat":
				combat.onExit();
    	}
    }
 
    @Override
    public void onPaint(Graphics2D g) {
    	switch(runningScript) {
			case "Combat":
				combat.onPaint(g);
    	}
    }
 
}