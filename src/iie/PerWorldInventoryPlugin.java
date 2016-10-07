package iie;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

public class PerWorldInventoryPlugin extends JavaPlugin {
	
	
	public void onEnable(){		
		getServer().getPluginManager().registerEvents(new WorldChangeListener(this), this);
		createConfig();	
	}
	
	
	private void createConfig(){
		try {
			
			
			if (!getDataFolder().exists())
				getDataFolder().mkdirs();
		
			File file = new File(getDataFolder(), "config.yml");
		
			if (!file.exists()){
				getLogger().info("Config.yml not found, creating!");
				saveDefaultConfig();
			}else{
				getLogger().info("Config.yml found, loading!");
			}
		
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
