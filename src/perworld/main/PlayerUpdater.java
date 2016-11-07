package perworld.main;

import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftInventory;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.IInventory;

import java.util.stream.Collectors;

import static perworld.cache.world.ShareSettings.compare;
import static perworld.main.MainPlugin.config;
import static perworld.main.MainPlugin.debugClock;
import static perworld.main.MainPlugin.plugin;

public class PlayerUpdater {
	
	/*	this class is called on world-change events
	 * 	
	 */
	
	
	//VALUES USED BY THE UPDATE METHODS
	public static class Values {
		public String uuid;
		public String worldTo;
		public String worldFrom;
		public String pTo;
		public String pFrom;
		public boolean shareinv;
		public boolean sharedata;
		
		public Values (Player player, String worldTo, String worldFrom){
			
			this.uuid = player.getUniqueId().toString();
			this.worldTo = worldTo;
			this.worldFrom = worldFrom;
			this.pTo = "worlds." + worldTo + ".players." + uuid;
			this.pFrom = "worlds." + worldFrom + ".players." + uuid;
			
			boolean[] compare = compare(worldTo, worldFrom);
			
			this.shareinv = compare[0];
			this.sharedata = compare[1];
		}
	}
		
	
	
	
	//MAIN UPDATE METHOD
	public static void update(Player player, String worldTo, String worldFrom){
		
		final Values values = new Values(player, worldTo, worldFrom);
		
		updateLocation(values, player);
		updateInventories(values, player);
		updatePlayerData(values, player);
		
		player.sendMessage("...done, " + (System.currentTimeMillis() - debugClock) + " ms");
		new perworld.cache.world.ShareSettings().initCache();
		player.sendMessage(perworld.cache.world.ShareSettings.cache.keySet().stream().collect(Collectors.joining(",")));
	}
	
	
	//UPDATE LOCATION
	public static void updateLocation(Values values, Player player){
		config.set(
				values.pFrom + ".location", 
				perworld.serializers.location.serialize(player.getLocation())
				);
		plugin.saveConfig();
		/*	players are not automatically moved to their stored location,
		 *	this is done only on request, in a dedicated teleport method
		 */
	}
	
	
	//UPDATE INVENTORIES
	public static void updateInventories(Values values, Player player){
		IInventory inventory = ((CraftInventory) player.getInventory()).getInventory();
		config.set(values.pFrom + ".inventory", perworld.serializers.inventory.serialize(inventory));
		plugin.saveConfig();
		if (!values.shareinv)
			perworld.serializers.inventory.setFromSerialized(
					inventory, config.getString(values.pTo + ".inventory")
					);
		
		IInventory enderchest = ((CraftInventory) player.getEnderChest()).getInventory();
		config.set(values.pFrom + ".enderchest", perworld.serializers.inventory.serialize(enderchest));
		plugin.saveConfig();		
		if (!values.shareinv)
			perworld.serializers.inventory.setFromSerialized(
					enderchest, config.getString(values.pTo + ".enderchest")
					);
	}
		
	
	//UPDATE PLAYERDATA
	public static void updatePlayerData(Values values, Player player){
		config.set(values.pFrom + ".playerdata", perworld.serializers.playerdata.serialize(player));
		plugin.saveConfig();
		if (!values.sharedata)
			perworld.serializers.playerdata.setFromSerialized(
					player, config.getString(values.pTo + ".playerdata")
					);
	}
}
