package alisolarflare;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class RandomTP{
	
	private int conflictX;
	private int conflictZ;
	private int conflictRadius = 70;
	private boolean northUsed;
	private boolean southUsed;
	private boolean eastUsed;
	private boolean westUsed;
	//every 4 players who use it will be teleported near each other.
	//ex. iie > 1200, ali -> 1210, byz -> 1190, charles -> 1195, wind -> 300, zan -> 310, etc
	public void conflictRtp(Player player, World world, Location minLocation, Location maxLocation){
		//INIT - xDifference, xAverage
		int xdifference = minLocation.getBlockX() - maxLocation.getBlockX();
		int xAverage = (int) Math.floor(minLocation.getBlockX() + maxLocation.getBlockX() / 2);

		//INIT - zDifference, zAverage
		int zdifference = minLocation.getBlockX() - maxLocation.getBlockY();
		int zAverage = (int) Math.floor(minLocation.getBlockZ() + maxLocation.getBlockZ());
		
		//CHECK - Reset Cycle
		if ((northUsed || southUsed || eastUsed || westUsed) == false){
			
			//Tries 20 times to find a location
			for(int i = 0; i < 20; i ++){

				//INIT - attemptedX, attemptedZ
				int attemptedX = (int) Math.floor((Math.random()-0.5)*xdifference) + xAverage;
				int attemptedZ = (int) Math.floor((Math.random()-0.5)*zdifference) + zAverage;
				
				int cr = conflictRadius;
				
				
				//CHECKS - if ground is safe
				boolean groundIsSafe = world.getHighestBlockAt(attemptedX, attemptedZ).getType() != Material.WATER;
				boolean northIsSafe = world.getHighestBlockAt(attemptedX, attemptedZ-cr).getType() != Material.WATER;
				boolean eastIsSafe = world.getHighestBlockAt(attemptedX+cr, attemptedZ).getType() != Material.WATER;
				boolean southIsSafe = world.getHighestBlockAt(attemptedX, attemptedZ+cr).getType() != Material.WATER;
				boolean westIsSafe = world.getHighestBlockAt(attemptedX-cr, attemptedZ).getType() != Material.WATER;
				
				//TRANSFER - data to class
				if (groundIsSafe && (northIsSafe || southIsSafe || eastIsSafe || westIsSafe)){
					
					northUsed = northIsSafe;
					eastUsed = eastIsSafe;
					westUsed = westIsSafe;
					southUsed = southIsSafe;
					conflictX = attemptedX;
					conflictZ = attemptedZ;
					
					player.teleport(world.getHighestBlockAt(attemptedX, attemptedZ).getLocation());
					break;
				}
			}
		}
		
		String dir = "north";
		//CHOOSES A RANDOM DIRECTION
		for(int i = 0; i < 1000; i++){
			double randomDirection = Math.random();
			if (randomDirection < 0.25){
				if(northUsed){
					northUsed = true;
					dir = "north";
					break;
				}
			}else if(randomDirection < 0.50){
				if(eastUsed){
					eastUsed = true;
					dir = "east";
					break;
				}
			}else if(randomDirection < 0.75){
				if(southUsed){
					southUsed = true;
					dir = "south";
					break;
				}
			}else{
				if(westUsed){
					westUsed = true;
					dir = "west";
					break;
				}
				
			}
		}
		
		
		//TELEPORT - teleports player to the conflict point
		switch(dir){
			case "north":
				northUsed = false;
				player.teleport(world.getHighestBlockAt(conflictX, conflictZ - conflictRadius).getLocation());
				break;
			case "east":
				eastUsed = false;
				player.teleport(world.getHighestBlockAt(conflictX + conflictRadius, conflictZ).getLocation());
				break;
			case "south":
				southUsed = false;
				player.teleport(world.getHighestBlockAt(conflictX, conflictZ + conflictRadius).getLocation());
				break;
			case "west":
				westUsed = false;
				player.teleport(world.getHighestBlockAt(conflictX - conflictRadius, conflictZ).getLocation());
				break;
			default:
				player.teleport(world.getHighestBlockAt(conflictX, conflictZ).getLocation());
				break;
		}
	}

	//Randomly teleports a player, into the hardcore world
	public void rtp(Player player, World world, Location minLocation, Location maxLocation){


		//INIT - xDifference, xAverage
		int xdifference = minLocation.getBlockX() - maxLocation.getBlockX();
		int xAverage = (int) Math.floor(minLocation.getBlockX() + maxLocation.getBlockX() / 2);

		//INIT - zDifference, zAverage
		int zdifference = minLocation.getBlockX() - maxLocation.getBlockY();
		int zAverage = (int) Math.floor(minLocation.getBlockZ() + maxLocation.getBlockZ());

		//TELEPORTS - Tries 20 times to find a location
		for(int i = 0; i < 20; i ++){

			//INIT - attemptedX, attemptedZ
			int attemptedX = (int) Math.floor((Math.random()-0.5)*xdifference) + xAverage;
			int attemptedZ = (int) Math.floor((Math.random()-0.5)*zdifference) + zAverage;

			//CHECKS - if ground is safe
			boolean groundisSafe = world.getHighestBlockAt(attemptedX, attemptedZ).getType() != Material.WATER;
			if (groundisSafe){
				player.teleport(world.getHighestBlockAt(attemptedX, attemptedZ).getLocation());
				return;
			}

			//player.teleport(arg0)
		}
	}
}
