package tbmc.perworld.serializers;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class playerdata {
	
	
	//SERIALIZE PLAYERDATA
	public static String serialize(Player player){
		StringBuilder result = new StringBuilder();
		result.append(player.getExp() + ",");
		result.append(player.getHealth() + ",");
		result.append(player.getFoodLevel() + ",");
		result.append(player.getSaturation() + ",");
		result.append(player.getExhaustion() + ",");
		result.append(player.getRemainingAir() + ",");
		result.append(player.getFireTicks() + ",");
		result.append(player.getWalkSpeed() + ",");
		result.append(
				player.getActivePotionEffects()
						.stream()
						.map(s -> 
									{
										String values = 
												s.getType().getName() + "."
												+ s.getDuration() + "."
												+ s.getAmplifier() + "."
												+ s.isAmbient() + "."
												+ s.hasParticles();
										Color color = s.getColor();
										return 
												color == null ?
														values :
															values + "." + color.asRGB();
									}
								)
						.collect(Collectors.joining("&"))
				);
		return result.toString();
	}
	
	
	//RESTORE PLAYER TO DEFAULTS
	public static void setToDefaults(Player player){
		player.setExp(0);
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(0);
		player.setExhaustion(0);
		player.setRemainingAir(20);
		player.setFireTicks(0);
		player.setWalkSpeed((float) 0.2);
		Arrays.asList(PotionEffectType.values()).forEach(s -> 
					{
						if (s != null){
							player.removePotionEffect(s);
							player.sendMessage("removing effect " + s.getName());
						}else{
							player.sendMessage("cannot remove null effect");
						}
					}
				);
	}
	
	
	//SET PLAYERDATA FROM SERIALIZED
	public static void setFromSerialized(Player player, String dataString){
		setToDefaults(player);
		if (dataString == null || dataString.isEmpty()) 
			return;
				
		String[] data = dataString.split(",");
		
		player.setExp(				Float.parseFloat(		data[0]));
		player.setHealth(			Double.parseDouble(		data[1]));
		player.setFoodLevel(		Integer.parseInt(		data[2]));
		player.setSaturation(		Float.parseFloat(		data[3]));
		player.setExhaustion(		Float.parseFloat(		data[4]));
		player.setRemainingAir(		Integer.parseInt(		data[5]));
		player.setFireTicks(		Integer.parseInt(		data[6]));
		player.setWalkSpeed(		Float.parseFloat(		data[7]));
				
		//ADD POTION EFFECTS
		if (data.length == 9)
			Arrays.asList(data[8].split("&"))
					.parallelStream()
					.map(effect -> effect.split("\\."))
					.forEach(effectArgs -> 
							player.addPotionEffect(
									new PotionEffect(
										
											PotionEffectType.getByName(			effectArgs[0]),
											Integer.parseInt(					effectArgs[1]),
											Integer.parseInt(					effectArgs[2]),
											Boolean.parseBoolean(				effectArgs[3]),
											Boolean.parseBoolean(				effectArgs[4]),
											effectArgs.length == 5 ? 			null:
													Color.fromRGB(
															Integer.parseInt(	effectArgs[5]))
											)
									)
							);
	}
}
