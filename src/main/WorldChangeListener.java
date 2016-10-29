package main;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeListener implements Listener {
	
	private static MainPlugin plugin;
	private static FileConfiguration config;
	public WorldChangeListener(MainPlugin plugin){
		WorldChangeListener.plugin = plugin;
		WorldChangeListener.config = plugin.getConfig();
	}	
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldChangeListener(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		String worldTo = player.getWorld().getName();
		String worldFrom = event.getFrom().getName();
		
		initWorldSettings(worldTo);
		initWorldSettings(worldFrom);
		WorldChangeManager.update(player,worldTo, worldFrom);
	}
	
	
	public static void initWorldSettings(String world){
		if (config.get(world + ".settings.shareinvgroup") == null) 
			config.set(world + ".settings.shareinvgroup", world);
		if (config.get(world + ".settings.sharedatagroup") == null) 
			config.set(world + ".settings.sharedatagroup", world);
		plugin.saveConfig();
	}
}
