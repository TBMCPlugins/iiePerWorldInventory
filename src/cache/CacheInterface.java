package cache;

import org.bukkit.configuration.ConfigurationSection;

public interface CacheInterface {
	
	public static ConfigurationSection worlds = main.MainPlugin.worlds;
	public static ConfigurationSection players = main.MainPlugin.players;
	
	Object generateElement(String string);
	
	void putCache(String string);
	
	void initCache();
	
}
