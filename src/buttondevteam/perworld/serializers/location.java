package buttondevteam.perworld.serializers;

import org.bukkit.Location;
import org.bukkit.World;

public class location {
	
	
	//SERIALIZE LOCATION
	public static String serialize(Location location){	
		return 
				location.getBlockX() + ","
				+ location.getBlockY() + ","
				+ location.getBlockZ() + ","
				+ location.getYaw() + ","
				+ location.getPitch();
	}
	
	
	//DESERIALIZE LOCATION
	public static Location deserialize(World world, String locationString){
		String[] s = locationString.split(",");
		return new Location(
				world, 
				Double.valueOf(s[0]), 
				Double.valueOf(s[1]), 
				Double.valueOf(s[2]), 
				Float.valueOf(s[3]), 
				Float.valueOf(s[4])
				);
	}
}
