package main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.IInventory;
import net.minecraft.server.v1_10_R1.ItemStack;
import net.minecraft.server.v1_10_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.PlayerList;

public class Serializers {
	
	
	//----------------------------------------------------------------------( INVENTORY )
	
	//SERIALIZE ITEMSTACK
	public static String serializeItemStack(ItemStack itemStack){
		
		NBTTagCompound tag = itemStack.save(new NBTTagCompound());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try { NBTCompressedStreamTools.a(tag, outputStream); } 
		catch (IOException e) { e.printStackTrace(); }
		
		return Base64.encodeBase64String(outputStream.toByteArray());
	}
	
	//DESERIALIZE ITEMSTACK
	public static ItemStack deserializeItemStack(String itemStackString){
		
		NBTTagCompound nbtTagCompound = null;
		ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(itemStackString));
		
		try {nbtTagCompound = NBTCompressedStreamTools.a(inputStream);} 
		catch (IOException e) {e.printStackTrace();}
		
		return ItemStack.createStack(nbtTagCompound);
	}
	
	//SERIALIZE INVENTORY
	public static String serializeInventory (IInventory invInventory){
		return IntStream.range(0, invInventory.getSize())
				.mapToObj(s -> {
						ItemStack i = invInventory.getItem(s);
						return Objects.isNull(i) ? null : s + "#" + serializeItemStack(i);
						})
				.filter(s -> s != null)
				.collect(Collectors.joining(";"));	
	}
	
	//SET INVENTORY FROM SERIALIZED
	public static void setInventoryFromSerialized (IInventory invInventory, String invString){
		invInventory.l();
		if (invString != null && !invString.isEmpty())
			Arrays.asList(invString.split(";"))
					.parallelStream()
					.forEach(s -> {
							String[] e = s.split("#");
							invInventory.setItem(Integer.parseInt(e[0]), deserializeItemStack(e[1]));
							});
	}
	
	
	
	
	
	
	//----------------------------------------------------------------------( LOCATION )
	
	//SERIALIZE LOCATION
	public static String serializeLocation(Location location){	
		return 
				"x:" + location.getBlockX() + 
				",z:" + location.getBlockZ() + 
				",y:" + location.getBlockY() + 
				",p:" + location.getPitch() + 
				",y:" + location.getYaw();
	}
	
	//DESERIALIZE LOCATION
	public static Location deserializeLocation(World world, String locationString){
		String[] s = locationString.split(",");
		return new Location(
				world, 
				Double.valueOf(s[0].split(":")[1]), 
				Double.valueOf(s[1].split(":")[1]), 
				Double.valueOf(s[2].split(":")[1]), 
				Float.valueOf(s[3].split(":")[1]), 
				Float.valueOf(s[4].split(":")[1])
				);
	}
	
	
	
	
	
	
	//----------------------------------------------------------------------( PLAYERDATA )
	
	//SERIALIZE PLAYERDATA
	public static String serializePlayerData(Player player){
		StringBuilder result = new StringBuilder();
		result.append("health:" + player.getHealth() + ";");
		result.append("food:" + player.getFoodLevel() + ";");
		result.append("exhaustion: " + player.getExhaustion() + ";");
		result.append("exp:" + player.getExp() + ";");
		result.append("air:" + player.getRemainingAir() + ";");
		result.append("fireticks:" + player.getFireTicks() + ";");
		return result.toString();
	}
	
}
