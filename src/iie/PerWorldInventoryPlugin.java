package iie;

import java.io.File;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PerWorldInventoryPlugin extends JavaPlugin {
	
	
	public void onEnable(){
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(new WorldChangeListener(this), this);
		PerWorldInventoryPlugin.getPlugin(this.getClass()).getServer().getListeningPluginChannels();
		createConfig();
	}
	
		
	private void createConfig(){
		try {
			
			
			if (!getDataFolder().exists())
				getDataFolder().mkdirs();
		
			
			//CONFIG.YML
			File file = new File(getDataFolder(), "config.yml");
		
			if (!file.exists()){
				getLogger().info("Config.yml not found, creating!");
				saveDefaultConfig();
			}else
				getLogger().info("Config.yml found, loading!");
		
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
