package iie;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.Inventory;


public class WorldChangeListener implements Listener {
	
	PerWorldInventoryPlugin plugin;
	public WorldChangeListener(PerWorldInventoryPlugin plugin){
		this.plugin = plugin;
	}	
		
		@EventHandler(priority = EventPriority.MONITOR)
		public void onWorldChangeListener(PlayerChangedWorldEvent event){
			
			Player player = event.getPlayer();
			String playername = player.getName();
			
			World worldTo = player.getWorld();
			World worldFrom = event.getFrom();
			String worldToName = worldTo.getName();
			String worldFromName = worldFrom.getName();
			
			String pathTo = worldToName + "." + playername + ".";
			String pathFrom = worldFromName + "." + playername + ".";
			
			Inventory invInventory;
			String invString;

			
			
			//----------------------------------------------------------------------INVENTORY


			invInventory = player.getInventory();
			invString = Serializer.StringFromInventory(invInventory);			
			plugin.getConfig().set(pathFrom + "inventory",invString);
			plugin.saveConfig();
			
			invString = (String) plugin.getConfig().get(pathTo + "inventory");
			
			player.getInventory().clear();
			player.getInventory().setContents(
					Serializer.InventoryFromString(invString, invInventory.getType()).getContents());
			
		   
			
			//----------------------------------------------------------------------ENDERCHEST
			
			
			invInventory = player.getEnderChest();
			invString = Serializer.StringFromInventory(invInventory);			
			plugin.getConfig().set(pathFrom + "enderchest",invString);
			plugin.saveConfig();
			
			invString = (String) plugin.getConfig().get(pathTo + "enderchest");
			
			player.getEnderChest().clear();
			player.getEnderChest().setContents(
					Serializer.InventoryFromString(invString, invInventory.getType()).getContents());
	}
}
