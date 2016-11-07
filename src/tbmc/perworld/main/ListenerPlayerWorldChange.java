package tbmc.perworld.main;

import static tbmc.perworld.main.MainPlugin.debugClock;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ListenerPlayerWorldChange implements Listener {
		
	MainPlugin plugin;
	public ListenerPlayerWorldChange(MainPlugin plugin){
		this.plugin = plugin;
		
	}	
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldChange(PlayerChangedWorldEvent event){
		
		debugClock = System.currentTimeMillis();
		
		Player player = event.getPlayer(); player.sendMessage("updating... ");
		String gamemode;
		String worldTo = player.getWorld().getName();
		String worldFrom = event.getFrom().getName();
		
		PlayerUpdater.update(player, worldTo, worldFrom);
	}
}
