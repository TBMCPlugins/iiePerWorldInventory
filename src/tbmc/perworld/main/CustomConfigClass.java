package tbmc.perworld.main;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class CustomConfigClass extends YamlConfiguration {
	
	public class SettingsSection extends YamlConfiguration {
		public final ConfigurationSection worlds = this.getConfigurationSection("worlds");
		public final ConfigurationSection players = this.getConfigurationSection("players");
	}
	
	public final ConfigurationSection commands = this.getConfigurationSection("teleport-commands");
	
	public final SettingsSection settings = (SettingsSection) this.getConfigurationSection("settings");
	
	public final ConfigurationSection worlds = this.getConfigurationSection("worlds");
	public final ConfigurationSection players = this.getConfigurationSection("players");
}
