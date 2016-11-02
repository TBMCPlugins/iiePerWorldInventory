package buttondevteam.perworld.serializers;

import java.util.Arrays;

import org.bukkit.entity.Player;

public class playerdata {
	
	
	//SERIALIZE PLAYERDATA
	public static String serialize(Player player){
		StringBuilder result = new StringBuilder();
		result.append("exp:" + player.getExp() + ",");
		result.append("health:" + player.getHealth() + ",");
		result.append("food:" + player.getFoodLevel() + ",");
		result.append("saturation:" + player.getSaturation() + ",");
		result.append("exhaustion:" + player.getExhaustion() + ",");
		result.append("air:" + player.getRemainingAir() + ",");
		result.append("walkspeed:" + player.getWalkSpeed() + ",");
		result.append("fireticks:" + player.getFireTicks() + ",");
		return result.toString();
	}
	
	
	//SET PLAYERDATA FROM SERIALIZED
	public static void setFromSerialized(String dataString, Player player){
		Double.valueOf(null);
		Arrays.asList(dataString.split(","))
				.parallelStream()
				.forEach(s -> {
						String[] e = s.split(":");
						switch(e[0]){
							case "health" :
								player.setHealth(Double.valueOf(e[1]));
						}
						});
		
	}
}
