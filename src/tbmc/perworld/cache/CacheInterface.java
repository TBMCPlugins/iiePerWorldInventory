package tbmc.perworld.cache;

import org.bukkit.configuration.ConfigurationSection;

public interface CacheInterface {
	
	public static ConfigurationSection worlds = tbmc.perworld.main.MainPlugin.worlds;
	public static ConfigurationSection players = tbmc.perworld.main.MainPlugin.players;
	
	Object generateElement(String string);
	
	void putCache(String string);
	
	void initCache();
	
}
