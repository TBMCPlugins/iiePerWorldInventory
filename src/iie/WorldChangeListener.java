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
			


			invInventory = player.getInventory();
			invString = InventoryStringDeSerializer.InventoryToString(invInventory);			
			plugin.getConfig().set(pathFrom + "inventory",invString);
			plugin.saveConfig();
			
			player.sendMessage("you changed worlds");
			player.sendMessage(invString);
			
			
			
			invString = (String) plugin.getConfig().get(pathTo + "inventory");
			player.sendMessage(invString);

			invInventory = InventoryStringDeSerializer.StringToInventory(invString);
			player.sendMessage(invInventory.toString());

			player.getInventory().setContents(invInventory.getContents());
			
	}

}
