package iie;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoundTest implements CommandExecutor{
	
	PerWorldInventoryPlugin plugin;
	public SoundTest(PerWorldInventoryPlugin plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command label, String command, String[] args) {
		
		
		Player player = (Player) sender;
		player.getWorld().playSound(player.getLocation(), Sound.AMBIENT_CAVE,1F,1F); 
		
		return false;		
	}

}
