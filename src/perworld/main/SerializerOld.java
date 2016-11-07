package perworld.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.ChunkProviderServer;
import net.minecraft.server.v1_10_R1.DataConverterManager;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.PlayerList;
import net.minecraft.server.v1_10_R1.WorldServer;
 
public class SerializerOld {
	
	//public void test(){
	//	GameProfile profile = new com.mojang.authlib.GameProfile(player.getUniqueId(), "");
	//	profile.getProperties();
	//}
	
	public static String StringFromInventory (Inventory invInventory){
		String serialization = invInventory.getSize() + ";";
		for (int i = 0; i < invInventory.getSize(); i++){
			ItemStack is = invInventory.getItem(i);
			ItemMeta im = null;
			if (is != null){
				
				if (is.hasItemMeta()) im = is.getItemMeta();
				String serializedItemStack = new String();
			   
				String isType = String.valueOf(is.getType());
				serializedItemStack += "t@" + isType;
			   
				if (is.getDurability() != 0)
					serializedItemStack += ":d@" + String.valueOf(is.getDurability());
			   
				if (is.getAmount() != 1)
					serializedItemStack += ":a@" + String.valueOf(is.getAmount());
			   
				Map<Enchantment,Integer> isEnch = is.getEnchantments();
				if (isEnch.size() > 0)
					for (Entry<Enchantment,Integer> ench : isEnch.entrySet())
						serializedItemStack += ":e@" + ench.getKey().getName() + "@" + ench.getValue();
				
				if (im == null){
					serialization += i + "#" + serializedItemStack + ";";
					continue;
				}
				
				
				//ITEM META
				if (im.getDisplayName() != null){
					serializedItemStack += ":n@" + is.getItemMeta().getDisplayName()
							.replace("#", "{hash}")
							.replace("@", "{ampersand}")
							.replace(";", "{semicolon}")
							.replace("'", "{single quote}")
							.replace("\"", "{double quote}");
				}
				if (im.getLore() != null){
					serializedItemStack += ":l@" + is.getItemMeta().getLore().stream()
							.map(s -> s
									.replace("[", "{bracket start}")
									.replace("]", "{bracket end}")
									.replace(",", "{comma}")
									
									.replace("#", "{hash}")
									.replace("@", "{ampersand}")
									.replace(";", "{semicolon}")
									.replace("'", "{single quote}")
									.replace("\"", "{double quote}"))
							.collect(Collectors.joining(","));
				}
				if (im.getItemFlags().size() > 0){
					ItemFlag[] flags = ItemFlag.values();
					if (im.hasItemFlag(flags[0]))
						serializedItemStack += ":f@HIDE_ATTRIBUTES";
					if (im.hasItemFlag(flags[1]))
						serializedItemStack += ":f@HIDE_DESTROYS";
					if (im.hasItemFlag(flags[2]))
						serializedItemStack += ":f@HIDE_ENCHANTS";
					if (im.hasItemFlag(flags[3]))
						serializedItemStack += ":f@HIDE_PLACED_ON";
					if (im.hasItemFlag(flags[4]))
						serializedItemStack += ":f@HIDE_POTION_EFFECTS";
					if (im.hasItemFlag(flags[5]))
						serializedItemStack += ":f@HIDE_UNBREAKABLE";
					}
				
				
				//BANNER META
				if (isType == "BANNER"){
					BannerMeta meta = (BannerMeta) im;
					serializedItemStack += ":basecolor@" + meta.getBaseColor().name();
					if (meta.getPatterns().size() > 0){
						serializedItemStack += ":patterns@";
						String list = meta.getPatterns().stream()
								.map(p -> p.getPattern().name() + "~" + p.getColor().name())
								.collect(Collectors.joining(","));
						serializedItemStack += list;
					}
				}
				
				
				//BOOK META
				if (isType == "WRITTEN_BOOK"){
					BookMeta meta = (BookMeta) im;
					if (meta.hasTitle())
						serializedItemStack += ":title@" + meta.getTitle();
					if (meta.hasAuthor())
						serializedItemStack += ":author@" + meta.getAuthor();
					//if (meta.hasGeneration())
					//	serializedItemStack += ":generation@" + meta.getGeneration().name();
					if (meta.hasPages()){
						serializedItemStack += ":pages@" + meta.getPages().stream()
								.map(s -> s
										.replace("º", "°")
										.replace("[", "{bracket start}")
										.replace("]", "{bracket end}")
										.replace(",", "{comma}")
										.replace("#", "{hash}")
										.replace("@", "{ampersand}")
										.replace(";", "{semicolon}")
										.replace("'", "{single quote}")
										.replace("\"", "{double quote}"))
								.collect(Collectors.joining("º"));
					}
				}
				
				
				//ENCHANTED BOOK META
				if (isType == "ENCHANTED_BOOK"){
					EnchantmentStorageMeta meta = (EnchantmentStorageMeta) im;
					if (meta.hasStoredEnchants()){
						//serializedItemStack += ":storedenchants@" + meta.getStoredEnchants().entrySet().stream()
								//.map(e -> e.getKey() + "~" + String.valueOf(e.getValue()))
								//.collect(Collectors.joining(","));
						ArrayList<String> list = new ArrayList<String>();
						meta.getStoredEnchants().forEach((k,v) -> list.add(k.getName() + "~" + v));
						serializedItemStack += ":storedenchants@" + list.stream().collect(Collectors.joining(","));
					}
				}
								
				
				//FIREWORK CHARGE META
				if (isType == "FIREWORK_CHARGE"){
					FireworkEffectMeta meta = (FireworkEffectMeta) im;
					if (meta.hasEffect()){
						FireworkEffect e = meta.getEffect();
						serializedItemStack += ":charge-effect@" 
									+ e.getType().name() + "~"
									+ e.getColors()
											.stream().map(c -> String.valueOf(c.asRGB()))
												.collect(Collectors.joining("/")) + "~"
									+ e.getFadeColors()
											.stream().map(c -> String.valueOf(c.asRGB()))
												.collect(Collectors.joining("/")) + "~"
									+ e.hasFlicker() + "~"
									+ e.hasTrail();
					}
				}
				
				
				//FIREWORK META
				if (isType == "FIREWORK"){
					FireworkMeta meta = (FireworkMeta) im;
					if (meta.hasEffects())
						serializedItemStack += ":firework-effects@" + meta.getEffects().stream()
								.map(s -> s.getType().name() + "~"
										+ s.getColors()
												.stream().map(c -> String.valueOf(c.asRGB()))
												.collect(Collectors.joining("/")) + "~"
										+ s.getFadeColors()
												.stream().map(c -> String.valueOf(c.asRGB()))
												.collect(Collectors.joining("/")) + "~"
										+ s.hasFlicker() + "~"
										+ s.hasTrail())
								.collect(Collectors.joining(","));
					serializedItemStack += ":power@" + meta.getPower();
				}
				
				
				//LEATHER ARMOR META
				if (isType == "LEATHER_HELMET" || isType == "LEATHER_CHESTPLATE" || isType == "LEATHER_LEGGINGS" || isType == "LEATHER_BOOTS")
					serializedItemStack += ":color@" + ((LeatherArmorMeta) im).getColor().asRGB();
				
				
				//MAP META
				if (isType == "MAP")
					serializedItemStack += ":scaling@" + ((MapMeta) im).isScaling();
				
				
				//POTION META
				if (isType == "POTION" || isType == "LINGERING_POTION" || isType == "SPLASH_POTION"){
					PotionMeta meta = (PotionMeta) im;
					PotionData data = (PotionData) meta.getBasePotionData();
					serializedItemStack += ":effect@" + data.getType().name() + "~" + data.isExtended() + "~" + data.isUpgraded();
					if (meta.hasCustomEffects()) 
						serializedItemStack += ":customeffects@" + meta.getCustomEffects().stream()
								.map(s -> {
									String p = s.getType().getName() + "~" 
											+ s.getAmplifier() + "~"
											+ s.getDuration() + "~"
											+ s.hasParticles() + "~"
											+ s.isAmbient() + "~";
									if (s.getColor() != null)
										return p + s.getColor().asRGB();
									return p;
									})
								.collect(Collectors.joining(","));
				}
				
				
				//SKULL META
				if (isType == "SKULL_ITEM"){
					String texture = null;
					//net.minecraft.server.v1_10_R1.ItemStack nmsItem = 
					texture = ((CraftInventory) invInventory)
									.getInventory()
									.getContents()[i]
									.getTag()
									.getCompound("SkullOwner")
									.toString();
					//texture = Base64.getUrlEncoder().encodeToString
					//		(
					//		nmsItem.getTag().getCompound("SkullOwner").toString().getBytes()
					//		);
								//.getCompound("SkullOwner")
								//.getCompound("Properties")
								//.getCompound("textures")
								//.getString("Value");
					if (((SkullMeta) im).hasOwner())
						serializedItemStack += ":owner@" + ((SkullMeta) im).getOwner();
					if (texture != null)
						serializedItemStack += ":texture@" + texture;
					
				}
				
				
				
				serialization += i + "#" + serializedItemStack + ";";
			}
		}
		return serialization;
	}
   
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	public static Inventory InventoryFromString(String invString, InventoryType type){
		String[] serializedBlocks = invString.split(";");
		int invSize = Integer.valueOf(serializedBlocks[0]);
		Inventory deserializedInventory = Bukkit.getServer().createInventory(null, type);
		for (int i = 1; i < serializedBlocks.length; i++){
			String[] serializedBlock = serializedBlocks[i].split("#");
			int stackPosition = Integer.valueOf(serializedBlock[0]);
			if (stackPosition >= invSize)
				continue;
			
			ItemStack is = null;
			ItemMeta im = null;
			Boolean createdItemStack = false;
			
			String[] serializedItemStack = serializedBlock[1].split(":");
			for (String itemInfo : serializedItemStack){
				String[] itemAttribute = itemInfo.split("@");
				if (itemAttribute[0].equals("t")){
					is = new ItemStack(Material.getMaterial((itemAttribute[1])));
					createdItemStack = true;
					if (is.getItemMeta() != null)
						im = is.getItemMeta(); 
				}else if(itemAttribute[0].equals("d") && createdItemStack){
					is.setDurability(Short.valueOf(itemAttribute[1]));
				}else if(itemAttribute[0].equals("a") && createdItemStack){
					is.setAmount(Integer.valueOf(itemAttribute[1]));
				}else if(itemAttribute[0].equals("e") && createdItemStack){
					is.addUnsafeEnchantment(Enchantment.getByName(itemAttribute[1]), Integer.valueOf(itemAttribute[2]));
				}else if(itemAttribute[0].equals("n") && createdItemStack){
					im.setDisplayName(itemAttribute[1]
							.replace("#", "{hash}")
							.replace("@", "{ampersand}")
							.replace(";", "{semicolon}")
							.replace("'", "{single quote}")
							.replace("\"", "{double quote}"));
					
				}else if(itemAttribute[0].equals("l") && createdItemStack){
					im.setLore(Arrays.asList(itemAttribute[1].split("(,)")).stream().map(s -> s
							.replace("[", "{bracket start}")
							.replace("]", "{bracket end}")
							.replace(",", "{comma}")
							
							.replace("#", "{hash}")
							.replace("@", "{ampersand}")
							.replace(";", "{semicolon}")
							.replace("'", "{single quote}")
							.replace("\"", "{double quote}"))
							.collect(Collectors.toList()));
					
				}else if(itemAttribute[0].equals("f") && createdItemStack){
					im.addItemFlags(ItemFlag.valueOf(itemAttribute[1]));
					
					
				//BANNER ATTRIBUTES
				}else if(itemAttribute[0].equals("basecolor") && createdItemStack){
					((BannerMeta) im).setBaseColor(DyeColor.valueOf(itemAttribute[1]));
				}else if(itemAttribute[0].equals("patterns") && createdItemStack){
					((BannerMeta) im).setPatterns((List<Pattern>) Arrays.asList(itemAttribute[1].split("(,)")).stream()
							.map(s -> new Pattern(
									DyeColor.valueOf(Arrays.asList(s.split("(~)")).get(1)), 
									PatternType.valueOf(Arrays.asList(s.split("(~)")).get(0))))
							.collect(Collectors.toList()));
					
					
				//BOOK ATTRIBUES
				}else if(itemAttribute[0].equals("title") && createdItemStack){
					((BookMeta) im).setTitle(itemAttribute[1]);
				}else if(itemAttribute[0].equals("author") && createdItemStack){
					((BookMeta) im).setAuthor(itemAttribute[1]);
				}else if(itemAttribute[0].equals("generation") && createdItemStack){
					((BookMeta) im).setGeneration(Generation.valueOf(itemAttribute[1]));
				}else if(itemAttribute[0].equals("pages") && createdItemStack){
					((BookMeta) im).setPages(Arrays.asList(itemAttribute[1]
							.replace("{bracket start}","[")
							.replace("{bracket end}","]")
							.replace("{comma}",",")
							.replace("{hash}","#")
							.replace("{ampersand}","@")
							.replace("{semicolon}",";")
							.replace("{single quote}","'")
							.replace("{double quote}","\"")
							.split("º")));
					
					
				//ENCHANTED BOOK ATTRIBUTES
				}else if(itemAttribute[0].equals("storedenchants") && createdItemStack){
					final EnchantmentStorageMeta storedEnchants = (EnchantmentStorageMeta) im;
					Arrays.asList(itemAttribute[1].split("(,)")).stream().map(e -> e.split("(~)"))
							.forEach(e -> storedEnchants.addStoredEnchant(Enchantment.getByName(e[0]),Integer.valueOf(e[1]),true));
					im = storedEnchants;
					
					
				//FIREWORK CHARGE ATTRIBUTES
				}else if(itemAttribute[0].equals("charge-effect") && createdItemStack){
					String[] e = itemAttribute[1].split("(~)");
					((FireworkEffectMeta) im).setEffect(
							FireworkEffect.builder()
									.with(FireworkEffect.Type.valueOf(e[0]))
									.withColor(Arrays.asList(e[1].split("/")).stream()
											.map(s -> Color.fromRGB(Integer.valueOf(s)))
											.collect(Collectors.toList()))
									.withFade(Arrays.asList(e[2].split("/")).stream()
											.map(s -> Color.fromRGB(Integer.valueOf(s)))
											.collect(Collectors.toList()))
									.flicker(Boolean.valueOf(e[3]))
									.trail(Boolean.valueOf(e[4]))
									.build());
					
					
				//FIREWORK ATTRIBUTES
				}else if(itemAttribute[0].equals("firework-effects") && createdItemStack){
					final FireworkMeta effects = (FireworkMeta) im;
					Arrays.asList(itemAttribute[1].split("(,)")).stream()
							.map(s -> s.split("(~)"))
							.forEach(s -> {
								effects.addEffect(
										FireworkEffect.builder()
										.with(FireworkEffect.Type.valueOf(s[0]))
										.withColor(Arrays.asList(s[1].split("/")).stream()
												.map(c -> Color.fromRGB(Integer.valueOf(c)))
												.collect(Collectors.toList()))
										.withFade(Arrays.asList(s[2].split("/")).stream()
												.map(c -> Color.fromRGB(Integer.valueOf(c)))
												.collect(Collectors.toList()))
										.flicker(Boolean.valueOf(s[3]))
										.trail(Boolean.valueOf(s[4]))
										.build());
							});
					im = effects;
					
				
				//LEATHER ARMOR ATTRIBUTES
				}else if(itemAttribute[0].equals("color") && createdItemStack){
					((LeatherArmorMeta) im).setColor(Color.fromRGB(Integer.valueOf(itemAttribute[1])));
					
					
				//MAP ATTRIBUTES
				}else if(itemAttribute[0].equals("scaling") && createdItemStack){
					((MapMeta) im).setScaling(Boolean.valueOf(itemAttribute[1]));
					
					
				//POTION ATTRIBUTES
				}else if(itemAttribute[0].equals("effect") && createdItemStack){
					String[] data = itemAttribute[1].split("(~)");
					((PotionMeta) im).setBasePotionData(new PotionData(PotionType.valueOf(data[0]), Boolean.valueOf(data[1]), Boolean.valueOf(data[2])));
				}else if(itemAttribute[0].equals("customeffects") && createdItemStack){
					final PotionMeta potionMeta = (PotionMeta) im;
					Arrays.asList(itemAttribute[1].split("(,)")).stream()
							.forEach(s -> {
								String[] e = s.split("(~)");
								if (e.length == 6) 
									((PotionMeta) potionMeta).addCustomEffect(
											new PotionEffect(
													PotionEffectType.getByName(e[0]),
													Integer.valueOf(e[1]),
													Integer.valueOf(e[2]),
													Boolean.valueOf(e[3]),
													Boolean.valueOf(e[4]),
													Color.fromRGB(Integer.valueOf(e[5]))),
													false
											);
								else 
									((PotionMeta) potionMeta).addCustomEffect(
											new PotionEffect(
													PotionEffectType.getByName(e[0]),
													Integer.valueOf(e[1]),
													Integer.valueOf(e[2]),
													Boolean.valueOf(e[3]),
													Boolean.valueOf(e[4])),
													false
											);
							});
					im = potionMeta;
					
					
				//SKULL ATTRIBUTES
				}else if(itemAttribute[0].equals("owner") && createdItemStack)
					((SkullMeta) im).setOwner(itemAttribute[1]);
				
				
			}
			is.setItemMeta(im);
			deserializedInventory.setItem(stackPosition, is);
		}
		return deserializedInventory;
	}


}