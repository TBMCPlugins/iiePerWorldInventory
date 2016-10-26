package iie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.IInventory;
import net.minecraft.server.v1_10_R1.ItemStack;
import net.minecraft.server.v1_10_R1.JsonList;
import net.minecraft.server.v1_10_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_10_R1.NBTTagCompound;

public class SerializerDebug {
	
	
	
	public static String serializeItemStack(ItemStack itemStack, Player player){
		player.sendMessage("serializeItemStack: called");
		if (itemStack == null){ player.sendMessage("serializeItemStack: NULL itemStack"); return "null";}
						
		player.sendMessage("serializeItemStack: nbt tag = " + itemStack.getTag().toString());
		
		return itemStack.getTag().toString();
	}
	
	
	
	public static ItemStack deserializeItemStack(String itemStackString, Player player){
		
		if (itemStackString.equals("null")){ 
			player.sendMessage("deserializeItemStack: NULL itemStackString");
			return null;
		}
		
		NBTTagCompound nbtTagCompound = (NBTTagCompound) JsonList.func_150315_a(itemStackString);;
				
		
		player.sendMessage(nbtTagCompound.toString());
		
		return ItemStack.createStack(nbtTagCompound);
	}
	
	
	
	public static String serializeInventory (IInventory invInventory, Player player){
		player.sendMessage("serializeInventory: invInventory.getSize() = " + invInventory.getSize());
		return IntStream.range(0, invInventory.getSize() - 1)
				.mapToObj(s -> {
						ItemStack i = invInventory.getItem(s);
						player.sendMessage(Objects.isNull(i) ? "serializeInventory: NULL item" + s : "serializeInventory: item " + s + " found");
						return Objects.isNull(i) ? null : s + "#" + serializeItemStack(i, player);
						})
				.filter(s -> s != null)
				.collect(Collectors.joining(";"));	
	}
	
	
	
	public static void setInventoryFromSerialized (IInventory invInventory, String invString, Player player){
		if (invInventory == null){ player.sendMessage("setInventoryFromSerialized: invInventory NULL"); return;}
		invInventory.l();
		if (invString == null){ player.sendMessage("setInventoryFromSerialized: NULL invString"); return;}
		if (invString.isEmpty()){ player.sendMessage("setInventoryFromSerialized: EMPTY invString"); return;}
		if (!invString.contains(";")){ player.sendMessage("setInventoryFromSerialized: DOESN'T CONTAIN ';' "); return;}
		Arrays.asList(invString.split(";"))
				.stream()
				.forEach(s -> {
						String[] e = s.split("#");
						invInventory.setItem(Integer.parseInt(e[0]), deserializeItemStack(e[1], player));
						player.sendMessage("set item " + e[0] + " to inventory");
						});
	}
}
