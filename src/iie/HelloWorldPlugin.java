package iie;

import java.util.AbstractMap;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;


public class HelloWorldPlugin extends JavaPlugin {
	
	public static Scoreboard board;
	public static Objective hardcoreTimeDead;	
	public static AbstractMap<String,String> deathMap = new HashMap<String,String>();
	
	public void onEnable(){
		
		
		try{
			board.getObjective("hardcoreTimeDead");
			}catch (NullPointerException e){
				hardcoreTimeDead = board.registerNewObjective("hardcoreTimeDead", "dummy");
			}finally{
				hardcoreTimeDead = board.getObjective("hardcoreTimeDead");
			}
		
		
		board = Bukkit.getServer().getScoreboardManager().getMainScoreboard();			
		if (board.getObjective("hardcoreTimeDead") != null){
			hardcoreTimeDead = board.getObjective("hardcoreTimeDead");
		}else{
			hardcoreTimeDead = board.registerNewObjective("hardcoreTimeDead", "dummy");

		}		
		
		registerCommands();
		getServer().getPluginManager().registerEvents(new DeathListener(this), this);
		
	}
	public void registerCommands(){
		getCommand("HelloWorld").setExecutor(new HelloWorld(this));
	}

}

//try{
//	board.getObjective("hardcoreTimeDead");
//}catch (IllegalArgumentException e){
//	hardcoreTimeDead = board.registerNewObjective("hardcoreTimeDead", "dummy");
//}finally{
//	hardcoreTimeDead = board.getObjective("hardcoreTimeDead");
//}