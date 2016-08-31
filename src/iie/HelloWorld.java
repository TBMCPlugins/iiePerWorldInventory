package iie;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class HelloWorld implements CommandExecutor {
	
	HelloWorldPlugin plugin;
	public HelloWorld(HelloWorldPlugin plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command label, String command, String[] args) {
		
		
		
				
		if (sender instanceof Player){

			long currentTime = System.currentTimeMillis();
			String playername = sender.getName();
			long deathTime = 0;
			//sender.sendMessage("Playername: " + playername);
			//sender.sendMessage("Data Imported: " + HelloWorldPlugin.deathMap.get(playername));
						
			if (HelloWorldPlugin.deathMap.get(playername) != null){								
				deathTime = Long.parseLong(HelloWorldPlugin.deathMap.get(playername));
				//sender.sendMessage("DeathTime: " + deathTime);
			}else{					
				sender.sendMessage("You have never died in hardcore");				
				Player player = (Player) sender;
				World hardcoreWorld = player.getServer().getWorld("hardcore");
				Location location = new Location(hardcoreWorld, 1280, 71, -179);
				player.teleport(location);		
			}
			
			
							
			if (currentTime - deathTime >= 86400000 && deathTime != 0){				
				sender.sendMessage("You died " + (86400000 - (currentTime - deathTime) ) /3600000 + " hours ago. Ready to give it another shot?");				
				Player player = (Player) sender;
				World hardcoreWorld = player.getServer().getWorld("hardcore");
				Location location = new Location(hardcoreWorld, 1280, 71, -179);
				player.teleport(location);
			}else if(deathTime == 0){		
				sender.sendMessage("good luck");
			}else{
				sender.sendMessage("you are dead for the next " + (86400000 - (currentTime - deathTime) ) /3600000 + " hours");			
			}
			
			
			
			sender.sendMessage("Your hardcoreTimeDead score is " + String.valueOf(HelloWorldPlugin.hardcoreTimeDead.getScore(playername).getScore()));
			
					
			
		}else{			
			sender.sendMessage("You must be a player to use this command!");			
		}
		
		
		
		
		
		
		return false;
	}
	
}
