package iie;

import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class DeathListener implements Listener {
	
	HelloWorldPlugin plugin;
	public DeathListener(HelloWorldPlugin plugin){
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onHardcoreDeath(PlayerDeathEvent deathEvent){
				
		String timeString = String.valueOf(System.currentTimeMillis());
		Player player = deathEvent.getEntity();
		String playerString = (String) player.getName();
		Location location = player.getLocation();
		String worldString = (String) location.getWorld().getName();
		
		//player.sendMessage(timeString);
		//player.sendMessage(playerString);
		//player.sendMessage(worldString);
		
		if (Objects.equals(worldString, "hardcore")){
				HelloWorldPlugin.deathMap.put(playerString, timeString);
				
				
				if (HelloWorldPlugin.hardcoreTimeDead == null){
					player.sendMessage("Objective hardcoreTimeDead was null");
				}else if (HelloWorldPlugin.hardcoreTimeDead.getScore(playerString) == null){
					HelloWorldPlugin.hardcoreTimeDead.getScore(playerString).setScore(0);
					player.sendMessage("Score for hardcoreTimeDead was null, set to " + String.valueOf(HelloWorldPlugin.hardcoreTimeDead.getScore(playerString).getScore()));
				}
						
				HelloWorldPlugin.hardcoreTimeDead.getScore(playerString).setScore((int) (System.currentTimeMillis()/1000));
				
				
				//player.sendMessage(HelloWorldPlugin.deathMap.toString());
				//player.sendMessage("Key saved: " + playerString);
				//player.sendMessage("Data saved: " + HelloWorldPlugin.deathMap.get(playerString));
		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent JoinEvent){
		
		
		
		Player player = JoinEvent.getPlayer();
		String playerString = (String) player.getName();
		
		player.sendMessage(playerString + " has joined");
		
		
		
		if (HelloWorldPlugin.hardcoreTimeDead.getScore(playerString) != null){
			HelloWorldPlugin.deathMap.put(playerString, String.valueOf((HelloWorldPlugin.hardcoreTimeDead.getScore(playerString).getScore()) * 1000));
			player.sendMessage("getScore(playerString) was not null");
		}else{
			HelloWorldPlugin.hardcoreTimeDead.getScore(playerString).setScore(0);
			HelloWorldPlugin.deathMap.put(playerString, String.valueOf(HelloWorldPlugin.hardcoreTimeDead.getScore(playerString).getScore()));
		}

		
		
		//try{
		//	HelloWorldPlugin.hardcoreTimeDead.getScore(playerString);
		//	player.sendMessage("try");
		//}catch(IllegalArgumentException e){
		//	HelloWorldPlugin.hardcoreTimeDead.getScore(playerString).setScore(1);
		//	player.sendMessage("catch");
		//}finally{
		//	HelloWorldPlugin.deathMap.put(playerString, String.valueOf((HelloWorldPlugin.hardcoreTimeDead.getScore(playerString).getScore()) * 1000));
		//	player.sendMessage("finally");
		//}
		
		
		
	}
	
}
