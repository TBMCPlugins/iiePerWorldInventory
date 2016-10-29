package main;

import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin extends JavaPlugin {
		
	public void onEnable(){
		
		//getServer().getPluginManager().registerEvents(new WorldLoadListener(this), this);
		getServer().getPluginManager().registerEvents(new WorldChangeListener(this), this);
		MainPlugin.getPlugin(this.getClass()).getServer().getListeningPluginChannels();
		
		saveDefaultConfig();
		WorldChangeManager.init(this);
		//BukkitTask task = new initWorldSettings().runTaskLater(this, 20);
	}
}
/*	
	public void initWorldSettings(){
		Bukkit.getServer().getWorlds()
				.stream()
				.forEach(s -> {
						FileConfiguration config = getConfig();
						String name = s.getName();
						
						if (config.get(name + ".settings.shareinvgroup") == null)
							config.set(name + ".settings.shareinvgroup", name);
						
						if (config.get(name + ".settings.sharedatagroup") == null)
							config.set(name + ".settings.sharedatagroup", name);
						
						});
		saveConfig();
	}
}
	
	private void createConfig(){
		try {
			
			
			if (!getDataFolder().exists())
				getDataFolder().mkdirs();
		
			
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

*/