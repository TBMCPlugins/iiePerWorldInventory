package main;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin extends JavaPlugin {
	
	
	public static MainPlugin plugin;
	public static volatile FileConfiguration config;
	
	public static volatile ConfigurationSection worlds;
	public static volatile ConfigurationSection players;
	public static long debugClock;
	
	
	public void onEnable(){
		
		getServer().getPluginManager().registerEvents(new ListenerPlayerJoin(this), this);
		getServer().getPluginManager().registerEvents(new ListenerPlayerWorldChange(this), this);
		saveDefaultConfig();
		
		plugin = this;
		config = getConfig();
		worlds = config.getConfigurationSection("worlds");
		players = config.getConfigurationSection("players");
		
		if (!config.contains("worlds")) config.createSection("worlds");
		if (!config.contains("players")) config.createSection("players");
		saveConfig();
		
		new cache.world.ShareSettings().initCache();
	}
}