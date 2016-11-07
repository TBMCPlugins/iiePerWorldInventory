package tbmc.perworld.serializers;

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

public class inventory {
	
	
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
	public static String serialize (IInventory invInventory){
		return IntStream.range(0, invInventory.getSize())
				.mapToObj(s -> {
						ItemStack i = invInventory.getItem(s);
						return Objects.isNull(i) ? null : s + "#" + serializeItemStack(i);
						})
				.filter(s -> s != null)
				.collect(Collectors.joining(";"));	
	}
	
	
	//SET INVENTORY FROM SERIALIZED
	public static void setFromSerialized (IInventory invInventory, String invString){
		invInventory.l();//clear inventory
		if (invString != null && !invString.isEmpty())
			Arrays.asList(invString.split(";"))
					.parallelStream()
					.forEach(s -> {
							String[] e = s.split("#");
							invInventory.setItem(Integer.parseInt(e[0]), deserializeItemStack(e[1]));
							});
	}
}
