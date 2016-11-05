package buttondevteam.perworld.cache.world;

import java.util.HashMap;

import buttondevteam.perworld.cache.CacheInterface;

public class ShareSettings implements CacheInterface {
	
	
	/*	ConfigurationSection "worlds" is a static import from main.MainPlugin.java,
	 * 	inherited from CacheInterface.
	 * 
	 * 	It points to the section found at config.getConfigurationSection("worlds")
	 * 	and contains all world keys, child keys, and their values 
	 * 
	 * 	this is a static field backed by the actual contents of config.yml
	 */	
	
	
	
	
	
	//CACHE ELEMENT
	public class E {
		/* 	the optional share type (in or out) specifies that the world 
		 * 	shares with its group in only one traffic direction.
		 */
		String group;
		int t;
		public E(String group, int type){
			this.group = group;
			this.t = type;//0-default, 1-in, 2-out
		}
	}
	
	
	//CACHE
	public static volatile HashMap<String,E[]> cache = new HashMap<String,E[]>();
	/*	this stores the share settings for each world
	 * 	in an easily-parsed format, mapping each world to
	 * 	an array e[] of two cache elements (inv and data)
	 */
	
	
	
	
	
	//GENERATE ELEMENT (FROM CONFIG)
	public E[] generateElement(String world){
		return new E[]
				{
					new E(
							worlds.getString(world + ".share.inventory.group"), 
							worlds.getInt(world + ".share.inventory.type", 0)
							),	
					new E(
							worlds.getString(world + ".share.playerdata.group"), 
							worlds.getInt(world + ".share.playerdata.type", 0)
							)
				};
	}
	
	
	//PUT CACHE
	public void putCache(String world){
		E[] element = generateElement(world);
		if ((element[0].group != null || element[1].group != null)) cache.put(world, element);
	}
	
	
	//INIT CACHE ( forEach -> putCache() )
	public void initCache(){
		worlds.getKeys(false).forEach(s -> { putCache(s); });
	}
	
	
	
	
	
	//COMPARE SETTINGS
	public static boolean[] compare (String worldTo, String worldFrom) {
		E[][] params = new E[][] { cache.get(worldTo), cache.get(worldFrom) };
		return params[0] == null || params [1] == null ?
				new boolean[] {false, false} : 
					new boolean[]
						{
							params[0][0] == null || params[1][0] == null ? false :
								
									params[0][0].group == params[1][0].group	//inv
									&& params[0][0].t == 0 ? true : params[0][0].t == 1
									&& params[1][0].t == 0 ? true : params[1][0].t == 2
							,
							params[0][1] == null || params[1][1] == null ? false :
								
									params[0][1].group == params[1][1].group	//data
									&& params[0][1].t == 0 ? true : params[0][1].t == 1
									&& params[1][1].t == 0 ? true : params[1][1].t == 2
						};
	}
}
