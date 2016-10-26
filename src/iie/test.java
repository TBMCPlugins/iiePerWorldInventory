package iie;

import net.minecraft.server.v1_10_R1.IInventory;

public class test {
	
	public static IInventory invInventory (){
		IInventory invInventory = null;
		try{
			invInventory = IInventory.class.newInstance();
		}catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		return invInventory;
	}
	
	public void method(){
		final IInventory invInventory = invInventory();
		Serializer.setInventoryFromSerialized(invInventory, "string");
	}
	
	public void method2(){
		method();
	}
}
