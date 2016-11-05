package buttondevteam.perworld.cache;

import org.bukkit.configuration.ConfigurationSection;

public interface CacheInterface {
	
	public static ConfigurationSection worlds = buttondevteam.perworld.main.MainPlugin.worlds;
	public static ConfigurationSection players = buttondevteam.perworld.main.MainPlugin.players;
	
	Object generateElement(String string);
	
	void putCache(String string);
	
	void initCache();
	
}
