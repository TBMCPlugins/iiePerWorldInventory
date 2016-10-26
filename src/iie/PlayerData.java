package iie;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerData {
	
	
	
	public static class Position {
		
		public static String StringFromLocation(Player player){	
			Location location = player.getLocation();	
			return "x:" + location.getBlockX() + ",z:" + location.getBlockZ() + ",y:" + location.getBlockY();
		}
		
		public Location LocationFromString(String locationString){
			return null;	
		}	
	}
	
	
	
}
