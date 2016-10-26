package iie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.codec.binary.Base64;

import net.minecraft.server.v1_10_R1.IInventory;
import net.minecraft.server.v1_10_R1.ItemStack;
import net.minecraft.server.v1_10_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_10_R1.NBTTagCompound;

public class Serializer {
	
	
	
	public static String serializeItemStack(ItemStack itemStack){
		
		if (itemStack == null) return "null";
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try { NBTCompressedStreamTools.a(itemStack.getTag(), outputStream); } 
		catch (IOException e) { e.printStackTrace(); }
		
		return Base64.encodeBase64String(outputStream.toByteArray());
	}
	
	
	
	public static ItemStack deserializeItemStack(String itemStackString){
		
		if (itemStackString.equals("null")) return null;
	   
		ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(itemStackString));
		NBTTagCompound nbtTagCompound = null;
		
		try {nbtTagCompound = NBTCompressedStreamTools.a(inputStream);} 
		catch (IOException e) {e.printStackTrace();}
		
		return ItemStack.createStack(nbtTagCompound);
	}
	
	
	
	public static String serializeInventory (IInventory invInventory){
		return IntStream.range(0, invInventory.getSize())
				.mapToObj(s -> {
						ItemStack i = invInventory.getItem(s);
						return Objects.isNull(i) ? null : s + "#" + serializeItemStack(i);
						})
				.filter(s -> s != null)
				.collect(Collectors.joining(";"));	
	}
	
	
	
	public static void setInventoryFromSerialized (IInventory invInventory, String invString){
		invInventory.l();
		if (invString != null)
			Arrays.asList(invString.split(";"))
					.stream()
					.forEach(s -> {
							String[] e = s.split("#");
							invInventory.setItem(Integer.parseInt(e[0]), deserializeItemStack(e[1]));
							});
	}
}
