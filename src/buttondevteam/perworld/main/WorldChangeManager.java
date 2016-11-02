package main;

import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftInventory;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.IInventory;

public class WorldChangeManager {
	
	private static MainPlugin plugin;
	private static FileConfiguration config;
	public static void init(MainPlugin plugin){ //onEnable()
		WorldChangeManager.plugin = plugin;
		WorldChangeManager.config = plugin.getConfig();
	}
	
	
	//EVALUATE SHARE SETTINGS
	public static boolean[] compare (String worldTo, String worldFrom) {
		boolean[] result = new boolean[] {false,false};
		String[][] params = 
				(String[][]) Arrays.asList
						(new String[] {
								(String) config.get(worldTo + ".settings.shareinvgroup"),
								(String) config.get(worldFrom + ".settings.shareinvgroup"),
								(String) config.get(worldTo + ".settings.sharedatagroup"),
								(String) config.get(worldFrom + ".settings.sharedatagroup")
								})
										.stream()
										.map(s -> (String[]) s
												.trim()
												.replaceAll("( )", "")
												.toLowerCase()
												.split("\\*"))
										.toArray(String[][]::new);
		
		/*	Each world has two sharing settings: for inventory sharing,
		 * 	and for playerdata sharing. Each setting has two parameters:
		 * 	the sharegroup flag, which labels a group of sharing worlds,
		 * 	and an optional suffix, *in or *out, specifying that the world
		 * 	shares with its group in only one traffic direction
		 */
		
		if (
				params[0][0].equals(params[1][0])//inv
				&& params[0].length > 1 ? params[0][1].equals("in") : true
				&& params[1].length > 1 ? params[1][1].equals("out") : true
				) 
			result[0] = true;
		
		if (
				params[2][0].equals(params[3][0])//data
				&& params[2].length > 1 ? params[2][1].equals("in") : true
				&& params[3].length > 1 ? params[3][1].equals("out") : true
				) 
			result[1] = true;
		
		return result;
	}
	
	
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
			this.pTo = worldTo + ".players." + uuid;
			this.pFrom = worldFrom + ".players." + uuid;
			
			boolean[] compare = compare(worldTo,worldFrom);
			
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
	}
	
	
	//UPDATE LOCATION
	public static void updateLocation(Values values, Player player){
		config.set(
				values.pFrom + ".location", 
				serializers.location.serialize(player.getLocation())
				);
		plugin.saveConfig();
		/*	players are not automatically moved to their stored location,
		 *	this is done only on request, in a dedicated teleport method
		 */
	}
	
	
	//UPDATE INVENTORIES
	public static void updateInventories(Values values, Player player){
		IInventory inventory = ((CraftInventory) player.getInventory()).getInventory();
		config.set(values.pFrom + ".inventory", serializers.inventory.serialize(inventory));
		plugin.saveConfig();
		if (!values.shareinv)
			serializers.inventory.setFromSerialized(
					inventory, (String) config.get(values.pTo + ".inventory")
					);
		
		IInventory enderchest = ((CraftInventory) player.getEnderChest()).getInventory();
		config.set(values.pFrom + ".enderchest", serializers.inventory.serialize(enderchest));
		plugin.saveConfig();		
		if (!values.sharedata)
			serializers.inventory.setFromSerialized(
					enderchest, (String) config.get(values.pTo + ".enderchest")
					);
	}
		
	
	//UPDATE PLAYERDATA
	public static void updatePlayerData(Values values, Player player){
		config.set(values.pFrom + ".playerdata", serializers.playerdata.serialize(player));
		plugin.saveConfig();
		if (!values.sharedata)
			;
	}
}
