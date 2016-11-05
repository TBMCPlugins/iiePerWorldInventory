package buttondevteam.perworld.main;

import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftInventory;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.IInventory;

import java.util.stream.Collectors;

import static buttondevteam.perworld.cache.world.ShareSettings.compare;
import static buttondevteam.perworld.main.MainPlugin.config;
import static buttondevteam.perworld.main.MainPlugin.debugClock;
import static buttondevteam.perworld.main.MainPlugin.plugin;

public class PlayerUpdater {
	
	
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
			//compare() is a static import from world.ShareSettings.java
			
			this.shareinv = compare[0];
			this.sharedata = compare[1];
		}
	}
		
	
	
	
	//MAIN UPDATE METHOD
	public static void update(Player player, String worldTo, String worldFrom){
		
		final Values values = new Values(player, worldTo, worldFrom);
		
		updateLocation(values, player);
		updateInventories(values, player);
		//updatePlayerData(values, player);
		
		player.sendMessage("...done, " + (System.currentTimeMillis() - debugClock) + " ms");
		new buttondevteam.perworld.cache.world.ShareSettings().initCache();
		player.sendMessage(buttondevteam.perworld.cache.world.ShareSettings.cache.keySet().stream().collect(Collectors.joining(",")));
	}
	
	
	//UPDATE LOCATION
	public static void updateLocation(Values values, Player player){
		config.set(
				values.pFrom + ".location", 
				buttondevteam.perworld.serializers.location.serialize(player.getLocation())
				);
		plugin.saveConfig();
		/*	players are not automatically moved to their stored location,
		 *	this is done only on request, in a dedicated teleport method
		 */
	}
	
	
	//UPDATE INVENTORIES
	public static void updateInventories(Values values, Player player){
		IInventory inventory = ((CraftInventory) player.getInventory()).getInventory();
		config.set(values.pFrom + ".inventory", buttondevteam.perworld.serializers.inventory.serialize(inventory));
		plugin.saveConfig();
		if (!values.shareinv)
			buttondevteam.perworld.serializers.inventory.setFromSerialized(
					inventory, (String) config.get(values.pTo + ".inventory")
					);
		
		IInventory enderchest = ((CraftInventory) player.getEnderChest()).getInventory();
		config.set(values.pFrom + ".enderchest", buttondevteam.perworld.serializers.inventory.serialize(enderchest));
		plugin.saveConfig();		
		if (!values.sharedata)
			buttondevteam.perworld.serializers.inventory.setFromSerialized(
					enderchest, (String) config.get(values.pTo + ".enderchest")
					);
	}
		
	
	//UPDATE PLAYERDATA
	public static void updatePlayerData(Values values, Player player){
		config.set(values.pFrom + ".playerdata", buttondevteam.perworld.serializers.playerdata.serialize(player));
		plugin.saveConfig();
		if (!values.sharedata)
			;
	}
}
